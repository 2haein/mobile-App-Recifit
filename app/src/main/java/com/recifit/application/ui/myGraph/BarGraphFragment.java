package com.recifit.application.ui.myGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.recifit.application.R;
import com.recifit.application.databinding.FragmentBarGraphBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BarGraphFragment extends Fragment {

    private FragmentBarGraphBinding binding;
    private String userId;
    private String todayScore = "0";
    private String todayDate;
    private String monthDate;
    private int totalMonthCount=0;
    private float totalScore=0;
    private String MonthScore   ="init";
    private BarDataSet barDataSet;
    private BarChart barChart;
    private String month;
    private Integer flag =0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBarGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        barChart = (BarChart) binding.chart;

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        final TextView textView = binding.representTextView;
        ImageButton imageButton1 = binding.imageButton;
        ImageButton imageButton2 = binding.imageButton2;
        TextView textView2 = binding.scoreText;
        textView2.setText("아직 오늘의 레시피가 기록되지 않았습니다!");
        TextView dateView = binding.DateView;
//        userId = ProfileData.getUserId();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = sdf2.format(date);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");

        month = monthFormat.format(date);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월");
        String getTime = sdf.format(date);
        dateView.setText(getTime);

        /**
         * 후에 API작업: 레시피 개수 받아오기
         * */
        textView.setText(month+"월 간의 평균 레시피 등록 건수");
        

        /**
         * 후에 API2작업: 월일별 레시피 개수 받아오기
         * @param: String userId
         * @param: String month (yyyy-MM)
         * */
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
        monthDate = sdf3.format(date);
        

        /**
         * 레시피 데이터 세팅*/
        barDataSet = new BarDataSet(getMonthEntry(0), "레시피 건수");
        drawGraph();
        textView2.setTextColor(Color.parseColor("#ff8d07"));
        textView.setText(month+"월 간의 평균 레시피 등록 개수");
        if(month.equals("01")){
            textView2.setText("해당 월의 레시피가 등록되지 않았습니다");
            textView2.setTextSize(12);
        }else {
            String resultScore = String.format("%.2f", totalScore / totalMonthCount);
            textView2.setText("2.28" + "개");
            textView2.setTextSize(25);
        }

        /**
         * 달력 기능
         * */
        Calendar cal = Calendar.getInstance( );

        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                totalScore = 0;
                totalMonthCount = 0;
                cal.add ( cal.MONTH, - 1 );
                String time = sdf.format(cal.getTime());
                monthDate = sdf3.format(cal.getTime());
                month = monthFormat.format(cal.getTime());
                dateView.setText(time);
                // 후에 레시피 개수 받아오기
//                MonthScore = getMonthScore(userId, monthDate);
                if(month.equals("12")){
                    barDataSet = new BarDataSet(getMonthEntry(0), "레시피 건수");
                } else if (month.equals("01")){
                    barDataSet = new BarDataSet(getMonthEntry(3), "레시피 건수");
                } else {
                    barDataSet = new BarDataSet(getMonthEntry(1), "레시피 건수");
                }
                drawGraph();

                textView.setText(month+"월 간의 평균 레시피 등록 건수");
                if(month.equals("01")){
                    textView2.setText("해당 월의 레시피가 등록되지 않았습니다");
                    textView2.setTextSize(12);
                } else if(month.equals("12")){
                    textView2.setText("2.28" + "개");
                    textView2.setTextSize(25);
                }
                else {
                    String resultScore = String.format("%.2f", totalScore / totalMonthCount);
                    textView2.setText("1.71" + "개");
                    textView2.setTextSize(25);
                }
            }

        });
        imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                totalScore = 0;
                totalMonthCount= 0;
                cal.add ( cal.MONTH, + 1 );
                String time = sdf.format(cal.getTime());
                monthDate = sdf3.format(cal.getTime());
                month = monthFormat.format(cal.getTime());
                dateView.setText(time);
                // 후에 레시피 개수 받아오기
//                MonthScore = getMonthScore(userId, monthDate);
                if(month.equals("12")){
                    barDataSet = new BarDataSet(getMonthEntry(0), "레시피 건수");
                } else if (month.equals("01")){
                    barDataSet = new BarDataSet(getMonthEntry(3), "레시피 건수");
                } else {
                    barDataSet = new BarDataSet(getMonthEntry(2), "레시피 건수");
                }
                drawGraph();

                textView.setText(month+"월 간의 평균 레시피 등록 건수");
                if(month.equals("01")){
                    textView2.setText("해당 월의 레시피가 등록되지 않았습니다");
                    textView2.setTextSize(12);
                }else if(month.equals("12")){
                    textView2.setText("2.28" + "개");
                    textView2.setTextSize(25);
                } else {
                    String resultScore = String.format("%.2f", totalScore / totalMonthCount);
                    textView2.setText("1.66" + "개");
                    textView2.setTextSize(25);
                }
            }

        });

        return root;
    }

    // 레시피 월별 데이터 얻는 함수
    public List<BarEntry> getMonthEntry(Integer flag){
        List<BarEntry> entries = new ArrayList<>();
        if(flag == 0) {
            entries.add(new BarEntry(0, 0));
            entries.add(new BarEntry(2, 2));
            entries.add(new BarEntry(3, 3));
            entries.add(new BarEntry(7, 2));
        } else if(flag == 1){
            entries.add(new BarEntry(0, 0));
            entries.add(new BarEntry(3, 2));
            entries.add(new BarEntry(6, 1));
            entries.add(new BarEntry(9, 2));
            entries.add(new BarEntry(12, 1));
            entries.add(new BarEntry(16, 1));
            entries.add(new BarEntry(27, 3));
            entries.add(new BarEntry(28, 2));
        } else if(flag ==2){
            entries.add(new BarEntry(0, 0));
            entries.add(new BarEntry(1, 1));
            entries.add(new BarEntry(2, 3));
            entries.add(new BarEntry(3, 2));
            entries.add(new BarEntry(9, 1));
            entries.add(new BarEntry(14, 1));
            entries.add(new BarEntry(17, 1));
            entries.add(new BarEntry(19, 2));
            entries.add(new BarEntry(25, 1));
            entries.add(new BarEntry(27, 2));
        } else {
            entries.add(new BarEntry(0, 0));
        }

        /**
         * Input String
         * [
         *    {
         *       "userName": "sandeep",
         *       "age": 30
         *    }
         * ]
         * Simple Way to Convert String to JSON
         * */

        return entries;
    }


    // 그래프 함수
    public void drawGraph(){
        barDataSet.setColor(Color.parseColor("#ff8d07"));
        barDataSet.setDrawValues(false);

        BarData BarData = new BarData();
        BarData.addDataSet(barDataSet);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisMinimum(1);
        xAxis.setGranularity(1.0f);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setLabelCount(10, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        if(month.equals("01") || month.equals("03") || month.equals("05")|| month.equals("07") || month.equals("08")|| month.equals("10")|| month.equals("12")) {
            xAxis.setAxisMaximum(31);
        } else {
            xAxis.setAxisMaximum(30);
        }


        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        YAxis yRAxis = barChart.getAxisRight();
        yLAxis.setAxisMaximum(5);
        yLAxis.setAxisMinimum(0);
        yLAxis.setTextSize(12);
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yLAxis.setGranularity(1.0f);
        yLAxis.setGranularityEnabled(true);

        Description description = new Description();
        description.setText("(일)/Day");
        description.setTextSize(15);

        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDescription(description);
        barChart.animateY(1500, Easing.EasingOption.EaseInCubic);
        barChart.invalidate();

        barChart.setData(BarData);
        MyMarkerView marker = new MyMarkerView(this, R.layout.markerviewtext);
        marker.setChartView(barChart);
        barChart.setMarker(marker);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}