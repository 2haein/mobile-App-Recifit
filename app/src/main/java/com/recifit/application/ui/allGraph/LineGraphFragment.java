package com.recifit.application.ui.allGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.recifit.application.MainActivity;
import com.recifit.application.databinding.FragmentLineGraphBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineGraphFragment extends Fragment {

    private FragmentLineGraphBinding binding;
    private LineChart lineChart;
    private String userId;
    private String todayScore = "0";
    private String todayDate;
    private String monthDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLineGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lineChart = (LineChart) binding.chart;
        List<Entry> entries = new ArrayList<>();
        lineChart.clear();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 3));
        entries.add(new Entry(2, 4));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 7));
        entries.add(new Entry(5, 11));
        entries.add(new Entry(6, 12));
        entries.add(new Entry(7, 14));
        entries.add(new Entry(8, 18));
        entries.add(new Entry(9, 22));
        entries.add(new Entry(10, 25));
        entries.add(new Entry(11, 27));

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        TextView textView = binding.representTextView;
//        textView.setText("오늘의 등록한 레시피 건수");
        textView.setText("올해 총 등록된 레시피 건수");

        TextView textView2 = binding.scoreText;

        textView2.setTextColor(Color.parseColor("#ff8d07"));
        textView2.setText("27건");
        textView2.setTextSize(25);
        TextView dateView = binding.DateView;
        MainActivity activity = (MainActivity) getActivity();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        todayDate = sdf2.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년");
        String getTime = sdf.format(date);
        dateView.setText(getTime);
        dateView.setTextSize(27);

        LineDataSet lineDataSet = new LineDataSet(entries, "등록한 레시피 수");
        lineDataSet.setLineWidth(3);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleColor(Color.parseColor("#FFBB86FC"));
        lineDataSet.setCircleColorHole(Color.parseColor("#FF6200EE"));
        lineDataSet.setColor(Color.parseColor("#FFBB86FC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisMinimum(1);
        xAxis.setGranularity(1.0f);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setLabelCount(10, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌
        xAxis.setAxisMaximum(12);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        YAxis yRAxis = lineChart.getAxisRight();
        yLAxis.setAxisMaximum(30);
        yLAxis.setAxisMinimum(0);
        yLAxis.setTextSize(12);
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        yLAxis.setGranularity(1.0f);
        yLAxis.setGranularityEnabled(true);

        Description description = new Description();
        description.setText("(월)/Month");
        description.setTextSize(10);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(1500, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        lineChart.setData(lineData);

        return root;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}