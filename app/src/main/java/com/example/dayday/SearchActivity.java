package com.example.dayday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    List<UserCircle> list=new ArrayList<>();
    RecyclerView recyclerView;
    SearchAdapter adapter;//不会自动转化的么
    String uid;
    String addName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);//R.id 会在这里面查找，如果没有就会报错
        //传递信息
        uid=getIntent().getStringExtra("uid");
        //点击按钮事件，在adapter里面设置
        //这里处理一下数据库的问题
        LitePal.initialize(this);//还要有初始化
        LitePal.getDatabase();//需要有litepal.xml;需要在配置文件里设置;修改的时候记得文件名和报名同时修改
        //todo：初始化recyclerview
//        initData();

        //绑定，其实就是点击，然后跳转事件
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);//修改1
        // 给recyclerView指定一个Linear线性布局
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(list,this,uid);
        recyclerView.setAdapter(adapter);

        final EditText editText=(EditText)findViewById(R.id.search_edit);
        final EditText editText1=(EditText)findViewById(R.id.search_edit1);
        Button button=(Button)findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //重新查的话，先删掉所有的东西
                for(int i=list.size()-1;i>=0;i--){
                    list.remove(i);
                    adapter.notifyItemRemoved(i);
                    adapter.notifyItemRangeRemoved(i,list.size());
                }
                adapter.notifyDataSetChanged();
                String adminId=editText.getText().toString();
                String cid=editText1.getText().toString();
                //因为是直接搜索，所以支持正则查询
//                List<ChatCircle> res=LitePal.where("adminId=?",cuid).where("cid=?",cid).find(ChatCircle.class);
//                Cursor cursor=LitePal.findBySQL("select * from UserCircle where admind="+adminId+" and cid="+cid+";");
//                if(cursor==null){
//                    Log.d("cursor","cursor is null");
//                }else{
//                    Log.d("cursor","cursor has something");
//                }
                List<UserCircle> res=LitePal.where("adminId=?  and cid=?",adminId,cid).find(UserCircle.class);
                if(res.size()==0){
                    Toast.makeText(SearchActivity.this,"not found",Toast.LENGTH_LONG).show();
                }else{
                    for(UserCircle circle:res){
                        list.add(circle);
                        adapter.notifyItemInserted(list.size()-1);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
