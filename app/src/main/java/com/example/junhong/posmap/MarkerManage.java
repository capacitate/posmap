package com.example.junhong.posmap;

import android.util.Log;

/**
 * Created by Junhong on 2015-10-05.
 */
public class MarkerManage {
    private final int LEN = 100;
    private int markerNumber;
    private int beforeNumber;

    private boolean[] marker;

    public MarkerManage(){
        markerNumber = 0;
        marker = new boolean[LEN];
    }

    public int getMarker(int pos){
        int falseNumber = 0;
        for(int i = 0; i < pos; i++){
            Log.i("마커", "i : " + i + String.valueOf(marker[i]));
            if(!marker[i]){
                falseNumber++;
            }
        }
        Log.i("마커 관리 getMarker", " " + (pos - falseNumber) + "pos " + pos + " falseNumber " + falseNumber);
        Log.i("마커 관리 getMarker", " " + " markerNumber : " + markerNumber);
        return (pos - falseNumber);
    }

    public void incMarker(){
        marker[markerNumber] = true;
        Log.i("마커 관리 incMarker", "markerNumber " + markerNumber);
        markerNumber++;
    }

    public void delMarker(int markerID){
        marker[markerID] = false;
        Log.i("마커 관리 delMarker", "markerNumber " + markerNumber);
    }

    public void delAllMarker(int lastMarkerID){
        Log.i("마커 관리 delAllMarker", "lastMarkerID " + lastMarkerID);
        for(int i = 0; i <= lastMarkerID; i++){
            marker[i] = false;
        }
    }
}
