package com.example.datadisplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PieChartActivity extends AppCompatActivity {
    @BindView(R.id.piechart)
    PieChart   piechart;
    ArrayList<String> pieChartList;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        ButterKnife.bind(this);
        pieChartList= (ArrayList<String>) getIntent().getSerializableExtra("pieChartList");
        nameList= (ArrayList<String>) getIntent().getSerializableExtra("nameList");
        if(pieChartList!=null&&nameList!=null)
            setPieChart(pieChartList,nameList);
    }

    private void setPieChart(ArrayList<String> pieChartList, ArrayList<String> nameList) {
       ArrayList salaries=new ArrayList();
       for(int i=0;i<10;i++)
       {
           salaries.add(new PieEntry(Float.parseFloat(pieChartList.get(i)),nameList.get(i)));
       }
        PieDataSet dataSet = new PieDataSet(salaries, "Salaries Of Employees");
      /* ArrayList name=new ArrayList();

            for(int j=0;j<10;j++)
            {
                name.add(nameList.get(j));
            }*/

        PieData data = new PieData(dataSet);
        piechart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        piechart.animateXY(5000, 5000);

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
