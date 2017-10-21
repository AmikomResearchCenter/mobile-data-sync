package libs;

/**
 * Created by ariflaksito on 10/19/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper {

    public DBClass(Context context) {
        super(context, "db_amsos", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists posting "
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER, "
                + "pubid INTEGER, nid VARCHAR, name VARCHAR, msg TEXT, status INTEGER, "
                + "postdate DATETIME, fetchdate DATETIME);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS posting");
        onCreate(db);

    }

}