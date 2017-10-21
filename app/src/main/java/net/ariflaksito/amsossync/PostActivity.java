package net.ariflaksito.amsossync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import libs.AccessApi;

public class PostActivity extends AppCompatActivity {

    private SharedPreferences sp;
    String msg;
    int pubid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        final EditText txt = (EditText)findViewById(R.id.txtPost);
        Button btnPost = (Button)findViewById(R.id.btnPost);
        sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        final Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            msg = extras.getString("msg");
            pubid = extras.getInt("pubid");
            txt.setText(msg);

        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject js = new JSONObject();
                    js.put("msg", txt.getText().toString());

                    if(extras!=null) {
                        new PostData().execute(js.toString(), pubid + "");

                    }else {
                        js.put("uid", sp.getInt("uid", 0));
                        new PostData().execute(js.toString());

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private class PostData extends AsyncTask<String, String, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(
                PostActivity.this);
        private String apiOut;

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean rs = false;

            AccessApi api = new AccessApi(getApplicationContext());

            if(strings.length>1){
                rs = api.updateMsg(strings[0], Integer.parseInt(strings[1]));
            }else{
                rs = api.postMsg(strings[0]);
            }

            apiOut = api.getOut();

            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Post data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                try {
                    JSONObject jsOut = new JSONObject(apiOut);
                    String msg = jsOut.getString("msg");

                    Toast.makeText(PostActivity.this, msg, Toast.LENGTH_SHORT)
                            .show();

                    finish();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(PostActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
