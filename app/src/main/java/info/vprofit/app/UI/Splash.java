package info.vprofit.app.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.signal.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    SharedPreferences shared = getApplication().getSharedPreferences("Prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    String token = shared.getString("token", "").trim();


                    if (token.matches("")) {
                        Intent mainIntent = new Intent(Splash.this, LoginPage.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                        finish();

                    }else{
                        Intent mainIntent1 = new Intent(Splash.this, MainActivity.class);
                        Splash.this.startActivity(mainIntent1);
                        Splash.this.finish();
                        finish();
                    }

                }
            }, SPLASH_DISPLAY_LENGTH);
        }

    }

