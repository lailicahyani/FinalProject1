package com.hactiv8.project1todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TaskDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "TaskDBHelper";

    private static final String DATABASE_NAME= "TaskDB";
    private static final String TABLE_TASK_NAME="TaskTable";
    private static final int DATABASE_VERSION=1;

    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_IS_DONE="done";

    public TaskDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_TASK_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    "title TEXT , done BOOLEAN);");
        }catch (SQLException e)
        {
            Log.e(TAG, "sqlException error: "+e.getMessage() );
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TASK_NAME);
        onCreate(sqLiteDatabase);
    }



    public ArrayList<Task> getTasks()
    {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();

        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TASK_NAME , null);

        int idIndex=cursor.getColumnIndex(KEY_ID);
        int titleIndex=cursor.getColumnIndex(KEY_TITLE);
        int doneIndex=cursor.getColumnIndex(KEY_IS_DONE);

        ArrayList<Task> tasks=new ArrayList<>();

        //true if table is full
        if (cursor.moveToFirst()){
            do {
                Task task=new Task(cursor.getString(titleIndex) ,
                        cursor.getInt(doneIndex)==1);
                task.setId(cursor.getInt(idIndex));
                tasks.add(task);
            }while (cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return tasks;
    }

    public long addTask(Task task){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_TITLE , task.getTitle());
        contentValues.put(KEY_IS_DONE , task.isDone());

        // it returns id if insertion is successful otherwise it returns -1
        long result=sqLiteDatabase.insert(TABLE_TASK_NAME , null , contentValues);

        sqLiteDatabase.close();

        return result;
    }

    public int updateTask(Task task){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(KEY_TITLE , task.getTitle());
        values.put(KEY_IS_DONE , task.isDone());

        //update() returns the number of rows effected
        int result=sqLiteDatabase.update(TABLE_TASK_NAME , values , "id = ?" ,
                new String[]{String.valueOf(task.getId())});

        sqLiteDatabase.close();
        return result;
    }

    public int deleteTask(Task task){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();

        int result= sqLiteDatabase.delete(TABLE_TASK_NAME , "id = ?" ,
                new String[]{String.valueOf(task.getId())});

        sqLiteDatabase.close();
        return result;
    }

    //LOCAL SEARCH
    public ArrayList<Task> searchInTasks(String query){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM " +TABLE_TASK_NAME + " WHERE title LIKE '%"+query+"%'", null);

        int idIndex=cursor.getColumnIndex(KEY_ID);
        int titleIndex=cursor.getColumnIndex(KEY_TITLE);
        int doneIndex=cursor.getColumnIndex(KEY_IS_DONE);

        ArrayList<Task> tasks=new ArrayList<>();

        //true if table is full
        if (cursor.moveToFirst()){
            do {
                Task task=new Task( cursor.getString(titleIndex) ,
                        cursor.getInt(doneIndex)==1);
                task.setId(cursor.getInt(idIndex));
                tasks.add(task);
            }while (cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return tasks;

    }
}
