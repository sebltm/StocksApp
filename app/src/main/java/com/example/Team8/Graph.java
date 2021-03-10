package com.example.Team8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Random;

public class Graph extends AppCompatActivity {

    LineChart mpLineChart;
    ArrayList<Float> xVAL;
    ArrayList<Float> yVAL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        getValues();
        createChart();
    }

    public void getValues(){
        xVAL.add((float) 0);
        yVAL.add((float) 20.11);

        xVAL.add((float) 1);
        yVAL.add((float) 25.55);
    }

    public ArrayList<Entry> addValuesOne(ArrayList<Float> xValues, ArrayList<Float> yValues){
        int size = xValues.size();

        ArrayList<Entry> dataVals = new ArrayList<>();
        for(int i=0;i<size;i++){
            Float x = xValues.get(i);
            Float y = yValues.get(i);
            dataVals.add(new Entry(x, y));
        }
        return dataVals;
    }

    private void createChart(){
        mpLineChart =(LineChart) findViewById(R.id.line_graph);
        LineDataSet lineDataset1 = new LineDataSet(addValuesOne(xVAL, yVAL), "Stocks 1");
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

    public void showSummary(View v){
        startActivity(new Intent(Graph.this, SearchActivity.class));
    }
}
