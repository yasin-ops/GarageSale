package com.muhammadyaseen.classifiedapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements Filterable {
private Context mContext;
private List<Upload> mUploads;
    private List<Upload> listFull;


public ImageAdapter(Context context, List<Upload> uploads)
        {
        mContext=context;
        mUploads=uploads;
        listFull=new ArrayList<>(uploads);
        }

@NonNull
@Override
public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.garage_home_sale_items, viewGroup,false);
        return  new ImageViewHolder(v);

        }

@Override
public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        Upload uploadCur=mUploads.get(i);
        imageViewHolder.home_tittle.setText(uploadCur.getImgName());
        imageViewHolder.home_price.setText(uploadCur.getImgPrice());
        imageViewHolder.home_description.setText(uploadCur.getImgDescription());
        imageViewHolder.home_number.setText(uploadCur.getImg_number());
        imageViewHolder.home_city.setText(uploadCur.getImg_City());
        Picasso.get()
        .load(uploadCur.getImgUrl())
        .placeholder(R.drawable.imagepreview)
        .fit()
        .centerCrop()
        .into(imageViewHolder.home_post_img);



        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }
    private Filter FilterUser=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String searchText=charSequence.toString().toLowerCase();
            List<Upload>temList=new ArrayList<>();
            if(searchText.length()==0|| searchText.isEmpty()){
                temList.addAll(listFull);


            }
            else {
                for (Upload upload:listFull){
                    if(upload.getImgName().toLowerCase().contains(searchText)){
                        temList.add(upload);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
              mUploads.clear();
              mUploads.addAll((Collection<? extends Upload>) filterResults.values);
              notifyDataSetChanged();
        }
    };

    public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView home_description,home_tittle,home_price,home_number,home_country,home_city;
    public ImageView home_post_img;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        home_description=itemView.findViewById(R.id.home_description);
        home_tittle=itemView.findViewById(R.id.home_tittle);
        home_price=itemView.findViewById(R.id.home_price);
        home_number=itemView.findViewById(R.id.home_number);
        home_country=itemView.findViewById(R.id.home_country);
        home_city=itemView.findViewById(R.id.home_city);
        home_post_img=itemView.findViewById(R.id.home_post_img);


    }
      }


 }
