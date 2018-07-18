package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Tsheko on 01-June-17.
 */

public class TransactionMainActivity extends AppCompatActivity {

    private Button eskomButton,telcoButton, muniButton,Gohome,DSTVbutton;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_main);

        tukishaApplication = (TukishaApplication) getApplication();

        eskomButton = (Button) findViewById(R.id.eskomButton);
        eskomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TransactionMainActivity.this, TransactionHistoryActivityEskom.class);
                startActivity(i);
            }
        });

        telcoButton = (Button) findViewById(R.id.telcoButton);
        telcoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TransactionMainActivity.this, TelcoTransactionHistoryActivity.class);
                startActivity(i);
            }
        });


        muniButton = (Button) findViewById(R.id.municipalityButton);
        muniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TransactionMainActivity.this, MunicipalityTransactionHistoryActivity.class);
                startActivity(i);
            }
        });

       DSTVbutton = (Button) findViewById(R.id.DSTVButton);
       DSTVbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TransactionMainActivity.this, DstvTransactionHistory.class);
                startActivity(i);
            }
        });

        Gohome = (Button) findViewById(R.id.gohome);
        Gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(TransactionMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

