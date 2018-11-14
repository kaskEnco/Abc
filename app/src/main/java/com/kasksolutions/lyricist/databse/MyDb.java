package com.kasksolutions.lyricist.databse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by home on 2/13/2018.
 */

public class MyDb extends SQLiteOpenHelper
{
    Context c;
    public static final String DBNAME="DATABASE";
    public static final int VERSION=1;
    public static final String TABLENAME="TABLENAME";
    public static final String SEARCH="SearchTable";
    public static final String COL1="Movie_Id";
    public static final String COL2="Movie_name";
    public static final String COL3="Lyric_Id";
    public static final String COL4="Lyric_Name";
    public static final String COL5="Writer_Name";
    public static final String COL6="Date_Time";

    public static final String SERKEY="SER_KEY";


    public MyDb(Context context)
    {
        super(context, DBNAME, null, VERSION);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        String qry = "create table " + TABLENAME + "( " + COL1 + " text," + COL2 + " text," + COL3 + " text primary key," + COL4 + " text," + COL5 + " text," + COL6 + " text)";
        sqLiteDatabase.execSQL(qry);

        String search="create table " + SEARCH + "( " + SERKEY + " text primary key)";
        sqLiteDatabase.execSQL(search);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
