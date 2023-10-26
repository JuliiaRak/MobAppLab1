package com.mobileapp.laba1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import java.util.ArrayList;
import java.util.List;

public class Graph extends Fragment {
    private AnyChartView anyChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        anyChartView = view.findViewById(R.id.any_chart_view);

        double[][] matrix = OneMatrix.returnMatrix();

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                data.add(new ValueDataEntry("Cell " + i + "-" + j, matrix[i][j]));
            }
        }

        Cartesian cartesian = AnyChart.column();
        cartesian.column(data);

        cartesian.xAxis(0).title("Стовпці");
        cartesian.yAxis(0).title("Значення");

        anyChartView.setChart(cartesian);

        return view;
    }

}
