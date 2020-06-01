# store-data-in-sqlite-database-after-fetching-from-server
store data in sqlite database after fetching from server

### MainActivity.java
- When pull to refresh than call the method to get the data from server and store into sqlite if there no internet it is show toast and data is shown from sqlite
- create the RecyclerView In using volley than get the response and store into sqlite as follow

   - get response from server and pass it to dbHelperAdapter class 
  
  ```
    dbHelperAdapter.SaveRide(new Post(post_id,post_title,post_sub_title,post_content,post_userid,
                                    post_username,post_date,post_image,post_link,topics,post_view,post_like,premium_flag));
                                     
  ```                      
- create the Database in sqlite and create table in database 

```

 class DbHelper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "postList.db";
        static final int VERSION = 1;
       .
       .
       .
       .
       .
       .
       .
       
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
         //create table 
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
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

```

- store the data into the sqlite database

```
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

```

- get the list data from sqlite and pass it to arraylist of post

```
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
```
