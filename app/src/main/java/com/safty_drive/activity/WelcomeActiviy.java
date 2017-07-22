package com.safty_drive.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.safty_drive.HttpResponeCallBack;
import com.safty_drive.ItLanBaoApplication;
import com.safty_drive.common.KeyConstance;
import com.safty_drive.R;
import com.safty_drive.RequestApiData;
import com.safty_drive.common.UrlConstance;
import com.safty_drive.vo.UserBaseInfo;
import com.safty_drive.UserPreference;

/**
 * 欢迎界面
 */
public class WelcomeActiviy extends Activity implements HttpResponeCallBack {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activiy);

        iv = (ImageView) this.findViewById(R.id.logo);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(2000);
        iv.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * 动画结束时判断是否保存有登录的信息
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent();
                intent.setClass(WelcomeActiviy.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();
                //暂时用用户名密码登录
//                String username = UserPreference.read(KeyConstance.USERNAME, null);//软件还没有保持账号
//                String token = UserPreference.read(KeyConstance.TOKEN, null);
//                String password = UserPreference.read(KeyConstance.PWD, null);
//
//                if (TextUtils.isEmpty(token)) {
//                    Intent intent = new Intent();
//                    intent.setClass(WelcomeActiviy.this, LoginActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                    finish();
//                } else {
//                    //用保存的信息直接登录
//                    RequestApiData.getInstance().getLoginData(username, password,
//                            UserBaseInfo.class, WelcomeActiviy.this);
//
//                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onResponeStart(String apiName) {

    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(WelcomeActiviy.this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
         if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {//当前接口是登录的接口
             if (object != null && object instanceof UserBaseInfo) {
                 UserBaseInfo info = (UserBaseInfo) object;

                 //登录成功，保存登录信息
                 ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中

                 //保存到SP中
                 UserPreference.save(KeyConstance.TOKEN, info.getToken());

                 UserPreference.save(KeyConstance.USERNAME, info.getUsername());

                 UserPreference.save(KeyConstance.PWD, info.getPassword());

                 Intent intent = new Intent();
                 intent.setClass(WelcomeActiviy.this, MainActivity.class);
                 startActivity(intent);
                 overridePendingTransition(android.R.anim.slide_in_left,
                         android.R.anim.slide_out_right);
                 finish();

             }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(WelcomeActiviy.this, strMsg, Toast.LENGTH_SHORT).show();
    }
}
