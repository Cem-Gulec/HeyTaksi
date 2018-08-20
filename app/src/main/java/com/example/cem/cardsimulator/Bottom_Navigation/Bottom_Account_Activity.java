package com.example.cem.cardsimulator.Bottom_Navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cem.cardsimulator.MapsActivity;
import com.example.cem.cardsimulator.R;

public class Bottom_Account_Activity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__account_);

        //Bottom navigation
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_account);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {

        backPressedEvent();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_account:
                    Intent account = new Intent(Bottom_Account_Activity.this, Bottom_Account_Activity.class);
                    startActivity(account);
                    return true;

                case R.id.navigation_search:
                    Intent search = new Intent(Bottom_Account_Activity.this, Bottom_Search_Activity.class);
                    startActivity(search);
                    return true;

                case R.id.navigation_surucuara:
                    Intent surucu = new Intent(Bottom_Account_Activity.this, Bottom_Gettaxi_Activity.class);
                    startActivity(surucu);
                    return true;

                case R.id.navigation_calculator:
                    Intent calculator = new Intent(Bottom_Account_Activity.this, Bottom_Calculator_Activity.class);
                    startActivity(calculator);
                    return true;

                case R.id.navigation_info:
                    Intent info = new Intent(Bottom_Account_Activity.this, Bottom_Info_Activity.class);
                    startActivity(info);
                    return true;

            }
            return false;
        }
    };

    private void backPressedEvent(){

        Intent i = new Intent(Bottom_Account_Activity.this, MapsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}