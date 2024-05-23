package com.example.dayday;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.List;

public class ChatCircleAdapter extends RecyclerView.Adapter<ChatCircleAdapter.ChatCircleHolder>{

    List<ChatCircle> list;
    Context context;//有时候需要进行很多操作的时候，传进来有好处
    //构造函数需要自己写
    String uid;

    ChatCircleAdapter(List<ChatCircle> list, Context context,String uid){
        this.list=list;
        this.context=context;
        this.uid=uid;
    }


    @NonNull
    @Override
    //设置xml
    public ChatCircleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_item, parent, false);
        return new ChatCircleAdapter.ChatCircleHolder(view);
    }

    @Override
    //这里是设置和监听的地方
    public void onBindViewHolder(@NonNull ChatCircleHolder holder, final int position) {
        Log.d("insert",position+"123"+list.size());
        holder.button.setText("name:"+list.get(position).getName()+"    cid:"+list.get(position).getCid());//list 没有同步？
        Log.d("position",""+list.get(position));
        holder.button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ChatCircle circle=list.get(position);
                UserCircle userCircle= LitePal.where("adminId=? and uid=? and cid=?",circle.getAdminId(),circle.getUid(),circle.getCid()).findFirst(UserCircle.class);
                if(userCircle!=null){
                    //进行chat模式
                    Intent intent=new Intent(context,ChatActivity.class);
                    intent.putExtra("cid",list.get(position).getCid());//虽然不需要检测，但还是需要判断的
                    intent.putExtra("uid",uid);
                    intent.putExtra("adminId",list.get(position).getAdminId());
                    context.startActivity(intent);
                }else {
                    setDialog(position);
                }

            }
        });


    }

    void setDialog(final int postion){
        //圈子已经被管理员删除，需要删除该圈子
        //确定
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(context).inflate(
                R.layout.chat_not_in, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById

        Button confirm = (Button) view.findViewById(R.id.confirm);

        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ChatCircle c = list.get(postion);
                LitePal.deleteAll(ChatCircle.class, "uid=? and adminId=? and cid=?", c.getUid(), c.getAdminId(), c.getCid());
                SharedPreferences.Editor editor = context.getSharedPreferences("first", Context.MODE_PRIVATE).edit();
                editor.putBoolean(c.getUid() + c.getCid(), false);
                editor.putString(c.getUid() + c.getCid() + "AAA", "");
                editor.apply();
                editor.commit();
                list.remove(postion);
                notifyDataSetChanged();
                dialog.dismiss();
            }
            }
        );

        dialog.show();

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    //是一个容器，真正进行绑定的地方
    class ChatCircleHolder extends RecyclerView.ViewHolder{
        Button button;
        public ChatCircleHolder(@NonNull View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.to_chat);
        }
    }
}