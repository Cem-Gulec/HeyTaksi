package com.example.cem.cardsimulator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cem.cardsimulator.Bottom_Navigation.Bottom_Search_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class final_payment extends AppCompatActivity {

    float tutar_bilgisi,yedek_bilgi;

    private String TUTAR_KEY="com.example.burak.erisim.TUTAR";
    private String MAIN_KEY="com.example.burak.erisim.MAIN_DATA";

    TextView tutar,toplam_tutar,promosyon,odeme_tamam;
    Button btn1,btn2,btn3,btn_ekle,btn_iptal,btn_onay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_payment);

        tutar = (TextView)findViewById(R.id.txt_tutar);
        toplam_tutar = (TextView)findViewById(R.id.txt_ToplamTutar);
        promosyon = (TextView)findViewById(R.id.txt_promosyon);
        odeme_tamam = (TextView)findViewById(R.id.txt_odemetamam);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn_ekle = (Button)findViewById(R.id.btn_Ekle);
        btn_iptal = (Button)findViewById(R.id.btn_iptal);
        btn_onay = (Button)findViewById(R.id.btn_onay);

        tutar_bilgisi = getSharedPreferences(MAIN_KEY, MODE_PRIVATE).getFloat(TUTAR_KEY, -1);
        tutar.setText(String.valueOf(tutar_bilgisi)+" TL");
        toplam_tutar.setText(String.valueOf(tutar_bilgisi)+" TL");

        btn1.setBackgroundColor(R.drawable.border);
        btn2.setBackgroundColor(R.drawable.border);
        btn3.setBackgroundColor(R.drawable.border);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setBackgroundColor(Color.YELLOW);
                btn2.setBackgroundColor(R.drawable.border);
                btn3.setBackgroundColor(R.drawable.border);
                toplam_tutar.setText(String.valueOf(tutar_bilgisi)+" TL");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yedek_bilgi = tutar_bilgisi + (tutar_bilgisi*10/100);
                btn2.setBackgroundColor(Color.YELLOW);
                btn1.setBackgroundColor(R.drawable.border);
                btn3.setBackgroundColor(R.drawable.border);
                toplam_tutar.setText(String.valueOf(yedek_bilgi)+" TL");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yedek_bilgi = tutar_bilgisi + (tutar_bilgisi*15/100);
                btn3.setBackgroundColor(Color.YELLOW);
                btn1.setBackgroundColor(R.drawable.border);
                btn2.setBackgroundColor(R.drawable.border);
                toplam_tutar.setText(String.valueOf(yedek_bilgi)+" TL");
            }
        });

        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(final_payment.this);
                    dialog.setTitle("PROMOSYON KOD EKRANI");
                    dialog.setMessage("Promosyon kodunuzu giriniz");

                    LayoutInflater inflater = LayoutInflater.from(final_payment.this);
                    View promosyon_layout= inflater.inflate(R.layout.promosyon,null);

                    final MaterialEditText edtPromosyon = promosyon_layout.findViewById(R.id.et1);

                    final String promosyon_text = edtPromosyon.getText().toString().trim();

                    dialog.setView(promosyon_layout);

                    //set button
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

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
        });

        btn_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(final_payment.this, Bottom_Search_Activity.class));
            }
        });

        btn_onay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final AlertDialog.Builder dialog = new AlertDialog.Builder(final_payment.this);
                LayoutInflater inflater = LayoutInflater.from(final_payment.this);
                final View odemetamam_layout= inflater.inflate(R.layout.odeme_tamam,null);

                dialog.setView(odemetamam_layout);


                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(final_payment.this,MapsActivity.class));
                            }
                        }, 2000);
                    }
                });

                    dialog.show();


            }
        });
    }
}
