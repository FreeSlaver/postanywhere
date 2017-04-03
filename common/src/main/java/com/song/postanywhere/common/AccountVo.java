package com.song.postanywhere.common;

/**
 * Created by Administrator on 2017/4/3.
 */
public class AccountVo {

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  private String userName;

  private String password;

  private String accessToken;
}
