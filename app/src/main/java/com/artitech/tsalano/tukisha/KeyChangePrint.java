package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.printer.PrinterCommand;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rammala on 10-May-17.
 */

public class KeyChangePrint extends AppCompatActivity {

    private static final String CHINESE = "GBK";
    private TextView mToolbarTitleTextView;

    private TextView tokenOne;
    private TextView terminal;
    private TextView client;
    private TextView tokentwo;
    private TextView tokenthree;
    private TextView  newSgc;
    private TextView  address;
    private TextView  newKrn;
    private TextView  newTi;
    private TextView  oldSgc;
    private TextView  oldKrn;
    private TextView  oldTi;
    private TextView  maxPwrKw;
    private TextView operatorMessage;
    private TextView date;
    private TextView ItemAgentName;
    private TextView itemMeterNumber;
    private TextView itemFBEToken,itemFBEKwh, itemFBEAmount;
    private TextView item_Date;
    private TextView fbeTokenNumberLabel;
    private TextView headerNameLabel;
    private Toolbar toolbar;
    private Button goHome,sendSMSButton,printButton;
    private LinearLayout seventhview;
    private String TokenOne,Tokentwo,Tokenthree,NewSgc,NewKrn,NewTi,OldSgc,OldKrn,OldTi,MaxPwrKw,OperatorMessage,Address,Date,Client,Terminal,meterNumber,
            tokTech, alg,fbetoken, fbekwh, fbeamount, header;
    private Boolean isReprint = false;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_change_print);

        tukishaApplication = (TukishaApplication) getApplication();

        Bundle bundle = getIntent().getExtras();

        TokenOne = bundle.getString("tokenOne");
        OperatorMessage = bundle.getString("operatorMessage");
        Address = bundle.getString("address");
        Terminal = bundle.getString("terminal");
        OperatorMessage = bundle.getString("operatorMessage");
        Tokentwo = bundle.getString("tokenTwo");
        Tokenthree = bundle.getString("tokenThree");
        NewSgc = bundle.getString("newSgc");
        NewKrn = bundle.getString("newKrne");
        NewTi = bundle.getString("newTi");
        OldSgc = bundle.getString("oldSgc");
        OldKrn = bundle.getString("oldKrn");
        OldTi = bundle.getString("oldTi");
        MaxPwrKw = bundle.getString("maxPwrKw");
        Client = bundle.getString("client");
        Date = bundle.getString("date");
        meterNumber = bundle.getString("meterNumber");
        tokTech = bundle.getString("tokTech");
        alg = bundle.getString("alg");
        header = bundle.getString("header");

        if (header.contains("REPRINT"))
            isReprint = true;

        if (bundle.containsKey("fbetoken")) {
            if (bundle.getString("fbetoken").isEmpty())
                fbetoken = null;
            else {

                fbetoken = bundle.getString("fbetoken");
                fbekwh = bundle.getString("fbekwh");
                fbeamount = bundle.getString("fbeamount");
                //fbeheader = bundle.getString("header");
            }
        } else
            fbetoken = null;

        headerNameLabel = (TextView) findViewById(R.id.header_name);
        headerNameLabel.setText(header);

        tokenOne = (TextView)findViewById(R.id.tokenOne);
        tokenOne.setText(TokenOne);

        tokentwo = (TextView)findViewById(R.id.tokenTwo);
        tokentwo.setText(Tokentwo);

        tokenthree = (TextView)findViewById(R.id.tokenThree);
        tokenthree.setText(Tokenthree);

        operatorMessage = (TextView)findViewById(R.id.distributor);
        operatorMessage.setText(OperatorMessage);

        newSgc = (TextView)findViewById(R.id.sgc);
        newSgc.setText(NewSgc);

        newKrn = (TextView)findViewById(R.id.krn);
        newKrn.setText(NewKrn);

        newTi = (TextView)findViewById(R.id.ti);
        newTi.setText(NewTi);

        oldTi = (TextView)findViewById(R.id.alg);
        oldTi.setText(OldTi);

        oldSgc = (TextView)findViewById(R.id.tokenTech);
        oldSgc.setText(OldSgc);

        oldKrn = (TextView)findViewById(R.id.meterno);
        oldKrn.setText(OldKrn);

        maxPwrKw = (TextView)findViewById(R.id.maxpwrkw);
        maxPwrKw.setText(MaxPwrKw);

        address = (TextView)findViewById(R.id.address);
        address.setText(Address);

        date = (TextView)findViewById(R.id.date);
        date.setText(Date);

        client = (TextView)findViewById(R.id.clientID);
        client.setText(Client);


        terminal = (TextView)findViewById(R.id.terminalID);
        terminal.setText(Terminal);


        itemMeterNumber = (TextView)findViewById(R.id.receiptno);
        itemMeterNumber.setText(meterNumber);

        operatorMessage = (TextView)findViewById(R.id.distributor);
        operatorMessage.setText(OperatorMessage);



        ItemAgentName  = (TextView)findViewById(R.id.agentName);
        ItemAgentName.setText(tukishaApplication.getAgentID());

        item_Date = (TextView) findViewById(R.id.item_Date);

        if (isReprint)
            item_Date.setText("RePrint Date");





        //PurchaseToken with FBE
        if (fbetoken != null) {

            fbeTokenNumberLabel = (TextView) findViewById(R.id.fbeTokenNumberLabel);
            fbeTokenNumberLabel.setVisibility(View.VISIBLE);

            LinearLayout seventh_view = (LinearLayout) findViewById(R.id.seventh_view);
            seventh_view.setVisibility(View.VISIBLE);

            RelativeLayout seventh_icon_detail = (RelativeLayout) findViewById(R.id.seventh_icon_detail);
            seventh_icon_detail.setVisibility(View.VISIBLE);

            itemFBEToken = (TextView) findViewById(R.id.fbeTokenNumber);
            itemFBEToken.setText(fbetoken);
            itemFBEToken.setVisibility(View.VISIBLE);

            itemFBEKwh = (TextView) findViewById(R.id.fbeenergykwh);
            itemFBEKwh.setText(fbekwh + "Kwh");
            itemFBEKwh.setVisibility(View.VISIBLE);

            itemFBEAmount = (TextView) findViewById(R.id.fbetokenAmount);
            itemFBEAmount.setText(fbeamount);
            itemFBEAmount.setVisibility(View.VISIBLE);

            seventhview = (LinearLayout) findViewById(R.id.seventh_view);
            seventhview.setVisibility(View.VISIBLE);

        }

        goHome = (Button)findViewById(R.id.thankyouHomeButton);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KeyChangePrint.this, MainActivity.class);
                startActivity(i);
            }
        });

        sendSMSButton = (Button)findViewById(R.id.sendSMSButton);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(KeyChangePrint.this, SendSMSActivity.class);
                i.putExtra("tokenOne", TokenOne);
                i.putExtra("flag", "Electricity");

                //PurchaseToken with FBE
                if (fbetoken != null) {
                    i.putExtra("fbetoken", fbetoken);
                }

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
            mToolbarTitleTextView.setText(header);

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

        tukishaApplication.SendDataString(String.format("           %s          \n\n",header));

        tukishaApplication.SendDataString("Distributor                         VAT Number\n"); //48
        tukishaApplication.SendDataString(String.format("%s                        %s\n\n",tokenOne,tokenthree));

        tukishaApplication.SendDataString("Address\n");
        tukishaApplication.SendDataString(String.format("%s\n\n",address));

        tukishaApplication.SendDataString("Date\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        tukishaApplication.SendDataString(currentDateandTime +"\n\n");

        if (isReprint)
            tukishaApplication.SendDataString("Reprint Date                          Agent ID\n");
        else
            tukishaApplication.SendDataString("Date of Purchase                      Agent ID\n");

        tukishaApplication.SendDataString(String.format("%s                          %s\n\n",tokenOne,tukishaApplication.getAgentID()));

        tukishaApplication.SendDataString("Receipt No      ClientID           Terminal ID\n"); //48
        tukishaApplication.SendDataString(String.format("%s %s      %s\n\n",tokenOne,client,Terminal));

        tukishaApplication.SendDataString("Meter No        Token Tech         ALG\n"); //48
        tukishaApplication.SendDataString(String.format("%s     %s                 %s\n\n",meterNumber,tokTech,alg));

        tukishaApplication.SendDataString("SGC             KRN                TI\n"); //48
        tukishaApplication.SendDataString(String.format("%s          %s                  %s\n\n",tokenOne,tokenOne,tokenOne));

        tukishaApplication.SendDataString("Description     Energy Kwh         Amount\n"); //48
        tukishaApplication.SendDataString(String.format("%s     %sKwh             R%s\n\n",tokenthree,tokenOne,tokenthree));

        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text(String.format(" %s \n\n\n\n",TokenOne), CHINESE, 0, 1, 1, 0));

        //PurchaseToken with FBE
        if (fbetoken != null) {

            tukishaApplication.SendDataString(String.format("           %s          \n\n", "FREE BASIC ELECTRICITY"));

            tukishaApplication.SendDataString("Description     Energy Kwh         Amount\n"); //48
            tukishaApplication.SendDataString(String.format("%s     %sKwh             %s\n\n", "FBE", fbekwh, fbeamount));

            tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text(String.format(" %s \n\n\n\n", fbetoken), CHINESE, 0, 1, 1, 0));

        }

        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }


}
