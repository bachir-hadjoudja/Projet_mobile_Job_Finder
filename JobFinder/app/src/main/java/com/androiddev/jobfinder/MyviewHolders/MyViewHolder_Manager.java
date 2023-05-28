package com.androiddev.jobfinder.MyviewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.androiddev.jobfinder.R;

public class MyViewHolder_Manager extends RecyclerView.ViewHolder {

    public ImageView iv_productImage;
    public TextView name_tv,  subject_tv, price_tv, applies_tv, employes_tv;

    public ImageView delete_btn, edit_btn;

    public MyViewHolder_Manager(View view) {
        super(view);


        iv_productImage = itemView.findViewById(R.id.card_iv);
        name_tv = itemView.findViewById(R.id.name_tv);
        subject_tv = itemView.findViewById(R.id.subject_tv);
        price_tv = itemView.findViewById(R.id.price_tv);
        applies_tv = itemView.findViewById(R.id.applies_tv);
        employes_tv = itemView.findViewById(R.id.employes_tv);

        delete_btn = itemView.findViewById(R.id.delete_btn);
        edit_btn = itemView.findViewById(R.id.edit_btn);

    }
}
