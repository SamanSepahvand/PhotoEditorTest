package com.samansepahvand.photoeditortest;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        fontAssign();


    }
    public void fontAssign() {


        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/iran_sans.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

}
