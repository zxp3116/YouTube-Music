package com.example.youtube_music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.youtube_music.Search_Window.Search_Window;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtube_music.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    TextView home_Title;
    static EditText search_EditText;
    ImageView search_ImageView,exit_ImageView;

    Search_Window.YouTube_AsyncTask youTube_asyncTask;

    ViewPager viewPager;
    Intent intent;


    public static EditText getSearch_EditText() {
        return search_EditText;
    }

    public static void setSearch_EditText(EditText search_EditText) {
        MainActivity.search_EditText = search_EditText;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        home_Title = findViewById(R.id.home_Title);
        exit_ImageView = findViewById(R.id.exit_ImageView);
        search_EditText = findViewById(R.id.search_EditText);
        search_ImageView = findViewById(R.id.search_ImageView);
        search_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Toast.makeText(MainActivity.this, "faaa", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        View.OnClickListener Click_IamageView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.search_ImageView:
                        if(search_EditText.getVisibility() == View.VISIBLE){
                            if(!search_EditText.getText().toString().equals("")){
                                intent = new Intent(MainActivity.this,Search_Window.class);
                                startActivity(intent);
                                home_Title.setText(search_EditText.getText().toString());
                            }else{

                            }
                            home_Title.setVisibility(View.VISIBLE);
                            exit_ImageView.setVisibility(View.GONE);
                            search_EditText.setVisibility(View.GONE);


                        }else{
                            exit_ImageView.setVisibility(View.VISIBLE);
                            home_Title.setVisibility(View.GONE);
                            search_EditText.setVisibility(View.VISIBLE);
                            search_EditText.requestFocus();
                        }
                        break;
                    case R.id.exit_ImageView:
                        if(!search_EditText.getText().toString().equals("")){
                            home_Title.setText(search_EditText.getText().toString());
                        }else{
                            home_Title.setText("YouTube_Music");
                        }
                        home_Title.setVisibility(View.VISIBLE);
                        search_EditText.setVisibility(View.GONE);
                        exit_ImageView.setVisibility(View.GONE);
                }
            }
        };
        exit_ImageView.setOnClickListener(Click_IamageView);
        search_ImageView.setOnClickListener(Click_IamageView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}