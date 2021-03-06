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

import com.example.capstone.DB.DDbOpenHelper;
import com.example.capstone.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SetDevice extends AppCompatActivity implements View.OnClickListener {
    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    Button btn_delete;
    EditText edit_dsection;
    EditText edit_dpointid;
    EditText edit_dpointnum;
    EditText edit_ddvname;
    TextView text_dsection;
    TextView text_dpointid;
    TextView text_dpointnum;
    TextView text_ddvname;

    CheckBox check_dSection;
    CheckBox check_dPointid;
    CheckBox check_dPointnum;
    CheckBox check_ddvname;

    long nowIndex;
    String Section;
    String Pointid;
    String Pointnum;
    String dvname;
    String sort = "Section";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList();
    static ArrayList<String> arrayData = new ArrayList();
    private DDbOpenHelper mDDbOpenHelper;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdevice);



        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        btn_Select = (Button) findViewById(R.id.btn_select);
        btn_Select.setOnClickListener(this);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        edit_dsection = (EditText) findViewById(R.id.edit_dsection);
        edit_dpointid = (EditText) findViewById(R.id.edit_dpointid);
        edit_dpointnum = (EditText) findViewById(R.id.edit_dpointnum);
        edit_ddvname = (EditText) findViewById(R.id.edit_ddvname);

        text_dsection = (TextView)findViewById(R.id.text_dsection);
        text_dpointid = (TextView)findViewById(R.id.text_dpointid);
        text_dpointnum = (TextView) findViewById(R.id.text_dpointnum);
        text_ddvname = (TextView) findViewById(R.id.text_ddvname);

        check_dSection = (CheckBox) findViewById(R.id.check_dsection);
        check_dSection.setOnClickListener(this);
        check_dPointid = (CheckBox) findViewById(R.id.check_dpointid);
        check_dPointid.setOnClickListener(this);
        check_dPointnum = (CheckBox) findViewById(R.id.check_dPointnum);
        check_dPointnum.setOnClickListener(this);
        check_ddvname = (CheckBox) findViewById(R.id.check_ddvname);
        check_ddvname.setOnClickListener(this);


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        mDDbOpenHelper = new DDbOpenHelper(this);
        mDDbOpenHelper.open();
        mDDbOpenHelper.create();

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

    @SuppressLint("ResourceAsColor")
    public void setInsertMode(){
        edit_dsection.setText("");
        edit_dpointid.setText("");
        edit_dpointnum.setText("");
        edit_ddvname.setText("");
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
        @SuppressLint("ResourceAsColor")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData.length);

            edit_dsection.setText(tempData[0].trim());
            edit_dpointid.setText(tempData[1].trim());
            edit_dpointnum.setText(tempData[2].trim());
            edit_ddvname.setText(tempData[3].trim());

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
            AlertDialog.Builder dialog = new AlertDialog.Builder(SetDevice.this);
            dialog.setTitle("????????? ??????")
                    .setMessage("?????? ???????????? ?????? ???????????????????" + "\n" + viewData)
                    .setPositiveButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetDevice.this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            mDDbOpenHelper.deleteColumn(nowIndex);
                            showDatabase(sort);
                            setInsertMode();
                        }
                    })
                    .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SetDevice.this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public void showDatabase(String sort){
        Cursor iCursor = mDDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            @SuppressLint("Range") String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            @SuppressLint("Range") String tempSection = iCursor.getString(iCursor.getColumnIndex("section"));
            tempSection = setTextLength(tempSection,15);
            @SuppressLint("Range") String tempPointid = iCursor.getString(iCursor.getColumnIndex("pointid"));
            tempPointid = setTextLength(tempPointid,15);
            @SuppressLint("Range") String tempPointnum = iCursor.getString(iCursor.getColumnIndex("pointnum"));
            tempPointnum = setTextLength(tempPointnum,20);
            @SuppressLint("Range") String tempDvname = iCursor.getString(iCursor.getColumnIndex("devicename"));
            tempDvname = setTextLength(tempDvname,10);



            String Result = tempSection + tempPointid + tempPointnum + tempDvname;
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
                Section = edit_dsection.getText().toString();
                Pointid = edit_dpointid.getText().toString();
                Pointnum = edit_dpointnum.getText().toString();
                dvname = edit_ddvname.getText().toString();

                if(Section.equals("")||Pointid.equals("")||Pointnum.equals("")||dvname.equals("")) {
                    Toast.makeText(SetDevice.this,"?????? ???????????????",Toast.LENGTH_SHORT);
                    Log.v("insert","??????");
                }
                else {
                    mDDbOpenHelper.open();
                    mDDbOpenHelper.insertColumn(Section, Pointid, Pointnum,dvname);
                    showDatabase(sort);
                    setInsertMode();
                    Log.v("dbtest",""+ mDDbOpenHelper);
                    edit_dsection.requestFocus();
                    edit_dsection.setCursorVisible(true);
                }


                break;

            case R.id.btn_update:
                Section = edit_dsection.getText().toString();
                Pointid = edit_dpointid.getText().toString();
                Pointnum = edit_dpointnum.getText().toString();
                dvname = edit_ddvname.getText().toString();



                mDDbOpenHelper.updateColumn(nowIndex,Section, Pointid, Pointnum,dvname);
                showDatabase(sort);
                setInsertMode();
                edit_dsection.requestFocus();
                edit_dsection.setCursorVisible(true);


                break;

            case R.id.btn_delete:
                Section = edit_dsection.getText().toString();
                Pointid = edit_dpointid.getText().toString();
                Pointnum = edit_dpointnum.getText().toString();
                dvname = edit_ddvname.getText().toString();

                mDDbOpenHelper.deleteColumn(nowIndex);
                showDatabase(sort);
                setInsertMode();
                edit_dsection.requestFocus();
                edit_dsection.setCursorVisible(true);
                break;

            case R.id.btn_select:
                showDatabase(sort);
                break;

            case R.id.check_dsection:
                check_dPointid.setChecked(false);
                check_dPointnum.setChecked(false);
                check_ddvname.setChecked(false);
                sort = "section";
                break;



            case R.id.check_dpointid:
                check_dSection.setChecked(false);
                check_dPointnum.setChecked(false);
                check_ddvname.setChecked(false);
                sort = "pointid";
                break;

            case R.id.check_dPointnum:
                check_dSection.setChecked(false);
                check_dPointid.setChecked(false);
                check_ddvname.setChecked(false);
                sort = "pointnum";
                break;

            case R.id.check_ddvname:
                check_dSection.setChecked(false);
                check_dPointid.setChecked(false);
                check_dPointnum.setChecked(false);
                sort = "pointnum";
                break;
        }

    }


}

