package com.song.postanywhere.common.user.service;

import com.song.postanywhere.common.exception.PostAnyWhereException;
import com.song.postanywhere.common.user.vo.UserVo;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public interface UserService {

  int register(UserVo uservo) throws PostAnyWhereException;

  boolean login(UserVo uservo);
}
