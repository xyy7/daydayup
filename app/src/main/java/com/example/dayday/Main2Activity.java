package com.example.dayday;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
生成apk要生成release的apk
如果不是，那么可以选择关闭开发者模式
 */
public class Main2Activity extends AppCompatActivity {

    private List<Habit> habitList = new ArrayList<>();
    HabitAdapter adapter;
    private TextView tv;
    private ItemTouchHelper itemTouchHelper;
    RecyclerView recyclerView;
    TextView textView;
    String userId;
    AimScore aimScore;
    Button button;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);//修改2
//            DebugDB.getAddressLog();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent=getIntent();
        userId=intent.getStringExtra("userId");
        initHabits();
        textView = (TextView) findViewById(R.id.score);
        textView.setText("TOTAL:" + aimScore.getHadTotal() + "       /"+aimScore.getTotal()+"\nTODAY:" + aimScore.getHadToday()
                + "      /"+aimScore.getToday());
        button = (Button) findViewById(R.id.add_score);
        button.setText(aimScore.getFirst());
        TextView score=(TextView) findViewById(R.id.score);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = textView.getText().toString();
//                    Log.d("here",s);
                String d1 = "", d2 = "";
                int i;
                for (i = 0; i < s.indexOf('/'); i++) {
                    if (Character.isDigit(s.charAt(i))) {
                        d1 = d1 + s.charAt(i);
                    }
                }
                for (i = s.lastIndexOf(':'); i < s.lastIndexOf('/'); i++) {
                    if (Character.isDigit(s.charAt(i))) {
                        d2 = d2 + s.charAt(i);
                    }
                }
                int first=Integer.parseInt(button.getText().toString());
                Integer x = Integer.parseInt(d1) + first;
                Integer y = Integer.parseInt(d2) + first;

                textView.setText("TOTAL:" + x + "       /"+aimScore.getTotal()+"\nTODAY:" + y + "      /"+aimScore.getToday());
                aimScore = LitePal.where("userId=?",userId).findFirst(AimScore.class);
//                    Log.d("b1:",""+aimScore.getText()+aimScore.getMyId());
                aimScore.setHadToday(x.toString());
                aimScore.setHadTotal(y.toString());
                aimScore.save();
            }
        });
        button1 = (Button) findViewById(R.id.add_score1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String s = textView.getText().toString();
//                    Log.d("here",s);
                String d1 = "", d2 = "";
                int i;
                for (i = 0; i < s.indexOf('/'); i++) {
                    if (Character.isDigit(s.charAt(i))) {
                        d1 = d1 + s.charAt(i);
                    }
                }
                for (i = s.lastIndexOf(':'); i < s.lastIndexOf('/'); i++) {
                    if (Character.isDigit(s.charAt(i))) {
                        d2 = d2 + s.charAt(i);
                    }
                }
                int first=Integer.parseInt(button1.getText().toString());
                Integer x = Integer.parseInt(d1) + first;
                Integer y = Integer.parseInt(d2) + first;

                textView.setText("TOTAL:" + x + "       /"+aimScore.getTotal()+"\nTODAY:" + y + "      /"+aimScore.getToday());
                aimScore = LitePal.where("userId=?",userId).findFirst(AimScore.class);
                aimScore.setHadToday(x.toString());
                aimScore.setHadTotal(y.toString());
                aimScore.save();

            }
        });


        //这里处理一下数据库的问题
        LitePal.initialize(this);//还要有初始化
        LitePal.getDatabase();//需要有litepal.xml;需要在配置文件里设置;修改的时候记得文件名和报名同时修改
        //todo：初始化recyclerview

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_habit);//修改1
        // 给recyclerView指定一个Linear线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HabitAdapter(habitList);
        recyclerView.setAdapter(adapter);

        //点击，深色，但是没有反应
        adapter.setMyItemClickListener(new HabitAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
                Log.v("MainActivity", "click in it");
            }
        });


        //todo: 实现拖动
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
        initHabits();
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





    //这里也需要修改，第四处
    //根据habitlist做最新修改
    void update() {

        List<Habit_data> habit_dataList1 = LitePal.where("userId=?",userId).find(Habit_data.class);
        Log.d("mid1", "" + habit_dataList1.size());
        if (habitList != null && habitList.size() != 0) {
            for (int i = 0; i < habitList.size(); i++) {
                Habit habit1 = habitList.get(i);
                habit1.setMyId(i + 1);
                habitList.set(i, habit1);
                if(adapter!=null)//如果已经绑定,就可以重新刷新
                    adapter.notifyItemChanged(i);
            }
            Log.d("ht.size", "" + habitList.size());
            LitePal.deleteAll(Habit_data.class,"userId=?",userId);
            for (Habit habit : habitList) {
                Habit_data hd=new Habit_data(habit);
                hd.save();
            }
        }
        List<Habit_data> habit_dataList = LitePal.where("userId=?",userId).find(Habit_data.class);
        Log.d("mid2", "" + habit_dataList.size());
    }





    //todo：这里都需要搞定数据库的问题
    //初始化的时候，记得在这里也要修改，否则会出现空指针报错，下面的add也一样，第一处
    private void initHabits() {
        aimScore=LitePal.where("userId=?",userId).findFirst(AimScore.class);
        if(aimScore!=null){

        }else{
            aimScore=new AimScore(userId);
            aimScore.save();
        }

        List<Habit_data> list= LitePal.where("userId=?",userId).find(Habit_data.class);
        Log.v("main3","restart?no");
//        DBThread dt = new DBThread();
//        Thread thread = new Thread(dt);
//        thread.start();

        if(list.size()==0);
//            for (int i = 0; i < 10; i++) {
//                EditText editText=new EditText(this);
//                TextView textView=new TextView(this);
//                CheckBox checkBox=new CheckBox(this);
//                textView.setText("c_day=0");
//                Habit habit=new Habit(checkBox,editText,textView,(long)i+1,userId);//id是从1开始的
//                habitList.add(habit);
//                Habit_data hd=new Habit_data(habit);
////                Habit_data hd=new Habit_data(habit.getCheckBox().isChecked(),
////                        habit.getEditText().getText().toString(),habit.getTextView().getText().toString(),habit.getMyId());
//                hd.save();
//                if(adapter!=null)//如果已经绑定,就可以重新刷新
//                    adapter.notifyItemChanged(i);
//            }
        else{
            for(Habit_data hd:list){
                EditText editText=new EditText(this);
                TextView textView=new TextView(this);
                CheckBox checkBox=new CheckBox(this);
                editText.setText(hd.getEd_state());
                checkBox.setChecked(hd.isCk_state());
                Log.d("restart:"," "+checkBox.isChecked());
                textView.setText(hd.getTx_state());
                Habit habit=new Habit(checkBox,editText,textView,hd.getMyId(),userId);
                habitList.add(habit);

            }

        }
    }

    public void addData(int position) {
        EditText editText=new EditText(this);
        TextView textView=new TextView(this);
        CheckBox checkBox=new CheckBox(this);
        textView.setText("c_day=0");
        Habit habit=new Habit(checkBox,editText,textView,habitList.size(),userId);//有问题，删除后，可能会重复
        habitList.add(habit);
        adapter.notifyItemInserted(2);
        adapter.notifyItemRangeChanged(2,habitList.size()-2);
        update();

    }

    //todo 注意还需要解决数据库问题
    public void removeData(int position) {
        Habit_data habit_data=LitePal.where("userId=?",userId).where("myId=?",""+habitList.get(position).getMyId()).findFirst(Habit_data.class);
        habit_data.delete();
        habitList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position,habitList.size()-position);
        update();
    }

    ////////////////////////////////////////菜单
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(Menu.NONE,Menu.FIRST+1,1,"flush state").setIcon(android.R.drawable.ic_menu_edit);
        menu.add(Menu.NONE,Menu.FIRST+2,2,"add at last").setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE,Menu.FIRST+3,3,"delete last").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE,Menu.FIRST+4,4,"delete all").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE,Menu.FIRST+5,5,"set aimScore").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE,Menu.FIRST+6,6,"quit").setIcon(android.R.drawable.ic_menu_info_details);
        return true;

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    //todo:这里的tv需要删除，同时很多功能没有完善
    public boolean onOptionsItemSelected(MenuItem item){
//            tv=(TextView)findViewById(R.id.tv);
        switch(item.getItemId()){
            case Menu.FIRST+1:
//                    tv.setText("你点击了保存菜单");
                for(int i=0;i<habitList.size();i++){
                    Habit habit=habitList.get(i);
                    habit.setCheckBox(false);
                    habitList.set(i,habit);
                    adapter.notifyItemChanged(i);

                }
                TextView textView=(TextView)findViewById(R.id.score);
                String s=textView.getText().toString();
                String d1="",d2="";
                int i;
                for(i=0;i<s.indexOf('/');i++){
                    if(Character.isDigit(s.charAt(i))) {
                        d1 = d1 + s.charAt(i);
                    }
                }
                textView.setText("TOTAL:"+d1+"       /100000\nTODAY:"+0+"      /100");
                AimScore aimScore =LitePal.where("userId=?",userId).findFirst(AimScore.class);
//                    Log.d("b1:",""+aimScore.getText()+aimScore.getMyId());
                aimScore.setHadTotal(d1);
                aimScore.setHadToday(d2);
                aimScore.save();
                break;
            case Menu.FIRST+2:
                setDialog();
                break;
            case Menu.FIRST+3:
                removeData(habitList.size()-1);
                break;
            case Menu.FIRST+4:
                LitePal.deleteAll(Habit_data.class,"userId=?",userId);
                LitePal.deleteAll(AimScore.class);
                for(int j=habitList.size()-1;j>=0;j--){
                    habitList.remove(j);
                    adapter.notifyItemRemoved(j);
                    adapter.notifyItemRangeRemoved(j,habitList.size());
                }
                adapter.notifyDataSetChanged();
                break;
            case Menu.FIRST+6:
                finish();
                break;
            case Menu.FIRST+5:
                setAimDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void setDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(Main2Activity.this).inflate(
                R.layout.habit_dialog, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final EditText name=(EditText)view.findViewById(R.id.habit_name_add);

        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText editText=new EditText(Main2Activity.this);
                editText.setText(name.getText().toString());
                CheckBox checkBox=new CheckBox(Main2Activity.this);
                TextView textView=new TextView(Main2Activity.this);
                textView.setText("0");
//                Plan plan = new Plan(editText,textView,checkBox,but,planList.size(),userId);
                Habit habit=new Habit(checkBox,editText,textView,habitList.size(),userId);
                habitList.add(habit);
                int pos=habitList.size()-1;
                adapter.notifyItemInserted(pos);
                adapter.notifyItemRangeChanged(pos, habitList.size()-pos);
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

    void setAimDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(Main2Activity.this).inflate(
                R.layout.aim_layout, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final EditText first=(EditText)view.findViewById(R.id.first_add);
        final EditText second=(EditText)view.findViewById(R.id.second_add);
        final EditText total=(EditText)view.findViewById(R.id.total_score);
        final EditText today=(EditText)view.findViewById(R.id.today_score);

        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        // 设置button的点击事件及获取editview中的文本内容
        confirm.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

//                Plan plan = new Plan(editText,textView,checkBox,but,planList.size(),userId);
                //存储和改变
                button.setText("+"+first.getText().toString());
                button1.setText("+"+second.getText().toString());
                textView.setText("TOTAL:" + aimScore.getHadTotal() + "       /"+total.getText().toString()+"\nTODAY:" + aimScore.getHadToday() + "      /"+today.getText().toString());
                aimScore= LitePal.where("userId=?",userId).findFirst(AimScore.class);
                aimScore.setFirst(button.getText().toString());
                aimScore.setSecond(button1.getText().toString());
                aimScore.setTotal(total.getText().toString());
                aimScore.setToday(today.getText().toString());
                aimScore.save();



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



    //todo: 拖拽功能实现，同时还要改变数据库里面的东西
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
            Collections.swap(habitList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                List<Habit_data> habit_dataList=LitePal.findAll(Habit_data.class);
//                Collections.swap(habit_dataList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            update();//不知道是否有+-1操作
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
