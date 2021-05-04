package com.example.youtube_music;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/7/18.
 */

public class TitlebarView extends RelativeLayout {
    ImageView titlebar_ImageView;
    onViewClick mClick;
    int attr;

    public TitlebarView(Context context) {
        this(context, null);
    }

    public TitlebarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitlebarView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.custom_titlebar, this);
        titlebar_ImageView = findViewById(R.id.titlebar_ImageView);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitlebarView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.TitlebarView_titlebar_ImageView:
                    titlebar_ImageView.setImageResource(array.getResourceId(attr, 0));
                    break;
            }
        }
        titlebar_ImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.image_Click();
            }
        });
        array.recycle();

    }

    public void setOnViewClick(onViewClick click) {
        this.mClick = click;
    }

    public interface onViewClick {
        void image_Click();
    }

}