package com.example.capstone.Connection;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Task extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    private String str, abc;


    private ArrayList<ArrayList<String>> result123 = new ArrayList<>();
    private ArrayList<String> rms1 = new ArrayList<>();
    private ArrayList<String> rms2 = new ArrayList<>();
    private ArrayList<String> rms3 = new ArrayList<>();
    private ArrayList<String> rms4 = new ArrayList<>();
    private ArrayList<String> calendar = new ArrayList<>();

    private ArrayList<String> file_info = new ArrayList<>();
    private ArrayList<String> json1 = new ArrayList<>();

    String info;

    HttpURLConnection conn;

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... strings) {
        URL url = null;


        try{
            Log.v("abcd","task진입");
            url = new URL(strings[0]);

            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);

            if(conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                Log.v("abcd","tmp : "+tmp);

                BufferedReader reader = new BufferedReader(tmp);
                Log.v("abcd","reader : "+reader);

                StringBuffer buffer = new StringBuffer();


                String[] temp;
                String[] temp2;



                info = reader.readLine();
                temp2 = info.split(",");

                abc = temp2[0];
                file_info.add(temp2[1]);
                Log.v("abcd","abc : "+info);
                if(abc.equals("csv")) {
                    Log.v("abcd","csv");
                    reader.readLine();
                    while ((str = reader.readLine()) != null) {
                        temp = str.split(",");


                        calendar.add(temp[0]);
                        rms1.add(temp[1]);
                        rms2.add(temp[2]);
                        rms3.add(temp[3]);
                        rms4.add(temp[4]);


                    }
                    result123.add(calendar);
                    result123.add(rms1);
                    result123.add(rms2);
                    result123.add(rms3);
                    result123.add(rms4);
                    result123.add(file_info);


                }
                else{
                    Log.v("abcd","json");

                    buffer.append(info);

                    json1.add(buffer.toString());
                    result123.add(json1);
                }

                conn.disconnect();
                tmp.close();
                reader.close();

            }
            else{
                Log.i("abcd","else 비정상임 타임아웃 Error");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return result123;
        } catch (IOException e){
            e.printStackTrace();

            return result123;
        }

        return result123;
    }




}
