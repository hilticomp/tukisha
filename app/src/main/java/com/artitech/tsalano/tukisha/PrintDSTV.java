




package com.artitech.tsalano.tukisha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.printer.PrinterCommand;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tsheko on 10-May-17.
 */

public class PrintDSTV extends AppCompatActivity {

    private static final String CHINESE = "GBK";
    private TextView mToolbarTitleTextView;




    private Toolbar toolbar;
    private Button goHome,sendSMSButton,printButton;



    TextView item_accountnumber,headerNameLabel,item_initials,item_Surname,item_customernumber,item_amountdue,itemPurchaseDate,itemAmountPaid,itemreceiptNumber,itemtransactionNumber;
    private String customerid, accountnumber,customerinitials,customersurname,amountdue,dateOfPurchase,amountPaid,receiptNumber,transactionNumber,header;


    private Boolean isReprint = false;
    private TukishaApplication tukishaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_dstv);
        tukishaApplication = (TukishaApplication) getApplication();


        Bundle bundle = getIntent().getExtras();

        customerinitials = bundle.getString("customerinitials");
        customersurname = bundle.getString("customersurname");
        receiptNumber = bundle.getString("receiptNumber");
        transactionNumber=bundle.getString("transactionNumber");
        dateOfPurchase=bundle.getString("paymentDate");
        amountdue = bundle.getString("amountdue");
        amountPaid = bundle.getString("amount");


        item_initials = (TextView) findViewById(R.id.item_initials);
        itemPurchaseDate = (TextView) findViewById(R.id.purchaseDate);
        itemreceiptNumber = (TextView) findViewById(R.id.item_receiptNumber);
        itemtransactionNumber=(TextView)findViewById(R.id.item_transaction);
        item_Surname = (TextView) findViewById(R.id.item_Surname);
        item_amountdue = (TextView) findViewById(R.id.item_amountdue);
        itemAmountPaid = (TextView) findViewById(R.id.amountpaid);


        itemreceiptNumber.setText(receiptNumber);
        itemPurchaseDate.setText(dateOfPurchase);
        item_amountdue.setText(amountdue);
        item_initials.setText(customerinitials);
        item_Surname.setText(customersurname);
        itemtransactionNumber.setText(transactionNumber);
        itemAmountPaid.setText(amountPaid);


        //PurchaseToken with FBE

        goHome = (Button)findViewById(R.id.gohome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PrintDSTV.this, MainActivity.class);
                startActivity(i);
            }
        });



        printButton = (Button)findViewById(R.id.PrintSlip);
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

    }

    private void PrintVoucher()
    {

        tukishaApplication.SendDataByte(PrinterCommand.POS_Print_Text("TUKISHA PREPAID (PTY)LTD\n      MULTICHOICE\n\n", CHINESE, 0, 1, 1, 0));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());

        tukishaApplication.SendDataString("Payment Date\n"); //48
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        tukishaApplication.SendDataString(currentDateandTime +"\n\n");

        tukishaApplication.SendDataString("Receipt No                Transaction Number \n");
        tukishaApplication.SendDataString(String.format("%s                    %s\n\n",receiptNumber,transactionNumber));

        if (isReprint)
            tukishaApplication.SendDataString("Reprint Date                          Agent ID\n");
        else
            tukishaApplication.SendDataString("Customer Name             Initials\n");

        tukishaApplication.SendDataString(String.format("%s                   %s\n\n",customersurname,customerinitials));

        tukishaApplication.SendDataString("Amount Due                Amount Paid\n"); //48
        tukishaApplication.SendDataString(String.format("%s                       %s\n\n",amountdue,amountPaid));

        tukishaApplication.SendDataString("Agent ID       \n"); //48
        tukishaApplication.SendDataString(String.format("%s     \n\n",tukishaApplication.getAgentID(),tukishaApplication.getAgentID(),tukishaApplication.getAgentID()));

        tukishaApplication.SendDataString("             \n"); //48
        tukishaApplication.SendDataString(String.format(" \n\n",transactionNumber));

        tukishaApplication.SendDataString("\n"); //48
        tukishaApplication.SendDataString(String.format("\n\n",customerinitials,customerid,accountnumber));


        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_Cut(1));
        tukishaApplication.SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }

    
}