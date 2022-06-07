package com.example.capstone.Activitiy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.capstone.DB.DDbOpenHelper;
import com.example.capstone.DB.SDbOpenHelper;
import com.example.capstone.ListControl.ChildItem;
import com.example.capstone.ListControl.MainListBtn;
import com.example.capstone.ListControl.MainListBtnAdapter;
import com.example.capstone.ListControl.ParentItem;
import com.example.capstone.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;

    int count=0;
    SDbOpenHelper mSDbOpenHelper;
    DDbOpenHelper mDDbOpenHelper;
    ArrayList<MainListBtn> items = new ArrayList<MainListBtn>() ;


    SwipeRefreshLayout swipeRefreshLayout;

    ExpandableListView listView;
    MainListBtnAdapter adapter;
    ArrayList<ParentItem> groupList = new ArrayList<>(); //부모 리스트
    ArrayList<ArrayList<ChildItem>> childList = new ArrayList<>(); //자식 리스트
    ArrayList<ArrayList<ChildItem>> monthArray = new ArrayList<>(); //1월 ~ 12월을 관리하기 위한 리스트
    Toolbar topAppBar;


    //앱이 처음켜지면 실행되는 곳으로 최신화된 계층형 리스트들의 정보를 나타낸다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        //swipeRefreshLayout = findViewById(R.id.swipe);
        //swipeRefreshLayout.setOnRefreshListener(this);
        listView = (ExpandableListView) findViewById(R.id.expandable_list);

        topAppBar = (Toolbar) findViewById(R.id.toolbar_main);


        setSupportActionBar(topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSDbOpenHelper = new SDbOpenHelper(this);
        mSDbOpenHelper.open();
        mSDbOpenHelper.create();
        Cursor iCursor = mSDbOpenHelper.sortColumnDist();


        while (iCursor.moveToNext()) {

            monthArray.add(new ArrayList<>());
            //groupList.add(new ParentItem(tempSec + "구간", tempLoc,tempIp,childList.get(Integer.parseInt(tempSec)-1).size() + "개"));
            Log.v("arrrray",""+(count++));
        }




        //어댑터에 각각의 배열 등록
        adapter = new MainListBtnAdapter();
        adapter.parentItems = groupList;
        adapter.childItems = childList;


        listView.setAdapter(adapter);
        listView.setGroupIndicator(null); //리스트뷰 기본 아이콘 표시 여부
        setListItems();
        setChild();
        mSDbOpenHelper.close();

    }



    public void setChild(){


        for(int i=1;i<=count;i++) {
            mDDbOpenHelper = new DDbOpenHelper(this);
            mDDbOpenHelper.open();
            mDDbOpenHelper.create();
            Cursor iCursor = mDDbOpenHelper.selectDevice(Integer.toString(i));
            while (iCursor.moveToNext()) {

                @SuppressLint("Range") String tempPointnum = iCursor.getString(iCursor.getColumnIndex("pointnum"));
                @SuppressLint("Range") String tempPointid = iCursor.getString(iCursor.getColumnIndex("pointid"));

                ChildItem item = new ChildItem(tempPointnum,tempPointid);
                monthArray.get(i - 1).add(item);

            }
        }
        Log.v("arrrray","setlist 진입");
        //mDDbOpenHelper.close();
        count=0;
        setListItems();


    }

    public void setListItems() {
        groupList.clear();
        childList.clear();

        childList.addAll(monthArray);

        mSDbOpenHelper = new SDbOpenHelper(this);
        mSDbOpenHelper.open();
        mSDbOpenHelper.create();
        Cursor iCursor = mSDbOpenHelper.sortColumnDist();

        Log.v("arrrray","setlist 진입");
        while (iCursor.moveToNext()) {

            @SuppressLint("Range") String tempSec = iCursor.getString(iCursor.getColumnIndex("section"));
            @SuppressLint("Range") String tempLoc = iCursor.getString(iCursor.getColumnIndex("location"));
            @SuppressLint("Range") String tempIp = iCursor.getString(iCursor.getColumnIndex("ip"));


            groupList.add(new ParentItem(tempSec, tempLoc,tempIp,childList.get(Integer.parseInt(tempSec)-1).size() + "개"));

            Log.v("arrrray",tempSec);
        }
        mSDbOpenHelper.close();

        adapter.notifyDataSetChanged();
    }




    public void updateLayoutView(){
        Intent intent = getIntent();
        finish(); //현재 액티비티 종료 실시
        overridePendingTransition(0, 0); //인텐트 애니메이션 없애기
        startActivity(intent); //현재 액티비티 재실행 실시
        overridePendingTransition(0, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)    {
        //getMenuInflater().inflate(R.menu.toolbar, menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_refresh:
                Log.v("menuzz","menu selected");
                updateLayoutView();
                return true;
            case R.id.search_section:
                Intent intent4 = new Intent(getApplicationContext(), SetSection.class);
                startActivity(intent4);
                return true;
            case R.id.search_device:
                Intent intent5 = new Intent(getApplicationContext(), SetDevice.class);
                startActivity(intent5);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}