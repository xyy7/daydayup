package com.example.dayday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    List<Msg> list=new ArrayList<>();//需要先初始化
    EditText inputText;
    Button send;
    Button daka;
    RecyclerView recyclerView;
    MsgAdapter adapter;
    String uid;
    String cid;
    String adminId;
    boolean dakaState=false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    DakaBroadcast dakaBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        uid=getIntent().getStringExtra("uid");
        cid=getIntent().getStringExtra("cid");
        adminId=getIntent().getStringExtra("adminId");
//        pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref=getSharedPreferences("first",MODE_PRIVATE);//文件名和写入模式，但是只有一种可以选择
        initData();
        inputText=(EditText)findViewById(R.id.input_text);
        send=(Button)findViewById(R.id.send);
        daka=(Button)findViewById(R.id.daKa);
        editor=pref.edit();
        //如果发现是true修改


        //如果是第一次启动的话，就开启//todo：删除的时候可以记得删除，虽然不删除实际影响也不大，就是占用内存
        if(pref.getString(uid+adminId+cid+"AAA","").equals("")){

            //首次应该是pendingIntent
//            Intent intent=new Intent(ChatActivity.this,EverydayUpdate.class);
//            intent.putExtra("state","0");//只需要帮忙做一件事，就是修改一下dakastate
//            intent.putExtra("first","yes");
//            startService(intent);
            //设置成每次打开都才能更新，或者下拉刷新啥的
            AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
            long now=System.currentTimeMillis();
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE,1);//避免溢出
            calendar.set(Calendar.HOUR,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
//            calendar.add(Calendar.MINUTE,1);
            long tomorrow= calendar.getTimeInMillis();
            long interval=tomorrow-now;
            long triggerTime= SystemClock.elapsedRealtime()+interval;
            Intent intent=new Intent(this,EverydayUpdate.class);
            intent.putExtra("uid",uid);
            intent.putExtra("cid",cid);
            intent.putExtra("adminId",adminId);

            PendingIntent pi=PendingIntent.getService(this,0,intent,0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        }

        Log.d("main123456",""+pref.getBoolean(uid+adminId+cid,false));
        Log.d("main123456",uid+adminId+cid);
        if(pref.getBoolean(uid+adminId+cid,false)==true){
            dakaState=true;
            daka.setBackgroundColor(Color.GREEN);
        }
        //进入的时候发现初始化为false，那么设置button颜色
        if(dakaState==false)
            daka.setBackgroundColor(Color.parseColor("#cccccc"));

        dakaBroadcast=new DakaBroadcast();//同样只是创建一次

        editor.putString(uid+adminId+cid+"AAA",uid+adminId+cid);
        editor.apply();

//        ArrayList<Boolean> dakaList=new ArrayList<>();
//        dakaList.add(dakaState);




        daka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dakaState){
//                    dakaState=false;//todo：新的一天会刷新
                    //每一次点击都会看是否已经刷新
//                    String state=dakaBroadcast.getState();//返回值有问题，未解之谜
//                    String state=pref.getString(uid+adminId+cid,"");
//                    Log.d("dakastais",state+"  "+dakaBroadcast.getTest());
                    //0则改为1，1则改为0
//                    if(state.equals("0")){
//                        dakaState=false;
//                        daka.setBackgroundColor(Color.parseColor("#6200EE"));
//                    }else{
//                        dakaState=true;
//                        Toast.makeText(ChatActivity.this,"新的一天还没来，不找着急哈！",Toast.LENGTH_SHORT).show();
//                    }
                    Toast.makeText(ChatActivity.this,"新的一天还没来，不找着急哈！",Toast.LENGTH_SHORT).show();
                }else{
                    dakaState=true;
                    editor.putBoolean(uid+adminId+cid,dakaState);
                    editor.apply();
                    Log.d("inButton",""+pref.getBoolean(uid+adminId+cid,false));
                    daka.setBackgroundColor(Color.GREEN);//不能获取颜色的话，可以通过一个状态变量来解决
                    Log.d("cidisxxx",cid+" "+uid+" "+adminId);
                    UserCircle userCircle=LitePal.where("uid=? and adminId=? and cid=?",uid,adminId,cid).findFirst(UserCircle.class);
                    if(userCircle!=null){
                        userCircle.setDays(userCircle.getDays()+1);
                        userCircle.save();
                        Toast.makeText(ChatActivity.this,userCircle.getUid()+"you have finished "+userCircle.getDays()+" days",Toast.LENGTH_SHORT).show();
                    }
//                    dakaBroadcast.setState("1");
                }


            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);

        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(list);
        recyclerView.setAdapter(adapter);

        if(list.size()>5){
            recyclerView.scrollToPosition(list.size()-5);//全部加载，但是只显示5个（其实可能是下拉刷新的时候获取
        }

        //发送数据也可以通过内存，而不是通过广播
        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String content=inputText.getText().toString();
                if(!"".equals(content)){
                    content=uid+":\n"+content;//存储的时候直接多存储一些，便于操作
                    Msg msg=new Msg(Msg.TYPE_SEND,content,System.currentTimeMillis(),uid,cid,uid,adminId);//sendId,adminId
                    list.add(msg);
//                    msg.setTime(System.currentTimeMillis());
//                    msg.setUid(uid);
//                    msg.setCid(cid);
                    msg.save();
                    adapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    inputText.setText("");
                }
            }
        });

    }

    ////////////////////////////////////////菜单
    public boolean onCreateOptionsMenu(Menu menu){
//        menu.add(Menu.NONE,Menu.FIRST+1,1,"清空").setIcon(android.R.drawable.ic_menu_edit);
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent=new Intent(ChatActivity.this,OperateActivity.class);
        intent.putExtra("uid",uid);
        intent.putExtra("cid",cid);
        intent.putExtra("adminId",adminId);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        Intent intent=new Intent(ChatActivity.this,OperateActivity.class);
//        intent.putExtra("uid",uid);
//        intent.putExtra("cid",cid);
//        startActivity(intent);
//        // 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用
//        return false;
//    }

    void initData(){
        List<Msg> msgList=LitePal.where("adminId=? and cid=?",adminId,cid).find(Msg.class);

        if(msgList.size()==0){
//            Msg msg1=new Msg(Msg.TYPE_RECEIVED,"system:\nHello guy");
//            list.add(msg1);
//            Msg msg2=new Msg(Msg.TYPE_SEND,"system:\nhello, who's that");
//            list.add(msg2);
            Toast.makeText(ChatActivity.this,"no information now!",Toast.LENGTH_SHORT).show();
        }else{
            Collections.sort(msgList);
            for(Msg msg:msgList){
                msg.setType(uid);//根据uid来设置，函数还是很有用的
                list.add(msg);
            }
        }
    }



}
