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

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder>{
    List<UserCircle> list;
    String uid;
    String cid;
    Context context;

    ApplyAdapter(List<UserCircle> list,String uid,String cid){
        this.list=list;
        this.uid=uid;
        this.cid=cid;
    }

    ApplyAdapter(List<UserCircle> list,Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    //设置xml
    public ApplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apply_item, parent, false);
        return new ApplyAdapter.ViewHolder(view);
    }

    @Override
    //这里是设置和监听的地方
    public void onBindViewHolder(@NonNull final ApplyAdapter.ViewHolder holder, final int position) {
        holder.textView.setText("申请人:"+list.get(position).getUid());
        //能到这里的都是
        final String adminId= list.get(position).getAdminId();
        cid=list.get(position).getCid();
        uid=list.get(position).getUid();
        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要标记一个用户
                UserCircle userCircle= LitePal.where("adminId=? and cid=? and state=? and uid=?",adminId,cid,"0",uid).findFirst(UserCircle.class);
                if(userCircle!=null){
                    userCircle.setState(1);
                    userCircle.save();
                    ChatCircle c=LitePal.where("adminId=? and cid=?",adminId,cid).findFirst(ChatCircle.class);
                    if(c!=null){
                        ChatCircle chatCircle=new ChatCircle(c.getName(),c.getCid(),c.getAdminId(),c.getUid());
                        chatCircle.setUid(uid);
                        chatCircle.save();
                    }else{
                        Log.w("holdagree","no chatcircle");
                    }
                }
                list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context,"成功添加"+holder.textView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //但是其实并不能标识出来
                UserCircle userCircle= LitePal.where("adminId=? and cid=? and state=? and uid=?",adminId,cid,"0",uid).findFirst(UserCircle.class);
                if(userCircle!=null){
                    LitePal.deleteAll(UserCircle.class,"adminId=? and cid=? and state=? and uid=?",adminId,cid,"0",uid);
                }
                list.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context,"拒绝"+holder.textView.getText().toString()+"的加入",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    //是一个容器，真正进行绑定的地方
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button agree;
        Button refuse;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.apply_info);//内部变量可以直接检测出来
            agree=(Button)itemView.findViewById(R.id.agree);
            refuse=(Button)itemView.findViewById(R.id.refuse);
        }
    }
}
