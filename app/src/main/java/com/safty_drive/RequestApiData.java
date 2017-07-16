package com.safty_drive;

import com.safty_drive.common.UrlConstance;
import com.safty_drive.vo.UserBaseInfo;
import com.safty_drive.vo.UserDriveVo;

import java.util.HashMap;

import static com.safty_drive.common.UrlConstance.USER_DRIVE_INFO;
 
/*
 * 网络接口 wjl
 */

/**
 * @author Administrator
 */
public class RequestApiData {
    private static RequestApiData instance = null;
    private HttpResponeCallBack mCallBack = null;

    //创建接口对象
    public static RequestApiData getInstance() {
        if (instance == null) {
            instance = new RequestApiData();
        }
        return instance;
    }


    public void register(String username, String password, String confirmPassword,
                         Integer gender, Integer age, Integer drivingAge,
                         Class<UserBaseInfo> clazz, HttpResponeCallBack callback) {
        mCallBack = callback;
        //这是每一个接口的唯一标示
        String tagUrl = UrlConstance.KEY_REGIST_INFO;//注册接口
        //将注册的信息保存在map中（须和服务器端一致）
        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("username", username);
        parameter.put("password", password);
        parameter.put("confirm_pwd", confirmPassword);
        parameter.put("gender", gender.toString());
        parameter.put("age", age.toString());
        parameter.put("drive_age", drivingAge.toString());

        //请求数据接口
        RequestManager.post(UrlConstance.APP_URL, tagUrl, parameter, clazz, callback);

    }


    /**
     * 4.8登录用户接口
     *
     * @param username 用户名
     * @param password 密码
     * @param clazz    数据返回的解析对象
     * @param callback 回调
     *                 特别要注意参数位置不能变要根据文档来
     *                 请求方式：POST
     */
    public void getLoginData(String username, String password,
                             Class<UserBaseInfo> clazz,
                             HttpResponeCallBack callback) {
        mCallBack = callback;
        //这是每一个接口的唯一标示
        String tagUrl = UrlConstance.KEY_LOGIN_INFO;//登录接口
        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("username", username);
        parameter.put("password", password);
        //请求数据接口
        RequestManager.post(UrlConstance.APP_URL, tagUrl, parameter, clazz, callback);

    }

    public void sendSensor(Integer x, Integer y, Integer z, Class<UserDriveVo> clazz, HttpResponeCallBack callback) {

        HashMap<String, String> parameter = new HashMap<String, String>();
        parameter.put("x", String.valueOf(x));
        parameter.put("y", String.valueOf(y));
        parameter.put("z", String.valueOf(z));
        RequestManager.post(UrlConstance.APP_URL, USER_DRIVE_INFO, parameter, clazz, callback);

    }

}
