package com.example.youtube_music.Song_Player;

public class Player_Load_Model {
    String Video_ID,Date_Time;
    public Player_Load_Model(String Video_ID,String Date_Time){
        this.Video_ID = Video_ID;
        this.Date_Time = Date_Time;
    }

    public String getVideo_ID() {
        return Video_ID;
    }

    public void setVideo_ID(String video_ID) {
        Video_ID = video_ID;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }
}
