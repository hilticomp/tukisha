package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.printer.PrinterCommand;

/**
 * Created by Tsheko on 10-May-17.
 */

public class ThankYouMuniEkurhuleniActivity extends AppCompatActivity {

    private static final String CHINESE = "GBK";
    private TextView mToolbarTitleTextView;
    private TextView itemVoucher;
    private TextView itemDistributor;
    private TextView itemDate;
    private TextView itemEnergyKWh;
    private TextView itemAmount;
    private TextView itemMeterNumber;
    private TextView itemSGC;
    private TextView itemKRN;
    private TextView itemTI;
    private TextView itemDescription;
    private TextView itemExcise;
    private TextView itemReceipt;
    private TextView item_Date;
    private TextView headerNameLabel;
    private Toolbar toolbar;
    private Button goHome,sendSMSButton,printButton;
    private String vouchernumber;
    private String distributor;
    private String date;
    private String energyKWh;
    private String amount;
    private String receiptNumber;
    private String actualReceipt;
    private String meterNumber;
    private String excise;
    private String sgc;
    private String krn;
    private String ti;
    private String description;
    private String header;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekurhuleni_thankyou);

        tukishaApplication = (TukishaApplication) getApplication();

        Bundle bundle = getIntent().getExtras();
        vouchernumber = bundle.getString("vouchernumber");
        distributor = bundle.getString("distributor");
        date = bundle.getString("date");
        energyKWh = bundle.getString("energyKWh");
        amount = bundle.getString("amount");
        meterNumber = bundle.getString("meterNumber");
        sgc = bundle.getString("sgc");
        krn = bundle.getString("krn");
        ti = bundle.getString("ti");
        receiptNumber = bundle.getString("receiptNumber");
        description = bundle.getString("description");
        header = bundle.getString("header");
        excise = bundle.getString("excise");
        actualReceipt = bundle.getString("actualReceipt");

        headerNameLabel = (TextView) findViewById(R.id.header_name);
        headerNameLabel.setText(header);

        itemVoucher = (TextView)findViewById(R.id.tokenNumber);
        itemVoucher.setText(vouchernumber);

        itemDistributor = (TextView)findViewById(R.id.distributor);
        itemDistributor.setText(distributor);

        itemDate = (TextView)findViewById(R.id.date);
        itemDate.setText(date);

        itemEnergyKWh = (TextView)findViewById(R.id.energykwh);
        itemEnergyKWh.setText(energyKWh +"KWh");

        itemAmount = (TextView)findViewById(R.id.tokenAmount);
        itemAmount.setText("R"+ amount);

        itemMeterNumber = (TextView)findViewById(R.id.meterno);
        itemMeterNumber.setText(meterNumber);

        itemSGC = (TextView)findViewById(R.id.sgc);
        itemSGC.setText(sgc);

        itemKRN = (TextView)findViewById(R.id.krn);
        itemKRN.setText(krn);

        itemTI = (TextView)findViewById(R.id.ti);
        itemTI.setText(ti);

        itemDescription = (TextView)findViewById(R.id.description);
        itemDescription.setText(description);

        itemExcise = (TextView)findViewById(R.id.excise);
        itemExcise.setText(excise);

        itemReceipt = (TextView)findViewById(R.id.receiptno);
        itemReceipt.setText(receiptNumber);

        item_Date = (TextView)findViewById(R.id.date);
        item_Date.setText(date);


        goHome = (Button)findViewById(R.id.thankyouHomeButton);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ThankYouMuniEkurhuleniActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        sendSMSButton = (Button)findViewById(R.id.sendSMSButton);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ThankYouMuniEkurhuleniActivity.this, SendSMSActivity.class);
                i.putExtra("vouchernumber", vouchernumber);
                i.putExtra("energyKWh", energyKWh);
                i.putExtra("amount", amount);
                i.putExtra("flag", "Electricity");

                startActivity(i);
            }
        });

        printButton = (Button)findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrintVoucher(actualReceipt);

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureToolbar();

        if (mToolbarTitleTextView != null) {
            //Setting name for toolbar
            mToolbarTitleTextView.setText("Thank You");
        }

    }

    private void configureToolbar() {
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mainToolbar != null) {
            mToolbarTitleTextView = (TextView) mainToolbar.findViewById(R.id.toolbar_title_textView);
            mToolbarTitleTextView.setText(header);

            setSupportActionBar(mainToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

    }

    private void PrintVoucher(String receipt)
    {

        tukishaApplication.SendDataString(receipt);

        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }


}
