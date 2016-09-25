package com.jipup.www.drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jipup.www.R;

import java.util.ArrayList;

/**
 * RC카를 몇 초 동안 주행하게 할지 설정하는 화면
 * 최소 30초 부터 최대 1분 30초(90초)까지 설정할 수 있다.
 * 시작버튼을 누르면 1초씩 타이머가 감소하는 화면이 출력되며
 * 타이머 완료 시 집중도를 주행완료 화면에게 송신하며 종료된다.
 */
public class TimerActivity extends Activity {

    private Button btnStart, btnEnd; //타이머 시작, 종료 버튼
    private TextView txtTimeEnd; //남은시간
    private NumberPicker npMin, npSec; //사용자 설정하는 분, 초

    private Integer time; //시간
    private Intent graphIntent; //그래프 인텐트
    private LinearLayout layTimerCountdown, layTimerSet; //타임설정 레이아웃 및 남는 시간 레이아웃
    private CountDownTimer timerCountDown; //타이머
    private boolean isTimerStart = false; //타이머 시작 체크

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //Xml 변수들에 대입
        npMin = (NumberPicker) findViewById(R.id.timer_npMin);
        npSec = (NumberPicker) findViewById(R.id.timer_npSec);
        btnStart = (Button) findViewById(R.id.timer_btnStart);
        btnEnd = (Button) findViewById(R.id.timer_btnEnd);
        txtTimeEnd = (TextView) findViewById(R.id.timer_txtTimeEnd);
        layTimerCountdown = (LinearLayout) findViewById(R.id.timer_layTimerCountdown);
        layTimerSet = (LinearLayout) findViewById(R.id.timer_layTimerSet);

        /**
         * 타이머 시간을 줄이는 버튼
         * */
        //넘버피커의 최소 숫자 설정
        npMin.setMinValue(0);
        npSec.setMinValue(0);
        //넘버피커의 최대 숫자 설정
        npMin.setMaxValue(1);
        npSec.setMaxValue(59);
        //넘버피커 기본 값 설정
        npSec.setValue(30);

        //넘버피커의 값이 변경 될때 사용되는 리스너
        npSec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (npMin.getValue() == 0) { // 분의 값이 0인 상태에서
                    if (newVal == 0) { // 초의 값이 0으로 됬을 때
                        npMin.setValue(1); // 분의 값을 1로 변경
                    }
                    //if (newVal < 1) { // 초의 값이 30미만으로 됬을 때
                   //     npSec.setValue(30); //초의 값을 30으로 변경
                  //  }
                } else if (npMin.getValue() == 1) { // 분의 값이 1인 상태에서
                    if (newVal == 0) { // 초의 값이 0으로 됬을 때
                        npMin.setValue(0); // 분의 값을 0로 변경
                    }
                    if(newVal > 30) { // 초의 값이 30초과가 됬을 때
                        npSec.setValue(30); //초의 값을 30으로 변경
                    }
                }
            }
        });

        /**
         *  시작 버튼 클릭시 타이머에 있는 시간을 하나씩 줄여가며 타이머가 종료시 알려줌
         * */
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 타이머에 있는 시간
                 * */
                time = npMin.getValue() * 60 + npSec.getValue();

                if (time < 1) {
                    Toast.makeText(getApplicationContext(), "30초 부터 가능합니다.", Toast.LENGTH_LONG).show();
                } else if (time > 90) {
                    Toast.makeText(getApplicationContext(), "1분 30초 까지 가능합니다.", Toast.LENGTH_LONG).show();
                } else {

                    /**
                     * 타이머가 시작되면 시작버튼은 보이지 않게 되고 취소버튼은 보이게 됨
                     * */
                    btnStart.setVisibility(View.GONE);
                    btnEnd.setVisibility(View.VISIBLE);
                    layTimerCountdown.setVisibility(View.VISIBLE);
                    layTimerSet.setVisibility(View.GONE);

                    /**
                     * 타이머에 있는 시간만큼 작동
                     * */
                    isTimerStart = true;
                    timerCountDown = new CountDownTimer(time * 1000, 1000) {
                        Integer sec = npSec.getValue();
                        Integer min = npMin.getValue();

                        /**
                         * 타이머가 작동중에 일어나는 메소드
                         * */
                        public void onTick(long mill) {
                            /**
                             * 줄어드는 시간을 남은 시간으로 표시해줌
                             * */
                            if (sec == 0) {
                                min = min - 1;
                                sec = 60;
                            }
                            time--;
                            sec = sec - 1;
                            if (sec < 10) {
                                txtTimeEnd.setText("0" + min + ":0" + sec);
                            } else {
                                txtTimeEnd.setText("0" + min + ":" + sec);
                            }


                            /**
                             * 취소버튼
                             * */
                            btnEnd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * 타이머는 멈춤
                                     * */
                                    cancel();
                                    /**
                                     * 취소버튼은 감추고 시작버튼을 보임
                                     * */
                                    btnStart.setVisibility(View.VISIBLE);
                                    btnEnd.setVisibility(View.GONE);
                                    layTimerCountdown.setVisibility(View.GONE);
                                    layTimerSet.setVisibility(View.VISIBLE);
                                    /**
                                     * 시간을 다시 적제 (이렇게 하지 않을 경우 도중 멈추었던 시간이 움직임)
                                     * */
                                    time = npMin.getValue() * 60 + npSec.getValue();
                                    Toast.makeText(getApplicationContext(), "타이머 취소!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        /**
                         * 타이머가 완료 되었을 때의 메소드
                         * */
                        public void onFinish() {
                            /**
                             * 남은 시간을 0로 표기하고
                             * 취소 버튼은 잠기고 시작 버튼은 누를 수 있게됨
                             * 토스트로 타이머가 완료 되었음을 알림
                             * */
                            Toast.makeText(getApplicationContext(), "타이머 완료!", Toast.LENGTH_SHORT).show();
                            graphIntent = new Intent(getApplicationContext(), GraphActivity.class);

                            //테스트 값 전송
                            ArrayList<Integer> test = new ArrayList<>();

                            test.add(20);
                            test.add(24);
                            test.add(27);
                            test.add(30);
                            test.add(35);
                            test.add(40);
                            test.add(52);
                            test.add(62);
                            test.add(63);
                            test.add(65);
                            test.add(50);
                            test.add(44);
                            test.add(37);
                            test.add(40);
                            test.add(45);
                            test.add(60);
                            test.add(72);
                            test.add(74);
                            test.add(80);
                            test.add(81);
                            test.add(90);
                            test.add(83);
                            test.add(72);
                            test.add(70);
                            test.add(65);
                            test.add(50);
                            test.add(44);
                            test.add(40);
                            test.add(30);
                            test.add(30);

                            graphIntent.putExtra("MeditationValue", test);
                            startActivity(graphIntent);
                            finish();
                        }
                    }.start();
                }
            }
        });
    }

    //뒤로가기 클릭 시 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isTimerStart){
                timerCountDown.cancel();
            }
            finish();
        }
        return false;
    }
}