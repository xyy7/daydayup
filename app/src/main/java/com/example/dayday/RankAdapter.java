package com.example.dayday;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    List<String> list;

    RankAdapter(List<String> list){
        this.list=list;
    }

    @NonNull
    @Override
    //设置xml
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
        return new RankAdapter.ViewHolder(view);
    }

    @Override
    //这里是设置和监听的地方
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, final int position) {
       holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //是一个容器，真正进行绑定的地方
    class ViewHolder extends RecyclerView.ViewHolder{
       TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           textView=(TextView) itemView.findViewById(R.id.rank_text);//内部变量可以直接检测出来
        }
    }
}


