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
 * Created by Tsheko on 10-May-17.
 */

public class ThankYouActivityDstv extends AppCompatActivity {

    private static final String CHINESE = "GBK";
    private TextView mToolbarTitleTextView;



    private TextView itemVoucher;
    private TextView itemDistributor;
    private TextView itemDate;
    private TextView ItemAgentName;
    private TextView itemPurchaseDate;
    private TextView itemEnergyKWh;
    private TextView itemAmount;
    private TextView itemClientID;
    private TextView itemTerminalID;
    private TextView itemVATNumber;
    private TextView itemMeterNumber;
    private TextView itemTokTech;
    private TextView itemALG;
    private TextView itemSGC;
    private TextView itemKRN;
    private TextView itemTI;
    private TextView itemDescription;
    private TextView itemAddress;
    private TextView itemReceipt;
    private TextView itemFBEToken,itemFBEKwh, itemFBEAmount,item_ReceiptNumber,item_TransactionNumber;
    private TextView item_Date;
    private TextView fbeTokenNumberLabel;
    private TextView headerNameLabel;
    private Toolbar toolbar;
    private Button goHome,sendSMSButton,printButton;
    private LinearLayout seventhview;
    private String vouchernumber,distributor,date,energyKWh,amount,amountd,client,terminal,vatNumber,meterNumber,
            tokTech, alg, sgc, krn, ti, description, address, receipt, fbetoken, fbekwh, fbeamount, header,headingN, dateOfPurchase;


    TextView item_accountnumber,item_initials,item_Surname,item_customernumber,item_amountdue,item_cellNumber;
    private String customerid, accountnumber,customerinitials,customersurname,amountdue,cellNumber,ReceiptNumber,TransactionNumber,amoudPaid;


    private Boolean isReprint = false;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_dstv);
        tukishaApplication = (TukishaApplication) getApplication();

        Bundle bundle = getIntent().getExtras();




        vouchernumber = bundle.getString("vouchernumber");
        headingN=bundle.getString(headingN);
        distributor = bundle.getString("distributor");
        date = bundle.getString("date");
        dateOfPurchase = bundle.getString("dateOfPurchase");
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
        amountd = bundle.getString("amountd");
        terminal = bundle.getString("terminal");
        description = bundle.getString("description");
        address = bundle.getString("address");
        receipt = bundle.getString("receipt");




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


        itemVoucher = (TextView)findViewById(R.id.tokenNumber);
        itemVoucher.setText(vouchernumber);

        itemVATNumber = (TextView)findViewById(R.id.vatnumber);
        itemVATNumber.setText(vatNumber);

        itemDistributor = (TextView)findViewById(R.id.distributor);
        itemDistributor.setText(distributor);




        itemDate = (TextView)findViewById(R.id.date);
        itemDate.setText(date);

        ItemAgentName  = (TextView)findViewById(R.id.agentName);
        ItemAgentName.setText(tukishaApplication.getAgentID());

        item_Date = (TextView) findViewById(R.id.item_Date);

        if (isReprint)
            item_Date.setText("RePrint Date");

        itemPurchaseDate = (TextView) findViewById(R.id.purchaseDate);
        itemPurchaseDate.setText(dateOfPurchase);

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
        itemSGC.setText(tukishaApplication.getAgentID());

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
                Intent i = new Intent(ThankYouActivityDstv.this, MainActivity.class);
                startActivity(i);
            }
        });

        sendSMSButton = (Button)findViewById(R.id.sendSMSButton);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ThankYouActivityDstv.this, SendSMSActivity.class);
                i.putExtra("vouchernumber", vouchernumber);
                i.putExtra("energyKWh", itemEnergyKWh.getText());
                i.putExtra("amount", itemAmount.getText());
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


        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("TUKISHA PREPAID (PTY)LTD\n      MULTICHOICE\n\n", CHINESE, 0, 1, 1, 0));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());


        tukishaApplication.SendDataString("Re-Print Date             Payment Date\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        tukishaApplication.SendDataString(currentDateandTime + String.format("       %s\n\n",dateOfPurchase));

        tukishaApplication.SendDataString("Receipt No                Transaction Number \n");
        tukishaApplication.SendDataString(String.format("%s                    %s\n\n",receipt,description));

        if (isReprint)
            tukishaApplication.SendDataString("Reprint Date                          Agent ID\n");
        else
            tukishaApplication.SendDataString("Customer Name             Initials\n");

        tukishaApplication.SendDataString(String.format("%s                   %s\n\n",distributor,address));

        tukishaApplication.SendDataString("Amount Due                Amount Paid\n"); //48
        tukishaApplication.SendDataString(String.format("%s                       %s\n\n",krn,vatNumber));

        tukishaApplication.SendDataString("Agent ID       \n"); //48
        tukishaApplication.SendDataString(String.format("%s     \n\n",tukishaApplication.getAgentID(),tokTech,alg));

        tukishaApplication.SendDataString("\n"); //48
        tukishaApplication.SendDataString(String.format("\n\n",description,description,description));

        tukishaApplication.SendDataString("\n"); //48
        tukishaApplication.SendDataString(String.format("\n\n",description,energyKWh,amount));


        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }



}
