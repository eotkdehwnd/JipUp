package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jipup.www.R;

import org.json.JSONObject;

/**
 * 임시 비밀번호를 발송하는 화면
 * FindPwActivity에서 이메일을 입력받은 곳으로 임시비밀번호를 전송한 뒤 사용자의 비밀번호를 변경한다.
 */
public class TempPwActivity extends Activity {

    private String strEmail, strState; //임시 비밀번호를 발송할 이메일, 상태정보
    private TextView tvEmail; //임시 비밀번호를 발송할 이메일을 보여주는 텍스트
    private Button btnOk; //확인버튼

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temppw);

        //Xml 변수들에 대입
        tvEmail = (TextView)findViewById(R.id.temppw_tvEmail);
        btnOk = (Button)findViewById(R.id.temppw_btnOK);

        Intent getIntent = getIntent();
        strEmail = getIntent.getStringExtra("findPwEmail");
        tvEmail.setText(strEmail);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTempPwIssue();
                if(strState.equals("0")) {
                    Toast.makeText(getApplicationContext(), "발급 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(strState.equals("1")) {
                    Toast.makeText(getApplicationContext(), "발급에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(strState.equals("2")) {
                    Toast.makeText(getApplicationContext(), "존재하지 않는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    //임시비밀번호 발급 메소드
    private boolean isTempPwIssue() {
        try {
            task.join();
            String result = task.getResult();
            JSONObject state = new JSONObject(result);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
