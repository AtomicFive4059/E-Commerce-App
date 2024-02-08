package com.example.creativecart_app.LoginOptionActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creativecart_app.R;
import com.example.creativecart_app.models.ModelAds;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;

public class myAdapter extends FirebaseRecyclerAdapter<ModelAds,myAdapter.myViewHolder> {


     /* Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
      {@link FirebaseRecyclerOptions} for configuration options.
      @param options& */

    public myAdapter(@NonNull FirebaseRecyclerOptions<ModelAds> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ModelAds model) {
       holder.titleTv.setText(model.getTitle());
       holder.descriptionTv.setText(model.getDescription());
       holder.addressTv.setText(model.getAddress());
       holder.conditonTv.setText(model.getCondition());
       holder.priceTv.setText(model.getPrice());
     holder.dateTv.setText(Utils.formatTimestampDate(Utils.getTimestap()));

        Glide.with(holder.imageIv.getContext()).load(model.getpUrl()).into(holder.imageIv);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView imageIv;
        TextView titleTv,descriptionTv,addressTv,conditonTv,priceTv,dateTv;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageIv=itemView.findViewById(R.id.imageIv);
            titleTv=itemView.findViewById(R.id.titleTv);
            descriptionTv=itemView.findViewById(R.id.descriptionTv);
            addressTv=itemView.findViewById(R.id.addressTv);
            conditonTv=itemView.findViewById(R.id.conditonTv);
            priceTv=itemView.findViewById(R.id.priceTv);
            dateTv=itemView.findViewById(R.id.dateTv);
        }
    }
}
