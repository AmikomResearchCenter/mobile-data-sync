package net.ariflaksito.amsossync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.AdapterData;
import controls.PostingLogic;
import libs.AccessApi;
import models.Posting;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SharedPreferences sp;
    private AdapterData adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Shout Option");
        menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        List<Posting> posting = new PostingLogic(getApplicationContext()).get();

        Posting post = posting.get(info.position);
        if(sp.getInt("uid",0)!=post.getUid()){
            Toast.makeText(MainActivity.this, "Tidak diperbolehkan merubah data", Toast.LENGTH_SHORT)
                    .show();
        }else{
            switch (item.getItemId()) {
                case 0:
                    Intent i = new Intent(MainActivity.this, PostActivity.class);
                    i.putExtra("msg", post.getMsg());
                    i.putExtra("pubid", post.getPubid());
                    startActivity(i);
                    return (true);
                case 1:
                    confirmDelete(post.getPubid());
                    return (true);
            }
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.reload:
                new getData().execute();
                return(true);
            case R.id.profile:
                //
                return(true);
            case R.id.logout:

                getApplicationContext().getSharedPreferences("MyData", Context.MODE_PRIVATE).edit().clear().commit();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                PostingLogic p = new PostingLogic(getApplicationContext());
                p.remove();

                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    private class getData extends AsyncTask<String, String, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(
                MainActivity.this);
        private String apiOut;

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean rs = false;

            AccessApi api = new AccessApi(getApplicationContext());
            rs = api.fetchPostings(sp.getInt("version",0));
            apiOut = api.getOut();

            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses Sync Data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {

                try{
                    JSONArray jsArray = new JSONArray(apiOut);
                    for(int i=0; i<jsArray.length(); i++){
                        JSONObject jsData = jsArray.getJSONObject(i);

                        if(jsData.getString("type").equals("add") && !jsData.getString("msg").equals("null")){
                            Posting p = new Posting(){};
                            p.setPubid(jsData.getInt("id"));
                            p.setUid(jsData.getInt("uid"));
                            p.setNid(jsData.getString("nid"));
                            p.setName(jsData.getString("fullname"));
                            p.setStatus(jsData.getInt("status"));
                            p.setMsg(jsData.getString("msg"));
                            p.setPostdate(Timestamp.valueOf(jsData.getString("postdate")));

                            PostingLogic post = new PostingLogic(getApplicationContext());
                            post.add(p);

                            android.util.Log.d(getPackageName(), "add "+jsData.getInt("id"));
                        }else if(jsData.getString("type").equals("delete")){
                            PostingLogic p = new PostingLogic(getApplicationContext());
                            p.delete(jsData.getInt("id"));

                            android.util.Log.d(getPackageName(), "delete "+jsData.getInt("id"));
                        }else if(jsData.getString("type").equals("update") && !jsData.getString("msg").equals("null")){
                            Posting p = new Posting(){};
                            p.setMsg(jsData.getString("msg"));

                            PostingLogic post = new PostingLogic(getApplicationContext());
                            post.update(jsData.getInt("id"), p);

                        }

                        if(i==jsArray.length()-1){
                            SharedPreferences sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("version", jsData.getInt("ver"));
                            editor.apply();
                        }

                    }

                    showListData();

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    protected void onResume() {
        new getData().execute();
        super.onResume();
    }

    private void showListData(){
        List<Posting> posting = new PostingLogic(getApplicationContext()).get();
        adapter = new AdapterData(sp.getInt("uid", 0), posting);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    private void confirmDelete(final int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Confirm Delete...");
        alertDialog.setMessage("Are you sure you want delete this data?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                new deleteData().execute(String.valueOf(id));

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private class deleteData extends AsyncTask<String, String, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(
                MainActivity.this);
        private String apiOut;

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean rs = false;

            AccessApi api = new AccessApi(getApplicationContext());
            rs = api.deleteMsg(Integer.parseInt(strings[0]));
            apiOut = api.getOut();

            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses Delete Data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                try {
                    JSONObject jsOut = new JSONObject(apiOut);
                    String msg = jsOut.getString("msg");
                    new getData().execute();

                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT)
                            .show();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
