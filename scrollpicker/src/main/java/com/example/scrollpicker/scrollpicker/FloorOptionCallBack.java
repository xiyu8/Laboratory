package com.example.scrollpicker.scrollpicker;

/**
 * Created by xiyu on 2018/4/9.
 */

public interface FloorOptionCallBack {

    void selectedFloor(int whichFloor);
    void confirmTimeStamp(long timeStamp, long dayTime, boolean isStartTime);
    void cancel();

}
