package com.example.dayday;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ChatCircleHolder>{

    List<UserCircle> list;
    String uid;
//    String adminId;
//    String cid;
    Context context;//有时候需要进行很多操作的时候，传进来有好处
    //构造函数需要自己写
    SearchAdapter(List<UserCircle> list, Context context,String uid){
        this.list=list;
        this.context=context;
        this.uid=uid;
    }




    @NonNull
    @Override
    //设置xml
    public ChatCircleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.ChatCircleHolder(view);
    }

    @Override
    //这里是设置和监听的地方
    public void onBindViewHolder(@NonNull ChatCircleHolder holder, final int position) {
        ChatCircle chatCircle= LitePal.where("adminId=? and cid=?",list.get(position).getAdminId(),list.get(position).getCid()).findFirst(ChatCircle.class);
        if(chatCircle!=null){
            holder.textView.setText(chatCircle.getName()+" adminId:"+list.get(position).getAdminId());
        }else{
            holder.textView.setText("no found");
        }

//        Log.d("idpos",list.get(position));
        holder.button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //是管理员id和自己的id对比才对
                Log.d("holder",uid+" "+list.get(position).getAdminId());
                apply(uid,list.get(position).getAdminId(),list.get(position).getCid());
            }
        });


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //发送申请？
    public void apply(String uid1,String adminId,String cid){
        if(uid1.equals(adminId)){
            //变色和删除操作还是蛮复杂的
            Toast.makeText(context,"你自己就是管理员了耶",Toast.LENGTH_SHORT).show();
        }else{
            UserCircle userCircle=new UserCircle(cid, uid1,adminId,0,0);//0处于申请状态
            userCircle.save();
            Toast.makeText(context,"ok，请耐心等待审核",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //是一个容器，真正进行绑定的地方
    class ChatCircleHolder extends RecyclerView.ViewHolder{
        Button button;
        TextView textView;
        public ChatCircleHolder(@NonNull View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.apply);
            textView=(TextView)itemView.findViewById(R.id.name);
        }
    }
}