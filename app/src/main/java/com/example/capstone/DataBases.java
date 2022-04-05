package com.example.capstone;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateSDB implements BaseColumns{
        public static final String SECTION = "section";
        public static final String LOCATION = "location";
        public static final String IP = "ip";
        public static final String _TABLENAME0 = "section_list";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +SECTION+" text not null , "
                +LOCATION+" text not null , "
                +IP+" text not null ); ";

    }

    public static final class CreateDDB implements BaseColumns{
        public static final String SECTION = "section";
        public static final String POINTID = "pointid";
        public static final String POINTNUM = "pointnum";
        public static final String DEVICENAME = "devicename";
        public static final String _TABLENAME0 = "device_list";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +SECTION+" text not null , "
                +POINTID+" text not null , "
                +POINTNUM+" text not null , "
                +DEVICENAME+" text not null); ";

    }
}
