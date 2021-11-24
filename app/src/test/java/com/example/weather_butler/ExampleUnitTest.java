package com.example.weather_butler;

import android.graphics.Color;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static String TAG = "LQL";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Time() {
        String time = "08:00";
        String t= time.substring(1);
        System.out.println(t);
    }

    @Test
    public void color(){
        int color = R.color.arc_progress_color;
        System.out.println(color);
    }
}