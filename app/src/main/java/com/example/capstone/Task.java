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
import java.util.ArrayList;

public class Task extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    private String str, receiveMsg, abc;
    //private int buffsize=288;
    //private String[][] result123 = new String[5][buffsize];

    private ArrayList<ArrayList<String>> result123 = new ArrayList<>();
    private ArrayList<String> rms1 = new ArrayList<>();
    private ArrayList<String> rms2 = new ArrayList<>();
    private ArrayList<String> rms3 = new ArrayList<>();
    private ArrayList<String> rms4 = new ArrayList<>();
    private ArrayList<String> file_info = new ArrayList<>();
    private ArrayList<String> json1 = new ArrayList<>();

    int rescode;

    Handler handler = new Handler();
    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... strings) {
        URL url = null;


        try{
            url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            rescode = conn.getResponseCode();

            Log.v("abcd","conn : "+conn);


            if(conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                Log.v("abcd","tmp : "+tmp);

                BufferedReader reader = new BufferedReader(tmp);
                Log.v("abcd","reader : "+reader);

                StringBuffer buffer = new StringBuffer();


                String[] temp;
                String[] temp2;



                int i=0;
                temp2 = reader.readLine().split(",");
                //abc = temp2[0];
                //result123[4][0] = temp2[1];
               // result123[4][1] = temp2[2];
                abc = temp2[0];
                file_info.add(temp2[1]);
                file_info.add(temp2[2]);
                Log.v("abcd","abc : "+abc);
                if(abc.equals("csv")) {
                    Log.v("abcd","csv");
                    reader.readLine();
                    while ((str = reader.readLine()) != null) {
                        //Log.v("abcd","str : "+str);
                        temp = str.split(",");
/*
                        result123[0][i] = temp[0];
                        result123[1][i] = temp[1];
                        result123[2][i] = temp[2];
                        result123[3][i] = temp[3];
                        i++;
*/
                        rms1.add(temp[0]);
                        rms2.add(temp[1]);
                        rms3.add(temp[2]);
                        rms4.add(temp[3]);


                        //buffer.append(str);
                    }

                    result123.add(rms1);
                    result123.add(rms2);
                    result123.add(rms3);
                    result123.add(rms4);
                    result123.add(file_info);


                }
                else{
                    Log.v("abcd","json");

                    buffer.append(abc);

                    //result123[0][0] = buffer.toString();
                    json1.add(buffer.toString());
                    result123.add(json1);
                }
                //Log.v("abcd","result123-0 : "+result123[0][4]);
                //Log.v("abcd","result123-1 : "+result123[1][4]);
                //Log.v("abcd","result123-2 : "+result123[2][4]);
                //Log.v("abcd","result123-3 : "+result123[3][4]);

                /*
                while ((str = reader.readLine()) != null) {

                        buffer.append(str);
                    }
                 */



                //Log.v("abcd","bf : "+buffer);


                //Log.v("abcd","str : "+buffer.length());

                //Log.i("receiveMsg : ","TASK : "+receiveMsg);
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

        return result123;
    }



}
