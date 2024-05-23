package com.example.dayday;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.*;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private List<Plan> mPlanList;
    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态
    private Context context;
    private int h;
    private int m;


    PlanAdapter(Context context,List<Plan> mPlanList) {
        this.mPlanList = mPlanList;
        this.context=context;
        initData();
    }

    /////////////////////////////点击，深色，但是没反应
//    private OnMyItemClickListener myItemClickListener;

//    public interface OnMyItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setMyItemClickListener(OnMyItemClickListener myItemClickListener) {
//        this.myItemClickListener = myItemClickListener;
//    }
//
//    private int selectPosition;
//
//    public int getSelectPosition() {
//        return selectPosition;
//    }

//    public void setSelectPosition(int selectPosition) {
//        this.selectPosition = selectPosition;
//    }
    ////////////////////////////////点击深色，但是没反应

    private void initData() {
        checkStatus = new HashMap<Integer, Boolean>();
        for (int i = 0; i < mPlanList.size(); i++) {
            boolean s=mPlanList.get(i).getCheckBox();
            checkStatus.put(i,s);
        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    //notify 回调会触发该函数
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        initData();
        //根据存储，如果有设置闹钟则维持状态
//        if(mPlanList.get(position).isSetAlarm()){
//            Log.d("ok","close is still useful");
//            h=mPlanList.get(position).getHour();
//            m=mPlanList.get(position).getMin();
//            setAlarm(position);
//        }
//        //防止焦点滑动出错,针对editText
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
//                mPlanList.get(position).setTextView(s.toString());
//            }
//        };

//        if (holder.editText1.getTag() instanceof TextWatcher) {
//            holder.editText1.removeTextChangedListener((TextWatcher) holder.editText1.getTag());
//        }
//        TextWatcher watcher1 = new TextWatcher() {
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
//                mPlanList.get(position).setTextView1(s.toString());
//            }
//        };


        //增添控件的时候，这里也是需要修改的，第s三处
        //这里才是绑定部分的精髓
        Plan plan = mPlanList.get(position);
//        holder.fruitImage.setImageResource(plan.getImageId());
//        holder.fruitName.setText(fruit.getName());
        holder.editText.setText(plan.getTextView());
        holder.editText1.setText(plan.getTextView1());
//        holder.textView.setText(fruit.getTextView().getText());

        holder.checkBox.setOnCheckedChangeListener(null);//清掉监听器
        holder.checkBox.setChecked(checkStatus.get(position));//设置选中状态
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//再设置监听器
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkStatus.put(position, isChecked);//check状态一旦改变，保存的check值也要发生相应的变化
                Plan plan1=mPlanList.get(position);
                plan1.setCheckBox(isChecked);
                mPlanList.set(position,plan1);
                Log.d("check",""+isChecked);
            }
        });
        holder.button.setText(plan.getButton().getText());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"you have clikc the "+position+"th button",Toast.LENGTH_SHORT).show();
                layDialog(position);
            }
        });


        //防止焦点滑动出错
//        holder.editText.addTextChangedListener(watcher);
//        holder.editText.setTag(watcher);
//        holder.editText1.addTextChangedListener(watcher1);
//        holder.editText1.setTag(watcher1);

    }

    //需要有一定的顺序，因为需要解决并发的问题
    private void setAlarm(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND,0);
        //理论上还要更新plan计划
        Log.d("hour",""+h+" "+m);
        Plan plan=mPlanList.get(position);
        plan.setHour(h);
        plan.setMin(m);
        plan.setSetAlarm(true);
        mPlanList.set(position,plan);

        Log.d("com123",""+calendar.getTimeInMillis()+" "+Calendar.getInstance().getTimeInMillis());
        if(calendar.getTimeInMillis()<Calendar.getInstance().getTimeInMillis()){
            Log.d("xiangcha",(calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())+"");
            Log.d("com",""+calendar.getTimeInMillis()+" "+Calendar.getInstance().getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,MyService.class);
        intent.putExtra("plan",mPlanList.get(position).getTextView());
        Log.d("naozhong",mPlanList.get(position).getTextView()+""+666+"pos:"+position);
        intent.setAction(MyService.ACTION_ALARM);
        //requestCode:设置成不同的，表示不同事件
        PendingIntent pendingIntent = PendingIntent.getService(context, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Toast.makeText(context, "设置闹钟ing", Toast.LENGTH_SHORT).show();
        assert am != null;
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void layDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
//        final TextView edt = (TextView) view.findViewById(R.id.edt);
        final TimePicker alarm=(TimePicker)view.findViewById(R.id.timePicker1);
        alarm.setIs24HourView(true);//如果没有设置，那么默认就是现在这个时刻
        alarm.setMinute((Calendar.getInstance().get(Calendar.MINUTE)-10+60)%60);
        alarm.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //获取设置完的时间
                h=hourOfDay;
                m=minute;
            }
        });

        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //todo 存储到litepal或者share里面,然后关闭
                Toast.makeText(context,"set successfully",Toast.LENGTH_SHORT).show();
                setAlarm(position);
                dialog.dismiss();

            }
        });
        // 取消按钮
        cancel.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fruitImage;
        TextView editText;
        TextView editText1;
        CheckBox checkBox;
        Button button;
        int h;
        int m;



        //这里同样需要修改，设置布局，第二处
        ViewHolder(View itemView) {
            super(itemView);

//            this.fruitName = itemView.findViewById(R.id.fruit_name);
            this.editText = itemView.findViewById(R.id.edit_text);
            this.editText1=itemView.findViewById(R.id.time_bound);
            this.button=itemView.findViewById(R.id.alarm_clock);
//            this.textView = itemView.findViewById(R.id.text_view);
            this.checkBox=itemView.findViewById(R.id.check_box);


//            //点击，但是没有反应
//            if (myItemClickListener != null) {
//                itemView.setOnClickListener(new OnClickListener(){
//                    @Override
//                    public void onClick(View view) {
//                        Log.v("MainActivity","item_listener");//有进来
//                        myItemClickListener.onItemClick(getAdapterPosition());
//                    }
//                });
//            } ;
        }
    }
}
