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

public class ApplyInfoActivity extends AppCompatActivity {

    String TAG="APPLYINFOACTIVITY";
    List<UserCircle> list=new ArrayList<>();
    RecyclerView recyclerView;
    ApplyAdapter adapter;//不会自动转化的么
    String uid;
    String cid;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_info);//R.id 会在这里面查找，如果没有就会报错
        //传递信息
        uid=getIntent().getStringExtra("uid");
        cid=getIntent().getStringExtra("cid");
        adminId=getIntent().getStringExtra("adminId");
        //点击按钮事件，在adapter里面设置
        //这里处理一下数据库的问题
        initData();

        //绑定，其实就是点击，然后跳转事件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_apply);//修改1
        // 给recyclerView指定一个Linear线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(ApplyInfoActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ApplyAdapter(list,ApplyInfoActivity.this);
        recyclerView.setAdapter(adapter);

    }

    public void initData(){
        //uid是null的时候，会有the bind value at index 1 is null 的报错
        //这里应该是adminId的
        List<UserCircle> userCircles= LitePal.where("adminId=?",adminId).where("cid=?",cid).where("state=?","0").find(UserCircle.class);
        Log.d("initData",""+list.size());
        if(userCircles.size()!=0){
            Log.d(TAG,"someone here");
            for(int i=0;i<userCircles.size();i++){
                list.add(userCircles.get(i));
            }
        }else{
            //todo： 到时需要注释掉
//            for(int i=0;i<5;i++){
//                Log.d("ffflog","123"+i);
//                list.add(new UserCircle(cid,"123"+i,adminId,0,1));
//            }
        }
    }
}
