package com.jipup.www.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jipup.www.R;
import com.jipup.www.user.LoginActivity;

/**
 * //인트로 화면 출력
 * Created by Jungminki on 2016-07-04.
 */
public class Intro extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1800);
    }

}
