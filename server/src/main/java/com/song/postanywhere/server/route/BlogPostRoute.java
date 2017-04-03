package com.song.postanywhere.server.route;

import com.song.postanywhere.common.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Administrator on 2017/4/3.
 */
@Service
public class BlogPostRoute implements Route{

  @Autowired
  private BlogService blogService;

  @Override public Object handle(Request request, Response response) throws Exception {


    //blogService.post();
    return null;
  }
}
