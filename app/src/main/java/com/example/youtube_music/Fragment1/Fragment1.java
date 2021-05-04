package com.example.youtube_music.Fragment1;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtube_music.AnimUtil;
import com.example.youtube_music.CommonAdapter;
import com.example.youtube_music.R;
import com.example.youtube_music.Song_Player.Song_Player;
import com.example.youtube_music.Song_Player.Song_Player_Model;
import com.example.youtube_music.ViewHolder;
import com.example.youtube_music.YouTube_API_Key;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    static View view,layout;

    ListView song_listView;
    static List<Song_List_Model> song_list_modelList = new ArrayList<>();
    Song_List_Model song_List_Model;
    static Handler handler;
    Song_List_AsyncTask song_list_asyncTask;
    static CommonAdapter commonAdapter;
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static String List_Data,Load_YouTube_Player;
    static Gson gson = new Gson();
    static FragmentActivity fragmentActivity;
    static FragmentManager fragmentManager;
    Intent intent;
    Song_Player song_player = new Song_Player();
    RelativeLayout add_Song;
    EditText add_Song_Edit;
    Button add_Song_Confirm,add_Song_Cancel;

    AnimUtil animUtil = new AnimUtil();
    PopupWindow popupWindow;
    float bgAlpha = 1f;
    boolean bright = false;

    public static List<Song_List_Model> getSong_list_modelList() {
        return song_list_modelList;
    }

    public static void setSong_list_modelList(List<Song_List_Model> song_list_modelList) {
        Fragment1.song_list_modelList = song_list_modelList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_1, container, false);
        song_listView = view.findViewById(R.id.song_listView);
        layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_song_popup,null);
        add_Song_Edit = layout.findViewById(R.id.add_Song_Edit);
        add_Song_Confirm = layout.findViewById(R.id.add_Song_Confirm);
        add_Song_Cancel = layout.findViewById(R.id.add_Song_Cancel);
        add_Song = view.findViewById(R.id.add_Song);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    commonAdapter = new CommonAdapter<Song_List_Model>(getActivity(), song_list_modelList,R.layout.item_song) {
                        @Override
                        public void convert(ViewHolder viewHolder, Song_List_Model song_List_Model) {
                            viewHolder.setText(R.id.song_Title, song_List_Model.getSong_Title());
                            viewHolder.setText(R.id.song_Size, song_List_Model.getSong_Size()+" 首歌曲");
                            viewHolder.setImageUrl_glide(R.id.song_ImageView, getActivity(),song_List_Model.getImage_URL());
                            viewHolder.setBottom_Window(R.id.song_More,getActivity(),song_List_Model.getSong_Title());
                        }
                    };
                    song_listView.setAdapter(commonAdapter);

                }
            }
        };
        sharedPreferences = getActivity().getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        List_Data = sharedPreferences.getString("YouTube_ListData","");
        if(!List_Data.equals("")){
            song_list_modelList = gson.fromJson(List_Data, new TypeToken<List<Song_List_Model>>() {}.getType());
            Message msg = Message.obtain();
            msg.what=1;
            handler.sendMessage(msg);
        }else{
            initialization("未命名歌單");

        }
        song_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = sharedPreferences.getString("第"+position,"");
                if( data.equals("")){
                    Toast.makeText(getActivity(), "此歌單尚未添加任何歌曲，快到最上方的搜尋欄添加喜愛的歌曲吧", Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent(getActivity(), Song_Player.class);
                    intent.putExtra("Data","第"+position);
                    startActivity(intent);
                }

            }
        });
        add_Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleBright(getActivity());
                setPopupWindow(layout,layout,getActivity());
                add_Song_Edit.setText("");
                View.OnClickListener add_Song_Button = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.add_Song_Confirm:
                                if(!add_Song_Edit.getText().toString().equals("")){
                                    initialization(add_Song_Edit.getText().toString());
                                }
                                popupWindow.dismiss();
                                break;
                            case R.id.add_Song_Cancel:
                                popupWindow.dismiss();
                                break;
                        }
                    }
                };
                add_Song_Confirm.setOnClickListener(add_Song_Button);
                add_Song_Cancel.setOnClickListener(add_Song_Button);

            }
        });
        return view;
    }
    public static void notifyDataSetChanged (){
        fragmentActivity = (FragmentActivity)view.getContext();
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        sharedPreferences = fragmentActivity.getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        List_Data = sharedPreferences.getString("YouTube_ListData","");
        if(!List_Data.equals("")) {
            song_list_modelList = gson.fromJson(List_Data, new TypeToken<List<Song_List_Model>>() {
            }.getType());
            Message msg = Message.obtain();
            msg.what = 1;
            handler.sendMessage(msg);
        }
        commonAdapter.notifyDataSetChanged();
    }
    public void initialization(String Song_name) {
        song_List_Model = new Song_List_Model(Song_name, null,song_player.getPlayer_load_modelList().size());
        song_list_modelList.add(song_List_Model);
        Message msg = Message.obtain();
        msg.what=1;
        handler.sendMessage(msg);
        editor.putString("YouTube_ListData",gson.toJson(song_list_modelList));
        editor.commit();
    }
    public void Change_Song_Name(int Position, String Song_name){
        song_list_modelList.get(Position).setSong_Title(Song_name);
        Message msg = Message.obtain();
        msg.what=1;
        handler.sendMessage(msg);
        editor.putString("YouTube_ListData",gson.toJson(song_list_modelList));
        editor.commit();
    }
    public void Delete_Song_List(int Position){

        fragmentActivity = (FragmentActivity)view.getContext();
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        sharedPreferences = fragmentActivity.getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("第"+Position);

        for(int i=Position;i<50;i++){
            int p = i+1;
            editor.putString("第"+i,sharedPreferences.getString("第"+p,""));
        }

        song_list_modelList.remove(Position);
        Message msg = Message.obtain();
        msg.what=1;
        handler.sendMessage(msg);
        editor.putString("YouTube_ListData",gson.toJson(song_list_modelList));
        editor.commit();
    }

    public class Song_List_AsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String  url = "https://www.googleapis.com/youtube/v3/videos?id="+song_player.getPlayer_load_modelList().get(0)+"&key="+ YouTube_API_Key.getApiKey()+"&part=snippet";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            JSONArray items = jsonObject.getJSONArray("items");
                            JSONObject itemsJSONObject = items.getJSONObject(0);
                            JSONObject snippet = itemsJSONObject.getJSONObject("snippet");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String url = high.getString("url");
                            song_List_Model = new Song_List_Model("未命名歌單",url,song_player.getPlayer_load_modelList().size());
                            song_list_modelList.add(song_List_Model);
                            commonAdapter.notifyDataSetChanged();
                            editor.putString("YouTube_ListData",gson.toJson(song_list_modelList));
                            editor.commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(jsonObjectRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Message msg = Message.obtain();
            msg.what=1;
            handler.sendMessage(msg);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
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
}