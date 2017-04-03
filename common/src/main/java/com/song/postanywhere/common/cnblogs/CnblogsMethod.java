package com.song.postanywhere.common.cnblogs;


/**
 * Created by Administrator on 2017/4/3.
 */
public enum CnblogsMethod {

  //blogger.deletePost
  //blogger.getUsersBlogs
  //metaWeblog.editPost
  //metaWeblog.getCategories
  //metaWeblog.getPost
  //metaWeblog.getRecentPosts
  //metaWeblog.newMediaObject
  //metaWeblog.newPost
  //wp.newCategory

  GET_POST         ("metaWeblog.getPost"),
  GET_RECENT_POSTS ("metaWeblog.getRecentPosts"),
  NEW_POST         ("metaWeblog.editPost"),
  EDIT_POST        ("metaWeblog.editPost"),
  DELETE_POST      ("blogger.deletePost"),
  GET_CATEGORIES   ("wp.newCategory"),
  NEW_CATEGORIES   ("metaWeblog.getCategories"),
  NEW_MEDIA_OBJECT ("metaWeblog.newMediaObject"),
  GET_USERS_BLOGS  ("blogger.getUsersBlogs");

  private final String value;

  private CnblogsMethod(final String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
