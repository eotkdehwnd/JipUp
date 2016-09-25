package com.jipup.www.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcjun.mkjar.QuadcoreView;
import com.jipup.www.R;
import com.jipup.www.drive.DrivingListActivity;
import com.jipup.www.drive.TimerActivity;
import com.jipup.www.user.LoginActivity;
import com.jipup.www.user.MyinfoEditActivity;

/**
 * 애플리케이션의 메인 화면
 * 주행하기, 정보수정, 로그아웃을 할 수 있으며 로그인할 시 기본적인 화면
 */
public class MainActivity extends Activity {

    private TextView tvGetEmail, tvGetName; //사용자 이메일 및 이름
    private Button btnLogout, btnEdit, btnStart, btnDrivingList; //로그아웃, 정보수정, 주행하기 버튼
    private QuadcoreView imgPhoto; //프로필 사진
    private Bitmap bitmapPhoto; //프로필 사진(비트맵)
    private String strName, strEmail, strPw, strImg, strNum; //사용자 정보 객체
    private Intent myinfoEditIntent, loginIntent, timerIntent, drivingListIntent; //화면 인텐트

    //뒤로가기 클릭 설정 변수
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Xml 변수들에 대입
        btnLogout = (Button) findViewById(R.id.main_btnLogout);
        tvGetEmail = (TextView) findViewById(R.id.main_tvGetEmail);
        tvGetName = (TextView) findViewById(R.id.main_tvGetName);
        btnEdit = (Button) findViewById(R.id.main_btnEdit);
        imgPhoto = (QuadcoreView) findViewById(R.id.main_imgPhoto);
        btnStart = (Button) findViewById(R.id.main_btnStart);
        btnDrivingList = (Button) findViewById(R.id.main_btnDrivingList);

        loginIntent = getIntent();

        //로그인된 사용자의 이름, 이메일, 비번을 문자변수에 저장시킴
        strName = loginIntent.getStringExtra("LoginName");
        strEmail = loginIntent.getStringExtra("LoginEmail");
        strPw = loginIntent.getStringExtra("LoginPW");
        strNum = loginIntent.getStringExtra("LoginNum");
        strImg = loginIntent.getStringExtra("LoginImg");

        //텍스트뷰에 로그인된 사용자의 이름, 이메일, 프로필 사진을 출력시킴
        tvGetName.setText(String.valueOf(strName));
        tvGetEmail.setText(String.valueOf(strEmail));
        photoLoad();


        //로그아웃 버튼 클릭
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                //자동로그인 캐시를 지운다.
                SharedPreferences setting;
                SharedPreferences.Editor editor;
                setting = getSharedPreferences("setting", 0);
                editor = setting.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });

        //수정하기 버튼 클릭
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수정하기 액티비티를 불러오는 인텐트 선언
                myinfoEditIntent = new Intent(getApplicationContext(), MyinfoEditActivity.class);

                //로그인된 사용자의 이름과 비번을 수정하기액티비티에 보내줌
                myinfoEditIntent.putExtra("UserName", strName);
                myinfoEditIntent.putExtra("UserPassWord", strPw);
                myinfoEditIntent.putExtra("UserNum", strNum);
                myinfoEditIntent.putExtra("UserImg", strImg);

                startActivityForResult(myinfoEditIntent, 0);
            }
        });

        //주행하기 버튼 클릭
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerIntent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(timerIntent);
            }
        });

        //내역보기 버튼 클릭
        btnDrivingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drivingListIntent = new Intent(getApplicationContext(), DrivingListActivity.class);
                startActivity(drivingListIntent);

            }
        });

    }

    @Override //양방향 액티비티를 사용하여 수정된 이름과 비번을 받아옴
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            strName = data.getStringExtra("EditName");
            strPw = data.getStringExtra("EditPassWord");
            //수정된 이름, 프로필사진 출력해줌
            tvGetName.setText(String.valueOf(strName));
            photoLoad();
        }
    }

    /**
     *사용자 이미지가 설정 된 것이 없으면 기본
     * 있을 시 이미지를 가져온다.
     */
    private void photoLoad() {

        if(strImg.equals("null")){
            bitmapPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.profile_default);
            imgPhoto.setImageBitmap(bitmapPhoto);
        }else {
            imgPhoto.show(url);
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}