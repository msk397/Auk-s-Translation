package com.auk.fanyi.ui.notifications;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.auk.fanyi.MySQLiteOpenHelper;
import com.auk.fanyi.R;
import com.auk.fanyi.ui.home.HomeFragment;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    Button bt;
    MySQLiteOpenHelper dbHelper;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        bt = root.findViewById(R.id.btn);
        dbHelper = new MySQLiteOpenHelper(getActivity(),"trans.db");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqliteDatabase5 = dbHelper.getReadableDatabase();
                sqliteDatabase5.execSQL("delete from history");
                ContentValues values2 = new ContentValues();
                values2.put("id", -1);
                sqliteDatabase5.update("starsql", values2, null,null);
                Toast.makeText(getActivity(),"清除成功",Toast.LENGTH_LONG).show();
                HomeFragment.setId(0);
            }
        });
        return root;
    }
}
