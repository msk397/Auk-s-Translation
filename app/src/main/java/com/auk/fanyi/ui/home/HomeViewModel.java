package com.auk.fanyi.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Date now;
    public HomeViewModel() {
        String time = "现在是什么时间？";
        mText = new MutableLiveData<>();
        mText.setValue( time );
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String getDate() {
        now = new Date();
        return now.toString();
    }

}
