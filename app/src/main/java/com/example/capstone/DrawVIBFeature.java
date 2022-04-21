package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class DrawVIBFeature extends AppCompatActivity implements View.OnClickListener{

    public static Context context_main;

    public TextView vibdate;
    public LineChart chart6;
    public LineChart chart7;
    public LineChart chart8;
    public TextView cur_x;
    public TextView cur_y1;
    public TextView cur_y2;
    public TextView cur_y3;
    public TextView cur_y4;

    public TextView selec_point1;
    public TextView selec_point2;
    public TextView selec_point3;
    public TextView selec_point4;


    public CheckBox check_hour;
    public CheckBox check_day;
    public CheckBox check_week;
    public CheckBox check_month;


    String period="day";

    Calendar cal = Calendar.getInstance();
    int buff_size=0;

    int i=0;

    double rms = 0.0;
    double hrms = 0.0;
    double erms = 0.0;
    double irms = 0.0;
    String date="";
    Date date1=null;

    boolean run = true;
    String ip;
    String facility;
    String type;


    String label1="";
    String label2="";
    String label3="";
    String label4="";

    ArrayList<String> now_date = new ArrayList<>();
    double[] rms1_darr;
    double[] rms2_darr;
    double[] rms3_darr;
    double[] rms4_darr;

/*
    LineData data;
    ILineDataSet set;
    ILineDataSet set2;
    ILineDataSet set3;
    ILineDataSet set4;
*/
    float time_sum=0;
    float date_sum=0;

    ArrayList<String> xAxis = new ArrayList<>();
    ArrayList<String> xAxis_allinfo = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibchart);

        context_main = this;



        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        facility = intent.getStringExtra("facility");
        type = intent.getStringExtra("type");
        Log.v("dfeature","ip : "+ip+"device : "+facility);

        ArrayList<String> label_group = new ArrayList<>();

        if(type.equals("2")){
            label1 = "MIH";
            label2 = "CIV";
            label3 = "PHV1";
            label4 = "PHV2";

        }
        else if(type.equals("3")){
            label1 = "MIH";
            label2 = "MIA";
            label3 = "PIH";
            label4 = "PIV";

        }
        //여기 추가

        label_group.add(label1);
        label_group.add(label2);
        label_group.add(label3);
        label_group.add(label4);

        vibdate = (TextView) findViewById(R.id.vibdate);
        chart6 = (LineChart) findViewById(R.id.chart6);
        //chart7 = (LineChart) findViewById(R.id.chart7);
        //chart8 = (LineChart) findViewById(R.id.chart8);
        cur_x = (TextView) findViewById(R.id.current_x);

        selec_point1 = (TextView) findViewById(R.id.current_label1);
        cur_y1 = (TextView) findViewById(R.id.current_y1);
        selec_point2 = (TextView) findViewById(R.id.current_label2);
        cur_y2 = (TextView) findViewById(R.id.current_y2);
        selec_point3 = (TextView) findViewById(R.id.current_label3);
        cur_y3 = (TextView) findViewById(R.id.current_y3);
        selec_point4 = (TextView) findViewById(R.id.current_label4);
        cur_y4 = (TextView) findViewById(R.id.current_y4);

        check_hour = (CheckBox) findViewById(R.id.check_hour);
        check_hour.setOnClickListener(this);
        check_day = (CheckBox) findViewById(R.id.check_day);
        check_day.setOnClickListener(this);
        check_week = (CheckBox) findViewById(R.id.check_week);
        check_week.setOnClickListener(this);
        check_month = (CheckBox) findViewById(R.id.check_month);
        check_month.setOnClickListener(this);

        //HandlerThread handlerThread2 = new HandlerThread("VIB_FEATURE2");
        //handlerThread2.start();
        //Handler statusHandler2 = new Handler(handlerThread2.getLooper());
        //statusHandler2.postDelayed(new com.example.capstone.DrawVIBFeature.getFeatureInit(ip,facility,period), 10);


        /*
        HandlerThread handlerThread2 = new HandlerThread("VIB_FEATURE2");
        handlerThread2.start();
        Handler statusHandler2 = new Handler(handlerThread2.getLooper());
        statusHandler2.postDelayed(new com.example.capstone.DrawVIBFeature.getFeatureInit(ip,facility,period), 10);
*/


        chart6.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.v("entry","e : "+e.toString());

                Log.v("entry","h : "+h.toString());

                //Log.v("entry",""+xAxis_allinfo.get((int)e.getX()));
                //Toast myToast = Toast.makeText(getApplicationContext(),"X : "+e.getX()+ "Y : "+e.getY(), Toast.LENGTH_SHORT);
                //myToast.show();
                //Log.v("valuese",""+xAxis_allinfo.size());


                String[] temp = now_date.get((int)e.getX()).split(" ");
                cur_x.setText(temp[1]);

                selec_point1.setText(label_group.get(0));
                selec_point2.setText(label_group.get(1));
                selec_point3.setText(label_group.get(2));
                selec_point4.setText(label_group.get(3));


                cur_y1.setText(""+rms1_darr[(int)e.getX()]);
                cur_y2.setText(""+rms2_darr[(int)e.getX()]);
                cur_y3.setText(""+rms3_darr[(int)e.getX()]);
                cur_y4.setText(""+rms4_darr[(int)e.getX()]);

                vibdate.setText(temp[0]);
                //selec_point.setText(label_group.get(h.getDataSetIndex()));



            }


            @Override
            public void onNothingSelected() {

            }
        });


        chart6.setDrawGridBackground(false);
        //chart6.setBackgroundColor(getResources().getColor(R.color.white));
        chart6.setBackground(getResources().getDrawable(R.drawable.round_black));


        //chart6.setBackground(getResources().getDrawable(R.drawable.arrow));

        //chart.setGridBackgroundColor(R.color.black);






// description text
        chart6.getDescription().setEnabled(true);
        Description des = chart6.getDescription();
        des.setEnabled(true);
        des.setText("RMS");
        des.setTextSize(15f);
        des.setTextColor(R.color.black);

// touch gestures (false-비활성화)
        chart6.setTouchEnabled(true);




// scaling and dragging (false-비활성화)
        chart6.setDragEnabled(true);
        chart6.setScaleEnabled(true);

//auto scale
        chart6.setAutoScaleMinMaxEnabled(false);

// if disabled, scaling can be done on x- and y-axis separately
        chart6.setPinchZoom(true);

//X축
        chart6.getXAxis().setDrawGridLines(false);
        chart6.getXAxis().setDrawAxisLine(true);
        chart6.getXAxis().setAxisLineColor(getResources().getColor(R.color.black));
        chart6.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart6.getXAxis().setEnabled(true);
        chart6.getXAxis().setTextSize(10f);
        //chart6.getXAxis().setLabelCount(7);
        chart6.getXAxis().setLabelRotationAngle(20f);
        //chart6.getXAxis().setDrawAxisLine(true);

        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter().getFormattedValue((float)1.0));


//Legend
        Legend l = chart6.getLegend();
        l.setEnabled(false);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(15f);
        l.setTextColor(R.color.black);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);

//Y축
        YAxis leftAxis = chart6.getAxisLeft();
        leftAxis.setAxisMaximum((float)10.0);
        leftAxis.setAxisMinimum((float)-1.0);

        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.black));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawTopYLabelEntry(true);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.black));
        //leftAxis.setDrawGridLines(false);
        //leftAxis.setGridColor(getResources().getColor(R.color.white));




        LimitLine llRange = new LimitLine(10, "Danger");
        llRange.setLineColor(Color.parseColor("#FF3D00"));
        llRange.setTextColor(Color.parseColor("#FF3D00"));
        llRange.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        llRange.setLineWidth(1f);

        LimitLine llRange2 = new LimitLine(6, "Warning");
        llRange2.setLineColor(Color.parseColor("#FB8C00"));
        llRange2.setTextColor(Color.parseColor("#FB8C00"));

        llRange2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        llRange2.setLineWidth(1f);

        LimitLine llRange3 = new LimitLine((float)2.3, "Normal");
        llRange3.setLineColor(Color.parseColor("#4CAF50"));
        llRange3.setTextColor(Color.parseColor("#4CAF50"));
        llRange3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        llRange3.setLineWidth(1f);


        leftAxis.addLimitLine(llRange);
        leftAxis.addLimitLine(llRange2);
        leftAxis.addLimitLine(llRange3);

        //leftAxis.setDrawGridLinesBehindData(true);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);


        YAxis rightAxis = chart6.getAxisRight();
        rightAxis.setEnabled(false);
        chart6.invalidate();
// don't forget to refresh the drawing





/*
        HandlerThread handlerThread = new HandlerThread("VIB_FEATURE");

        handlerThread.start();

        Handler statusHandler = new Handler(handlerThread.getLooper());

        statusHandler.postDelayed(new com.example.capstone.DrawVIBFeature.getFeature(ip,facility), 10);
*/



    }
/*
    public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            Log.v("xaxis",""+value);
            // Convert float value to date string
            // Convert from seconds back to milliseconds to format time  to show to the user
            //SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");
            //value = ((value) / 10) + (date_sum-4);  //+분
            //value = value*5;




            //long valueToMinutes = TimeUnit.MINUTES.toMillis((long)value);
            //Date timeMinutes = new Date(valueToMinutes);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            // Show time in local version
            //long millis = TimeUnit.SECONDS.toMillis((long) value);
            return formatMinutes.format(cal.getTime());
        }
    }
*/

    @Override
    public void onBackPressed(){
        run = false;
        //thread2.interrupt();
        super.onBackPressed();

    }


    private LineDataSet createSet(String label,int hcolor) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.black));
        set.setColor(getResources().getColor(hcolor));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(getResources().getColor(hcolor));
        set.setHighlightLineWidth(1f);

        return set;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getPeriodFeature(String facility,String period){
        ArrayList<ArrayList<String>> resultText1 = new ArrayList<>();
        String[] temp_time;
        String[] temp_date;
        String all_date;
        LineData data = chart6.getData();

        ILineDataSet set;
        ILineDataSet set2;
        ILineDataSet set3;
        ILineDataSet set4;
        try {


            resultText1 = new Task().execute("http://203.250.77.240:50010/manage/Device/SDG" + facility + ".csv/" + period).get();

            if(resultText1.get(0).get(0).equals("timeout")){
                Toast myToast = Toast.makeText(getApplicationContext(),"서버 연결X", Toast.LENGTH_SHORT);
                myToast.show();
                Intent intent = getIntent();
                finish(); //현재 액티비티 종료 실시
                overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
                startActivity(intent); //현재 액티비티 재실행 실시
                overridePendingTransition(0, 0);
            }
            else {

                buff_size = resultText1.get(0).size();
                Log.v("buffsize", "bf" + resultText1.get(0).get(0));

                now_date = resultText1.get(0);
                rms1_darr = Arrays.stream(resultText1.get(1).toArray(new String[resultText1.get(1).size()])).mapToDouble(Double::parseDouble).toArray();
                rms2_darr = Arrays.stream(resultText1.get(2).toArray(new String[resultText1.get(2).size()])).mapToDouble(Double::parseDouble).toArray();
                rms3_darr = Arrays.stream(resultText1.get(3).toArray(new String[resultText1.get(3).size()])).mapToDouble(Double::parseDouble).toArray();
                rms4_darr = Arrays.stream(resultText1.get(4).toArray(new String[resultText1.get(4).size()])).mapToDouble(Double::parseDouble).toArray();

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        data = new LineData();
        chart6.setData(data);

        set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well
        set2 = data.getDataSetByIndex(1);
        set3 = data.getDataSetByIndex(2);
        set4 = data.getDataSetByIndex(3);


        if (set == null) {
            set = createSet(label1,R.color.blue);
            data.addDataSet(set);
        }
        if (set2 == null) {
            set2 = createSet(label2,R.color.green);
            data.addDataSet(set2);
        }
        if (set3 == null) {
            set3 = createSet(label3,R.color.yellow);
            data.addDataSet(set3);
        }
        if (set4 == null) {
            set4 = createSet(label4,R.color.red);
            data.addDataSet(set4);
        }

        for(i=0;i<buff_size;i++){
            data.addEntry(new Entry((float)i, (float)rms1_darr[i]), 0);
            data.addEntry(new Entry((float)i, (float)rms2_darr[i]), 1);
            data.addEntry(new Entry((float)i, (float)rms3_darr[i]), 2);
            data.addEntry(new Entry((float)i, (float)rms4_darr[i]), 3);
        }

        //SimpleDateFormat mFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //vibdate.setText(all_date.split(" ")[0]);


        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());

        data.notifyDataChanged();
        chart6.notifyDataSetChanged();
        chart6.animateXY(1000, 1000);
        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
        chart6.invalidate();
    }

    public ArrayList<String> increaseDate(){

        /*
        xAxis = new ArrayList<>();

        xAxis_allinfo = new ArrayList<>();

        Log.v("calendar","in function : "+cal.get(Calendar.YEAR)+"년"
                +cal.get(Calendar.MONTH)+"월"
                +(cal.get(Calendar.DAY_OF_MONTH)+1)+"일"
                +cal.get(Calendar.HOUR)+"시"
                +cal.get(Calendar.MINUTE)+"분"
                +cal.get(Calendar.SECOND)+"초");
        //long valueToMinutes = TimeUnit.MINUTES.toMillis((long)value);
        //Date timeMinutes = new Date(valueToMinutes);

        // Show time in local version
        //long millis = TimeUnit.SECONDS.toMillis((long) value);
        Log.v("buffsize","bf"+buff_size);



        if(buff_size<20) {
            cal.add(Calendar.HOUR,-1);
            cal.add(Calendar.MINUTE, 5);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatMinutes2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < buff_size; i++) {
                //Log.v("calendar","1시간단위"+formatMinutes2.format(cal.getTime()));
                xAxis.add(formatMinutes.format(cal.getTime()));
                xAxis_allinfo.add(formatMinutes2.format(cal.getTime()));

                cal.add(Calendar.MINUTE, 5);


            }
        }
        else if(buff_size > 20 && buff_size<300){
            cal.add(Calendar.DAY_OF_MONTH,-1);
            cal.add(Calendar.MINUTE, 5);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatMinutes2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 0; i < buff_size; i++) {

                xAxis.add(formatMinutes.format(cal.getTime()));
                xAxis_allinfo.add(formatMinutes2.format(cal.getTime()));

                cal.add(Calendar.MINUTE, 5);

            }
        }
        else if(buff_size > 300 && buff_size<3000){
            cal.add(Calendar.DAY_OF_MONTH,-7);
            cal.add(Calendar.MINUTE, 5);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatMinutes2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < buff_size; i++) {

                xAxis.add(formatMinutes.format(cal.getTime()));
                xAxis_allinfo.add(formatMinutes2.format(cal.getTime()));

                cal.add(Calendar.MINUTE, 5);

            }
        }
        else{
            cal.add(Calendar.MONTH,-1);
            cal.add(Calendar.MINUTE, 5);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatMinutes2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < buff_size; i++) {


                xAxis.add(formatMinutes.format(cal.getTime()));
                xAxis_allinfo.add(formatMinutes2.format(cal.getTime()));

                cal.add(Calendar.MINUTE, 5);

            }
        }

         */

        //String[] temp = now_date.split(" ");

        return now_date;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        /*
        ArrayList<ArrayList<String>> resultText1 = new ArrayList<>();
        String[] temp_date;
        LineData data = chart6.getData();
        ILineDataSet set;
        ILineDataSet set2;
        ILineDataSet set3;
        ILineDataSet set4;
*/
        switch (v.getId()) {

            case R.id.check_hour:
                check_day.setChecked(false);
                check_week.setChecked(false);
                check_month.setChecked(false);
                period = "hour";


                getPeriodFeature(facility,period);
                chart6.getXAxis().setValueFormatter(new IndexAxisValueFormatter(increaseDate()));
                break;

            case R.id.check_day:
                check_hour.setChecked(false);
                check_week.setChecked(false);
                check_month.setChecked(false);
                period = "day";
                getPeriodFeature(facility,period);
                chart6.getXAxis().setValueFormatter(new IndexAxisValueFormatter(increaseDate()));

                break;

            case R.id.check_week:
                check_day.setChecked(false);
                check_hour.setChecked(false);
                check_month.setChecked(false);
                period = "week";
                getPeriodFeature(facility,period);
                chart6.getXAxis().setValueFormatter(new IndexAxisValueFormatter(increaseDate()));

                break;

            case R.id.check_month:
                check_day.setChecked(false);
                check_week.setChecked(false);
                check_hour.setChecked(false);
                period = "month";

                getPeriodFeature(facility,period);
                chart6.getXAxis().setValueFormatter(new IndexAxisValueFormatter(increaseDate()));

                break;



        }

    }

}
