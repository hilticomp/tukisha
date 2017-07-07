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

import com.artitech.tsalano.tukisha.model.TransactionHistoryModel;

import java.util.ArrayList;

/**
 * Created by solly on 2017/06/29.
 */


public class ListAdapter extends ArrayAdapter<TransactionHistoryModel> {

    int vg;

    ArrayList<TransactionHistoryModel> list;

    Context context;

    private AssetManager assetManager;

    public ListAdapter(Context context, int vg, int id, ArrayList<TransactionHistoryModel> list) {

        super(context, vg, id, list);

        this.context = context;

        this.vg = vg;

        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        TextView item_VoucherNumber = (TextView) itemView.findViewById(R.id.item_VoucherNumber);

        TextView item_DateProcessed = (TextView) itemView.findViewById(R.id.item_DateProcessed);

        TextView item_Amount = (TextView) itemView.findViewById(R.id.item_Amount);

        TextView item_ProductType = (TextView) itemView.findViewById(R.id.item_ProductType);

        ImageView thumbnail = (ImageView) itemView.findViewById(R.id.list_image);

        try {

            item_VoucherNumber.setText(list.get(position).getVoucherNumber());

            item_DateProcessed.setText(list.get(position).getDateProcessed());

            item_Amount.setText(list.get(position).getAmount());

            item_ProductType.setText(list.get(position).getProductType());

            int drawableResourceId = getContext().getResources().getIdentifier("default_icon_launcher", "drawable", getContext().getPackageName());

            switch (list.get(position).getProvider()) {
                case "Vodacom":
                    drawableResourceId = getContext().getResources().getIdentifier("vodacom_logo", "drawable", getContext().getPackageName());
                    break;
                case "MTN":
                    drawableResourceId = getContext().getResources().getIdentifier("mtn_logo", "drawable", getContext().getPackageName());
                    break;
                case "CellC":
                    drawableResourceId = getContext().getResources().getIdentifier("cellc_logo", "drawable", getContext().getPackageName());
                    break;
                case "Virgin Mobile":
                    drawableResourceId = getContext().getResources().getIdentifier("virginmobile_logo", "drawable", getContext().getPackageName());
                    break;
                case "Telkom Mobile":
                    drawableResourceId = getContext().getResources().getIdentifier("telkom_logo", "drawable", getContext().getPackageName());
                    break;
                case "Neotel":
                    drawableResourceId = getContext().getResources().getIdentifier("neotel_logo", "drawable", getContext().getPackageName());
                    break;
                case "Other":
                    drawableResourceId = getContext().getResources().getIdentifier("default_icon_launcher", "drawable", getContext().getPackageName());
                    break;
            }

            thumbnail.setImageResource(drawableResourceId);


        } catch (Exception e) {

            Log.d("ERROR", e.toString());

        }

        return itemView;

    }


}