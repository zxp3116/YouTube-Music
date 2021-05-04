package com.example.youtube_music.Search_Window;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtube_music.CommonAdapter;
import com.example.youtube_music.DateUtil;
import com.example.youtube_music.Fragment1.Fragment1;
import com.example.youtube_music.MainActivity;
import com.example.youtube_music.R;
import com.example.youtube_music.TitlebarView;
import com.example.youtube_music.ViewHolder;
import com.example.youtube_music.YouTube_API_Key;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Search_Window extends AppCompatActivity {
    JSONObject snippet,id;
    MainActivity mainActivity = new MainActivity();
    ListView search_ListView;
    List<Search_Model> search_ModelList = new ArrayList<>();
    Search_Model search_Model;
    Handler handler;
    static CommonAdapter commonAdapter;
    YouTube_AsyncTask youTube_asyncTask;
    TitlebarView titlebarView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    Fragment1 fragment1 = new Fragment1();

    public List<Search_Model> getSearch_ModelList() {
        return search_ModelList;
    }

    public void setSearch_ModelList(List<Search_Model> search_ModelList) {
        this.search_ModelList = search_ModelList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_window);
        getSupportActionBar().hide();
        search_ListView = findViewById(R.id.search_ListView);
        titlebarView = findViewById(R.id.search_TitlebarView);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

            }
        };
        sharedPreferences = getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        search_ModelList.clear();
        youTube_asyncTask = new YouTube_AsyncTask();
        youTube_asyncTask.execute();
        titlebarView.setOnViewClick(new TitlebarView.onViewClick() {
            @Override
            public void image_Click() {
                finish();

            }
        });
        commonAdapter = new CommonAdapter<Search_Model>(Search_Window.this, search_ModelList,R.layout.item_search) {
            @Override
            public void convert(ViewHolder viewHolder, Search_Model search_model) {
                viewHolder.setText(R.id.youTube_Title, search_model.getTitile());
                viewHolder.setText(R.id.youTube_Time, search_model.getTime());
                viewHolder.setImageUrl_glide(R.id.youTube_Image,Search_Window.this, search_model.getImage_URL());
                viewHolder.setPopupWindow_Search(R.id.youTube_More,Search_Window.this, search_model.getId(),search_model.getImage_URL());


            }
        };
        search_ListView.setAdapter(commonAdapter);
    }
    @Override
    public void finish() {
        super.finish();
        sharedPreferences = getSharedPreferences("Data_SAVE",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        fragment1.notifyDataSetChanged();

    }
    public static void notifyDataSetChanged (){
        commonAdapter.notifyDataSetChanged();
    }

    public class YouTube_AsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                String  url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="+mainActivity.getSearch_EditText().getText().toString()+"&key="+ YouTube_API_Key.getApiKey()+"&type=video&maxResults=100000";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try{
                            JSONArray items = jsonObject.getJSONArray("items");
                            for(int i=0;i<items.length();i++){
                                JSONObject itemsJSONObject = items.getJSONObject(i);
                                id = itemsJSONObject.getJSONObject("id");
                                snippet = itemsJSONObject.getJSONObject("snippet");
                                String title = snippet.getString("title");
                                String title_Replace_1 = title.replace("&quot;","＂");
                                String title_Replace_2 = title_Replace_1.replace("&amp;","&");
                                String title_Replace_3 = title_Replace_2.replace("&lt;","<");
                                String title_Replace_4 = title_Replace_3.replace("&gt;",">");
                                String title_Replace_5 = title_Replace_4.replace("&#39;","'");
                                if(title_Replace_5.length()>40){
                                    String title_shorten = title_Replace_5.substring(0,40)+"...";
                                    ListAdd_Load(title_shorten);
                                }else{
                                    ListAdd_Load(title_Replace_5);
                                }

                            }
                            commonAdapter.notifyDataSetChanged();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(Search_Window.this);
                requestQueue.add(jsonObjectRequest);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            search_ListView.setVisibility(View.VISIBLE);
            Message msg = Message.obtain();
            msg.what=1;
            handler.sendMessage(msg);

        }
    }
    private void ListAdd_Load(String string) throws JSONException {
        String publishedAt = snippet.getString("publishedAt");
        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
        JSONObject default_Json = thumbnails.getJSONObject("high");
        String videoId = id.getString("videoId");
        String high_Url = default_Json.getString("url");
        String date = publishedAt.substring(0, 10);
        String time = publishedAt.substring(11,19);
        String dateData = date+" "+time;
        String Date = DateUtil.getYouTubeTime(dateData);
        search_Model = new Search_Model(dateData,string,high_Url,videoId,Date);
        search_ModelList.add(search_Model);

    }
}
