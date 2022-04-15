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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public TextView cur_y;
    public TextView selec_point;


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
        label_group.add(label1);
        label_group.add(label2);
        label_group.add(label3);
        label_group.add(label4);

        vibdate = (TextView) findViewById(R.id.vibdate);
        chart6 = (LineChart) findViewById(R.id.chart6);
        //chart7 = (LineChart) findViewById(R.id.chart7);
        //chart8 = (LineChart) findViewById(R.id.chart8);
        cur_x = (TextView) findViewById(R.id.current_x);
        cur_y = (TextView) findViewById(R.id.current_y);
        selec_point = (TextView) findViewById(R.id.selected_point);

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
                Log.v("entry",""+e.toString()+h.toString());
                Log.v("entry",""+xAxis_allinfo.get((int)e.getX()));
                //Toast myToast = Toast.makeText(getApplicationContext(),"X : "+e.getX()+ "Y : "+e.getY(), Toast.LENGTH_SHORT);
                //myToast.show();
                //Log.v("valuese",""+xAxis_allinfo.size());


                String[] temp = xAxis_allinfo.get((int)e.getX()).split(" ");
                cur_x.setText(temp[1]);
                cur_y.setText(""+e.getY());

                vibdate.setText(temp[0]);
                selec_point.setText(label_group.get(h.getDataSetIndex()));



            }

            @Override
            public void onNothingSelected() {

            }
        });


        chart6.setDrawGridBackground(false);
        chart6.setBackgroundColor(getResources().getColor(R.color.white));


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
        chart6.getXAxis().setDrawAxisLine(false);
        chart6.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart6.getXAxis().setEnabled(true);
        chart6.getXAxis().setTextSize(10f);
        chart6.getXAxis().setLabelCount(7);
        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter().getFormattedValue((float)1.0));


//Legend
        Legend l = chart6.getLegend();
        l.setEnabled(true);
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
        leftAxis.setAxisMinimum((float)-3.0);


        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.black));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawTopYLabelEntry(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGridColor(getResources().getColor(R.color.white));




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

        leftAxis.setDrawGridLinesBehindData(true);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);


        YAxis rightAxis = chart6.getAxisRight();
        rightAxis.setEnabled(false);
        chart6.invalidate();
// don't forget to refresh the drawing



/*
        //차트2
        chart7.setDrawGridBackground(true);
        chart7.setBackgroundColor(getResources().getColor(R.color.white));
        //chart4.setGridBackgroundColor(R.color.black);

// description text
        chart7.getDescription().setEnabled(true);
        Description des1 = chart7.getDescription();
        des1.setEnabled(true);
        des1.setText("ECU_RMS");
        des1.setTextSize(15f);
        des1.setTextColor(R.color.black);

// touch gestures (false-비활성화)
        chart7.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart7.setDragEnabled(false);
        chart7.setScaleEnabled(false);

//auto scale
        chart7.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart7.setPinchZoom(false);

//X축
        chart7.getXAxis().setDrawGridLines(true);
        chart7.getXAxis().setDrawAxisLine(false);
        chart7.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart7.getXAxis().setEnabled(true);
        chart7.getXAxis().setDrawGridLines(false);

//Legend
        Legend ll = chart7.getLegend();
        ll.setEnabled(true);
        ll.setFormSize(10f); // set the size of the legend forms/shapes
        ll.setTextSize(12f);
        ll.setTextColor(R.color.black);

//Y축
        YAxis leftAxis1 = chart7.getAxisLeft();
        leftAxis1.setEnabled(true);
        leftAxis1.setTextColor(getResources().getColor(R.color.black));
        leftAxis1.setDrawGridLines(true);
        leftAxis1.setGridColor(getResources().getColor(R.color.white));

        YAxis rightAxis1 = chart7.getAxisRight();
        rightAxis1.setEnabled(false);


// don't forget to refresh the drawing
        chart7.invalidate();

        //차트2
        chart8.setDrawGridBackground(true);
        chart8.setBackgroundColor(getResources().getColor(R.color.white));
        //chart4.setGridBackgroundColor(R.color.black);

// description text
        chart8.getDescription().setEnabled(true);
        Description des2 = chart8.getDescription();
        des2.setEnabled(true);
        des2.setText("ISO_RMS_speed");
        des2.setTextSize(15f);
        des2.setTextColor(R.color.black);

// touch gestures (false-비활성화)
        chart8.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart8.setDragEnabled(false);
        chart8.setScaleEnabled(false);

//auto scale
        chart8.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart8.setPinchZoom(false);

//X축
        chart8.getXAxis().setDrawGridLines(true);
        chart8.getXAxis().setDrawAxisLine(false);
        chart8.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart8.getXAxis().setEnabled(true);
        chart8.getXAxis().setDrawGridLines(false);

//Legend
        Legend lll = chart8.getLegend();
        lll.setEnabled(true);
        lll.setFormSize(10f); // set the size of the legend forms/shapes
        lll.setTextSize(12f);
        lll.setTextColor(R.color.black);

//Y축
        YAxis leftAxis2 = chart8.getAxisLeft();
        leftAxis2.setEnabled(true);
        leftAxis2.setTextColor(getResources().getColor(R.color.black));
        leftAxis2.setDrawGridLines(true);
        leftAxis2.setGridColor(getResources().getColor(R.color.white));

        YAxis rightAxis2 = chart8.getAxisRight();
        rightAxis2.setEnabled(false);


// don't forget to refresh the drawing
        chart8.invalidate();

*/
        //thread2 = new Thread(new getFeature(ip,device));
        //thread2.start();






/*
        HandlerThread handlerThread = new HandlerThread("VIB_FEATURE");

        handlerThread.start();

        Handler statusHandler = new Handler(handlerThread.getLooper());

        statusHandler.postDelayed(new com.example.capstone.DrawVIBFeature.getFeature(ip,facility), 10);
*/


    }

    public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            Log.v("xaxis",""+value);
            // Convert float value to date string
            // Convert from seconds back to milliseconds to format time  to show to the user
            //SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm:ss");
            //value = ((value) / 10) + (date_sum-4);  //+분
            //value = value*5;




            cal.add(Calendar.MINUTE,10);
            Log.v("calendar","int function : "+cal.get(Calendar.YEAR)+"년"
                    +cal.get(Calendar.MONTH)+"월"
                    +(cal.get(Calendar.DAY_OF_MONTH)+1)+"일"
                    +cal.get(Calendar.HOUR)+"시"
                    +cal.get(Calendar.MINUTE)+"분"
                    +cal.get(Calendar.SECOND)+"초");
            //long valueToMinutes = TimeUnit.MINUTES.toMillis((long)value);
            //Date timeMinutes = new Date(valueToMinutes);
            SimpleDateFormat formatMinutes = new SimpleDateFormat("HH:mm:ss");
            // Show time in local version
            //long millis = TimeUnit.SECONDS.toMillis((long) value);
            return formatMinutes.format(cal.getTime());
        }
    }


    @Override
    public void onBackPressed(){
        run = false;
        //thread2.interrupt();
        super.onBackPressed();

    }
    //getFeature thread
/*
    public class getFeature implements Runnable {
        //private final AtomicBoolean condition = new AtomicBoolean(false);

        private String ip;
        private String facility;


        public getFeature(String ip, String facility){
            this.ip = ip;
            this.facility = facility;
        }

        String[][] resultText1 = null;


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {

            Log.v("process1","feature진입");


            //Log.v("thread","condition : "+condition);
            while(run) {

                try {
                    //Log.v("ipipip", "feature : " + ip + " " + facility);


                    //resultText1 = new Task().execute("http://" + ip + ":50010/manage/Device/" + device).get();
                    resultText1 = new Task().execute("http://203.250.77.240:50010/manage/Device/VIB"+facility).get();


                    //Thread.sleep(100000);

                    //Log.v("abcd","resultText1 : "+resultText1[0]);
                    //Log.v("abcd","resultText2 : "+resultText1[1]);
                    //Log.v("abcd","resultText3 : "+resultText1[2]);
                    //Log.v("abcd","resultText4 : "+resultText1[3]);


                    //Thread.sleep(100000);

                    JSONObject jsonObject = new JSONObject(resultText1[0][0]);
                    hrms = Double.parseDouble(jsonObject.getString("RMS1"));
                    erms = Double.parseDouble(jsonObject.getString("RMS2"));
                    irms = Double.parseDouble(jsonObject.getString("RMS3"));
                    rms = Double.parseDouble(jsonObject.getString("RMS4"));
                    date = jsonObject.getString("DATE");
                    Log.v("date",date);
// 포맷터
                    DateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// 문자열 -> Date
                    date1 = formatter1.parse(date);



                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.v("zzz",""+date1);
                //Ch1 = resultText.replace("Ch1", "").split(",");
                //double[] nums;
                //nums = Arrays.stream(Ch1).mapToDouble(Double::parseDouble).toArray();

                SimpleDateFormat mFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                        vibdate.setText(mFormat2.format(date1));

                        addEntry(hrms, erms, irms, rms, chart6, "HIGH_RMS");
                        //addEntry(erms,chart7,"ECU_RMS");
                        //addEntry(irms,chart8,"ISO_RMS_speed");

                    }
                });
                try {
                    Thread.sleep(300000);
                    //Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

 */
    //addEntry thread
/*
    private void addEntry(double num, double num2, double num3, double num4, LineChart chart, String label) {


        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well
        ILineDataSet set2 = data.getDataSetByIndex(1);
        ILineDataSet set3 = data.getDataSetByIndex(2);
        ILineDataSet set4 = data.getDataSetByIndex(3);


        if (set == null) {
            set = createSet(label);
            data.addDataSet(set);
        }
        if (set2 == null) {
            set2 = createSet2(label);
            data.addDataSet(set2);
        }
        if (set3 == null) {
            set3 = createSet3(label);
            data.addDataSet(set3);
        }
        if (set4 == null) {
            set4 = createSet4(label);
            data.addDataSet(set4);
        }

        data.addEntry(new Entry((float)i, (float)num), 0);
        data.addEntry(new Entry((float)i, (float)num2), 1);
        data.addEntry(new Entry((float)i, (float)num3), 2);
        data.addEntry(new Entry((float)i, (float)num4), 3);

        //data.addEntry(new Entry((float)i, (float)2), 4);
        //data.addEntry(new Entry((float)i, (float)6.3), 5);

        i++;

        //data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(60);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }
*/

/*
    public class getFeatureInit implements Runnable {
        //private final AtomicBoolean condition = new AtomicBoolean(false);

        private String ip;
        private String facility;
        private String period;


        public getFeatureInit(String ip, String facility, String period) {
            this.ip = ip;
            this.facility = facility;

            this.period = period;

        }

        String resultText1[][];


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            Log.v("process1", "initfeature진입");


            //Log.v("thread","condition : "+condition);


            try {

                resultText1 = new Task().execute("http://203.250.77.240:50010/manage/Device/SDG" + facility + ".csv/" + period).get();
                //Thread.sleep(100000);

                //Log.v("process1", "resultText1" + resultText1[0][279]);

                //rms1_sarr = resultText1.replace("rms1","").split(",");


                rms1_darr = Arrays.stream(resultText1[0]).mapToDouble(Double::parseDouble).toArray();
                rms2_darr = Arrays.stream(resultText1[1]).mapToDouble(Double::parseDouble).toArray();
                rms3_darr = Arrays.stream(resultText1[2]).mapToDouble(Double::parseDouble).toArray();
                rms4_darr = Arrays.stream(resultText1[3]).mapToDouble(Double::parseDouble).toArray();




            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String[] temp_date;

            temp_date = resultText1[4][0].split(" ")[1].split(":");

            //Log.v("datetest",""+resultText1[4][0].split(" ")[1].split(":")[0]);
            //Log.v("datetest",""+resultText1[4][0].split(" ")[1].split(":")[1]);
            //Log.v("datetest",""+resultText1[4][0].split(" ")[1].split(":")[2]);

            date_sum = (float) (Integer.parseInt(temp_date[0])) + (float) (Integer.parseInt(temp_date[1]) / 60.0) + (float) (Integer.parseInt(temp_date[2]) / 3600.0);


            chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());


        }

    }
*/




    private LineDataSet createSet(String label) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.blue));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(getResources().getColor(R.color.blue));
        set.setHighlightLineWidth(1f);

        return set;
    }

    private LineDataSet createSet2(String label) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.green));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(getResources().getColor(R.color.green));
        set.setHighlightLineWidth(1f);

        return set;
    }
    private LineDataSet createSet3(String label) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.yellow));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(getResources().getColor(R.color.yellow));
        set.setHighlightLineWidth(1f);

        return set;
    }
    private LineDataSet createSet4(String label) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.red));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(getResources().getColor(R.color.red));
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

            buff_size = resultText1.get(0).size();
            rms1_darr = Arrays.stream(resultText1.get(0).toArray(new String[resultText1.get(0).size()])).mapToDouble(Double::parseDouble).toArray();
            rms2_darr = Arrays.stream(resultText1.get(1).toArray(new String[resultText1.get(1).size()])).mapToDouble(Double::parseDouble).toArray();
            rms3_darr = Arrays.stream(resultText1.get(2).toArray(new String[resultText1.get(2).size()])).mapToDouble(Double::parseDouble).toArray();
            rms4_darr = Arrays.stream(resultText1.get(3).toArray(new String[resultText1.get(3).size()])).mapToDouble(Double::parseDouble).toArray();



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //temp_date = resultText1[4][0].split(" ")[1].split(":");
        all_date = resultText1.get(4).get(0);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date abc= new Date();
// 문자열 -> Date
        try {
            abc = fmt.parse(all_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        cal.setTime(abc);
        //cal.add(Calendar.HOUR,-1);
        //cal.add(Calendar.YEAR, 1); //1년 더하기
        //cal.add(Calendar.MONTH, -3);

        Log.v("calendar","bef"+cal.get(Calendar.YEAR)+"년"
                +(cal.get(Calendar.MONTH)+1)+"월"
                +(cal.get(Calendar.DAY_OF_MONTH))+"일"
                +cal.get(Calendar.HOUR)+"시"
                +cal.get(Calendar.MINUTE)+"분"
                +cal.get(Calendar.SECOND)+"초");


        //시간쪼개기
        temp_time = all_date.split(" ")[1].split(":");
        //날짜 쪼개기
        temp_date = all_date.split(" ")[0].split("-");



        //분 기준
        time_sum = (float) (Integer.parseInt(temp_time[0])/60.0) + (float) (Integer.parseInt(temp_time[1])) + (float) (Integer.parseInt(temp_time[2])*60);
        //일 기준
        date_sum = (float) (Integer.parseInt(temp_date[1])) + (float) (Integer.parseInt(temp_date[2])*60);

        data = new LineData();
        chart6.setData(data);

        set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well
        set2 = data.getDataSetByIndex(1);
        set3 = data.getDataSetByIndex(2);
        set4 = data.getDataSetByIndex(3);


        if (set == null) {
            set = createSet(label1);
            data.addDataSet(set);
        }
        if (set2 == null) {
            set2 = createSet2(label2);
            data.addDataSet(set2);
        }
        if (set3 == null) {
            set3 = createSet3(label3);
            data.addDataSet(set3);
        }
        if (set4 == null) {
            set4 = createSet4(label4);
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

        //chart6.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
        chart6.invalidate();
    }

    public ArrayList<String> increaseDate(){
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
        return xAxis;
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
