package com.safty_drive.common;

/**
 * @author itlanbao
 * 处理网络的参数常量类
 */
public class UrlConstance {

   //签约公钥，即客户端与服务器协商订的一个公钥
   public static final String APP_URL = "http://192.168.1.4:8080/";
   
   //4.6注册用户接口
   public static final String KEY_REGIST_INFO ="web_api/v1/user/register";
   
   //4.8登录用户接口
   public static final String KEY_LOGIN_INFO ="web_api/v1/user/login";

   public static final String USER_DRIVE_INFO ="web_api/v1/user/drive/info";
}
