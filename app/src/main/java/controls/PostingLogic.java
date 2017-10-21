package controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import libs.DBClass;
import models.Posting;

/**
 * Created by ariflaksito on 10/19/17.
 */

public class PostingLogic implements IPosting {

    DBClass db;

    public PostingLogic(Context c){
        db = new DBClass(c);
    }

    @Override
    public void add(Posting p) {
        SQLiteDatabase sqldb = db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("pubid", p.getPubid());
        values.put("nid", p.getNid());
        values.put("uid", p.getUid());
        values.put("name", p.getName());
        values.put("msg", p.getMsg());
        values.put("status", p.getStatus());
        values.put("postdate", String.valueOf(p.getPostdate()));

        long millis = new Date().getTime();
        values.put("fetchdate", millis);

        sqldb.insert("posting",null, values);
        sqldb.close();
    }

    @Override
    public void update(int pubid, Posting p) {
        SQLiteDatabase sqldb = db.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("msg", p.getMsg());

        long millis = new Date().getTime();
        values.put("fetchdate", millis);

        sqldb.update("posting",values,"pubid="+pubid, null);
        sqldb.close();
    }

    @Override
    public void delete(int pubid) {
        SQLiteDatabase sqldb = db.getWritableDatabase();
        String str = "Delete From posting Where pubid = " + pubid;

        sqldb.execSQL(str);
        sqldb.close();
    }

    public void remove(){
        SQLiteDatabase sqldb = db.getWritableDatabase();
        String str = "Delete From posting ";

        sqldb.execSQL(str);
        sqldb.close();
    }

    @Override
    public List<Posting> get() {
        List<Posting> list = new ArrayList<Posting>();
        SQLiteDatabase sqldb = db.getWritableDatabase();
        Cursor cur = sqldb.rawQuery("select _id,pubid,nid,uid,name,msg,status,postdate " +
                "from posting order by pubid desc", null);

        if (cur.moveToFirst()) {
            do{
                Posting p = new Posting(){};
                p.setId(cur.getInt(cur.getColumnIndex("_id")));
                p.setPubid(cur.getInt(cur.getColumnIndex("pubid")));
                p.setUid(cur.getInt(cur.getColumnIndex("uid")));
                p.setNid(cur.getString(cur.getColumnIndex("nid")));
                p.setName(cur.getString(cur.getColumnIndex("name")));
                p.setMsg(cur.getString(cur.getColumnIndex("msg")));
                p.setStatus(cur.getInt(cur.getColumnIndex("status")));
                p.setPostdate(Timestamp.valueOf(cur.getString(cur.getColumnIndex("postdate"))));

                list.add(p);

            }while (cur.moveToNext());
        }

        cur.close();
        sqldb.close();

        return list;
    }

    public Posting get(int id){
        SQLiteDatabase sqldb = db.getWritableDatabase();
        Cursor cur = sqldb.rawQuery("select _id,pubid,nid,uid,name,msg,status,postdate " +
                "from posting where _id="+id, null);

        Posting p = new Posting(){};
        if (cur.moveToFirst()) {
            p.setId(cur.getInt(cur.getColumnIndex("_id")));
            p.setPubid(cur.getInt(cur.getColumnIndex("pubid")));
            p.setUid(cur.getInt(cur.getColumnIndex("uid")));
            p.setNid(cur.getString(cur.getColumnIndex("nid")));
            p.setName(cur.getString(cur.getColumnIndex("name")));
            p.setMsg(cur.getString(cur.getColumnIndex("msg")));
            p.setStatus(cur.getInt(cur.getColumnIndex("status")));
            p.setPostdate(Timestamp.valueOf(cur.getString(cur.getColumnIndex("postdate"))));
        }

        return p;
    }
}
