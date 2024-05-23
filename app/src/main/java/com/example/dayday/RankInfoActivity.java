package com.example.dayday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankInfoActivity extends AppCompatActivity {

    List<String> list=new ArrayList<>();
    RecyclerView recyclerView;
    RankAdapter adapter;//不会自动转化的么
    String uid;
    String cid;
    String adminId;//如果数据不是很大的话，那么还是先获取了，这样用的时候也方便

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_list);//R.id 会在这里面查找，如果没有就会报错
        //传递信息
        uid=getIntent().getStringExtra("uid");
        cid=getIntent().getStringExtra("cid");
        adminId=getIntent().getStringExtra("adminId");
        //点击按钮事件，在adapter里面设置
        //这里处理一下数据库的问题
        initData();

        //绑定，其实就是点击，然后跳转事件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);//修改1
        // 给recyclerView指定一个Linear线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(RankInfoActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(list);
        recyclerView.setAdapter(adapter);

    }

    public void initData(){
        //uid是null的时候，会有the bind value at index 1 is null 的报错
        //这里应该是adminId的
        Log.d("cidiswhat",cid);
        List<UserCircle> userCircles=LitePal.where("adminId=? and cid=? and state=?",adminId,cid,"1").find(UserCircle.class);
        if(userCircles.size()!=0){
            Collections.sort(userCircles);
            for(int i=0;i<userCircles.size();i++){
                list.add("NO."+(i+1)+": "+userCircles.get(i).getUid()+" 天数："+userCircles.get(i).getDays());
                Log.d("cidiswhat",userCircles.get(i).getCid());
            }
        }else{

        }
    }
}
