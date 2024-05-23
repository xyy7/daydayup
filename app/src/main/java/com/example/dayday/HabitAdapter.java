package com.example.dayday;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> implements Serializable {

    private List<Habit> myList;
    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态



    HabitAdapter(List<Habit> myList) {
        this.myList=myList;
        initData();
    }

    //todo：需要修改，只在初始化的时候使用，并且notify的时候难以解决
    private void initData() {
        checkStatus = new HashMap<Integer, Boolean>();
        //因为checkdata，根据这个来，所以有点麻烦
        for (int i = 0; i < myList.size(); i++) {
            boolean s=myList.get(i).getCheckBox().isChecked();
            checkStatus.put(i,s);
        }
    }

    private OnMyItemClickListener myItemClickListener;

    public interface OnMyItemClickListener {
        void onItemClick(int position);
    }

    public void setMyItemClickListener(OnMyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    private int selectPosition;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        return new ViewHolder(view);
    }

    //notify 回调会触发该函数
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //防止焦点滑动出错,针对editText
//        if (holder.editText.getTag() instanceof TextWatcher) {
//            holder.editText.removeTextChangedListener((TextWatcher) holder.editText.getTag());
//        }
//        TextWatcher watcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //设置fruitlist的，还是设置holder的？
//                myList.get(position).setEditText(s.toString());
//            }
//        };

        initData();//尝试刷新状态；解决notify问题,这里也是需要注意的地方

        //增添控件的时候，这里也是需要修改的，第s三处
        //这里才是绑定部分的精髓,因为有了位置，所以可以又很多操作
        final Habit habit = myList.get(position);
        holder.id=habit.getMyId();
        holder.textView1.setText(habit.getEditText().getText());
        holder.textView.setText(habit.getTextView().getText());
        holder.checkBox.setOnCheckedChangeListener(null);//清掉监听器
        holder.checkBox.setChecked(checkStatus.get(position));//设置选中状态
        //需要同时修改的地方5，因为它自己不会顺便修改list
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatus.put(position, isChecked);//check状态一旦改变，保存的check值也要发生相应的变化
                String res=(String)holder.textView.getText();
//                int index=res.indexOf('=');
//                index++;
//                String s1=res.substring(index);
                String s1=res;
                if(isChecked){
                    String s2=new String((Integer.parseInt(s1)+1)+"");
                    holder.textView.setText(s2);
                    Log.d("position:"," "+position);
                    Habit habit1=myList.get(position);
                    habit1.setTextView(s2);
                    habit1.setCheckBox(true);
                    myList.set(position,habit1);

                }else{
                    String s2=new String((Integer.parseInt(s1)-1)+"");
                    holder.textView.setText(s2);
                    Habit habit1=myList.get(position);
                    habit1.setTextView(s2);
                    habit1.setCheckBox(false);
                    myList.set(position,habit1);
                }
            }
        });


        //防止焦点滑动出错
//        holder.editText.addTextChangedListener(watcher);
//        holder.editText.setTag(watcher);

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView;
        CheckBox checkBox;
        long id;


        //这里同样需要修改，设置布局，第二处
        ViewHolder(View itemView) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.habit_name_text);
            this.textView = itemView.findViewById(R.id.habit_time_text);
            this.checkBox=itemView.findViewById(R.id.check_box);



            if (myItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Log.v("MainActivity","item_listener");//有进来
                        myItemClickListener.onItemClick(getAdapterPosition());
                    }
                });
            } ;
        }
    }

}
