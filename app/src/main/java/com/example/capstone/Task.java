package com.example.capstone;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, Void, String> {

    private String str, receiveMsg;
    int rescode;

    Handler handler = new Handler();
    @Override
    protected String doInBackground(String... strings) {
        URL url = null;


        try{
            url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            rescode = conn.getResponseCode();

            Log.v("receiveMsg",""+rescode);


            if(conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ","TASK : "+receiveMsg);
                conn.disconnect();
                tmp.close();
                reader.close();

            }
            else{
                //Log.i("receiveMsg",conn.getResponseCode() + "Error");
                Log.i("receiveMsg","비정상임 타임아웃 Error");
            }
        } catch (MalformedURLException e) {
            Log.i("receiveMsg","연결안됨");
            e.printStackTrace();
        } catch (IOException e){
            Log.i("receiveMsg","타임아웃 예외발생");
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("receiveMsg","타임아웃 핸들러 실행");

                }
            });
        }
        finally {
            Log.i("receiveMsg","무조건 실행");
        }

        return receiveMsg;
    }



}
