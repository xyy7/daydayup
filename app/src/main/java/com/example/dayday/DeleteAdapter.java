package com.example.dayday;

import android.content.Context;
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

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.Viewholder> {
    List<UserCircle> list;
    Context context;
    String uid;

    public DeleteAdapter(List<UserCircle> list, Context context, String uid) {
        this.list = list;
        this.context = context;
        this.uid = uid;
    }

    DeleteAdapter(List<UserCircle> list){this.list=list;}
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_item, parent, false);
        return new DeleteAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {
        holder.textView.setText(list.get(position).getUid());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCircle u=list.get(position);
                if(uid.equals(list.get(position).getUid())){
                    Toast.makeText(context,"你不能删除自己，除非解散圈子",Toast.LENGTH_SHORT).show();
                }else{
                    LitePal.deleteAll(UserCircle.class,"adminId=? and uid=? and cid=?",u.getAdminId(),u.getUid(),u.getCid());
//                    LitePal.deleteAll(ChatCircle.class,"adminId=? and uid=? and cid=?",u.getAdminId(),u.getUid(),u.getCid());
                    //正好可以用来解决不能搞定别人家的问题；就是不删除你，剩点东西，自己来搞定
                    list.remove(position);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        Button button;
        TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            button=(Button)itemView.findViewById(R.id.deletexxx);
            textView=(TextView)itemView.findViewById(R.id.xxxname);
        }
    }
}
