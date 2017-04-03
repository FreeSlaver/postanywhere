package com.song.postanywhere.common.user.service.impl;

import com.song.postanywhere.common.exception.PostAnyWhereException;
import com.song.postanywhere.common.user.service.UserService;
import com.song.postanywhere.common.user.vo.UserVo;
import com.song.postanywhere.common.xmlrpc.XmlRpcClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class UserServiceImpl implements UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override public int register(UserVo uservo) throws PostAnyWhereException {
    String mobile = uservo.getMobile();
    String email = uservo.getEmail();
    if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)) {
      throw new PostAnyWhereException(333333);
    }
    if (!StringUtils.isEmpty(mobile)) {
      //send sms

    } else {
      //send email

    }
    return 0;
  }

  @Override public boolean login(UserVo uservo) {
    return false;
  }
}
