package com.jipup.www.drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.jipup.www.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 주행 타이머가 완료되어 RC카의 주행이 완료되었을 때의 화면
 * 주행 타이머 화면에서 집중도를 보내준 집중도를 가지고 그래프와 평균집중도, 최대집중도를 출력한다.
 */
public class GraphActivity extends Activity {

    private LineChart lineChart; //그래프 차트
    private ArrayList<Integer> arrMeditationValue; //측정된 집중도
    private TextView txtTime, txtAvgmeditation, txtMaxmeditation; //텍스트 현재시간, 평균 집중도, 최고 집중도
    private ImageButton btnGomain; //메인으로 버튼
    private ImageView ibtnDetail; //세부내역 버튼(이미지)
    private TableLayout tableDetail; //새부내역 테이블
    private int avgMeditation, maxMeditation = 0; //평균 집중도, 최고 집중도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //그래프 출력 메소드 호출
        graphDrow();

        //현재시간 설정
        txtTime = (TextView) findViewById(R.id.graph_txtTime);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        txtTime.setText(currentDateTimeString);

        //평균 집중도, 최고 집중도 설정
        txtAvgmeditation = (TextView) findViewById(R.id.graph_txtAvgmeditation);
        txtMaxmeditation = (TextView) findViewById(R.id.graph_txtMaxmeditation);
        txtAvgmeditation.setText(Integer.toString(avgMeditation));
        txtMaxmeditation.setText(Integer.toString(maxMeditation));

        //메인으로 버튼 설정
        btnGomain = (ImageButton) findViewById(R.id.graph_btnGomain);
        btnGomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //세부내역 버튼 설정
        ibtnDetail = (ImageButton) findViewById(R.id.graph_ibtnDetail);
        ibtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableDetail.getVisibility() == View.GONE) {
                    tableDetail.setVisibility(View.VISIBLE);
                } else {
                    tableDetail.setVisibility(View.GONE);
                }
            }
        });


    }

    /**
     * 그래프를 그리는 메소드
     * 라이브러리 mpandroidchartlibrary를 이용하여 라인차트를 만든다.
     * 그래프에 들어가는 값은 Intent를 통하여 받는다.
     */
    private void graphDrow() {

        //Xml 변수들에 대입
        lineChart = (LineChart) findViewById(R.id.graph_chart);
        ArrayList<Entry> arrMeditationValueGraph = new ArrayList<>();

        Intent intent = this.getIntent();
        arrMeditationValue = new ArrayList<>();
        arrMeditationValue = intent.getIntegerArrayListExtra("MeditationValue");

        //임시변수 선언
        int count = arrMeditationValue.size() / 10;
        int avgMeditation = 0, intTemp = 0, entryCount = 0;

        //그래프에 들어갈 값 파싱
        for (int i = 0; i < arrMeditationValue.size(); i++) {
            intTemp++;
            avgMeditation = avgMeditation + arrMeditationValue.get(i);
            if (count == intTemp) {
                arrMeditationValueGraph.add(new Entry(avgMeditation / count, entryCount));
                intTemp = 0;
                avgMeditation = 0;
                entryCount++;
            }

            //평균 집중도 합계, 최고 집중도 추출
            this.avgMeditation = this.avgMeditation + arrMeditationValue.get(i);
            if (this.maxMeditation < arrMeditationValue.get(i)) {
                this.maxMeditation = arrMeditationValue.get(i);
            }

            //세부내역 설정
            String[] rowValue = {Integer.toString(i + 1), Integer.toString(arrMeditationValue.get(i))};
            appendRow(rowValue);
        }

        //평균 집중도 추출
        this.avgMeditation = this.avgMeditation / arrMeditationValue.size();

        LineDataSet dataset = new LineDataSet(arrMeditationValueGraph, "Meditation");

        //X축 시간 파싱
        ArrayList<String> labels = new ArrayList<String>();
        String stringTemp;
        for (int i = 1; i <= 10; i++) {
            stringTemp = Integer.toString(count * i);
            labels.add(stringTemp);
        }

        //차트 설정
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setTouchEnabled(false);
        lineChart.setDescription("");

    }

    /**
     * 세부내역보기 테이블에 집중도 값을 추가하는 메소드
     * String[] 형식으로 들어오면 행의 수 만큼 Row를 만들며 열의 수 만큼 Row에 추가한다.
     */
    private void appendRow(String[] strarray) {

        tableDetail = (TableLayout) findViewById(R.id.graph_tableDetail);
        TextView[] tv = new TextView[strarray.length];
        for (int i = 1; i < strarray.length + 1; i++) {
            tv[i - 1] = new TextView(this);
            tv[i - 1].setText(strarray[i - 1].toString());
            tv[i - 1].setGravity(Gravity.CENTER);

            if ((i != 0 && i % 2 == 0)) {
                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));
                tr.addView(tv[i - 2], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1));
                tr.addView(tv[i - 1], new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1));
                tableDetail.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.FILL_PARENT));
            }
        }
    }

    //뒤로가기 클릭 시 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}