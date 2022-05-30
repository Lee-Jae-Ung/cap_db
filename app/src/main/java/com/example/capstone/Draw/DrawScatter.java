package com.example.capstone.Draw;

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

import com.example.capstone.Connection.Task;
import com.example.capstone.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DrawScatter extends AppCompatActivity {

    public static Context context_main;

    public ScatterChart chart;



    int i=0;

    double avg = 0.0;
    double rms = 0.0;
    int predict = 0;


    String time="";

    boolean run = true;
    String ip;
    String point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter);

        context_main = this;



        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        point = intent.getStringExtra("point");

        Log.v("dfeature","ip : "+ip+"device : "+point);


        chart = (ScatterChart) findViewById(R.id.scatterChart);


        //차트1
        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(getResources().getColor(R.color.white));

// description text
        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText("AMPLITUDE");
        des.setTextSize(15f);
        des.setTextColor(R.color.black);

// touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

//auto scale
        chart.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);


//Legend
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(R.color.black);

//Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.black));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.white));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


// don't forget to refresh the drawing
        chart.invalidate();

        HandlerThread handlerThread = new HandlerThread("AE_FEATURE");

        handlerThread.start();

        Handler statusHandler = new Handler(handlerThread.getLooper());

        statusHandler.postDelayed(new getFeature(ip,point), 10);

    }
    @Override
    public void onBackPressed(){
        run = false;
        super.onBackPressed();

    }

    private class getFeature implements Runnable {

        private String ip;
        private String point;

        public getFeature(String ip, String point){
            this.ip = ip;
            this.point = point;
        }

        ArrayList<ArrayList<String>> resultText = new ArrayList<>();
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {



            while(run) {

                try {
                    Log.v("ipipip","feature : "+ip + "device : "+point);

                    Thread.sleep(1000);
                    //resultText = new Task().execute("http://"+ip+":50010/manage/Device/daqtest").get();
                    resultText = new Task().execute("http://cb0a-203-250-77-240.ngrok.io/manage/Device/Test"+point).get();

                    JSONObject jsonObject = new JSONObject(resultText.get(0).get(0));
                    avg = Double.parseDouble(jsonObject.getString("AVG"));
                    rms = Double.parseDouble(jsonObject.getString("RMS"));
                    //predict = Integer.parseInt(jsonObject.getString("PREDICT"));
                    //time = jsonObject.getString("DATE");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.v("receiveMsg",""+amplitude+" "+rms);

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
                Log.v("ipipip","avg : "+avg);
                Log.v("ipipip","rms : "+rms);
                Log.v("ipipip","predict : "+predict);
                Log.v("ipipip","time : "+time);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        addEntry(avg,rms,0,chart,"RMS");

                    }
                });

            }
        }

    }

    private void addEntry(double num, double num2, int predict, ScatterChart chart, String label) {

        ScatterData data = chart.getData();

        if (data == null) {
            data = new ScatterData();
            chart.setData(data);
        }

        IScatterDataSet set = data.getDataSetByIndex(0);
        IScatterDataSet set2 = data.getDataSetByIndex(1);



        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet("AVG",R.color.red);
            data.addDataSet(set);
        }
        if (set2 == null) {
            set2 = createSet("RMS",R.color.blue);
            data.addDataSet(set2);
        }


        data.addEntry(new Entry((float)i, (float)num), 0);
        data.addEntry(new Entry((float)i, (float)num2), 1);
        i++;



        //data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        //chart.setVisibleXRangeMaximum(60);
        // this automatically refreshes the chart (calls invalidate())
        //chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);
        chart.invalidate();

    }



    private ScatterDataSet createSet(String label, int hcolor) {

        ScatterDataSet set = new ScatterDataSet(null, label);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(hcolor));
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }

}

