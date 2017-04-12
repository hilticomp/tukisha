package com.imbyou.pavan.mykart.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imbyou.pavan.mykart.R;
import com.imbyou.pavan.mykart.model.CategoriesModel;
import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 12-Apr-17.
 */

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    public CardView mInviteCardView;
    public TextView categoriesName_textView;
    public ImageView categories_imageView;
    public RelativeLayout mSingleCardRelativeLayout;

    public CategoriesViewHolder(View itemView) {
        super(itemView);

        mInviteCardView = (CardView)itemView.findViewById(R.id.invite_cardView);
        categoriesName_textView = (TextView)itemView.findViewById(R.id.categoriesName_textView);
        categories_imageView = (ImageView)itemView.findViewById(R.id.categories_imageView);
        mSingleCardRelativeLayout = (RelativeLayout)itemView.findViewById(R.id.singleCardRelativeLayout);
    }

    public void bindToWorkouts(Context context, CategoriesModel post,
                               View.OnClickListener relativeLayoutClickListener) {


        categoriesName_textView.setText(post.getName());
        mSingleCardRelativeLayout.setOnClickListener(relativeLayoutClickListener);
        Picasso.with(context).load(post.getImgurl()).fit().centerCrop().into(categories_imageView);

    }


}

