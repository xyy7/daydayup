package com.example.dayday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircleActivity extends AppCompatActivity {

    List<ChatCircle> list=new ArrayList<>();
    RecyclerView recyclerView;
    ChatCircleAdapter adapter;//不会自动转化的么
    String uid;
    String cid;
    String addName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_list);//R.id 会在这里面查找，如果没有就会报错
        //传递信息
        uid=getIntent().getStringExtra("uid");
        //点击按钮事件，在adapter里面设置
        //这里处理一下数据库的问题
        LitePal.initialize(this);//还要有初始化
        LitePal.getDatabase();//需要有litepal.xml;需要在配置文件里设置;修改的时候记得文件名和报名同时修改
        //todo：初始化recyclerview
        initData();

        //绑定，其实就是点击，然后跳转事件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);//修改1
        // 给recyclerView指定一个Linear线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(CircleActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatCircleAdapter(list,this,uid);
        recyclerView.setAdapter(adapter);





    }

    public void initData(){
        //uid是null的时候，会有the bind value at index 1 is null 的报错
        List<ChatCircle> chatCircleList=LitePal.where("uid=?",uid).find(ChatCircle.class);
        Log.d("417",chatCircleList.size()+"?");
        if(chatCircleList.size()!=0){
            for (ChatCircle circle:chatCircleList){
                list.add(circle);
            }
        }else{
            //自带的，需要自己删除
//            list.add("好好学习");
//            list.add("daydayup");
//            list.add("背英语");
//            ChatCircle circle1=new ChatCircle(list.get(0),0+"",uid,uid);
//            circle1.save();
//            ChatCircle circle2=new ChatCircle(list.get(1),1+"",uid,uid);
//            circle2.save();
//            ChatCircle circle3=new ChatCircle(list.get(2),2+"",uid,uid);
//            circle3.save();
            Toast.makeText(CircleActivity.this,"no circles now",Toast.LENGTH_SHORT).show();
        }

    }

    //如果只是增加的话，update用处不大
    public void update(){

    }


    public void addData(ChatCircle chatCircle){
        list.add(chatCircle);
        Log.d("insert",list.size()-1+"insert");
        //在数据集中的位置
        adapter.notifyItemInserted(list.size()-1);//是否应该减1
//        adapter.notifyItemRangeChanged(list.size(),list.size());
        List<ChatCircle> chatCircleList=LitePal.where("uid=?",uid).find(ChatCircle.class);
        String cid;
        if(chatCircleList.size()!=0){
            cid=Collections.max(chatCircleList)+"";
        }else{
            cid="0";
        }

        UserCircle userCircle=new UserCircle(chatCircle.getCid(),uid,uid,1,0);//同时要更新两个
        Log.d("new make123",uid+"  "+cid);
        userCircle.save();

        update();

    }
    ////////////////////////////////////////菜单
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(Menu.NONE,Menu.FIRST+1,1,"创建").setIcon(android.R.drawable.ic_menu_edit);
        menu.add(Menu.NONE,Menu.FIRST+2,2,"搜索").setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE,Menu.FIRST+3,3,"删除数据").setIcon(android.R.drawable.ic_menu_add);
        return true;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    //todo:这里的tv需要删除，同时很多功能没有完善
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case Menu.FIRST+1:
                //todo：设置一个对话框比较好
                //逻辑需要放在里面实现，否则会挂掉，不要想着传参出来，这就是监控器应该注意的地方
                String cid=LitePal.max(ChatCircle.class,"cid",String.class);//无效语句
                if(cid==null){
                    getDialog(cid);
                }else{
                    getDialog("0");
                }
                break;
            case Menu.FIRST+2:
                search();//前往搜索窗口
                break;
            case Menu.FIRST+3:
                //还需要删除别人的东西
                List<ChatCircle> chatCircleList=LitePal.where("adminId=?",uid).find(ChatCircle.class);
                for(int i=0;i<chatCircleList.size();i++){
                    ChatCircle c=chatCircleList.get(i);
                    LitePal.deleteAll(UserCircle.class,"adminId=? and cid=?",c.getAdminId(),c.getCid());
                    LitePal.deleteAll(Msg.class,"adminId=? and cid=?",c.getAdminId(),c.getCid());
                }




                //一个删除需要删除好多东西！！！
                LitePal.deleteAll(ChatCircle.class,"uid=?",uid);
                LitePal.deleteAll(UserCircle.class,"uid=?",uid);
                LitePal.deleteAll(Msg.class,"uid=?",uid);//删除所有的数据;


                for(int i=list.size()-1;i>=0;i--){
                    list.remove(i);
                    adapter.notifyItemRemoved(i);
                    adapter.notifyItemRangeRemoved(i,list.size());
                }
                adapter.notifyDataSetChanged();
                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref=getSharedPreferences("first",MODE_PRIVATE);//文件名和写入模式，但是只有一种可以选择
                editor=pref.edit();
                editor.clear();
                editor.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDialog(String cid123){
        //1.要有个builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //2. 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.make_circle, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();


        Button confirm = (Button) view.findViewById(R.id.confirm);
        final Button cancel = (Button) view.findViewById(R.id.cancel);
        final EditText editText=(EditText)view.findViewById(R.id.add_circle);

        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //String cid1=LitePal.max(ChatCircle.class,"cid",String.class);//不知道十个之后会是怎样
                List<ChatCircle> chatCircleList=LitePal.where("uid=?",uid).find(ChatCircle.class);
                ChatCircle circle ;
                if(chatCircleList.size()==0){
                    circle =new ChatCircle(editText.getText().toString(),"0",uid,uid);
                }else{
                    Collections.sort(chatCircleList);
                    circle =new ChatCircle(editText.getText().toString(),""+(Integer.parseInt(chatCircleList.get(chatCircleList.size()-1).getCid())+1),uid,uid);
                }
                circle.save();
                addData(circle);
                dialog.dismiss();
            }
        });
        // 取消按钮
        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
        return ;
    }

    //todo 搜索
    public void search(){
        Intent intent=new Intent(CircleActivity.this,SearchActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    //防止异常退出
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        update();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        update();
        Log.d("main", "ondestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
        Log.d("main", "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        update();
        Log.d("main", "onstop");
    }

}





