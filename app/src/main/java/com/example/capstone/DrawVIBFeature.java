package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DrawVIBFeature extends AppCompatActivity {

    public static Context context_main;

    public LineChart chart6;
    public LineChart chart7;
    public LineChart chart8;


    int i=0;

    double amplitude = 0.0;
    double rms = 0.0;
    double hrms = 0.0;
    double erms = 0.0;
    double irms = 0.0;

    boolean run = true;
    String ip;
    String device;
    public static Thread thread2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibchart);

        context_main = this;



        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        device = intent.getStringExtra("device");

        Log.v("dfeature","ip : "+ip+"device : "+device);


        chart6 = (LineChart) findViewById(R.id.chart6);
        chart7 = (LineChart) findViewById(R.id.chart7);
        chart8 = (LineChart) findViewById(R.id.chart8);


        //차트1
        chart6.setDrawGridBackground(true);
        chart6.setBackgroundColor(getResources().getColor(R.color.white));
        //chart.setGridBackgroundColor(R.color.black);

// description text
        chart6.getDescription().setEnabled(true);
        Description des = chart6.getDescription();
        des.setEnabled(true);
        des.setText("HIGH_RMS");
        des.setTextSize(15f);
        des.setTextColor(R.color.black);

// touch gestures (false-비활성화)
        chart6.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart6.setDragEnabled(false);
        chart6.setScaleEnabled(false);

//auto scale
        chart6.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart6.setPinchZoom(false);

//X축
        chart6.getXAxis().setDrawGridLines(true);
        chart6.getXAxis().setDrawAxisLine(false);
        chart6.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart6.getXAxis().setEnabled(true);
        chart6.getXAxis().setDrawGridLines(false);


//Legend
        Legend l = chart6.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(R.color.black);

//Y축
        YAxis leftAxis = chart6.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.black));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.white));

        YAxis rightAxis = chart6.getAxisRight();
        rightAxis.setEnabled(false);


// don't forget to refresh the drawing
        chart6.invalidate();


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


        //thread2 = new Thread(new getFeature(ip,device));
        //thread2.start();

        HandlerThread handlerThread = new HandlerThread("VIB_FEATURE");

        handlerThread.start();

        Handler statusHandler = new Handler(handlerThread.getLooper());

        statusHandler.postDelayed(new com.example.capstone.DrawVIBFeature.getFeature(ip,device), 10);

    }
    @Override
    public void onBackPressed(){
        run = false;
        //thread2.interrupt();
        super.onBackPressed();

    }

    private class getFeature implements Runnable {
        //private final AtomicBoolean condition = new AtomicBoolean(false);

        private String ip;
        private String device;

        public getFeature(String ip, String device){
            this.ip = ip;
            this.device = device;
        }

        String resultText = "[NULL]";

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {



            //Log.v("thread","condition : "+condition);
            while(run) {

                try {
                    Log.v("ipipip","feature : "+ip);

                    Thread.sleep(1000);
                    resultText = new Task().execute("http://"+ip+":50010/manage/Device/"+device).get();
                    Log.v("resultText",""+resultText);
                    JSONObject jsonObject = new JSONObject(resultText);
                    hrms = Double.parseDouble(jsonObject.getString("HIGHRMS"));
                    erms = Double.parseDouble(jsonObject.getString("ECURMS"));
                    irms = Double.parseDouble(jsonObject.getString("ISORMS_speed"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("testdata1","zz");

/*
                if(peak>0.24){

                    NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //Notification 객체를 생성해주는 건축가객체 생성(AlertDialog 와 비슷)
                    NotificationCompat.Builder builder= null;

                    //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨.
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                        String channelID="channel_01"; //알림채널 식별자
                        String channelName="MyChannel01"; //알림채널의 이름(별명)

                        //알림채널 객체 만들기
                        NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

                        //알림매니저에게 채널 객체의 생성을 요청
                        notificationManager.createNotificationChannel(channel);

                        //알림건축가 객체 생성
                        builder=new NotificationCompat.Builder(DrawFeature.this, channelID);


                    }else{
                        //알림 건축가 객체 생성
                        builder= new NotificationCompat.Builder(DrawFeature.this, (Notification) null);
                    }


                    //건축가에게 원하는 알림의 설정작업
                    builder.setSmallIcon(android.R.drawable.ic_menu_view);

                    //상태바를 드래그하여 아래로 내리면 보이는
                    //알림창(확장 상태바)의 설정
                    builder.setContentTitle("*이상신호*");//알림창 제목
                    builder.setContentText("이상신호감지");//알림창 내용
                    //알림창의 큰 이미지
                    //Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
                    //builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

                    //건축가에게 알림 객체 생성하도록
                    Notification notification=builder.build();

                    //알림매니저에게 알림(Notify) 요청
                    notificationManager.notify(1, notification);
                }
*/
                //Ch1 = resultText.replace("Ch1", "").split(",");
                //double[] nums;
                //nums = Arrays.stream(Ch1).mapToDouble(Double::parseDouble).toArray();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        addEntry(hrms,chart6,"HIGH_RMS");
                        addEntry(erms,chart7,"ECU_RMS");
                        addEntry(irms,chart8,"ISO_RMS_speed");

                    }
                });

            }
        }

    }

    private void addEntry(double num,LineChart chart,String label) {

        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet(label);
            data.addDataSet(set);
        }

        data.addEntry(new Entry((float)i++, (float)num), 0);


        //data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(60);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }



    private LineDataSet createSet(String label) {

        LineDataSet set = new LineDataSet(null, label);
        set.setLineWidth(1f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.green));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

}
