package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jipup.www.R;

/**
 * 비밀번호 수정 화면
 * 비밀번호 찾기를 통해 임시 비밀번호를 가진 사용자가 로그인시 출력 되는 화면
 */
public class ReisEditPwActivity extends Activity {

    String strNum;
    EditText edtNewPw, edtNewPwCheck;
    Button btnEditPwComplete, btnEditPwCancle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reissueeditpw);

        Intent intent = getIntent();
        strNum = intent.getStringExtra("LoginNum");

        //Xml 변수들에 대입
        edtNewPw = (EditText)findViewById(R.id.reieditpw_edtNewPw);
        edtNewPwCheck = (EditText)findViewById(R.id.reieditpw_edtNewPwCheck);
        btnEditPwComplete = (Button)findViewById(R.id.reieditpw_btnEditPwComplete);
        btnEditPwCancle = (Button)findViewById(R.id.reieditpw_btnEditPwCancle);

        btnEditPwComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNewPw.equals("")||edtNewPwCheck.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(edtNewPwCheck.getText().toString().equals(edtNewPw.getText().toString())){
                    reissueEditPw();
                }else {
                    Toast.makeText(getApplicationContext(), "입력된 비밀번호와 다릅니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEditPwCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void reissueEditPw() {
        Toast.makeText(getApplicationContext(), "다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        //자동로그인 캐시를 지운다.
        SharedPreferences setting;
        SharedPreferences.Editor editor;
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        editor.clear();
        editor.commit();
        finish();
    }
}
