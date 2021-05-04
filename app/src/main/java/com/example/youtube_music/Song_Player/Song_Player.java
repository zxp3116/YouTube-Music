package com.example.youtube_music.Song_Player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtube_music.CommonAdapter;
import com.example.youtube_music.DateUtil;
import com.example.youtube_music.R;
import com.example.youtube_music.Search_Window.Search_Model;
import com.example.youtube_music.TitlebarView;
import com.example.youtube_music.ViewHolder;
import com.example.youtube_music.YouTube_API_Key;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class Song_Player extends AppCompatActivity {
    ListView player_ListView;
    Song_Player_Model song_player_model;
    Song_Player_AsyncTask song_player_asyncTask;
    CommonAdapter commonAdapter;
    Handler handler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    List<Player_Load_Model> player_load_modelList = new ArrayList<>();
    List<Song_Player_Model> song_player_modelList = new ArrayList<>();
    YouTubePlayer youTubePlayer;
    YouTubePlayerView pierfrancescosoffritti;
    String Load_YouTube_Player,Load_YouTubePlayer_Sort;
    TitlebarView titlebarView;
    int item_Position;
    List<Player_Load_Model>[] list_Player_Load_Model = new List[50];
    String[] strings_Player = new String[50];
    String[] strings_Player_Sort = new String[50];
    Button random_Play,cycle_Play,one_cycle_Play;
    List<Integer> random_list = new ArrayList<>();
    int load_Replace;
    boolean last_one,boolean_cycle,boolean_one_cycle,boolean_random,random_repeat;
    NotificationManagerCompat notificationManager;
    LinearLayout linearLayout_PlayMode;
    ProgressBar progressBar_Song_Player;
    TextView textView_Song_Player;




    public List<Player_Load_Model>[] getList_Player_Load_Model() {
        return list_Player_Load_Model;
    }


    public void setList_Player_Load_Model(List<Player_Load_Model>[] list_Player_Load_Model) {
        this.list_Player_Load_Model = list_Player_Load_Model;
    }

    public String[] getStrings_Player() {
        return strings_Player;
    }

    public void setStrings_Player(String[] strings_Player) {
        this.strings_Player = strings_Player;
    }

    public int getItem_Position() {
        return item_Position;
    }

    public void setItem_Position(int item_Position) {
        this.item_Position = item_Position;
    }


    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    public void setYouTubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    public List<Player_Load_Model> getPlayer_load_modelList() {
        return player_load_modelList;
    }

    public void setPlayer_load_modelList(List<Player_Load_Model> player_load_modelList) {
        this.player_load_modelList = player_load_modelList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_player);
        getSupportActionBar().hide();
        player_ListView = findViewById(R.id.player_ListView);
        random_Play = findViewById(R.id.random_Play);
        cycle_Play = findViewById(R.id.cycle_Play);
        one_cycle_Play = findViewById(R.id.one_cycle_Play);
        titlebarView = findViewById(R.id.player_TitlebarView);

        linearLayout_PlayMode = findViewById(R.id.linearLayout_PlayMode);
        progressBar_Song_Player = findViewById(R.id.progressBar_Song_Player);
        textView_Song_Player = findViewById(R.id.textView_Song_Player);
        notificationManager = NotificationManagerCompat.from(this);
        random_Play.setBackgroundColor(0xFF1C1C1C);
        cycle_Play.setBackgroundColor(0xFF1C1C1C);
        one_cycle_Play.setBackgroundColor(0xFF1C1C1C);
        View.OnClickListener Play_Mode =new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.random_Play:
                        if(!boolean_random){
                            while(true) {
                                int num=(int)(Math.random() * song_player_modelList.size());
                                boolean isHave=false;
                                for (int j = 0; j < random_list.size(); j++) {
                                    if (num == random_list.get(j)) {
                                        isHave=true;
                                        break;
                                    }
                                }
                                if(!isHave) {
                                    random_list.add(num);
                                }
                                if(random_list.size()==song_player_modelList.size()) {
                                    break;
                                }
                            }
                            getYouTubePlayer().loadVideo(song_player_modelList.get(random_list.get(0)).getVideo_ID(), 0);
                            setItem_Position(random_list.get(0));
                            random_list.remove(0);
                            random_Play.setBackgroundColor(Color.WHITE);
                            random_Play.setTextColor(Color.BLACK);
                            cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            cycle_Play.setTextColor(Color.WHITE);
                            one_cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            one_cycle_Play.setTextColor(Color.WHITE);
                            boolean_random = true;
                            boolean_cycle = false;
                            boolean_one_cycle = false;
                        }else{
                            boolean_random = false;
                            random_Play.setBackgroundColor(0xFF1C1C1C);
                            random_Play.setTextColor(Color.WHITE);
                        }


                        break;
                    case R.id.cycle_Play:
                        if(!boolean_cycle){
                            cycle_Play.setBackgroundColor(Color.WHITE);
                            cycle_Play.setTextColor(Color.BLACK);
                            random_Play.setBackgroundColor(0xFF1C1C1C);
                            random_Play.setTextColor(Color.WHITE);
                            one_cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            one_cycle_Play.setTextColor(Color.WHITE);
                            getYouTubePlayer().loadVideo(song_player_modelList.get(0).getVideo_ID(), 0);
                            setItem_Position(0);
                            boolean_cycle = true;
                            boolean_random = false;
                            boolean_one_cycle = false;
                        }else{
                            boolean_cycle = false;
                            cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            cycle_Play.setTextColor(Color.WHITE);
                        }

                        break;

                    case R.id.one_cycle_Play:
                        if(!boolean_one_cycle){
                            one_cycle_Play.setBackgroundColor(Color.WHITE);
                            one_cycle_Play.setTextColor(Color.BLACK);
                            random_Play.setBackgroundColor(0xFF1C1C1C);
                            random_Play.setTextColor(Color.WHITE);
                            cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            cycle_Play.setTextColor(Color.WHITE);
                            boolean_one_cycle = true;
                            boolean_cycle = false;
                            boolean_random = false;


                        }else{
                            boolean_one_cycle = false;
                            one_cycle_Play.setBackgroundColor(0xFF1C1C1C);
                            one_cycle_Play.setTextColor(Color.WHITE);
                        }

                        break;
                }

            }
        };
        random_Play.setOnClickListener(Play_Mode);
        cycle_Play.setOnClickListener(Play_Mode);
        one_cycle_Play.setOnClickListener(Play_Mode);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what ==2){
                    commonAdapter = new CommonAdapter<Song_Player_Model>(Song_Player.this, song_player_modelList,R.layout.item_song_player) {
                        @Override
                        public void convert(ViewHolder viewHolder, Song_Player_Model song_player_model) {
                            viewHolder.setText(R.id.song_Player_Title, song_player_model.getTitile());
                            viewHolder.setText(R.id.song_Player_Time, song_player_model.getTime());
                            viewHolder.setImageUrl_glide(R.id.song_Player_ImageView, Song_Player.this, song_player_model.getImage_URL());
                            viewHolder.setPopupWindow_Player(R.id.song_Player_More,Song_Player.this);
                        }
                    };
                    player_ListView.setAdapter(commonAdapter);
                }
            }
        };
        pierfrancescosoffritti = findViewById(R.id.pierfrancescosoffritti);
        pierfrancescosoffritti.enableBackgroundPlayback(true);

        pierfrancescosoffritti.addYouTubePlayerListener(new YouTubePlayerListener(){
            @Override
            public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String s) {

            }

            @Override
            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState playerState) {
                if(playerState.toString().equals("ENDED")){
                    if(getItem_Position()+1 == song_player_modelList.size()&&last_one){
                        getYouTubePlayer().loadVideo(song_player_modelList.get(0).getVideo_ID(), 0);
                        last_one = false;
                    }
                    if(boolean_cycle&&getItem_Position() != song_player_modelList.size()-1){
                        setItem_Position(getItem_Position()+1);
                        getYouTubePlayer().loadVideo(song_player_modelList.get(getItem_Position()).getVideo_ID(), 0);
                        if(getItem_Position()+1 == song_player_modelList.size()){
                            last_one =true;
                        }
                    }
                    if(boolean_random) {
                        if (random_list.size() > 1) {
                            for (int i = 0; i < song_player_modelList.size(); i++) {
                                getYouTubePlayer().loadVideo(song_player_modelList.get(random_list.get(i)).getVideo_ID(), 0);
                                setItem_Position(random_list.get(i));
                                random_list.remove(i);
                                //random_repeat = true;
                                break;
                            }
                        } else {
                            while(true) {
                                int num=(int)(Math.random() * song_player_modelList.size());
                                boolean isHave=false;
                                for (int j = 0; j < random_list.size(); j++) {
                                    if (num == random_list.get(j)) {
                                        isHave=true;
                                        break;
                                    }
                                }
                                if(!isHave) {
                                    random_list.add(num);
                                }
                                if(random_list.size()==song_player_modelList.size()) {
                                    break;
                                }
                            }

                            getYouTubePlayer().loadVideo(song_player_modelList.get(random_list.get(0)).getVideo_ID(), 0);
                            random_list.remove(0);
                        }


                    }
                    if(boolean_one_cycle){
                        getYouTubePlayer().loadVideo(song_player_modelList.get(getItem_Position()).getVideo_ID(), 0);
                    }
                }
            }

            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                setYouTubePlayer(youTubePlayer);
            }

            @Override
            public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

            }
        });

        sharedPreferences = getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String load = getIntent().getStringExtra("Data");
        load_Replace = Integer.parseInt(load.replace("第",""));
        Load_YouTube_Player = sharedPreferences.getString(load,"");
        Load_YouTubePlayer_Sort = sharedPreferences.getString(strings_Player_Sort[load_Replace],"");
        if(!Load_YouTube_Player.equals("")){
            player_load_modelList = gson.fromJson(Load_YouTube_Player, new TypeToken<List<Player_Load_Model>>() {}.getType());
            if(!Load_YouTubePlayer_Sort.equals("")){
                song_player_modelList = gson.fromJson(Load_YouTubePlayer_Sort, new TypeToken<List<Song_Player_Model>>() {}.getType());
                if(player_load_modelList.size() == song_player_modelList.size()) {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                    //getYouTubePlayer().loadVideo("ioNng23DkIM",0);
                }else{
                    song_player_asyncTask = new Song_Player_AsyncTask();
                    song_player_asyncTask.execute();
                    //getYouTubePlayer().loadVideo("ioNng23DkIM",0);
                }
            }else{
                song_player_asyncTask = new Song_Player_AsyncTask();
                song_player_asyncTask.execute();
            }

        }
        player_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getYouTubePlayer().loadVideo( song_player_modelList.get(position).getVideo_ID(),0);
                setItem_Position(position);
                if(boolean_cycle&&getItem_Position()+1 == song_player_modelList.size()){
                    last_one =true;
                }
            }
        });
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void image_Click() {
                finish();
            }
        });

    }
    public void Delete_Song(Context context){
        song_player_modelList = gson.fromJson(Load_YouTubePlayer_Sort, new TypeToken<List<Song_Player_Model>>() {}.getType());

    }

    public class Song_Player_AsyncTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            for(int i =0; i<player_load_modelList.size();i++){
                String  url = "https://www.googleapis.com/youtube/v3/videos?id="+player_load_modelList.get(i).getVideo_ID()+"&key="+ YouTube_API_Key.getApiKey()+"&part=snippet";
                int finalI = i;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            JSONArray items = jsonObject.getJSONArray("items");
                            JSONObject itemsJSONObject = items.getJSONObject(0);
                            JSONObject snippet = itemsJSONObject.getJSONObject("snippet");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String id = itemsJSONObject.getString("id");
                            String title = snippet.getString("title");
                            String url = high.getString("url");


                            song_player_model = new Song_Player_Model(title,player_load_modelList.get(finalI).getDate_Time(),url,id);
                            song_player_modelList.add(song_player_model);
                            Collections.sort(song_player_modelList, new Comparator<Song_Player_Model>() {
                                @Override
                                public int compare(Song_Player_Model o1, Song_Player_Model o2) {
                                    Date date01 = DateUtil.stringToDate(o1.getTime());
                                    Date date02 = DateUtil.stringToDate(o2.getTime());
                                    if (date01.after(date02)) {
                                        return 1;
                                    }
                                    return -1;
                                }
                            });

                            commonAdapter.notifyDataSetChanged();
                            strings_Player_Sort[load_Replace] = "歌單"+load_Replace;

                            editor.putString(strings_Player_Sort[load_Replace],gson.toJson(song_player_modelList));
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
                RequestQueue requestQueue = Volley.newRequestQueue(Song_Player.this);
                requestQueue.add(jsonObjectRequest);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(player_load_modelList.size() > song_player_modelList.size()&&song_player_modelList.size()>0){
                for(int i=0;i<song_player_modelList.size();i ++){
                    player_load_modelList.remove(0);
                }
            }
            progressBar_Song_Player.setVisibility(View.VISIBLE);
            textView_Song_Player.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Message msg = Message.obtain();
            msg.what=2;
            handler.sendMessage(msg);
            pierfrancescosoffritti.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                @Override
                public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {
                    pierfrancescosoffritti.setVisibility(View.VISIBLE);
                    player_ListView.setVisibility(View.VISIBLE);
                    linearLayout_PlayMode.setVisibility(View.VISIBLE);
                    progressBar_Song_Player.setVisibility(View.GONE);;
                    textView_Song_Player.setVisibility(View.GONE);
                    youTubePlayer.cueVideo(song_player_modelList.get(0).getVideo_ID(),0);
                }
            });
        }
    }
}
