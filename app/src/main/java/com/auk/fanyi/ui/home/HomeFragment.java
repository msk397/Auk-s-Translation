package com.auk.fanyi.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.Runnable;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.auk.fanyi.MainActivity;
import com.auk.fanyi.MySQLiteOpenHelper;
import com.auk.fanyi.R;
import com.auk.fanyi.myutil;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment{
    ListView listView;
    static List<history>list = new ArrayList<>();
    static MyAdapter adapter;
    EditText ed;
    ImageView iv;
    TextView tv;
    public  static int id=0;
    String url1 = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=";
    String url2="&from=";
    String url3="&to=";
    String url4="&appid=20200506000441805&salt=1435660288&sign=";
    String key = "20200506000441805",password="5bK7WRyygImv4cU3LXr4";
    String salt="1435660288";
    Spinner ed1,ed2;
    Map m1;
    static MySQLiteOpenHelper dbHelper;
    public static void setId(int a){
        id=a;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        dbHelper = new MySQLiteOpenHelper(getActivity(),"trans.db");
        listView = root.findViewById(R.id.list);
        ed= root.findViewById(R.id.et_yuan);
        tv = root.findViewById(R.id.tv_mudi);
        ed1 = root.findViewById(R.id.sp1);
        ed2 = root.findViewById(R.id.sp2);
        iv = root.findViewById(R.id.im1);
        adapter = new MyAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        m1 = new HashMap();
        m1.put("自动检测", "auto");m1.put("中文", "zh");m1.put("英语", "en");
        m1.put("粤语", "yue");m1.put("文言文", "wyw");m1.put("日语", "jp");
        m1.put("韩语", "kor");m1.put("法语", "fra");m1.put("西班牙语", "spa");
        m1.put("泰语", "th");m1.put("阿拉伯语", "ara");m1.put("俄语", "ru");
        m1.put("繁体中文","cht");
        show();
        SQLiteDatabase sqliteDatabase4 = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // 返回一个Cursor对象：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase4.query("history", new String[] { "id","yuan",
                "mudi","star" }, null,null,null, null, null);
        //将光标移动到下一行，从而判断该结果集是否还有下一条数据
        //如果有则返回true，没有则返回false
        while (cursor.moveToNext()) {
            id++;
        }
        //关闭数据库
        cursor.close();
        sqliteDatabase4.close();
        System.out.println(id);
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                com.auk.fanyi.myutil md = new myutil();
                                String a =md.encrypByMd5(32,key+ed.getText()+salt+password);
                                String url = url1+ed.getText()+url2+m1.get(ed1.getSelectedItem())+url3+m1.get(ed2.getSelectedItem())+url4+a;
                                HttpURLConnection connection;
                                URL urll = null;
                                try {
                                    urll = new URL(url);
                                    connection = (HttpURLConnection) urll.openConnection();
                                    connection.setConnectTimeout(8000);
                                    connection.setReadTimeout(8000);
                                    InputStream in = connection.getInputStream();
                                    String re = io2String(in);
                                    showResult(re);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                ).start();

            }
        });
    }

    public void showResult(final String re){
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        bean user = gson.fromJson(re, bean.class);
                        List<bean.TransResultBean> ls=user.getTrans_result();
                        Iterator<bean.TransResultBean> it = ls.iterator();
                        while (it.hasNext()) {
                            bean.TransResultBean a= it.next();
                            tv.setText(a.getDst());
                            SQLiteDatabase  sqliteDatabase1 = dbHelper.getWritableDatabase();
                            // 创建ContentValues对象
                            ContentValues values1 = new ContentValues();
                            // 向该对象中插入键值对
                            values1.put("id", id++);
                            values1.put("yuan", a.getSrc());
                            values1.put("mudi", a.getDst());
                            values1.put("star", 1);
                            // 调用insert()方法将数据插入到数据库当中
                            sqliteDatabase1.insert("history", null, values1);
                            //关闭数据库
                            sqliteDatabase1.close();
                        }
                        show();
                    }
                }

        );
    }
    public static void show(){
        list.clear();
        SQLiteDatabase sqliteDatabase4 = dbHelper.getReadableDatabase();
        // 调用SQLiteDatabase对象的query方法进行查询
        // 返回一个Cursor对象：由数据库查询返回的结果集对象
        Cursor cursor = sqliteDatabase4.query("history", new String[] { "id","yuan",
                "mudi","star" }, null,null,null, null, null);

        String yuan = null;
        String mudi = null;
        int star = 222,id1=0;
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
        Collections.reverse(list);
        //关闭数据库
        cursor.close();
        adapter.notifyDataSetChanged();
        sqliteDatabase4.close();

    }
    public String io2String(InputStream io) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = io.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
    //自定义adapter
    public class MyAdapter extends BaseAdapter {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.listview, null);
                viewHolder = new ViewHolder();
                viewHolder.tv1 = (TextView) convertView.findViewById(R.id.list_yuan);
                viewHolder.tv2 =(TextView) convertView.findViewById(R.id.list_mudi);
                viewHolder.im =(ImageView) convertView.findViewById(R.id.star_image);
                viewHolder.im.setTag(position);
                System.out.println(position);
                viewHolder.im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = position;
                        switch (v.getId()) {
                            case R.id.star_image:
                                if(list.get(pos).getStar() == 1){
                                    MySQLiteOpenHelper dbHelper2 = new MySQLiteOpenHelper(getActivity(),"trans.db", 1);
                                    // 调用getWritableDatabase()得到一个可写的SQLiteDatabase对象
                                    SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                                    // 创建一个ContentValues对象
                                    ContentValues values2 = new ContentValues();
                                    values2.put("star", 2);
                                    // 调用update方法修改数据库
                                    sqliteDatabase2.update("history", values2, "id=?", new String[]{String.valueOf(list.get(pos).getId())});
                                    viewHolder.im.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star_fill, null));
                                    // 创建一个ContentValues对象
                                    boolean a = query(list.get(pos).getyuan());
                                    sqliteDatabase2.close();
                                    if(a){
                                        SQLiteDatabase sqliteDatabase20 = dbHelper2.getWritableDatabase();
                                        ContentValues values3 = new ContentValues();
                                        values3.put("id",list.get(pos).getId());
                                        values3.put("yuan",list.get(pos).getyuan());
                                        values3.put("mudi",list.get(pos).getmudi());
                                        values3.put("star", 2);
                                        // 调用update方法修改数据库
                                        sqliteDatabase20.insert("starsql", null, values3);
                                        Toast.makeText(getActivity(),"此记录已保存",Toast.LENGTH_SHORT).show();
                                        sqliteDatabase20.close();
                                        System.out.println("homepos: "+ list.get(pos).getId());
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"此翻译结果已存在于保存记录，不再记录",Toast.LENGTH_LONG).show();
                                    }
                                    sqliteDatabase2.close();

                                }
                                else if(list.get(pos).getStar()==2){
                                    MySQLiteOpenHelper dbHelper2 = new MySQLiteOpenHelper(getActivity(),"trans.db", 1);
                                    // 调用getWritableDatabase()得到一个可写的SQLiteDatabase对象
                                    SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                                    // 创建一个ContentValues对象
                                    ContentValues values2 = new ContentValues();
                                    values2.put("star", 1);
                                    // 调用update方法修改数据库
                                    sqliteDatabase2.update("history", values2, "id=?", new String[]{(String.valueOf(list.get(pos).getId()))});
                                    viewHolder.im.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.star, null));
                                    sqliteDatabase2.delete("starsql", "id=?", new String[]{(String.valueOf(list.get(pos).getId()))});
                                    //关闭数据库
                                    sqliteDatabase2.close();
                                    Toast.makeText(getActivity(),"此记录已取消保存",Toast.LENGTH_SHORT).show();
                                }
                        }
                        show();
                    }
                });
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

        private boolean query(String yuan1) {
            SQLiteDatabase sqliteDatabase10 = dbHelper.getReadableDatabase();
            // 调用SQLiteDatabase对象的query方法进行查询
            // 返回一个Cursor对象：由数据库查询返回的结果集对象
            Cursor cursor = sqliteDatabase10.query("starsql", new String[]{"id", "yuan",
                    "mudi", "star"}, "yuan=?", new String[]{yuan1}, null, null, null);

            //关闭数据库
            if(cursor.getCount()==0){
                cursor.close();
                sqliteDatabase10.close();
                return true;
            }
            else{
                cursor.close();
                sqliteDatabase10.close();
                return false;
            }

        }
    }
    //辅助类
    public class ViewHolder{
        TextView tv1;
        TextView tv2;
        ImageView im;
    }

}
