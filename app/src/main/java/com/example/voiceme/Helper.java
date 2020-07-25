package com.example.voiceme;

import android.app.Activity;
import android.content.Intent;

public class Helper {
    public static void nextPage(Activity from, Activity to){
        Intent i = new Intent(from,to.getClass());
        from.startActivity(i);
        from.finish();
    }

}
