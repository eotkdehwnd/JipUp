package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jipup.www.R;

/**
 * 비밀번호 수정 화면
 * 현재 비밀번호, 변경할 비밀빈호를 입력받으며 완료 시 정보수정 화면에 전달한다.
 */
public class EditPwActivity extends Activity {

    private EditText edtOriginPw, edtNewPw, edtNewPwCheck; //현재 패스워드, 변경할 패스워드 및 체크
    private Button btnEditPwComplete, btnEditPwCancle; //수정확인 및 취소 버튼
    private String OriginPw; //기존의 비밀번호
    private Intent myinfoEditIntent, editPwIntent; //화면 인텐트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpw);

        //수정하기 액티비티에서 현재비번 받아옴
        editPwIntent = getIntent();
        OriginPw = editPwIntent.getStringExtra("password");

        //Xml 변수들에 대입
        edtOriginPw = (EditText) findViewById(R.id.editpw_edtOriginPw);
        edtNewPw = (EditText) findViewById(R.id.editpw_edtNewPw);
        edtNewPwCheck = (EditText) findViewById(R.id.editpw_edtNewPwCheck);
        btnEditPwCancle = (Button) findViewById(R.id.editpw_btnEditPwCancle);
        btnEditPwComplete = (Button) findViewById(R.id.editpw_btnEditPwComplete);
        myinfoEditIntent = new Intent(getApplicationContext(), MyinfoEditActivity.class);

        //수정완료 버튼 클릭 시
        btnEditPwComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strOriginPw = edtOriginPw.getText().toString();
                String strNewPw = edtNewPw.getText().toString();
                String strNewPwCheck = edtNewPwCheck.getText().toString();

                //비밀번호 제약조건 체크 및 변경 완료
                if (strOriginPw.equals(OriginPw) && strNewPw.equals(strNewPwCheck) &&
                        !(edtNewPw.getText().toString().length() == 0) && !(edtNewPwCheck.getText().toString().length() == 0)) {

                    myinfoEditIntent.putExtra("newpassword", strNewPw);

                    setResult(RESULT_OK, myinfoEditIntent);

                    //자동로그인 캐시를 지운다.
                    SharedPreferences setting;
                    SharedPreferences.Editor editor;
                    setting = getSharedPreferences("setting", 0);
                    editor = setting.edit();
                    editor.clear();
                    editor.commit();
                    finish();

                    Toast.makeText(getApplicationContext(), "비밀번호 수정 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (!(strOriginPw.equals(OriginPw))) {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 정확히 입력하세요!", Toast.LENGTH_SHORT).show();
                } else if (!(strNewPw.equals(strNewPwCheck))) {
                    Toast.makeText(getApplicationContext(), "새 비밀번호를 정확히 입력하세요!", Toast.LENGTH_SHORT).show();
                } else if (edtOriginPw.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "현재 비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                } else if (edtNewPw.getText().length() == 0 || edtNewPwCheck.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "새 비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //취소 버튼
        btnEditPwCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myinfoEditIntent.putExtra("newpassword", OriginPw);
                setResult(RESULT_OK, myinfoEditIntent);
                finish();
            }
        });
    }
}
