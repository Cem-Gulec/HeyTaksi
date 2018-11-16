/*
TODO: konum açma dialoğu tasarım
TODO: dialog positive & negative button tasarım
TODO: deviceta çıkacak logo değiştir
TODO: botton navigation tasarım
TODO: default bottom navigation color
TODO: Mainactivity butonlar için güzel bir tasarım
TODO: Sayfalar arası geçiş animasyonu beklet ve güzelleştir
TODO: başlat(GetTaxi) butonu diğerlerinden daha farklı ve öne çıkar olsun (button navigation elementlerini teker teker düzenlemeyi öğren
TODO: dialoglardan her girmediği alan için uyarı
TODO: phone number 5 dışında bir şey ile başlayamasın
TODO: kullanıcı şifresini maskeleyip depola firebasede -- hashlemek, kriptolamak, şifrelemek
TODO: bitaksideki gibi sol üstte yer alan --- yapılabilir
TODO: Account kısmına koyacağım "How to use" gibi bir şeyde -> https://www.youtube.com/watch?v=RUkbmVvxu5U
TODO: Toolbar title gravity center https://stackoverflow.com/questions/12387345/how-to-center-align-the-actionbar-title-in-android
TODO: en başta konum sorarken default metin gibi yaz ve konum şekli falan koy ( bitaksinin aynısı)
TODO: bottom navigation with center floating button-> https://github.com/ittianyu/BottomNavigationViewEx + https://www.youtube.com/watch?v=ALbjkVDkB7I&list=PLc2rvfiptPSRCMzAoxGQ2dF4G6zqDvo_v&index=34
TODO: MainActivitydeki dialoglar açılırken animasyon ile gelsin - https://www.youtube.com/watch?v=-7xLyPLJ_NI
TODO: Splash screen güzelleştirmek için birşeyler yapılabilir (en başta gelen ekran) - https://www.youtube.com/watch?v=ZZkanr8tS6w
TODO: Map_type için floating button yap -> diğer yapılabilecek map_typeları da içine göm
TODO: searchintentte konum değiştiğinde camera animasyonlu mu gitsin? bitaksiden bak
TODO: calculator için searchdeki map olaylarını taşı

TODO: bir kere login olduysa Main Acitivity atlayıp direkt map çalışsın
TODO: Search intent dizayn ( arama çubuğu altında hazır keyler olacak)
TODO: Bottom navigationdakilere tıklandığında çıkacak intentlerin tasarımı
TODO: Rastgele taksi konumları oluşturma(random generator ile)
TODO: Yol para ücreti hesap(intent olarak) -> if(açılış ücret+ kmbaşı*km) < min return min    else return yol ücret
TODO: Account intent içinde; Home,Recents,Settings,Write to us, About us olacak
TODO: Info kısmına bize yazın olacak -> Benim linklerimi içeren sayfa olacak -- https://github.com/medyo/android-about-page
TODO: gps izninin direkt otomatik olarak belirlenmesi kodda
TODO: her aktivite gerçekleştirecek şeye tıklamadan önce location permission kontrol etsin
TODO: gps kısmına while gibi bir loop koy: en başta intente girdikten sonra eğer konum açılmayıp geri gelindiyse tekrar intente girsin / konum açma otomatik yapana kadar yapılabilecek çözüm   -> gpsalert boolean olsun while içindeki durumuna göre çağrılmaya dev
TODO: clearden sonra benim konum eklensin // en başta konum imgesini silmemek için -> (çözüldü) isSearched ile if koşulu konularak clearden sonra yapılabilir
TODO: son ve önemli görevimiz -> Bottom__search intentte yapacağı aramayı onPlaceSelected kısmındaki Place objesinin lat ve longitude özelliklerini tutmamız gerek
- bilgiyi tuttuktan sonra alttaki tamam butonuna basıldığında MapsActivity'e o konumu lastKonum gibi bir şekilde dönecek
- lokasyon servislerinden aldığı benim konumum ile DiğerActivityden dönen lokasyon arası çizgi çekilecek
- Son olarak GetTaxi'ye basılır ise yolculuk animasyonu başlayacak
- Yolculuk sonunda para bilgisini kaydetmemiz gerekiyor
TODO: defterde yazdığım son maddeler
TODO: son durum -> ilk( benim konum ) ile varmak istenilen yer arası route çizilecek / sonrasında aşağıdan yolculuk başlat denilirse yolculuk başlasın detaylar ekrana verilsin -> süre uzaklık fiyat vs.
TODO: kamera açısı direkt üstümde başlayacak7
TODO: double değerlerde noktadan sonra 2 değer olması gerekiyor.
*/

package com.example.cem.cardsimulator;

import android.support.v7.app.AlertDialog;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    LocationManager manager;

    private ProgressDialog progressDialog;

    MaterialEditText edtEmail,edtEmailSign;
    MaterialEditText edtName ;
    MaterialEditText edtPassword,edtPasswordSign ;
    MaterialEditText edtPhone;

    SharedPreferences sharedPreferences,sharedPreferencesSign;
    SharedPreferences.Editor editor,editorSign;
    String email_data,password_data,name_data,phone_data;
    String email_dataSign,password_dataSign;
    int sayac=0;
    boolean registerClicked=false, checkboxClickCheck=false, textClickCheck=false;
    private String EMAIL_KEY="com.example.cem.cardsimulator.EMAIL";
    private String PASSWORD_KEY="com.example.cem.cardsimulator.PASSWORD";
    private String NAME_KEY="com.example.cem.cardsimulator.NAME";
    private String PHONE_KEY="com.example.cem.cardsimulator.PHONE";
    private String MAIN_KEY="com.example.cem.cardsimulator.MAIN_DATA";

    private String EMAIL_KEYSIGN="com.example.cem.cardsimulator.EMAILSIGN";
    private String PASSWORD_KEYSIGN="com.example.cem.cardsimulator.PASSWORDSIGN";
    private String MAIN_KEYSIGN="com.example.cem.cardsimulator.MAIN_DATASIGN";

    Button btnSignin,btnRegister, btnForget;
    RelativeLayout rootLayout;
    CheckBox checkBox;
    TextView tv;

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
        sharedPreferencesSign = getSharedPreferences(MAIN_KEYSIGN,MODE_PRIVATE);
        editorSign = sharedPreferencesSign.edit();


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

    private void GpsAlert() {

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

                                   /* if(!provider.contains("gps")){ //if gps is disabled
                                        final Intent poke = new Intent();
                                        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                                        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                                        poke.setData(Uri.parse("3"));
                                        sendBroadcast(poke);
                                    }*/


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

            email_dataSign = getSharedPreferences(MAIN_KEYSIGN, MODE_PRIVATE).getString(EMAIL_KEYSIGN, "");
            password_dataSign = getSharedPreferences(MAIN_KEYSIGN, MODE_PRIVATE).getString(PASSWORD_KEYSIGN,"");

            edtEmailSign = login_layout.findViewById(R.id.edtEmail);
            edtPasswordSign = login_layout.findViewById(R.id.edtPassword);
            checkBox= login_layout.findViewById(R.id.checkbox1);
            tv= login_layout.findViewById(R.id.txtL);


            edtEmailSign.setText(email_dataSign);
            edtPasswordSign.setText(password_dataSign);

            dialog.setView(login_layout);

            //set button
            dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    //check validation
                    if (TextUtils.isEmpty(edtEmailSign.getText().toString())) {

                        Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(edtPasswordSign.getText().toString())) {

                        Snackbar.make(rootLayout, "Please enter a password", Snackbar.LENGTH_SHORT).show();
                        return;
                    }


                    if ((edtPasswordSign.getText().toString().length() < 6)) {

                        Snackbar.make(rootLayout, "Password is short", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                    waitingDialog.show();

                    //Login
                    auth.signInWithEmailAndPassword(edtEmailSign.getText().toString(),edtPasswordSign.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    waitingDialog.dismiss();
                                    if(checkboxClickCheck){

                                        editorSign.putString(EMAIL_KEYSIGN, edtEmailSign.getText().toString());
                                        editorSign.putString(PASSWORD_KEYSIGN, edtPasswordSign.getText().toString());
                                        editorSign.commit();
                                    }

                                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            waitingDialog.dismiss();
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

    public void textClick(View v){

        if(textClickCheck){
            checkBox.setChecked(false);
            textClickCheck=false;

        }
        else
        {
            checkBox.setChecked(true);
            textClickCheck=true;
        }


    }

    public void checkboxClick(View v){

        if(checkboxClickCheck)
            checkboxClickCheck=false;

        else
            checkboxClickCheck=true;
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


        if(sayac>0 && !registerClicked){
            edtEmail.setText(email_data);
            edtName.setText(name_data);
            edtPassword.setText(password_data);
            edtPhone.setText(phone_data);
        }

        dialog.setView(register_layout);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                editor.putString(EMAIL_KEY, edtEmail.getText().toString());
                editor.putString(PASSWORD_KEY, edtPassword.getText().toString());
                editor.putString(NAME_KEY, edtName.getText().toString());
                editor.putString(PHONE_KEY, edtPhone.getText().toString());
                editor.commit();
                sayac++;
            }
        });

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

                                registerClicked=true;
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

                dialog.dismiss();


            }
        });

        dialog.show();
    }
}
