package com.example.dayday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginActivity  extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;
    private  EditText userId;
    private EditText password;


    public void rememberAccount(){
        editor=pref.edit();
        if(checkBox.isChecked()){//被选中，登陆后，就需要存储
            editor.putBoolean("remember_password",true);
            editor.putString("account",userId.getText().toString());
            editor.putString("password",password.getText().toString());
            editor.apply();//不要忘记apply，不方便查询，但有的时候不用那么复杂
        }else{
            editor.clear();
        }
    }

    public void login(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("userId",userId.getText().toString());
        startActivity(intent);
        Toast.makeText(LoginActivity.this,"Welcome to login!",Toast.LENGTH_SHORT).show();
        rememberAccount();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userId=(EditText)findViewById(R.id.userId);
        password=(EditText)findViewById(R.id.password);
        checkBox=(CheckBox)findViewById(R.id.remember_pd);
        Button button=(Button)findViewById(R.id.login);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            userId.setText(pref.getString("account",""));
            password.setText(pref.getString("password",""));
            checkBox.setChecked(true);
        }


        //这里处理一下数据库的问题
        LitePal.initialize(this);//还要有初始化
        LitePal.getDatabase();//需要有litepal.xml;需要在配置文件里设置;修改的时候记得文件名和报名同时修改
//        initAccout();
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String id=userId.getText().toString();
                String pd=password.getText().toString();
                UserData user=LitePal.where("userId=?",id).findFirst(UserData.class);

                if(user!=null){
                    //todo：如果账号已经存在，那么密码必须配得上
                    if(pd.equals(user.getPassword())){
                        login();
                    }else{
                        Toast.makeText(LoginActivity.this,"password is wrong!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //todo：如果账号不存在，那么就是新建一个账户
                    //空值是怎么处理的？
                    UserData userData=new UserData(id,pd);
                    userData.save();
                    Toast.makeText(LoginActivity.this,"create a new account!",Toast.LENGTH_SHORT).show();
                    login();
                }


            }
        });

        //如果改成直接向数据库拿去会不会比较快？
        List<String> encourageWord=new ArrayList<>();
        encourageWord.add("生活的理想，就是为了理想而生活");
        encourageWord.add("青年时种下什么，老年时，就收获什么");
        encourageWord.add("要成就一件大事业，必须从小事做起");
        encourageWord.add("如果错过太阳时你流了泪，那么你也要错过群星");

        encourageWord.add("为中华之崛起而读书");
        encourageWord.add("天行健，君子以自强不息");
        encourageWord.add("所谓天才，只不过是把别人喝咖啡的功夫都用在工作上了");
        encourageWord.add("不傲才以骄人，不以宠而作威");
        encourageWord.add("真者，精诚之至也，不精不诚，不能动人");
        encourageWord.add("恢弘志士之气，不宜妄自菲薄");

        Random rd=new Random();
        int rdNum=rd.nextInt(encourageWord.size());
        Toast.makeText(LoginActivity.this,encourageWord.get(rdNum),Toast.LENGTH_SHORT).show();


    }
}

