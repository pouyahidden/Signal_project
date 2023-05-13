package info.vprofit.app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import info.vprofit.app.Fragment.Menu;
import com.example.signal.R;

public class Subscribtion extends AppCompatActivity {

    ImageView menu;
    ConstraintLayout back;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribtion);

        menu = findViewById(R.id.menu);
        back = findViewById(R.id.back);
        ////////////////////////////////////////////////////////
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragMgnr = getSupportFragmentManager();
                FragmentTransaction fragtrans;
                fragtrans = fragMgnr.beginTransaction();
                fragtrans.setCustomAnimations(R.animator.enter_from_left
                        , R.animator.enter_from_right);
                fragtrans.replace(R.id.nav_host_fragment_activity_main23, new Menu());
                fragtrans.addToBackStack(SyncStateContract.Constants.class.getName());
                fragtrans.commit();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}