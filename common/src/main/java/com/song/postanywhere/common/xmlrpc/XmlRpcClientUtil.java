package com.song.postanywhere.common.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/3.
 */
public class XmlRpcClientUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(XmlRpcClientUtil.class);

  public static void main(String[] args) {
    String url = "http://rpc.cnblogs.com/metaweblog/3gods";
    String methodName = "metaWeblog.getRecentPosts";
    String userName = "sx504252262";
    String password = "sx198942";
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    try {
      config.setServerURL(new URL(url));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    XmlRpcClient client = new XmlRpcClient();
    client.setConfig(config);
    //Object[] params = new Object[] {new Integer(33), new Integer(9)};
    Object[] params = new Object[]{"000000",userName,password,10};

    try {
      //Integer result = (Integer) client.execute("Calculator.add", params);
      Object obj = client.execute(methodName,params);

      client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));

      System.out.println(obj);
      System.out.println("hello world");
    } catch (XmlRpcException e) {
      e.printStackTrace();
    }

  }

}
