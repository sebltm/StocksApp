package com.example.Team8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Graph extends AppCompatActivity { //hi

    LineChart mpLineChart;
    ArrayList<Float> xValues = new ArrayList<Float>();
    ArrayList<Float> yValues = new ArrayList<Float>();

    ArrayList<String> dateXAxis = new ArrayList<String>();
    ArrayList<String> testDateXAxis = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        createChart();
    }

    public void setValues(ArrayList<Date> xIncoming, ArrayList<BigDecimal> yIncoming){
        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        BigDecimal tempBigData;
        Float tempFloat;

        for (int i=0; i<xIncoming.size(); i++) {
            tempBigData = yIncoming.get(i);
            tempFloat = tempBigData.floatValue();

            dateXAxis.add(xIncoming.get(i).toString());
            dataValues.add(new Entry(i, tempFloat));
        }

    }

    public void createValues(){

        yValues.add((float) 20.15);
        yValues.add((float) 29.26346);
        yValues.add((float) 34.512);

        testDateXAxis.add("Hi");
        testDateXAxis.add("Hey");
        testDateXAxis.add("Hello");
    }

    public ArrayList<String> convertXAxis() {

        ArrayList<String> xLabel = new ArrayList<>();
        for (int i = 0; i < dateXAxis.size(); i++)
            xLabel.add(dateXAxis.get(i));
        return xLabel;
    }

    public ArrayList<String> testConvertXAxis() {

        ArrayList<String> xLabel = new ArrayList<>();
        for (int i = 0; i < testDateXAxis.size(); i++)
            xLabel.add(testDateXAxis.get(i));
        return xLabel;
    }
    public ArrayList<Entry> addValuesOne(){
        createValues();
        ArrayList<Entry> dataVals = new ArrayList<Entry>();

        for(int i=0;i<yValues.size();i++){
            Float y = yValues.get(i);
            dataVals.add(new Entry(i, y));
        }
        return dataVals;
    }
    private void createChart(){
        mpLineChart =(LineChart) findViewById(R.id.line_graph);
        LineDataSet lineDataset1 = new LineDataSet(addValuesOne(), "Date");

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(testConvertXAxis()));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataset1);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
    }

    //https://www.programiz.com/java-programming/examples/generate-random-string
    public String randomStringGen(){
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 7;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();

    }
//https://www.codota.com/code/java/methods/com.github.mikephil.charting.charts.Chart/saveToGallery
  //  https://www.programmersought.com/article/71794763317/
    public void saveImageAsPNG(View view) {
       String fileName = randomStringGen();
       mpLineChart.saveToGallery(fileName, "", "MPAndroidChart-Library Save", Bitmap.CompressFormat.PNG, 40);
    }

  //  public void showSummary(View v){
  //      startActivity(new Intent(Graph.this, SearchActivity.class));
   // }
}
