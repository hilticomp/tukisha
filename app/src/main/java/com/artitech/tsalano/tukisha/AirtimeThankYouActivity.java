package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tsheko on 10-May-17.
 */

public class AirtimeThankYouActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mToolbarTitleTextView, itemVoucher, itemOperator, itemDate, itemAmount, itemInstructions, itemAgent;
    private Toolbar toolbar;
    private Button goHome;
    private String vouchernumber,operator,date,amount,instructions,balance;
    private Button sendSMSButton,printButton;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_thankyou);

        tukishaApplication = (TukishaApplication) getApplication();

        Bundle bundle = getIntent().getExtras();
        vouchernumber = bundle.getString("vouchernumber");
        operator = bundle.getString("operator");
        date = bundle.getString("date");
        amount = bundle.getString("amount");
        instructions = bundle.getString("instructions");
        balance = bundle.getString("balance");

        itemVoucher = (TextView)findViewById(R.id.vouchernumber);
        itemVoucher.setText(vouchernumber);

        itemOperator = (TextView)findViewById(R.id.operator);
        itemOperator.setText("Operator : "+ operator);

        itemDate = (TextView)findViewById(R.id.date);
        itemDate.setText(date);

        itemAmount = (TextView)findViewById(R.id.amount);
        itemAmount.setText(amount);

        itemInstructions = (TextView)findViewById(R.id.instructions);
        itemInstructions.setText(instructions);

        itemAgent = (TextView)findViewById(R.id.agent);
        itemAgent.setText("Agent ID : "+ tukishaApplication.getAgentID());



        printButton = (Button)findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tukishaApplication.SendDataString(String.format("\n\n\n%s\n\n\n",vouchernumber));

                tukishaApplication.SendDataString(String.format("Operator:%s\n\n\n",operator));

                tukishaApplication.SendDataString(String.format("%s\n\n\n",amount));

                tukishaApplication.SendDataString(String.format("%s\n\n\n",instructions));

                tukishaApplication.SendDataString(String.format("Agent:%s\n\n\n",tukishaApplication.getAgentID()));

                tukishaApplication.SendDataString("Date\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                tukishaApplication.SendDataString(currentDateandTime +"\n\n\n\n");

            }
        });

        sendSMSButton = (Button)findViewById(R.id.sendSMSButton);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AirtimeThankYouActivity.this, SendSMSActivity.class);
                i.putExtra("vouchernumber", vouchernumber);
                i.putExtra("amount", itemAmount.getText());
                i.putExtra("flag", "Airtime");
                startActivity(i);

            }
        });

        goHome = (Button)findViewById(R.id.thankyouHomeButton);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AirtimeThankYouActivity.this, MainActivity.class);

                startActivity(i);

            }
        });


        if (!TextUtils.isEmpty(vouchernumber)) {
            if (vouchernumber.length() < 13) {
                sendSMSButton.setVisibility(View.GONE);
                printButton.setVisibility(View.GONE);
            }
        }



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Thank You");
        }

    }

    @Override
    public void onBackPressed()
    {

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mainToolbar != null) {
            mToolbarTitleTextView = (TextView) mainToolbar.findViewById(R.id.toolbar_title_textView);

            setSupportActionBar(mainToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}
