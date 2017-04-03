package com.song.postanywhere.common.user.vo;

/**
 * Created by Administrator on 2017/4/3.
 */
public class UserVo {
  private String mobile;

  private String email;

  public UserVo() {

  }

  public UserVo(String mobile, String email) {
    this.mobile = mobile;
    this.email = email;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
