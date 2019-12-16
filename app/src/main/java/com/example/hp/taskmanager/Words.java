package com.example.hp.taskmanager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hp on 2019/11/29.
 */

public class Words {
    public static final String AUTHORITY = "com.example.hp.wordslist.wordsprovider";//URI授权者
    public Words() {
    }
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME="task";
        public static final String COLUMN_NAME_NAME="name";//列：任务名
        public static final String COLUMN_NAME_DATE1="date1";//列：起始时间
        public static final String COLUMN_NAME_DATE2="date2";//列：结束时间
        public static final String COLUMN_NAME_MATTER="matter";//列：任务内容
        public static final String COLUMN_NAME_OTHER="other";//备注
    }
}