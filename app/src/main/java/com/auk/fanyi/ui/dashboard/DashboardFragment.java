package com.auk.fanyi.ui.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.auk.fanyi.MySQLiteOpenHelper;
import com.auk.fanyi.R;
import com.auk.fanyi.ui.home.HomeFragment;
import com.auk.fanyi.ui.home.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardFragment extends Fragment {
    ListView listView;
    List<history> list = new ArrayList<>();
    MyAdapter adapter;
    MySQLiteOpenHelper dbHelper;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listView = root.findViewById(R.id.list1);
        dbHelper = new MySQLiteOpenHelper(getActivity(),"trans.db");
        adapter = new MyAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        show();
        return root;
    }
    public void show(){
        list.clear();
        SQLiteDatabase sqliteDatabase4 = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // 返回一个Cursor对象：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase4.query("starsql", new String[] { "id","yuan",
                "mudi","star" }, "star=?",new String[]{"2"},null,null, null, null);
        String yuan;
        String mudi;
        int star,id1;
        //将光标移动到下一行，从而判断该结果集是否还有下一条数据
        //如果有则返回true，没有则返回false
        while (cursor.moveToNext()) {
            id1 = cursor.getInt(cursor.getColumnIndex("id"));
            yuan = cursor.getString(cursor.getColumnIndex("yuan"));
            mudi = cursor.getString(cursor.getColumnIndex("mudi"));
            star = cursor.getInt(cursor.getColumnIndex("star"));
            history history = new history(yuan,mudi,star,id1);
            //输出查询结果
            list.add(history);
        }
        //关闭数据库
        cursor.close();
        adapter.notifyDataSetChanged();
        sqliteDatabase4.close();

    }
    public class MyAdapter extends BaseAdapter implements View.OnClickListener {
        List<history>list;
        LayoutInflater inflater;
        public MyAdapter(Context context, List<history>list){
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.listview, null);
                viewHolder = new ViewHolder();
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.list_yuan);
                viewHolder.tv2 =(TextView) convertView.findViewById(R.id.list_mudi);
                viewHolder.im =(ImageView) convertView.findViewById(R.id.star_image);
                viewHolder.im.setOnClickListener(this);
                viewHolder.im.setTag(position);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv1.setText(list.get(position).getyuan());
            viewHolder.tv2.setText(list.get(position).getmudi());
            if(list.get(position).getStar()==1){
                viewHolder.im.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star, null));
            }
            else if(list.get(position).getStar()==2){
                viewHolder.im.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            int pos=(int)v.getTag();
            switch (v.getId()) {
                case R.id.star_image:
                        MySQLiteOpenHelper dbHelper2 = new MySQLiteOpenHelper(getActivity(),"trans.db", 1);
                        // 调用update方法修改数据库
                        System.out.println(list.get(pos).getyuan());
                        if(query(list.get(pos).getyuan())){
                            System.out.println(list.get(pos).getyuan()+"1111");
                            SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                            ContentValues values2 = new ContentValues();
                            values2.put("star", 1);
                            sqliteDatabase2.update("history", values2, "id=?", new String[]{(String.valueOf(list.get(pos).getId()))});
                            sqliteDatabase2.close();
                        }
                        SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                        sqliteDatabase2.delete("starsql", "yuan=?", new String[]{(list.get(pos).getyuan())});
                        //关闭数据库
                        sqliteDatabase2.close();
                        Toast.makeText(getActivity(),"此记录已取消保存",Toast.LENGTH_SHORT).show();
                    System.out.println("starpos: "+ list.get(pos).getId());
            }
            show();
            HomeFragment.show();

        }
        public  boolean query(String a) {
            SQLiteDatabase sqliteDatabase10 = dbHelper.getReadableDatabase();
            // 调用SQLiteDatabase对象的query方法进行查询
            // 返回一个Cursor对象：由数据库查询返回的结果集对象
            Cursor cursor = sqliteDatabase10.query("starsql", new String[]{"id"}, "yuan=?", new String[]{a}, null, null, null);
            int id1=0;
            //将光标移动到下一行，从而判断该结果集是否还有下一条数据
            //如果有则返回true，没有则返回false
            while (cursor.moveToNext()) {
                id1 = cursor.getInt(cursor.getColumnIndex("id"));
            }
            //关闭数据库
            cursor.close();
            sqliteDatabase10.close();
            if(id1==-1){
                return false;
            }
            else{
                return true;
            }

        }
    }
    //辅助类
    class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView im;
    }

}
