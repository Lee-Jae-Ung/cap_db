package com.example.capstone.Activitiy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.DB.SDbOpenHelper;
import com.example.capstone.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SetSection extends AppCompatActivity implements View.OnClickListener {
    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    Button btn_delete;
    EditText edit_ssection;
    EditText edit_slocation;
    EditText edit_sIp;
    TextView text_ssection;
    TextView text_slocation;
    TextView text_sIp;

    CheckBox check_Section;
    CheckBox check_Location;
    CheckBox check_Ip;

    long nowIndex;
    String Section;
    String Location;
    String Ip;
    String sort = "Section";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList();
    static ArrayList<String> arrayData = new ArrayList();
    private SDbOpenHelper mSDbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsection);

        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        btn_Select = (Button) findViewById(R.id.btn_select);
        btn_Select.setOnClickListener(this);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        edit_ssection = (EditText) findViewById(R.id.edit_ssection);
        edit_slocation = (EditText) findViewById(R.id.edit_slocation);
        edit_sIp = (EditText) findViewById(R.id.edit_sIp);

        text_ssection = (TextView)findViewById(R.id.text_ssection);
        text_slocation = (TextView) findViewById(R.id.text_slocation);
        text_sIp = (TextView) findViewById(R.id.text_sIp);

        check_Section = (CheckBox) findViewById(R.id.check_ssection);
        check_Section.setOnClickListener(this);
        check_Location = (CheckBox) findViewById(R.id.check_slocation);
        check_Location.setOnClickListener(this);
        check_Ip = (CheckBox) findViewById(R.id.check_sip);
        check_Ip.setOnClickListener(this);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        mSDbOpenHelper = new SDbOpenHelper(this);
        mSDbOpenHelper.open();
        mSDbOpenHelper.create();

        showDatabase(sort);


        btn_Insert.setEnabled(true);
        btn_Insert.setBackground(getDrawable(R.drawable.button_unpressed));
        btn_Insert.setTextColor(ColorTemplate.rgb("#000000"));
        btn_Update.setEnabled(false);
        btn_Update.setBackground(getDrawable(R.drawable.button_pressed));
        btn_Update.setTextColor(ColorTemplate.rgb("#BDBDBD"));
        btn_delete.setEnabled(false);
        btn_delete.setBackground(getDrawable(R.drawable.button_pressed));
        btn_delete.setTextColor(ColorTemplate.rgb("#BDBDBD"));
    }

    public void setInsertMode(){
        edit_ssection.setText("");
        edit_slocation.setText("");
        edit_sIp.setText("");
        btn_Insert.setEnabled(true);
        btn_Insert.setBackground(getDrawable(R.drawable.button_unpressed));
        btn_Insert.setTextColor(ColorTemplate.rgb("#000000"));
        btn_Update.setEnabled(false);
        btn_Update.setBackground(getDrawable(R.drawable.button_pressed));
        btn_Update.setTextColor(ColorTemplate.rgb("#BDBDBD"));
        btn_delete.setEnabled(false);
        btn_delete.setBackground(getDrawable(R.drawable.button_pressed));
        btn_delete.setTextColor(ColorTemplate.rgb("#BDBDBD"));
    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            edit_ssection.setText(tempData[0].trim());
            edit_slocation.setText(tempData[1].trim());
            edit_sIp.setText(tempData[2].trim());

            btn_Insert.setEnabled(false);
            btn_Insert.setBackground(getDrawable(R.drawable.button_pressed));
            btn_Insert.setTextColor(ColorTemplate.rgb("#BDBDBD"));
            btn_Update.setEnabled(true);
            btn_Update.setBackground(getDrawable(R.drawable.button_unpressed));
            btn_Update.setTextColor(ColorTemplate.rgb("#000000"));
            btn_delete.setEnabled(true);
            btn_delete.setBackground(getDrawable(R.drawable.button_unpressed));
            btn_delete.setTextColor(ColorTemplate.rgb("#000000"));
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            String[] nowData = arrayData.get(position).split("\\s+");
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2];
            AlertDialog.Builder dialog = new AlertDialog.Builder(SetSection.this);
            dialog.setTitle("????????? ??????")
                    .setMessage("?????? ???????????? ?????? ???????????????????" + "\n" + viewData)
                    .setPositiveButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetSection.this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            mSDbOpenHelper.deleteColumn(nowIndex);
                            showDatabase(sort);
                            setInsertMode();
                        }
                    })
                    .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetSection.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public void showDatabase(String sort){
        Cursor iCursor = mSDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            @SuppressLint("Range") String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            @SuppressLint("Range") String tempSection = iCursor.getString(iCursor.getColumnIndex("section"));
            tempSection = setTextLength(tempSection,18);
            @SuppressLint("Range") String tempLocation = iCursor.getString(iCursor.getColumnIndex("location"));
            tempLocation = setTextLength(tempLocation,22);
            @SuppressLint("Range") String tempIp = iCursor.getString(iCursor.getColumnIndex("ip"));
            tempIp = setTextLength(tempIp,20);


            String Result = tempSection + tempLocation + tempIp;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                Section = edit_ssection.getText().toString();
                Location = edit_slocation.getText().toString();
                Ip = edit_sIp.getText().toString();

                if(Section.equals("")||Location.equals("")||Ip.equals("")) {
                    Toast.makeText(SetSection.this,"?????? ???????????????",Toast.LENGTH_SHORT);
                    Log.v("insert","??????");
                }
                else {


                    mSDbOpenHelper.open();
                    mSDbOpenHelper.insertColumn(Section, Location, Ip);
                    showDatabase(sort);
                    setInsertMode();
                    Log.v("dbtest",""+ mSDbOpenHelper);
                    edit_ssection.requestFocus();
                    edit_ssection.setCursorVisible(true);
                    }


                break;

            case R.id.btn_update:
                Section = edit_ssection.getText().toString();
                Location = edit_slocation.getText().toString();
                Ip = edit_sIp.getText().toString();



                mSDbOpenHelper.updateColumn(nowIndex,Section, Location, Ip);
                showDatabase(sort);
                setInsertMode();
                edit_ssection.requestFocus();
                edit_ssection.setCursorVisible(true);


                break;

            case R.id.btn_delete:
                Section = edit_ssection.getText().toString();
                Location = edit_slocation.getText().toString();
                Ip = edit_sIp.getText().toString();
                mSDbOpenHelper.deleteColumn(nowIndex);
                showDatabase(sort);
                setInsertMode();
                edit_ssection.requestFocus();
                edit_ssection.setCursorVisible(true);
                break;

            case R.id.btn_select:
                showDatabase(sort);
                break;

            case R.id.check_ssection:
                check_Location.setChecked(false);
                check_Ip.setChecked(false);
                sort = "section";
                break;



            case R.id.check_slocation:
                check_Section.setChecked(false);
                check_Ip.setChecked(false);
                sort = "location";
                break;

            case R.id.check_sip:
                check_Section.setChecked(false);
                check_Location.setChecked(false);
                sort = "ip";
                break;
        }

    }


}
