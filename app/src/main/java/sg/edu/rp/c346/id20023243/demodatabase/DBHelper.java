package sg.edu.rp.c346.id20023243.demodatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //final means cannot change
    private static final int DATABASE_VER = 1; //1 because this is the first ever db
    private static final String DATABASE_NAME = "tasks.db";

    private static final String TABLE_TASK = "task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";

    public DBHelper(@Nullable Context context) { //will check if task.db exists; if not onCreate() is called
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //create the table

        //Build SQL Query
        String createTableSql = "CREATE TABLE " + TABLE_TASK + "(" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DESCRIPTION +" TEXT," + COLUMN_DATE + " TEXT)";

        Log.i("info","created tables");

        //execute sql query
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_TASK); //actually should not drop bc drop means all user data is gone
        onCreate(db); //create table(s) again
    }

    public void insertTask(String description, String date) {

        SQLiteDatabase db = this.getWritableDatabase(); //get editable data

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_TASK, null, values);
        db.close();

    }

    public ArrayList<String> getTaskContent() {
        ArrayList<String> tasks = new ArrayList<>(); //create Strings arrayList named tasks
        String selectQuery = "SELECT "+ COLUMN_DESCRIPTION + " FROM " + TABLE_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); //cursor points to each row of data

        if(cursor.moveToFirst()) { //if got data in database
            do {
                tasks.add(cursor.getString(0)); //tasks is arrayList, column 0 is description
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return tasks;
    }

    public ArrayList<Task> getTasks(boolean asc) { //true asc, false desc
        ArrayList<Task> tasks = new ArrayList<>();
        String sort = "";
        if(asc) {
            sort = " ASC";
        }
        else {
            sort = " DESC";
        }
        String selectQuery = "SELECT " + COLUMN_ID + ", "
                + COLUMN_DESCRIPTION + ", " + COLUMN_DATE
                + " FROM " + TABLE_TASK
                + " ORDER BY " + COLUMN_DESCRIPTION + sort;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                //index is corresponding to sql query,
                // if u put id as 3rd one in query then index would be 2
                String description = cursor.getString(1);
                String date = cursor.getString(2);
                Task obj = new Task(id, description, date);
                tasks.add(obj);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tasks;
    }

}
