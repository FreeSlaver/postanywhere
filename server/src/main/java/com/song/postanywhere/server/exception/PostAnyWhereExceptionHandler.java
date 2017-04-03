package com.song.postanywhere.server.exception;

import com.song.postanywhere.common.exception.PostAnyWhereException;
import com.song.postanywhere.common.xmlrpc.XmlRpcClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class PostAnyWhereExceptionHandler implements ExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(PostAnyWhereExceptionHandler.class);

  @Override public void handle(Exception e, Request request, Response response) {
    if (e instanceof PostAnyWhereException) {
      PostAnyWhereException postAnyWhereException = (PostAnyWhereException) e;

      LOGGER.error(postAnyWhereException.toString());
    }
  }
}
