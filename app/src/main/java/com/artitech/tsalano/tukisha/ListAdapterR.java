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

import com.artitech.tsalano.tukisha.model.VoucherModel;

import java.util.ArrayList;

/**
 * Created by solly on 2017/06/29.
 */


public class ListAdapterR extends ArrayAdapter<VoucherModel> {

    int vg;

    ArrayList<VoucherModel> list;

    Context context;

    private AssetManager assetManager;

    public ListAdapterR(Context context, int vg, int id, ArrayList<VoucherModel> list) {

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

            item_VoucherNumber.setText(list.get(position).getTokenNumber());

            item_DateProcessed.setText(list.get(position).getDatePurchased());

            item_Amount.setText(list.get(position).getAmount());

            item_ProductType.setText(list.get(position).getDescription());

            int drawableResourceId = getContext().getResources().getIdentifier("eskom", "drawable", getContext().getPackageName());

            thumbnail.setImageResource(drawableResourceId);


        } catch (Exception e) {

            Log.d("ERROR", e.toString());

        }

        return itemView;

    }


}