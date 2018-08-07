package com.example.cem.cardsimulator;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.example.cem.cardsimulator.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button btnSignin,btnRegister;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_main);


        //Initialize firebase
        auth= FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users= db.getReference("Users");


        //init layout elements
        btnSignin= (Button)findViewById(R.id.btnSignin);
        btnRegister= (Button)findViewById(R.id.btnRegister);
        rootLayout= (RelativeLayout)findViewById(R.id.rootLayout);

        //Event
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
    }

    private void showLoginDialog(){

        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("SIGN IN");
            dialog.setMessage("Use your email to register");


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

                                            startActivity(new Intent(MainActivity.this,Main2Activity.class));
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

    private void showRegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Use your email to register");


        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

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
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
