package com.tarasbarabash.newsreader.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tarasbarabash.newsreader.R;
import com.tarasbarabash.newsreader.ViewModel.MainActivityVM;
import com.tarasbarabash.newsreader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainActivityVM mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new MainActivityVM(this, activityMainBinding);
        mViewModel.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.onStop();
    }
}
