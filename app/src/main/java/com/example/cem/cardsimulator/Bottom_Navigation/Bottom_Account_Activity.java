package com.example.cem.cardsimulator.Bottom_Navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cem.cardsimulator.MainActivity;
import com.example.cem.cardsimulator.MapsActivity;
import com.example.cem.cardsimulator.R;

public class Bottom_Account_Activity extends AppCompatActivity {

    private TextView mTextMessage;
    Toolbar toolbar;
    Button btn_Relog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__account_);

        toolbar= (Toolbar)findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Layout elements
        btn_Relog = (Button)findViewById(R.id.btn_Relog);

        //Bottom navigation
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_account);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        btn_Relog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Bottom_Account_Activity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent info = new Intent(Bottom_Account_Activity.this, MapsActivity.class);
                startActivity(info);
                break;
        }
        return super.onOptionsItemSelected(item);
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
