package monitor.mobie.hdy.im.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by hdy on 28/11/2018.
 */

public class AppinfosDatabase extends SQLiteOpenHelper {

    private static AppinfosDatabase database;
    private static final String DB_NAME = "appinfos.db";
    private static final int DB_VERSION = 1;
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;

    private AppinfosDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private AppinfosDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static AppinfosDatabase getInstance(Context context) {
        if (database == null) {
            database = new AppinfosDatabase(context, DB_NAME, null, DB_VERSION);
        }
        return database;
    }

    public static SQLiteDatabase getWriteInstance(Context context) {
        AppinfosDatabase database = getInstance(context);
        if (writableDatabase == null) {
            writableDatabase = database.getWritableDatabase();
        }
        return writableDatabase;
    }

    public static SQLiteDatabase getReadInstance(Context context) {
        AppinfosDatabase database = getInstance(context);
        if (readableDatabase == null) {
            readableDatabase = database.getReadableDatabase();
        }
        return readableDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个表
        db.execSQL("CREATE TABLE IF NOT EXISTS appinfos(id integer PRIMARY KEY autoincrement,packagename varchar(255) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //查询所有的数据
    public HashMap<String, Object> selectAll(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select id,packagename from appinfos", new String[]{});
        if (cursor != null && cursor.getCount() > 0) {//判断当中是否有数据
            HashMap<String, Object> hashMap = new HashMap<>();
            while (cursor.moveToNext()) {
                //从数据当中查询到相关的数据之后
                hashMap.put(cursor.getString(1), cursor.getInt(0));
            }
            return hashMap;
        }
        return null;
    }

    //移除所有的数据
    public void removeAll(SQLiteDatabase db) {
        db.execSQL("delete from appinfos");
    }

    public void removeOne(SQLiteDatabase db, String packagename) {
        String sql = "delete from appinfos where packagename = ?";
        db.execSQL(sql, new String[]{packagename});
    }

    //根据packageName查询,判断是否已经存在相关的数据
    public boolean selectByName(SQLiteDatabase db, String name) {
        String sql = "select id,packagename from appinfos where packagename = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if (cursor != null && cursor.getCount() > 0) {//判断当中是否有数据
            return true;
        }
        return false;
    }


    public void insert(SQLiteDatabase db, String packageName) {
        String sql = "INSERT INTO appinfos(packagename) VALUES(?)";
        db.execSQL(sql, new String[]{packageName});
    }
}
