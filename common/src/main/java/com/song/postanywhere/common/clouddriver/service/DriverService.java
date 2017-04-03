package com.song.postanywhere.common.clouddriver.service;

import com.song.postanywhere.common.AccountVo;

/**
 * Created by Administrator on 2017/4/3.
 */
public interface DriverService {

  void uploadFile(AccountVo accountVo, String localPath, String dropboxPath);

}
