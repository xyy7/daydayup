package com.example.dayday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class OperateActivity extends AppCompatActivity {

    String uid;
    String cid;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        uid= getIntent().getStringExtra("uid");
        cid=getIntent().getStringExtra("cid");
        adminId=getIntent().getStringExtra("adminId");
        final EditText editText=(EditText)findViewById(R.id.edit_jianjie);
        final TextView textView=(TextView)findViewById(R.id.jianjie);
        Log.d("uid=,cid=",uid+" "+cid);
        //初始化
        ChatCircle chatCircle= LitePal.where("adminId=? and cid=?",adminId,cid).findFirst(ChatCircle.class);
        if(chatCircle==null){
            Log.d("null","chatCircle is null123");
        }else{
            Log.d("st123",chatCircle.getContent()+"123");
            textView.setText(chatCircle.getContent());
        }


        final Button finish=(Button)findViewById(R.id.finish);
        finish.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);


        //修改后的存储
        final Button edit=(Button)findViewById(R.id.eidt_jianjie_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                finish.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                editText.setText(textView.getText().toString());
                editText.setVisibility(View.VISIBLE);
//                ChatCircle chatCircle= LitePal.where("uid=?",uid).where("cid=?",cid).findFirst(ChatCircle.class);
//                if(chatCircle==null){
//                    Log.d("null","chatCircle is null");
//
//                }else{
//                    Log.d("save",editText.getText().toString());
//                    chatCircle.setContent(editText.getText().toString());
//                    chatCircle.save();
//                }
//                Log.d("save123",editText.getText().toString());
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.VISIBLE);
                finish.setVisibility(View.GONE);
                textView.setText(editText.getText().toString());
                textView.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                ChatCircle chatCircle= LitePal.where("adminId=? and cid=?",adminId,cid).findFirst(ChatCircle.class);
                if(chatCircle==null){
                    Log.d("null","chatCircle is null");

                }else{
                    Log.d("save",editText.getText().toString());
                    chatCircle.setContent(editText.getText().toString());
                    chatCircle.save();
                }
                Log.d("save123",editText.getText().toString());
            }
        });

        Button quit=(Button)findViewById(R.id.quit_circle);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo：如果是管理员不能直接退出，除非解散
//                ChatCircle circle=LitePal.where("adminId=? and cid=?",uid,cid).findFirst(ChatCircle.class);
                if(adminId.equals(uid)){
                    Toast.makeText(OperateActivity.this,"you are adminstator,if you want to quit,you need to " +
                            "delete the circle ",Toast.LENGTH_SHORT).show();
                }else{
                    LitePal.deleteAll(ChatCircle.class,"adminId=? and uid=? and cid=?",adminId,uid,cid);
                    LitePal.deleteAll(UserCircle.class,"adminId =? and uid=? and cid=?",adminId,uid,cid);
                    LitePal.deleteAll(Msg.class,"adminId=? and cid=?",adminId,cid);
                    SharedPreferences pref=getSharedPreferences("first",MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString(uid+cid+"AAA","");
                    editor.putBoolean(uid+cid,false);
                    editor.apply();
                    editor.commit();
                    Intent intent=new Intent(OperateActivity.this,CircleActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("cid",cid);
                    intent.putExtra("adminId",adminId);
                    startActivity(intent);
                    Toast.makeText(OperateActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button cdel=(Button)findViewById(R.id.delete_circle);
        //todo:解散也可以，只是需要通过来云数据库来解除
        cdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(adminId.equals(uid)){
                   //只删除自己的圈子
                   // 删除圈子的时候，还要删除usercircle和msg
                    LitePal.deleteAll(ChatCircle.class,"adminID=? and cid=? and uid=?",uid,cid,uid);//这里是删除管理员的全部，上面只是删除自己的而已
                    LitePal.deleteAll(UserCircle.class,"uid=? and cid=?",uid,cid);
                    LitePal.deleteAll(Msg.class,"adminId=? and cid=?",uid,cid);


                    Toast.makeText(OperateActivity.this,"解散成功",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=getSharedPreferences("first",MODE_PRIVATE).edit();
                    editor.putBoolean(uid+cid,false);
                    editor.putString(uid+cid+"AAA","");
                    editor.apply();
                    editor.commit();
                    Intent intent=new Intent(OperateActivity.this,CircleActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("cid",cid);
                    startActivity(intent);
                }else{
                    Toast.makeText(OperateActivity.this,"you are not an adminstator",Toast.LENGTH_SHORT).show();
                }};
        });
        //如果有太多按钮，应该implements比较好
        Button pdel=(Button)findViewById(R.id.delete_sb);
        pdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteSb();//跳出一个对话框，写上姓名即可
                if(adminId.equals(uid)){
                    Intent intent=new Intent(OperateActivity.this,DeleteActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("cid",cid);
                    intent.putExtra("adminId",adminId);
                    startActivity(intent);
                }else{
                    Toast.makeText(OperateActivity.this,"you are not an adminstator",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button lookApply=(Button)findViewById(R.id.look_apply);
        lookApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminId.equals(uid)){
                    Intent intent=new Intent(OperateActivity.this,ApplyInfoActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("cid",cid);
                    intent.putExtra("adminId",adminId);
                    startActivity(intent);
                }else{
                    Toast.makeText(OperateActivity.this,"you are not an adminstator",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button rank=(Button)findViewById(R.id.rank);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OperateActivity.this,RankInfoActivity.class);
                intent.putExtra("uid",uid);
                intent.putExtra("cid",cid);
                intent.putExtra("adminId",adminId);
                startActivity(intent);

            }
        });
    }
}
