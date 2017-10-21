package models;

import java.sql.Timestamp;

/**
 * Created by ariflaksito on 10/19/17.
 */

public class Posting {
    private int _id;
    private int pubid;
    private int uid;
    private String nid;
    private String name;
    private String msg;
    private int status;
    private Timestamp postdate;
    private Timestamp fetchdate;

    public void setId(int id){
        this._id = id;
    }

    public int getId(){
        return _id;
    }

    public int getPubid() {
        return pubid;
    }

    public void setPubid(int pubid) {
        this.pubid = pubid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getPostdate() {
        return postdate;
    }

    public void setPostdate(Timestamp postdate) {
        this.postdate = postdate;
    }

    public Timestamp getFetchdate() {
        return fetchdate;
    }

    public void setFetchdate(Timestamp fetchdate) {
        this.fetchdate = fetchdate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
