package net.ariflaksito.amsossync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import libs.AccessApi;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        final EditText txtId = (EditText) findViewById(R.id.txtId);
        final EditText txtPwd = (EditText) findViewById(R.id.txtPwd);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    JSONObject json = new JSONObject();
                    json.put("nid", txtId.getText().toString());
                    json.put("pwd", txtPwd.getText().toString());

                    new doLogin().execute(json.toString());

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        });

    }

    public class doLogin extends AsyncTask<String, String, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(
                LoginActivity.this);
        private String apiOut;

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;

            AccessApi api = new AccessApi(LoginActivity.this);
            rs = api.login(params[0]);

            apiOut = api.getOut();
            return true;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses login..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                try {
                    JSONObject jsOut = new JSONObject(apiOut);
                    String msg = jsOut.getString("msg");
                    boolean login = jsOut.getBoolean("status");

                    if(login){
                        JSONObject jsUser = new JSONObject(jsOut.getString("user"));
                        SharedPreferences sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                        Editor editor = sp.edit();
                        editor.putInt("uid", jsUser.getInt("uid"));
                        editor.putString("name", jsUser.getString("fullname"));
                        editor.putInt("status", jsUser.getInt("status"));
                        editor.putInt("version", 0);
                        editor.commit();

                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                        finish();

                    }

                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT)
                            .show();

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }else{
                Toast.makeText(LoginActivity.this, "Gagal Menghubungi Server", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
}
