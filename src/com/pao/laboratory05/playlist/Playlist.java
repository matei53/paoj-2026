package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    public Playlist(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void addSong(Song song){
        Song[] added = new Song[songs.length + 1];
        System.arraycopy(songs, 0, added, 0, songs.length);
        added[added.length - 1] = song;
        songs = added;
    }
    public void printSortedByTitle(){
        Song[] copy = songs;
        Arrays.sort(copy);
        for (Song s : copy){
            System.out.println(s);
        }
    }
    public void printSortedByDuration(){
        Song[] copy = songs;
        Arrays.sort(copy, new SongDurationComparator());
        for (Song s : copy){
            System.out.println(s);
        }
    }
    public int getTotalDuration(){
        int d = 0;
        for (Song s : songs){
            d += s.durationSeconds();
        }
        return d;
    }
}
