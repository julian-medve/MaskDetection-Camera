package com.silverit.maskdetectioncamera.utils;

public class SoundItem {

    public int soundID;
    public float volume;
    public boolean stop = false;

    /**
     * Default constructor
     *
     * @param soundID
     * @param volume
     */
    public SoundItem(int soundID, float volume) {

        this.soundID = soundID;
        this.volume = volume;
    }

    /**
     * Constructor for the item
     * which will kill the thread
     *
     * @param stop
     */
    public SoundItem(boolean stop) {

        this.stop = stop;
    }
}