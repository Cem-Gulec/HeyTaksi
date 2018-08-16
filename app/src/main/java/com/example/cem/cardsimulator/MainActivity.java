package com.example.cem.cardsimulator;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cem.cardsimulator.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    MaterialEditText edtEmail;
    MaterialEditText edtName ;
    MaterialEditText edtPassword ;
    MaterialEditText edtPhone;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email_data,password_data,name_data,phone_data;
    int sayac=0;
    private String EMAIL_KEY="com.example.cem.cardsimulator.EMAIL";
    private String PASSWORD_KEY="com.example.cem.cardsimulator.PASSWORD";
    private String NAME_KEY="com.example.cem.cardsimulator.NAME";
    private String PHONE_KEY="com.example.cem.cardsimulator.PHONE";
    private String MAIN_KEY="com.example.cem.cardsimulator.MAIN_DATA";

    Button btnSignin,btnRegister, btnForget;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    public void onBackPressed() {

        backPressedEvent();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GPSAlert
        GpsAlert();

        //saatin olduğu ekran şeridini kaldırmak için
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Initialize firebase
        auth= FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");




        //init layout elements
        btnSignin= (Button)findViewById(R.id.btnSignin);
        btnRegister= (Button)findViewById(R.id.btnRegister);
        btnForget = (Button) findViewById(R.id.btnForget) ;
        rootLayout= (RelativeLayout)findViewById(R.id.rootLayout);

        //init sharedPreferences
        sharedPreferences = getSharedPreferences(MAIN_KEY,MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //Call for the button events
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetEvent();
            }
        });
    }

    private void backPressedEvent(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setCancelable(false);
            dialog.setMessage("Are you sure ?");


            LayoutInflater inflater = LayoutInflater.from(this);
            View exit_layout = inflater.inflate(R.layout.layout_exit,null);


            dialog.setView(exit_layout);

            //set button
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });

            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });




            dialog.show();



        }
    }

    private void GpsAlert(){

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                MainActivity.this.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        });
        builder.create().show();
    }

    private void forgetEvent(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("FORGOT PASSWORD");
            dialog.setMessage("Use your email to get validation key");


            LayoutInflater inflater = LayoutInflater.from(this);
            View forgot_layout = inflater.inflate(R.layout.activity_forget_password,null);

            final MaterialEditText edtEmail = forgot_layout.findViewById(R.id.et1);

            final String email = edtEmail.getText().toString().trim();

            dialog.setView(forgot_layout);

            //set button
            dialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    //check validation
                    if (TextUtils.isEmpty(edtEmail.getText().toString())) {

                        Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    //Logic kısmı
                    String email = edtEmail.getText().toString().trim();
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "e-mail sent",Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                                }
                            });


                }
            });

            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });




            dialog.show();



        }
    }

    private void showLoginDialog(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("SIGN IN");
            dialog.setMessage("Use your email to sign in");


            LayoutInflater inflater = LayoutInflater.from(this);
            View login_layout = inflater.inflate(R.layout.layout_login,null);

            final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
            final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);

            dialog.setView(login_layout);

            //set button
            dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    //check validation
                    if (TextUtils.isEmpty(edtEmail.getText().toString())) {

                        Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(edtPassword.getText().toString())) {

                        Snackbar.make(rootLayout, "Please enter a password", Snackbar.LENGTH_SHORT).show();
                        return;
                    }


                    if ((edtPassword.getText().toString().length() < 6)) {

                        Snackbar.make(rootLayout, "Password is short", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    //Login
                    auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    progressDevent();

                                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });




            dialog.show();



        }
    }

    private void progressDevent(){

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        /*AsyncTask<Void,Void,Void> asyn = new AsyncTask<Void, Void, Void>() {
            private ProgressDialog dialog= new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Loading..");
                dialog.setTitle("Please Wait");
                dialog.setCancelable(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }

            protected Void doInBackground(Void... args) {
                try{
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                // do UI work here
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

        }.execute();*/








    }

    private void showRegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Use your email to register");


        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        email_data = getSharedPreferences(MAIN_KEY, MODE_PRIVATE).getString(EMAIL_KEY, "");
        password_data = getSharedPreferences(MAIN_KEY, MODE_PRIVATE).getString(PASSWORD_KEY,"");
        name_data = getSharedPreferences(MAIN_KEY, MODE_PRIVATE).getString(NAME_KEY,"");
        phone_data = getSharedPreferences(MAIN_KEY, MODE_PRIVATE).getString(PHONE_KEY,"");

        edtEmail = register_layout.findViewById(R.id.edtEmail);
        edtName = register_layout.findViewById(R.id.edtName);
        edtPassword = register_layout.findViewById(R.id.edtPassword);
        edtPhone = register_layout.findViewById(R.id.edtPhone);

        if(sayac!=0){
            edtEmail.setText(email_data);
            edtName.setText(name_data);
            edtPassword.setText(password_data);
            edtPhone.setText(phone_data);
        }

        dialog.setView(register_layout);

        //set button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                //check validation
                if(TextUtils.isEmpty(edtEmail.getText().toString())){

                    Snackbar.make(rootLayout,"Please enter email address",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edtPhone.getText().toString())){

                    Snackbar.make(rootLayout,"Please enter phone number",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(edtName.getText().toString())){

                    Snackbar.make(rootLayout,"Please enter your name",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if((edtPassword.getText().toString().length() < 6)){

                    Snackbar.make(rootLayout,"Password is short",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //Save user to db
                                User user = new User();

                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                user.setPhone(edtPhone.getText().toString());

                                //Use email to key
                                users.child(FirebaseAuth.getInstance().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Register successfull",Snackbar.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                                    }
                                })
                                ;

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editor.putString(EMAIL_KEY, edtEmail.getText().toString());
                editor.putString(PASSWORD_KEY, edtPassword.getText().toString());
                editor.putString(NAME_KEY, edtName.getText().toString());
                editor.putString(PHONE_KEY, edtPhone.getText().toString());
                editor.commit();
                sayac++;

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
