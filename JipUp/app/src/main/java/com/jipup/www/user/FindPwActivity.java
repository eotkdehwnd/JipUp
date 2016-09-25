package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jipup.www.R;

import java.util.regex.Pattern;

/**
 * 비밀번호 찾기 화면
 * 사용자가 비밀번호를 찾을 이메일을 입력한다.
 */
public class FindPwActivity extends Activity {

    private EditText edtEmail; //이메일 입력받는 텍스트
    private Button btnOk, btnCancel; //확인, 취소 버튼
    private Intent tempPwIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        //Xml 변수들에 대입
        edtEmail = (EditText)findViewById(R.id.findpw_edtEmail);
        btnOk = (Button)findViewById(R.id.findpw_btnOk);
        btnCancel = (Button)findViewById(R.id.findpw_btnCancel);

        //확인버튼 클릭 시 작성된 이메일이 형식이 맞는지 확인 후 임시 비밀번호 화면에 보낸다.
        //그 후 종료하고 임시 비밀번호 화면을 불러온다.
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEmailForm(edtEmail.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "이메일 형식으로 적어주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                tempPwIntent = new Intent(getApplication(), TempPwActivity.class);
                tempPwIntent.putExtra("findPwEmail", edtEmail.getText().toString());
                startActivity(tempPwIntent);
                finish();
            }
        });

        //취소버튼 클릭 시 종료
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *   아이디 이메일 형식 확인
     *   email 형식 체크 함수
     *   true : 이메일 형식에 맞음
     *   false : 이메일 형식에 맞지 않음
     **/
    public boolean checkEmailForm(String src){
        String emailRegex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        return Pattern.matches(emailRegex, src);
    }
}