package com.jipup.www.drive;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jipup.www.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Kim WooJin on 2016-06-24.
 */
public class DrivingListActivity extends Activity{

    ImageButton btnCalendar;
    TextView tvDateView;
    int year_x, month_x, getmonth, day_x; //날짜를 저장할 변수
    static final int DIALOG_ID = 0; //다이얼로그 아이디 설정
    private Intent graphIntent; //그래프 인텐트
    ListView listView;
    ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivinglist);


        //캘린더에서 현재날짜를 얻어 셋팅함
        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        getmonth = month_x + 1;
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        tvDateView = (TextView)findViewById(R.id.drivinglist_tvDateView);
        tvDateView.setText(year_x+". "+getmonth +". "+day_x+". ");

        showDialogOnClickButton();

       /*리스트뷰 어뎁터 생성*/
        adapter = new ListViewAdapter();

        listView = (ListView)findViewById(R.id.listview);

        /*리스트뷰 어뎁터 연결*/
        listView.setAdapter(adapter);

        //임시값 추가
        for (int i=1 ;i<10;i++){
            adapter.addItem(i+"일"+i+"시",i*10+"",i*5+"",i*7+"");
        }

        /*리스트 클릭 리스너*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*리스트 내부에 있는 값을 가져오기위한 변수*/
                ListViewItem item = (ListViewItem)parent.getItemAtPosition(position);

                /*리스트 값 출력방법 (모두 String 형태)
                * 날짜 : item.getTvDate()
                * 타이머 : item.getTvTimer()
                * 평균 : item.getTvAvg()
                * 최대값 : item.getTvMax()
                * */

                /*그래프화면 연결*/
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
            }
        });
    }

    //캘린더 버튼을 클릭했을 때 다이얼로그 호출
    private void showDialogOnClickButton(){
        btnCalendar = (ImageButton)findViewById(R.id.drivinglist_btnCalendar);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });
    }

    //데이트피커 다이얼로그 생성
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        return null;
    }

    //데이트피커 다이얼로그에서 설정한 날짜를 텍스트뷰에 출력
    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;

            tvDateView.setText(year_x + ". "+ month_x +". "+day_x+". ");

        }
    };

}
