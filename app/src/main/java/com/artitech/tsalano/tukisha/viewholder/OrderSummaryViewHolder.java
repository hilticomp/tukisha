package com.artitech.tsalano.tukisha.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artitech.tsalano.tukisha.R;
import com.artitech.tsalano.tukisha.model.OrderSummaryModel;
import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 19-Apr-17.
 */

public class OrderSummaryViewHolder extends RecyclerView.ViewHolder {

    public CardView mCardListItem;
    public TextView mItemName;
    public TextView mItemShortDesc;
    public TextView mItemPrice;
    public ImageView mProductThumb;
    public LinearLayout mTopView;
    public TextView mTotal;
    public TextView mQuantity;

    public OrderSummaryViewHolder(View itemView) {
        super(itemView);

        mCardListItem = (CardView)itemView.findViewById(R.id.cardlist_item);
        mItemName = (TextView)itemView.findViewById(R.id.item_address);
        mItemShortDesc = (TextView)itemView.findViewById(R.id.item_short_desc);
        mItemPrice = (TextView)itemView.findViewById(R.id.item_price);
        mProductThumb = (ImageView)itemView.findViewById(R.id.product_thumb);
        mTopView = (LinearLayout) itemView.findViewById(R.id.top_view);
        mTotal = (TextView)itemView.findViewById(R.id.total);
        mQuantity = (TextView)itemView.findViewById(R.id.quantity);
    }


    public void bindtoCartTypes(Context context, OrderSummaryModel post,
                                View.OnClickListener relativeLayoutClickListener) {


        mItemName.setText(post.getName());
        mItemShortDesc.setText(post.getDescription());
        mItemPrice.setText(post.getRs());
        mQuantity.setText("Quantity :" + " " + post.getQuantity());

        String rupees = post.getRs().replace("Rs: ","");


        int d = Integer.parseInt(post.getQuantity()) * Integer.parseInt(rupees);
        mTotal.setText("Total:" + " " + String.valueOf(d));


        mTopView.setOnClickListener(relativeLayoutClickListener);
        Picasso.with(context).load(post.getImgurl()).fit().centerCrop().into(mProductThumb);

    }


}

