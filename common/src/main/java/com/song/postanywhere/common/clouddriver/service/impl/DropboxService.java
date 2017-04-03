package com.song.postanywhere.common.clouddriver.service.impl;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxPathV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.UploadSessionCursor;
import com.dropbox.core.v2.files.UploadSessionFinishErrorException;
import com.dropbox.core.v2.files.UploadSessionLookupErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.song.postanywhere.common.AccountVo;
import com.song.postanywhere.common.clouddriver.service.DriverService;
import com.song.postanywhere.common.exception.PostAnyWhereException;
import com.song.postanywhere.common.user.service.impl.UserServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class DropboxService implements DriverService{
  private static final Logger logger = LoggerFactory.getLogger(DropboxService.class);

  private static final String ACCESS_TOKEN =
      "2fHRxAhKjKAAAAAAAAAACZFSw7CGCDxTgVvTl2zw1amJgqG-vDD43nzZDjVX1xy2";

  private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 8L << 20; // 8MiB
  private static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;

  public void uploadFile(AccountVo accountVo, String localPath, String dropboxPath)
      throws PostAnyWhereException {
    //String accessToken = "2fHRxAhKjKAAAAAAAAAACZFSw7CGCDxTgVvTl2zw1amJgqG-vDD43nzZDjVX1xy2";
    //String localPath = "C:/博客发布平台.txt";
    //String dropboxPath = "/hello/博客发布平台.txt";
    String accessToken = accountVo.getAccessToken();

    // Read auth info file.
    DbxAuthInfo authInfo;
    try {
      authInfo = new DbxAuthInfo(accessToken, DbxHost.DEFAULT);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new PostAnyWhereException(99999, ex);
    }

    String pathError = DbxPathV2.findError(dropboxPath);
    if (pathError != null) {
      throw new PostAnyWhereException(99999, "Invalid <dropbox-path>: " + pathError);
    }

    File localFile = new File(localPath);
    if (!localFile.exists()) {
      throw new PostAnyWhereException(99999,
          "Invalid <local-path>" + localPath + ": file does not exist.");
    }

    if (!localFile.isFile()) {
      throw new PostAnyWhereException(99999, "Invalid <local-path>" + localPath + ": not a file.");
    }

    // Create a DbxClientV2, which is what you use to make API calls.
    //DbxRequestConfig requestConfig = new DbxRequestConfig("examples-upload-file");
    DbxRequestConfig requestConfig = new DbxRequestConfig("upload-file");
    DbxClientV2 dbxClient =
        new DbxClientV2(requestConfig, authInfo.getAccessToken(), authInfo.getHost());

    if (localFile.length() <= (2 * CHUNKED_UPLOAD_CHUNK_SIZE)) {
      uploadFile(dbxClient, localFile, dropboxPath);
    } else {
      chunkedUploadFile(dbxClient, localFile, dropboxPath);
    }
  }

  private static void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
    try (InputStream in = new FileInputStream(localFile)) {
      FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
          .withMode(WriteMode.ADD)
          .withClientModified(new Date(localFile.lastModified()))
          .uploadAndFinish(in);

      System.out.println(metadata.toStringMultiline());
    } catch (UploadErrorException ex) {
      System.err.println("Error uploading to Dropbox: " + ex.getMessage());
      System.exit(1);
    } catch (DbxException ex) {
      System.err.println("Error uploading to Dropbox: " + ex.getMessage());
      System.exit(1);
    } catch (IOException ex) {
      System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
      System.exit(1);
    }
  }

  private static void chunkedUploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) {
    long size = localFile.length();

    // assert our file is at least the chunk upload size. We make this assumption in the code
    // below to simplify the logic.
    if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
      System.err.println("File too small, use upload() instead.");
      System.exit(1);
      return;
    }

    long uploaded = 0L;
    DbxException thrown = null;

    // Chunked uploads have 3 phases, each of which can accept uploaded bytes:
    //
    //    (1)  Start: initiate the upload and get an upload session ID
    //    (2) Append: upload chunks of the file to append to our session
    //    (3) Finish: commit the upload and close the session
    //
    // We track how many bytes we uploaded to determine which phase we should be in.
    String sessionId = null;
    for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
      if (i > 0) {
        System.out.printf("Retrying chunked upload (%d / %d attempts)\n", i + 1,
            CHUNKED_UPLOAD_MAX_ATTEMPTS);
      }

      try (InputStream in = new FileInputStream(localFile)) {
        // if this is a retry, make sure seek to the correct offset
        in.skip(uploaded);

        // (1) Start
        if (sessionId == null) {
          sessionId = dbxClient.files().uploadSessionStart()
              .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE)
              .getSessionId();
          uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
          printProgress(uploaded, size);
        }

        UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);

        // (2) Append
        while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
          dbxClient.files().uploadSessionAppendV2(cursor)
              .uploadAndFinish(in, CHUNKED_UPLOAD_CHUNK_SIZE);
          uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
          printProgress(uploaded, size);
          cursor = new UploadSessionCursor(sessionId, uploaded);
        }

        // (3) Finish
        long remaining = size - uploaded;
        CommitInfo commitInfo = CommitInfo.newBuilder(dropboxPath)
            .withMode(WriteMode.ADD)
            .withClientModified(new Date(localFile.lastModified()))
            .build();
        FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
            .uploadAndFinish(in, remaining);

        System.out.println(metadata.toStringMultiline());
        return;
      } catch (RetryException ex) {
        thrown = ex;
        // RetryExceptions are never automatically retried by the client for uploads. Must
        // catch this exception even if DbxRequestConfig.getMaxRetries() > 0.
        sleepQuietly(ex.getBackoffMillis());
        continue;
      } catch (NetworkIOException ex) {
        thrown = ex;
        // network issue with Dropbox (maybe a timeout?) try again
        continue;
      } catch (UploadSessionLookupErrorException ex) {
        if (ex.errorValue.isIncorrectOffset()) {
          thrown = ex;
          // server offset into the stream doesn't match our offset (uploaded). Seek to
          // the expected offset according to the server and try again.
          uploaded = ex.errorValue
              .getIncorrectOffsetValue()
              .getCorrectOffset();
          continue;
        } else {
          // Some other error occurred, give up.
          System.err.println("Error uploading to Dropbox: " + ex.getMessage());
          System.exit(1);
          return;
        }
      } catch (UploadSessionFinishErrorException ex) {
        if (ex.errorValue.isLookupFailed() && ex.errorValue.getLookupFailedValue()
            .isIncorrectOffset()) {
          thrown = ex;
          // server offset into the stream doesn't match our offset (uploaded). Seek to
          // the expected offset according to the server and try again.
          uploaded = ex.errorValue
              .getLookupFailedValue()
              .getIncorrectOffsetValue()
              .getCorrectOffset();
          continue;
        } else {
          // some other error occurred, give up.
          System.err.println("Error uploading to Dropbox: " + ex.getMessage());
          System.exit(1);
          return;
        }
      } catch (DbxException ex) {
        System.err.println("Error uploading to Dropbox: " + ex.getMessage());
        System.exit(1);
        return;
      } catch (IOException ex) {
        System.err.println("Error reading from file \"" + localFile + "\": " + ex.getMessage());
        System.exit(1);
        return;
      }
    }

    // if we made it here, then we must have run out of attempts
    System.err.println(
        "Maxed out upload attempts to Dropbox. Most recent error: " + thrown.getMessage());
    System.exit(1);
  }

  private static void printProgress(long uploaded, long size) {
    System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size,
        100 * (uploaded / (double) size));
  }

  private static void sleepQuietly(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ex) {
      // just exit
      System.err.println("Error uploading to Dropbox: interrupted during backoff.");
      System.exit(1);
    }
  }
}
