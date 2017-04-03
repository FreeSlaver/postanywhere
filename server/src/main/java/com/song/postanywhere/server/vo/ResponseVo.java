package com.song.postanywhere.server.vo;

import com.alibaba.fastjson.JSON;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/3.
 */
public class ResponseVo {

  private int code;

  private String msg;

  private long timestamp;

  private String data;

  public ResponseVo() {
    this.code = 0;
    this.timestamp = System.currentTimeMillis();
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

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override public String toString() {
    return JSON.toJSONString(this);
  }
}
