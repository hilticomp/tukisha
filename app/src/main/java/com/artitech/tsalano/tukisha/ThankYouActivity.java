package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.artitech.tsalano.tukisha.printer.PrinterCommand;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 10-May-17.
 */

public class ThankYouActivity extends AppCompatActivity {

    private TextView mToolbarTitleTextView, itemVoucher,itemDistributor,itemDate,itemEnergyKWh,itemAmount,
            itemClientID,itemTerminalID, itemVATNumber, itemMeterNumber,itemTokTech,itemALG,itemSGC,itemKRN,itemTI,
            itemDescription, itemAddress, itemReceipt;
    private Toolbar toolbar;
    private Button goHome,sendSMSButton,printButton;
    private String vouchernumber,distributor,date,energyKWh,amount,client,terminal,vatNumber,meterNumber,
            tokTech,alg,sgc,krn,ti,description,address,receipt,balance,header;
    private TukishaApplication tukishaApplication;

    private static final String CHINESE = "GBK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        tukishaApplication = (TukishaApplication) getApplication();

        Bundle bundle = getIntent().getExtras();
        vouchernumber = bundle.getString("vouchernumber");
        distributor = bundle.getString("distributor");
        date = bundle.getString("date");
        energyKWh = bundle.getString("energyKWh");
        amount = bundle.getString("amount");
        vatNumber = bundle.getString("vatNumber");
        meterNumber = bundle.getString("meterNumber");
        tokTech = bundle.getString("tokTech");
        alg = bundle.getString("alg");
        sgc = bundle.getString("sgc");
        krn = bundle.getString("krn");
        ti = bundle.getString("ti");
        client = bundle.getString("client");
        terminal = bundle.getString("terminal");
        description = bundle.getString("description");
        address = bundle.getString("address");
        receipt = bundle.getString("receipt");
        balance = bundle.getString("balance");
        header = bundle.getString("header");

        itemVoucher = (TextView)findViewById(R.id.tokenNumber);
        itemVoucher.setText(vouchernumber);

        itemVATNumber = (TextView)findViewById(R.id.vatnumber);
        itemVATNumber.setText(vatNumber);

        itemDistributor = (TextView)findViewById(R.id.distributor);
        itemDistributor.setText(distributor);

        itemDate = (TextView)findViewById(R.id.date);
        itemDate.setText(date);

        itemEnergyKWh = (TextView)findViewById(R.id.energykwh);
        itemEnergyKWh.setText(energyKWh + "KWh");

        itemAmount = (TextView)findViewById(R.id.tokenAmount);
        itemAmount.setText("R"+ amount);

        itemClientID = (TextView)findViewById(R.id.clientID);
        itemClientID.setText(client);

        itemTerminalID = (TextView)findViewById(R.id.terminalID);
        itemTerminalID.setText(terminal);

        itemMeterNumber = (TextView)findViewById(R.id.meterno);
        itemMeterNumber.setText(meterNumber);

        itemTokTech = (TextView)findViewById(R.id.tokenTech);
        itemTokTech.setText(tokTech);

        itemALG = (TextView)findViewById(R.id.alg);
        itemALG.setText(alg);

        itemSGC = (TextView)findViewById(R.id.sgc);
        itemSGC.setText(sgc);

        itemKRN = (TextView)findViewById(R.id.krn);
        itemKRN.setText(krn);

        itemTI = (TextView)findViewById(R.id.ti);
        itemTI.setText(ti);

        itemDescription = (TextView)findViewById(R.id.description);
        itemDescription.setText(description);

        itemAddress = (TextView)findViewById(R.id.address);
        itemAddress.setText(address);

        itemReceipt = (TextView)findViewById(R.id.receiptno);
        itemReceipt.setText(receipt);

        goHome = (Button)findViewById(R.id.thankyouHomeButton);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ThankYouActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        sendSMSButton = (Button)findViewById(R.id.sendSMSButton);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ThankYouActivity.this, SendSMSActivity.class);
                i.putExtra("vouchernumber", vouchernumber);
                i.putExtra("energyKWh", itemEnergyKWh.getText());
                i.putExtra("amount", itemAmount.getText());
                i.putExtra("flag", "Electricity");
                startActivity(i);
            }
        });

        printButton = (Button)findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrintVoucher();

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

            setSupportActionBar(mainToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }

    }

    private void PrintVoucher()
    {
        //tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text(String.format("  %s    \n\n",header), CHINESE, 0, 1, 1, 0));

        tukishaApplication.SendDataString(String.format("           %s          \n\n",header));

        //tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("Distributor                         VAT Number\n", CHINESE, 0, 0, 0, 0));


        tukishaApplication.SendDataString("Distributor                         VAT Number\n"); //48
        tukishaApplication.SendDataString(String.format("%s                        %s\n\n",distributor,vatNumber));

        tukishaApplication.SendDataString("Address\n");
        tukishaApplication.SendDataString(String.format("%s\n\n",address));

        tukishaApplication.SendDataString("Date\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        tukishaApplication.SendDataString(currentDateandTime +"\n\n");

        tukishaApplication.SendDataString("Receipt No      ClientID           Terminal ID\n"); //48
        tukishaApplication.SendDataString(String.format("%s %s      %s\n\n",receipt,client,terminal));

        tukishaApplication.SendDataString("Meter No        Token Tech         ALG\n"); //48
        tukishaApplication.SendDataString(String.format("%s     %s                 %s\n\n",meterNumber,tokTech,alg));

        tukishaApplication.SendDataString("SGC             KRN                TI\n"); //48
        tukishaApplication.SendDataString(String.format("%s          %s                  %s\n\n",sgc,krn,ti));

        tukishaApplication.SendDataString("Description     Energy Kwh         Amount\n"); //48
        tukishaApplication.SendDataString(String.format("%s     %sKwh             R%s\n\n",description,energyKWh,amount));

        //tukishaApplication.SendDataString(String.format("           %s          \n\n\n\n",vouchernumber));

        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text(String.format(" %s \n\n\n\n",vouchernumber), CHINESE, 0, 1, 1, 0));

        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }


}
