package com.example.dayday;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private List<Plan> planList = new ArrayList<>();
    PlanAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    String userId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");

        /////////////////////////////////////
        Button button=(Button) findViewById(R.id.change_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"you have click it",Toast.LENGTH_SHORT).show();
                Log.v("MainActivity","click in it");
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Button button1=(Button)findViewById(R.id.chat_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CircleActivity.class);
                intent.putExtra("uid",userId);
                startActivity(intent);
            }
        });

        Button button2=(Button)findViewById(R.id.habit_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        //////////////////////////////////////////////
        //这里处理一下数据库的问题
        LitePal.initialize(this);//还要有初始化
        LitePal.getDatabase();//需要有litepal.xml;需要在配置文件里设置;修改的时候记得文件名和报名同时修改
        //todo：初始化recyclerview
        initPlan();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // 给recyclerView指定一个Linear线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlanAdapter(MainActivity.this,planList);
        recyclerView.setAdapter(adapter);


        //点击，深色，但是没有反应
        //没有效果
//        adapter.setMyItemClickListener(new PlanAdapter.OnMyItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                adapter.setSelectPosition(position);
//                Log.d("position",position+"789456");
//                adapter.notifyDataSetChanged();
//                Log.v("MainActivity","click in it");
//            }
//        });



        //实现拖动
        //1.创建item helper
        itemTouchHelper = new ItemTouchHelper(callback);
        //2.绑定到recyclerview上面去
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //3.在ItemHelper的接口回调中过滤开启长按拖动，拓展其他操作

    }

    //防止异常退出
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        update();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initPlan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        update();
        Log.d("main", "ondestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
        Log.d("main", "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        update();
        Log.d("main", "onstop");
    }

    public void update(){
        List<Plan_data> list = LitePal.where("userId=?",userId).find(Plan_data.class);
//        Log.d("mid1", "" + habit_dataList1.size());
        if (planList!= null && planList.size() != 0) {
            for (int i = 0; i < planList.size(); i++) {
                Plan plan = planList.get(i);
                plan.setMyId(i + 1);
                planList.set(i, plan);
                if(adapter!=null)//如果已经绑定,就可以重新刷新
                    adapter.notifyItemChanged(i);
            }
//            Log.d("ht.size", "" + habitList.size());
            LitePal.deleteAll(Plan_data.class,"userId=?",userId);//理论上不应该如此暴力
            for (Plan plan:planList) {
                Plan_data pd=new Plan_data(plan);
                Log.d("logd","pd_data"+pd.getPlanName()+pd.getUserId());
                pd.save();
            }
        }
//        List<Habit_data> habit_dataList = LitePal.findAll(Habit_data.class);
//        Log.d("mid2", "" + habit_dataList.size());

    }


    //初始化的时候，记得在这里也要修改，否则会出现空指针报错，下面的add也一样，第一处
    private void initPlan() {
        List<Plan_data> list= LitePal.where("userId=?",userId).find(Plan_data.class);
        Log.d("size555745",userId+' '+list.size());
        List<Plan_data> list1= LitePal.findAll(Plan_data.class);
        if(list1.size()!=0)
        Log.d("size777756",""+list1.get(0).getUserId()+list1.size());
        if(list.size()==0){
//            for (int i = 0; i < 10; i++) {
//                TextView TextView=new TextView(this);
//                CheckBox checkBox=new CheckBox(this);
//                TextView TextView1=new TextView(this);
//                Button button=new Button(this);
//                button.setText("闹钟");
//                Plan plan = new Plan(TextView,TextView1,checkBox,button,i+1,userId);
//                planList.add(plan);
//                Plan_data pd=new Plan_data(plan);
//                pd.save();
//                if(adapter!=null)//如果已经绑定,就可以重新刷新
//                    adapter.notifyItemChanged(i);
//            }
                TextView textView=(TextView)findViewById(R.id.hintSomething);
                textView.setVisibility(View.VISIBLE);
            }

        else{
            TextView textView = (TextView) findViewById(R.id.hintSomething);
            textView.setVisibility(View.GONE);
            for(Plan_data pd:list){
                TextView TextView=new TextView(this);
                CheckBox checkBox=new CheckBox(this);
                TextView TextView1=new TextView(this);
                Button button=new Button(this);
                button.setText("闹钟");
                TextView.setText(pd.getPlanName());
                TextView1.setText(pd.getStEnd());
                checkBox.setChecked(pd.isPlanIsFinished());
                Plan plan = new Plan(TextView,TextView1,checkBox,button,pd.getMyId(),userId);
                plan.setSetAlarm(pd.isAlarmIsSet());
                plan.setHour(pd.getHour());
                plan.setMin(pd.getMin());
                planList.add(plan);

            }

        }
    }

    public void addData(int position) {
        TextView TextView=new TextView(this);
        CheckBox checkBox=new CheckBox(this);
        TextView TextView1=new TextView(this);
        Button button=new Button(this);
        button.setText("闹钟");
        Plan plan = new Plan(TextView,TextView1,checkBox,button,planList.size(),userId);
        planList.add(plan);
        adapter.notifyItemInserted(position);
        adapter.notifyItemRangeChanged(position, planList.size()-position);
        update();
    }

    public void removeData(int position) {
        Plan_data plan_data=LitePal.where("userId=?",userId).where("myId=?",""+planList.get(position).getMyId()).findFirst(Plan_data.class);
        plan_data.delete();
        planList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, planList.size()-position);
        update();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(Menu.NONE,Menu.FIRST+1,1,"clear checkbox").setIcon(android.R.drawable.ic_menu_edit);
        menu.add(Menu.NONE,Menu.FIRST+2,2,"add at last").setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE,Menu.FIRST+3,3,"delete last").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE,Menu.FIRST+4,4,"delete all").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE,Menu.FIRST+5,5,"quit").setIcon(android.R.drawable.ic_menu_info_details);
        return true;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    //todo:这里的tv需要删除，同时很多功能没有完善
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case Menu.FIRST+1:
                for(int i=0;i<planList.size();i++){
                    Plan plan=planList.get(i);
                    plan.setCheckBox(false);
                    planList.set(i,plan);
                    adapter.notifyItemChanged(i);

                }
                break;
            case Menu.FIRST+2:
//                addData(2);
                setDialog();
                break;
            case Menu.FIRST+3:
                removeData(planList.size()-1);
                break;
            case Menu.FIRST+4:
                LitePal.deleteAll(Plan_data.class,"userId=?",userId);
                for(int i=planList.size()-1;i>=0;i--){
                    planList.remove(i);
                    adapter.notifyItemRemoved(i);
                    adapter.notifyItemRangeRemoved(i,planList.size());
                }
                adapter.notifyDataSetChanged();
                break;
            case Menu.FIRST+5:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.plan_dialog, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final EditText name=(EditText)view.findViewById(R.id.plan_name_add);
        final EditText time=(EditText)view.findViewById(R.id.plan_time_add);

        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText editText=new EditText(MainActivity.this);
                editText.setText(name.getText().toString());
                CheckBox checkBox=new CheckBox(MainActivity.this);
                EditText editText1=new EditText(MainActivity.this);
                editText1.setText(time.getText().toString());
                Button button=new Button(MainActivity.this);
                button.setText("闹钟");
                Plan plan = new Plan(editText,editText1,checkBox,button,planList.size(),userId);
                planList.add(plan);
                int pos=planList.size()-1;
                adapter.notifyItemInserted(pos);
                adapter.notifyItemRangeChanged(pos, planList.size()-pos);
                update();
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



    /////////////////////////////////////拖动
    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

        /**
         * 官方文档的说明如下：
         * o control which actions user can take on each view, you should override getMovementFlags(RecyclerView, ViewHolder)
         * and return appropriate set of direction flags. (LEFT, RIGHT, START, END, UP, DOWN).
         * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
         * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
         * 不过点入到源码里面有惊喜。源码标记方向如下：
         *  public static final int UP = 1     0001
         *  public static final int DOWN = 1 << 1; （位运算：值其实就是2）0010
         *  public static final int LEFT = 1 << 2   左 值是3
         *  public static final int RIGHT = 1 << 3  右 值是8
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //也就是说返回值是组合式的
            //makeMovementFlags (int dragFlags, int swipeFlags)，看下面的解释说明
            int swipFlag=0;
            //如果也监控左右方向的话，swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            int dragflag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //等价于：0001&0010;多点触控标记触屏手指的顺序和个数也是这样标记哦
            return  makeMovementFlags(dragflag,swipFlag);

            /**
             * 备注：由getMovementFlags可以联想到setMovementFlags，不过文档么有这个方法，但是：
             * 有 makeMovementFlags (int dragFlags, int swipeFlags)
             * Convenience method to create movement flags.便捷方法创建moveMentFlag
             * For instance, if you want to let your items be drag & dropped vertically and swiped left to be dismissed,
             * you can call this method with: makeMovementFlags(UP | DOWN, LEFT);
             * 这个recyclerview的文档写的简直完美，示例代码都弄好了！！！
             * 如果你想让item上下拖动和左边滑动删除，应该这样用： makeMovementFlags(UP | DOWN, LEFT)
             */

            //拓展一下：如果只想上下的话：makeMovementFlags（UP | DOWN, 0）,标记方向的最小值1
        }



        /**
         * 官方文档的说明如下
         * If user drags an item, ItemTouchHelper will call onMove(recyclerView, dragged, target). Upon receiving this callback,
         * you should move the item from the old position (dragged.getAdapterPosition()) to new position (target.getAdapterPosition())
         * in your adapter and also call notifyItemMoved(int, int).
         * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
         * viewHolde:正在拖动item
         * target：要拖到的目标
         * @return true 表示消费事件
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //直接按照文档来操作啊，这文档写得太给力了,简直完美！
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            //注意这里有个坑的，itemView 都移动了，对应的数据也要移动
            Collections.swap(planList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }



        /**
         * 谷歌官方文档说明如下：
         * 这个看了一下主要是做左右拖动的回调
         * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls onSwiped(ViewHolder, int).
         * At this point, you should update your adapter (e.g. remove the item) and call related Adapter#notify event.
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //暂不处理
        }



        /**
         * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
         * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
         * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
         * @param recyclerView
         * @param current
         * @param target
         * @return
         */
        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            return true;
        }


        /**
         * 官方文档说明如下：
         * Returns whether ItemTouchHelper should start a drag and drop operation if an item is long pressed.
         * 是否开启长按 拖动
         * @return
         */
        @Override
        public boolean isLongPressDragEnabled() {
            //return true后，可以实现长按拖动排序和拖动动画了
            return true;
        }
    };

}

