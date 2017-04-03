package com.song.postanywhere.server.route;

import com.alibaba.fastjson.JSON;
import com.song.postanywhere.common.exception.PostAnyWhereException;
import com.song.postanywhere.common.user.service.UserService;
import com.song.postanywhere.common.user.vo.UserVo;
import com.song.postanywhere.server.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class LoginRoute implements Route {

  @Autowired
  private UserService userService;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String body = request.body();
    if (StringUtils.isEmpty(body)) {
      throw new PostAnyWhereException(333333);
    }

    UserVo userVo = JSON.parseObject(body, UserVo.class);
    userService.login(userVo);

    return new ResponseVo().toString();
  }
}
