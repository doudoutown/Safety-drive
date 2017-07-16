package com.safty_drive.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.safty_drive.ItLanBaoApplication;
import com.safty_drive.UserPreference;
import com.safty_drive.common.Constant;
import com.safty_drive.HttpResponeCallBack;
import com.safty_drive.R;
import com.safty_drive.RequestApiData;
import com.safty_drive.common.KeyConstance;
import com.safty_drive.common.UrlConstance;
import com.safty_drive.vo.UserBaseInfo;

public class RegisterActivity extends Activity implements HttpResponeCallBack {

    private EditText username;//用户昵称
    private EditText password;//注册密码
    private EditText confirmPassword;
    private RadioGroup gender;
    private NumberPicker age;
    private NumberPicker drivingAge;
    private Button registBtn;//注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        gender = findViewById(R.id.genderGroup);
        age = findViewById(R.id.numberPickerAge);
        drivingAge = findViewById(R.id.numberPickerDrivingAge);
        registBtn = (Button) findViewById(R.id.regist_btn);

        registBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String usernamStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String confirmStr = confirmPassword.getText().toString();
                RadioButton genderRadio = (RadioButton)findViewById(gender.getCheckedRadioButtonId());
                Integer genderValue = "男".equals(genderRadio.getText())?1:0;
                Integer ageValue = age.getValue();
                Integer drivingAgeValue = drivingAge.getValue();
                if (!TextUtils.isEmpty(usernamStr) &&
                        !TextUtils.isEmpty(passwordStr)
                        && !TextUtils.isEmpty(confirmStr)) {
                    RequestApiData.getInstance().register(usernamStr,passwordStr,confirmStr,genderValue,ageValue,drivingAgeValue,
                            UserBaseInfo.class, RegisterActivity.this);
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息未完全", Toast.LENGTH_SHORT).show();
                }
            }
        });

        age.setMinValue(10);
        age.setMaxValue(70);
        drivingAge.setMinValue(0);
        drivingAge.setMaxValue(70);
        age.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            }
        });

        drivingAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            }
        });
    }

    @Override
    public void onResponeStart(String apiName) {
        // TODO Auto-generated method stub
        Toast.makeText(RegisterActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(RegisterActivity.this, "Loading...", Toast.LENGTH_SHORT).show(); 
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        // TODO Auto-generated method stub
        //注册接口
        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;

                //登录成功，保存登录信息
                ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中

                //保存到SP中
                UserPreference.save(KeyConstance.TOKEN, info.getToken());

                UserPreference.save(KeyConstance.USERNAME, info.getUsername());

                UserPreference.save(KeyConstance.PWD, info.getPassword());

                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();

            }
        }

    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(RegisterActivity.this, strMsg, Toast.LENGTH_SHORT).show();
    }
}
