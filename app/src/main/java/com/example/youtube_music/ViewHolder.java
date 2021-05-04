package com.example.youtube_music;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.youtube_music.Fragment1.Fragment1;
import com.example.youtube_music.Fragment1.Song_List_Model;
import com.example.youtube_music.Search_Window.Search_Window;
import com.example.youtube_music.Song_Player.Player_Load_Model;
import com.example.youtube_music.Song_Player.Song_Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    View mConvertView;
    PopupWindow popupWindow;
    float bgAlpha = 1f;
    boolean bright = false;
    AnimUtil animUtil = new AnimUtil();
    int currentItem = -1;
    LinearLayout popup_LinearLayout01, popup_LinearLayout02, popup_LinearLayout03, player_LinearLayout01, player_LinearLayout02, player_LinearLayout03;
    RoundedCorners roundedCorners;

    List<Song_List_Model> song_list_modelLists = new ArrayList<>();
    Song_List_Model song_list_model;
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button confirm_Button, cancel_Button;
    CommonAdapter commonAdapter;
    ListView popup_ListView;
    Search_Window search_window = new Search_Window();
    Song_Player song_player = new Song_Player();
    Fragment1 fragment1 = new Fragment1();
    Player_Load_Model player_load_model;


    boolean[] booleans = new boolean[50];

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {

            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    public ViewHolder setTextBackgroundColor(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        if(booleans[mPosition]){
            view.setSelected(true);
        }else{
            view.setSelected(false);
        }
        return this;
    }

    public ViewHolder setImageUrl_glide(int Viewid, Context context, String url) {
        roundedCorners = new RoundedCorners(50);
        ImageView imageView = getView(Viewid);
        if(url != null){
            Glide.with(context).load(url).centerCrop().apply(RequestOptions.bitmapTransform(roundedCorners)).into(imageView);
        }

        return this;
    }

    public ViewHolder setBottom_Window(int viewId, Context context,String Song_Title) {
        ImageView view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_window(view, context,Song_Title);
                ToggleBright(context);
            }
        });
        return this;
    }

    public ViewHolder setPopupWindow_Player(int viewId, Context context) {
        ImageView view = getView(viewId);
        View layout = LayoutInflater.from(context).inflate(R.layout.item_song_player_popup, null);
        player_LinearLayout01 = layout.findViewById(R.id.player_LinearLayout01);
        player_LinearLayout02 = layout.findViewById(R.id.player_LinearLayout02);
        player_LinearLayout03 = layout.findViewById(R.id.player_LinearLayout03);

        View.OnClickListener player_PopupWindow = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.player_LinearLayout01:
                        popupWindow.dismiss();
                        break;
                    case R.id.player_LinearLayout02:
                        popupWindow.dismiss();
                        break;
                    case R.id.player_LinearLayout03:
                        //Toast.makeText(context, song_player.getSong_player_modelList().size()+","+song_player.getPlayer_load_modelList().size()+","+song_player.getList_Player_Load_Model(), Toast.LENGTH_SHORT).show();
                        song_player.Delete_Song(context);
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        player_LinearLayout01.setOnClickListener(player_PopupWindow);
        player_LinearLayout02.setOnClickListener(player_PopupWindow);
        player_LinearLayout03.setOnClickListener(player_PopupWindow);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleBright(context);
                setPopupWindow(layout,view,context);
            }
        });

        return this;
    }
    public ViewHolder setPopupWindow_Search(int viewId, Context context,String Video_ID,String Image_Url) {
        ImageView view = getView(viewId);
        View layout = LayoutInflater.from(context).inflate(R.layout.item_youtube_popup,null);

        confirm_Button = layout.findViewById(R.id.confirm_Button);
        cancel_Button = layout.findViewById(R.id.cancel_Button);
        popup_ListView = layout.findViewById(R.id.popup_ListView);
        confirm_Button.setBackgroundColor(0x484948);
        cancel_Button.setBackgroundColor(0x484948);
        sharedPreferences = context.getSharedPreferences("Data_SAVE", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String Load_ListData = sharedPreferences.getString("YouTube_ListData","");
        song_list_modelLists = gson.fromJson(Load_ListData, new TypeToken<List<Song_List_Model>>() {}.getType());

        commonAdapter = new CommonAdapter<Song_List_Model>(context, song_list_modelLists,R.layout.item_youtube_popup_list) {
            @Override
            public void convert(ViewHolder viewHolder, Song_List_Model song_list_model) {
                viewHolder.setText(R.id.youtube_Song_Title, song_list_model.getSong_Title());
                viewHolder.setImageUrl_glide(R.id.youtube_Song_ImageView, context,song_list_model.getImage_URL());
                viewHolder.setText(R.id.youtube_Song_Size, song_list_model.getSong_Size()+" 首歌曲");

            }
        };

        popup_ListView.setAdapter(commonAdapter);
        popup_ListView.setChoiceMode(popup_ListView.CHOICE_MODE_MULTIPLE);
        popup_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(popup_ListView.isItemChecked(position)) {
                    view.setBackgroundColor(0xFFff73b3);
                    booleans[position] =true;
                } else {
                    view.setBackgroundColor(Color.WHITE);
                    booleans[position] =false;
                }


            }
        });
        for(int i = 0; i<song_player.getList_Player_Load_Model().length;i++){
            song_player.getList_Player_Load_Model()[i] = new ArrayList<Player_Load_Model>();
        }
        View.OnClickListener PopupWindow_List =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.confirm_Button:
                        Date date = new Date( );
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        for(int i =0; i<booleans.length;i++){
                            String Load_YouTube_Player = sharedPreferences.getString("第"+i,"");
                            if(!Load_YouTube_Player.equals("")){
                                song_player.getList_Player_Load_Model()[i] = gson.fromJson(Load_YouTube_Player, new TypeToken<List<Player_Load_Model>>() {}.getType());
                            }
                        }
                        for(int i=0;i<booleans.length;i++){
                            if(booleans[i]){
                                if(song_list_modelLists.get(i).getImage_URL() != null){
                                    song_player.getStrings_Player()[i] = "第"+i;
                                    song_list_model = new Song_List_Model(song_list_modelLists.get(i).getSong_Title(),song_list_modelLists.get(i).getImage_URL(),song_list_modelLists.get(i).getSong_Size()+1);
                                    song_list_modelLists.set(i,song_list_model);

                                    player_load_model = new Player_Load_Model(Video_ID, simpleDateFormat.format(date));
                                    song_player.getList_Player_Load_Model()[i].add(player_load_model);
                                    editor.putString("YouTube_ListData",gson.toJson(song_list_modelLists));
                                    editor.putString(song_player.getStrings_Player()[i],gson.toJson(song_player.getList_Player_Load_Model()[i]));
                                    editor.commit();
                                    search_window.notifyDataSetChanged();
                                }else{
                                    song_player.getStrings_Player()[i] = "第"+i;
                                    song_list_model = new Song_List_Model(song_list_modelLists.get(i).getSong_Title(),Image_Url,song_list_modelLists.get(i).getSong_Size()+1);
                                    song_list_modelLists.set(i,song_list_model);
                                    player_load_model = new Player_Load_Model(Video_ID, simpleDateFormat.format(date));
                                    song_player.getList_Player_Load_Model()[i].add(player_load_model);
                                    editor.putString("YouTube_ListData",gson.toJson(song_list_modelLists));
                                    editor.putString(song_player.getStrings_Player()[i],gson.toJson(song_player.getList_Player_Load_Model()[i]));
                                    editor.commit();
                                    search_window.notifyDataSetChanged();
                                }
                            }

                        }
                        for(int j = 0;j<booleans.length;j++){
                            booleans[j] = false;
                        }
                        if(!booleans[0] &&song_player.getStrings_Player()[0] == null ){

                        }

                        popupWindow.dismiss();
                        break;
                    case R.id.cancel_Button:
                        popupWindow.dismiss();
                        for(int i = 0;i<booleans.length;i++){
                            booleans[i] = false;
                        }
                        break;
                }
            }
        };
        confirm_Button.setOnClickListener(PopupWindow_List);
        cancel_Button.setOnClickListener(PopupWindow_List);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commonAdapter.notifyDataSetChanged();
                //search_window.notifyDataSetChanged();
                ToggleBright(context);
                setPopupWindow(layout,view,context);
            }
        });
        return this;
    }

    public void bottom_window(View view, Context context,String Song_Title) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.item_song_popup,null);
        View layout01 = LayoutInflater.from(context).inflate(R.layout.item_change_name_popup,null);
        popup_LinearLayout01 =  layout.findViewById(R.id.popup_LinearLayout01);
        popup_LinearLayout02 =  layout.findViewById(R.id.popup_LinearLayout02);
        popup_LinearLayout03 =  layout.findViewById(R.id.popup_LinearLayout03);
        TextView change_old_name = layout01.findViewById(R.id.change_old_name);
        EditText change_new_name = layout01.findViewById(R.id.change_new_name);
        Button change_Cancel = layout01.findViewById(R.id.change_Cancel);
        Button change_Confirm = layout01.findViewById(R.id.change_Confirm);
        change_Cancel.setBackgroundColor(0x484948);
        change_Confirm.setBackgroundColor(0x484948);
        View.OnClickListener popup_LinearLayout = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.popup_LinearLayout01:
                        popupWindow.dismiss();
                        change_old_name.setText("舊名稱:"+Song_Title);
                        ToggleBright(context);
                        setPopupWindow(layout01,layout01,context);
                        break;
                    case R.id.popup_LinearLayout02:
                        popupWindow.dismiss();
                        break;
                    case R.id.popup_LinearLayout03:
                        fragment1.Delete_Song_List(mPosition);
                        popupWindow.dismiss();
                        break;
                    case R.id.change_Cancel:
                        popupWindow.dismiss();
                        break;
                    case R.id.change_Confirm:

                        if(change_new_name.getText().toString().trim().length()>0){
                            fragment1.Change_Song_Name(mPosition,change_new_name.getText().toString());
                        }else{

                        }
                        popupWindow.dismiss();
                        break;
                }

            }
        };
        change_Cancel.setOnClickListener(popup_LinearLayout);
        change_Confirm.setOnClickListener(popup_LinearLayout);
        popup_LinearLayout01.setOnClickListener(popup_LinearLayout);
        popup_LinearLayout02.setOnClickListener(popup_LinearLayout);
        popup_LinearLayout03.setOnClickListener(popup_LinearLayout);
        setPopupWindow(layout,view,context);
    }



    public void setPopupWindow(View view_Layout, View view, Context context){
        popupWindow = new PopupWindow(view_Layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
        //setButtonListeners(layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ToggleBright(context);
            }
        });
    }

    public void ToggleBright(Context context) {
        //三个参数分别为： 起始值 结束值 时长  那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(0.5f, 1f, 350);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (1.5f - progress);//三目运算，应该挺好懂的。
                BackgroundAlpha(bgAlpha,context);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }
    public void BackgroundAlpha(float bgAlpha,Context context) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)context).getWindow().setAttributes(lp);
        ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public int getPosition()
    {
        return mPosition;
    }
}