package com.sky.moviebonanza.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sky.moviebonanza.R;
import com.sky.moviebonanza.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());
    }
}