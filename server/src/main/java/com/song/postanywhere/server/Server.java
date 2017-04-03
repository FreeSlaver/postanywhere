package com.song.postanywhere.server;

import com.song.postanywhere.server.route.AccountBindingRoute;
import com.song.postanywhere.server.route.BlogPostRoute;
import com.song.postanywhere.server.route.DriverBindingRoute;
import com.song.postanywhere.server.route.LoginRoute;
import com.song.postanywhere.server.route.RegisterRoute;
import spark.Spark;

/**
 * Created by Administrator on 2017/4/3.
 */
public class Server {

  public static void main(String[] args) {

    Spark.post("/user/register", new RegisterRoute());
    Spark.post("/user/login", new LoginRoute());

    Spark.post("/account/binding", new AccountBindingRoute());
    Spark.post("/driver/binding", new DriverBindingRoute());

    Spark.post("/blog/post",new BlogPostRoute());

  }
}
