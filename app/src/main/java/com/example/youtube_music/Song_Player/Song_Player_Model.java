package com.example.youtube_music.Song_Player;

public class Song_Player_Model {
    String titile,time,image_URL,video_ID;
    public Song_Player_Model(String titile,String time,String image_URL,String video_ID){
        this.titile = titile;
        this.image_URL = image_URL;
        this.time = time;
        this.video_ID = video_ID;
    }

    public String getVideo_ID() {
        return video_ID;
    }

    public void setVideo_ID(String video_ID) {
        this.video_ID = video_ID;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
