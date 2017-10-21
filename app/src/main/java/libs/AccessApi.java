package libs;

import android.content.Context;

/**
 * Created by ariflaksito on 10/19/17.
 */

public class AccessApi {
    String uri;
    Context context;
    String out;

    public AccessApi(Context c) {
        uri = "http://10.0.2.2/@amsos-v2/";
        context = c;
    }

    public boolean login(String json){
        HttpConnect conn = new HttpConnect(uri + "auth", json);

        boolean rs = conn.postData();
        out = conn.getOutput();

        return rs;
    }

    public boolean fetchPostings(int ver){
        HttpConnect conn = new HttpConnect(uri + "ver/"+ver);

        boolean rs = conn.getData();
        out = conn.getOutput();

        return rs;
    }

    public boolean postMsg(String json){
        HttpConnect conn = new HttpConnect(uri + "msg", json);

        boolean rs = conn.postData();
        out = conn.getOutput();

        return rs;
    }

    public boolean updateMsg(String json, int id){
        HttpConnect conn = new HttpConnect(uri + "msg/"+id, json);

        boolean rs = conn.putData();
        out = conn.getOutput();

        return rs;
    }

    public boolean deleteMsg(int id){
        HttpConnect conn = new HttpConnect(uri + "msg/"+id);

        boolean rs = conn.delData();
        out = conn.getOutput();

        return rs;
    }

    public String getOut(){
        return out;
    }
}
