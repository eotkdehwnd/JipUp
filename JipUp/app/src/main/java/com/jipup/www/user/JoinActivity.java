package com.jipup.www.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jipup.www.R;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * 회원가입 화면
 * 사용자의 이메일, 비밀번호 및 비밀번호 체크, 이름을 입력받아 회원가입을 한다.
 */
public class JoinActivity extends Activity {

    private Button btnOK, btnCancel, btnPasswordCheck; //회원가입 완료, 취소 및 비밀번호 체크 버튼
    private EditText edtEmail, edtName, edtPassword, edtPasswordCheck; //이메일, 이름, 비밀번호 및 비밀번호 재확인
    private CheckBox checkBoxPasswordCheck; //비밀번호 체크를 보여주는 체크박스
    private TextView txtPasswordCheck; //비밀번호 체크확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //Xml 변수들에 대입
        btnOK = (Button) findViewById(R.id.join_btnOK);
        btnCancel = (Button) findViewById(R.id.join_btnCancel);
        edtEmail = (EditText) findViewById(R.id.join_edtEmail);
        edtName = (EditText) findViewById(R.id.join_edtName);
        edtPassword = (EditText) findViewById(R.id.join_edtPassword);
        edtPasswordCheck = (EditText) findViewById(R.id.join_edtPasswordCheck);
        checkBoxPasswordCheck = (CheckBox) findViewById(R.id.join_checkboxPasswordCheck);
        btnPasswordCheck = (Button) findViewById(R.id.join_btnPasswordCheck);
        txtPasswordCheck = (TextView) findViewById(R.id.join_txtPasswordCheck);

        //비밀번호 수정 시 비밀번호 체크 해제
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBoxPasswordCheck.setChecked(false);
            }
        });
        edtPasswordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBoxPasswordCheck.setChecked(false);
            }
        });

        //비밀번호 체크 버튼 클릭 시 제약조건 확인 및 체크
        btnPasswordCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    txtPasswordCheck.setText("비밀번호 확인을 해주세요");
                    checkBoxPasswordCheck.setChecked(false);
                } else if (edtPasswordCheck.getText().toString().equals(edtPassword.getText().toString())) {
                    txtPasswordCheck.setText("비밀번호 확인 완료");
                    checkBoxPasswordCheck.setChecked(true);
                } else {
                    Toast.makeText(getApplicationContext(), "입력된 비밀번호와 다릅니다", Toast.LENGTH_SHORT).show();
                    txtPasswordCheck.setText("비밀번호 확인을 해주세요");
                    checkBoxPasswordCheck.setChecked(false);
                }
            }
        });

        //회원가입 버튼 클릭 시 제약조건 확인 및 회원가입 완료
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (edtName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (!checkBoxPasswordCheck.isChecked()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 체크해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!checkEmailForm(edtEmail.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "본인의 이메일로 아이디를 적어주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(edtName.getText().toString().trim(), edtEmail.getText().toString().trim(), edtPassword.getText().toString());
                }
            }
        });

        //취소 시 로그인 화면을 출력
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void signUp(String name, String id, String pw) {

            String result = task.getResult();
            JSONObject state = new JSONObject(result);
            if (state.getString("State").equals("3")) {
                Toast.makeText(getApplicationContext(), "아이디 중복입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (state.getString("State").equals("1")) {
                Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                return;
            } else if (state.getString("State").equals("0")) {
                Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
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
