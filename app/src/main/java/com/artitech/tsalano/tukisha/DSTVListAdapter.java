package com.artitech.tsalano.tukisha;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.model.DSTVTransactionHistoryModel;

import java.util.ArrayList;

/**
 * Created by solly on 2017/06/29.
 */


public class DSTVListAdapter extends ArrayAdapter<DSTVTransactionHistoryModel> {

    int vg;

    ArrayList<DSTVTransactionHistoryModel> list;

    Context context;

    private AssetManager assetManager;

    public DSTVListAdapter(Context context, int vg, int id, ArrayList<DSTVTransactionHistoryModel> list) {

        super(context, vg, id, list);

        this.context = context;

        this.vg = vg;

        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView item_ReceiptNumber = (TextView) itemView.findViewById(R.id.item_ReceiptNumber);

        TextView item_PaymentDate = (TextView) itemView.findViewById(R.id.item_PaymentDate);

        TextView item_FirstName = (TextView) itemView.findViewById(R.id.item_FirstName);

        TextView item_LastName = (TextView) itemView.findViewById(R.id.item_LastName);

        TextView item_AmountDue = (TextView) itemView.findViewById(R.id.item_AmountDue);

        TextView item_AmountPaid = (TextView) itemView.findViewById(R.id.item_AmountPaid);

        ImageView thumbnail = (ImageView) itemView.findViewById(R.id.list_image);

        try {

            item_ReceiptNumber.setText("Receipt Number: " + list.get(position).getReceiptNumber());

            item_PaymentDate.setText("Date: " + list.get(position).getDatetime());

            item_FirstName.setText(list.get(position).getFirstName());

            item_LastName.setText(list.get(position).getSurname());

            item_AmountDue.setText("Amount Due: R " + list.get(position).getAmountDue());

            item_AmountPaid.setText("Amount Paid: R " + list.get(position).getAmount());

            int drawableResourceId = getContext().getResources().getIdentifier("dstv", "drawable", getContext().getPackageName());

            thumbnail.setImageResource(drawableResourceId);


        } catch (Exception e) {

            Log.d("ERROR", e.toString());

        }

        return itemView;

    }


}