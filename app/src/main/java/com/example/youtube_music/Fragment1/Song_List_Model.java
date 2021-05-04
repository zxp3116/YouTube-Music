package com.example.youtube_music.Fragment1;

public class Song_List_Model {
    String song_Title,image_URL;
    int song_Size;
    public Song_List_Model(String song_Title, String image_URL, int song_Size){
        this.song_Title = song_Title;
        this.song_Size = song_Size;
        this.image_URL = image_URL;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getSong_Title() {
        return song_Title;
    }

    public void setSong_Title(String song_Title) {
        this.song_Title = song_Title;
    }

    public int getSong_Size() {
        return song_Size;
    }

    public void setSong_Size(int song_Size) {
        this.song_Size = song_Size;
    }
}
