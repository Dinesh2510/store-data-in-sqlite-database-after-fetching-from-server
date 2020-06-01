package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_AutoID;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_CONTENT;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_DATE;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_ID;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_IMAGE;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_LIKE;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_LINK;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_SUB_TITLE;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_TITLE;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_USERID;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_USERNAME;
import static com.example.myapplication.DbHelperAdapter.DbHelper.POST_VIEW;
import static com.example.myapplication.DbHelperAdapter.DbHelper.PREMIUM_FLAG;
import static com.example.myapplication.DbHelperAdapter.DbHelper.TOPICS;

public class DbHelperAdapter {

    Context context;
    private final DbHelper helper;

    public DbHelperAdapter(Context context) {
        helper = new DbHelper(context);
    }

    public DbHelperAdapter open() throws SQLException {
        SQLiteDatabase mDb = helper.getWritableDatabase();
        return this;
    }

    public void SaveRide(Post post) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(POST_ID, post.getPost_id());
        values.put(POST_TITLE, post.getPost_title());
        values.put(POST_SUB_TITLE, post.getPost_sub_title());
        values.put(POST_USERID, post.getPost_userid());
        values.put(POST_USERNAME, post.getPost_username());
        values.put(POST_CONTENT, post.getPost_content());
        values.put(POST_DATE, post.getPost_date());
        values.put(POST_IMAGE, post.getPost_image());
        values.put(POST_LINK, post.getPost_link());
        values.put(TOPICS, post.getTopics());
        values.put(POST_VIEW, post.getPost_view());
        values.put(POST_LIKE, post.getPost_like());
        values.put(PREMIUM_FLAG, post.getPremium_flag());
        long id = db.insert(DbHelper.RIDE_TABLE_NAME, null, values);

        db.close();
    }

    private final ArrayList<Post> ride_display = new ArrayList<>();

    public ArrayList<Post> GetAllRide() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + DbHelper.RIDE_TABLE_NAME + " ORDER BY " + POST_ID + " DESC ";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Post post1 = new Post();
                post1.setPost_id(cursor.getString(cursor.getColumnIndex(POST_ID)));
                post1.setPost_title(cursor.getString(cursor.getColumnIndex(POST_TITLE)));
                post1.setPost_sub_title(cursor.getString(cursor.getColumnIndex(POST_SUB_TITLE)));
                post1.setPost_content(cursor.getString(cursor.getColumnIndex(POST_CONTENT)));
                post1.setPost_userid(cursor.getString(cursor.getColumnIndex(POST_USERID)));
                post1.setPost_username(cursor.getString(cursor.getColumnIndex(POST_USERNAME)));
                post1.setPost_date(cursor.getString(cursor.getColumnIndex(POST_DATE)));
                post1.setPost_image(cursor.getString(cursor.getColumnIndex(POST_IMAGE)));
                post1.setPost_link(cursor.getString(cursor.getColumnIndex(POST_LINK)));
                post1.setTopics(cursor.getString(cursor.getColumnIndex(TOPICS)));
                post1.setPost_view(cursor.getString(cursor.getColumnIndex(POST_VIEW)));
                post1.setPost_like(cursor.getString(cursor.getColumnIndex(POST_LIKE)));
                post1.setPremium_flag(cursor.getString(cursor.getColumnIndex(PREMIUM_FLAG)));


                Log.d("SERVER_IDS", "" + cursor.getString(cursor.getColumnIndex(POST_USERNAME)));
                ride_display.add(post1);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ride_display;

    }

    public void DeleteRideTable() {

        Log.d("DELETE", "DELETING TABLE");
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.delete(RIDE_TABLE_NAME, RIDE_AUTO_ID+" = "+"'"+RIDE_AUTO_ID+"'", null);

        db.execSQL("DELETE FROM " + DbHelper.RIDE_TABLE_NAME);
    }


    class DbHelper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "postList.db";
        static final int VERSION = 1;
        static final String RIDE_TABLE_NAME = "posts";
        static final String POST_AutoID = "post_autoid";
        static final String POST_ID = "post_id";
        static final String POST_TITLE = "post_title";
        static final String POST_SUB_TITLE = "post_sub_title";
        static final String POST_CONTENT = "_post_content";
        static final String POST_USERID = "post_userid";
        static final String POST_USERNAME = "post_username";
        static final String POST_DATE = "post_date";
        static final String POST_IMAGE = "post_image";
        static final String POST_LINK = "post_link";
        static final String TOPICS = "topics";
        static final String POST_VIEW = "post_view";
        static final String POST_LIKE = "post_like";
        static final String PREMIUM_FLAG = "premium_flag";
        //  Table create
        private static final String CREATE_RIDE_TABLE = "CREATE TABLE IF NOT EXISTS " + RIDE_TABLE_NAME
                + "("
                + POST_AutoID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + POST_ID + " INTEGER(255),"
                + POST_TITLE + " VARCHAR(255),"
                + POST_SUB_TITLE + " VARCHAR(255),"
                + POST_CONTENT + " VARCHAR(255),"
                + POST_USERID + " VARCHAR(255),"
                + POST_USERNAME + " VARCHAR(255),"
                + POST_DATE + " VARCHAR(255),"
                + POST_IMAGE + " VARCHAR(255),"
                + POST_LINK + " VARCHAR(255),"
                + TOPICS + " VARCHAR(255),"
                + POST_VIEW + " VARCHAR(255),"
                + POST_LIKE + " VARCHAR(255),"
                + PREMIUM_FLAG + " VARCHAR(255)"
                + ")";
        //DROP TABLE
        private static final String DROP_RIDE_TABLE = "DROP TABLE IF EXISTS " + RIDE_TABLE_NAME;
        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                Toast.makeText(context, "CREATE_RIDE_TABLE", Toast.LENGTH_SHORT).show();
                sqLiteDatabase.execSQL(CREATE_RIDE_TABLE);
            } catch (SQLiteException ignored) {
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            sqLiteDatabase.execSQL(DROP_RIDE_TABLE);

            onCreate(sqLiteDatabase);
        }

    }
}