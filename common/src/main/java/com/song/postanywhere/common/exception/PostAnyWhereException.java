package com.song.postanywhere.common.exception;

/**
 * Created by Administrator on 2017/4/3.
 */
public class PostAnyWhereException extends Exception {

  private int code;

  private String msg;

  private Throwable cause;

  public PostAnyWhereException() {

  }

  public PostAnyWhereException(int code) {
    this.code = code;
  }

  public PostAnyWhereException(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public PostAnyWhereException(int code, Throwable cause) {
    this.code = code;
    this.cause = cause;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override public Throwable getCause() {
    return cause;
  }

  public void setCause(Throwable cause) {
    this.cause = cause;
  }

  @Override public String toString() {
    return "code:" + code + ",msg:" + msg + ",cause" + cause;
  }
}
