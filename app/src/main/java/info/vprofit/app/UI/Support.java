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
import androidx.viewpager.widget.ViewPager;

import info.vprofit.app.Adapter.CheshmAdapter;
import info.vprofit.app.Fragment.Menu;
import info.vprofit.app.Fragment.Notification;
import com.example.signal.R;
import com.google.android.material.tabs.TabLayout;

public class Support extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    ImageView menu, profile;
    ConstraintLayout back;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Unread"));
        menu = findViewById(R.id.menu);
        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final CheshmAdapter adapter = new CheshmAdapter(getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(1);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMgnr = getSupportFragmentManager();
                FragmentTransaction fragtrans;
                fragtrans = fragMgnr.beginTransaction();
                fragtrans.setCustomAnimations(R.animator.enter_from_right1,
                        R.animator.enter_from_left1);
                fragtrans.replace(R.id.nav_host_fragment_activity_main24, new Notification());
                fragtrans.addToBackStack(SyncStateContract.Constants.class.getName());
                fragtrans.commit();
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