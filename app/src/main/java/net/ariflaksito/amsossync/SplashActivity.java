package net.ariflaksito.amsossync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    SharedPreferences sp = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                    int uid = sp.getInt("uid", 0);

                    Intent i;
                    if(uid>0){
                        i = new Intent(getBaseContext(), MainActivity.class);
                    }else{
                        i = new Intent(getBaseContext(), LoginActivity.class);
                    }

                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}
