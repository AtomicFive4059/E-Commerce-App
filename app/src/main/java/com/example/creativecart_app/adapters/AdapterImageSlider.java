package com.example.creativecart_app.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.RowImageSliderBinding;
import com.example.creativecart_app.models.ModelImageSlider;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterImageSlider extends RecyclerView.Adapter<AdapterImageSlider.HolderImageSlider> {

    //View Binding
    private RowImageSliderBinding binding;

    //TAG for log in the logcat
    private static final String TAG = "IMAGE_SLIDER_TAG";

    //Context of Activity/Fragment from where instance of the Adapter class is created
    private Context context;

    //imageSliderArrayList the list of Images
    private ArrayList<ModelImageSlider> imageSliderArrayList;


    /*Constructor
    param1: Context of Activity/Fragment from where instance of the Adapter class is created
    param2: imageSliderArrayList The list of the images
     */
    public AdapterImageSlider(Context context, ArrayList<ModelImageSlider> imageSliderArrayList) {
        this.context = context;
        this.imageSliderArrayList = imageSliderArrayList;
    }

    @NonNull
    @Override
    public HolderImageSlider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate/bind row_image_slider.xml
        binding = RowImageSliderBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderImageSlider(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImageSlider holder, int position) {

        //get data from particular position of list and  set to the UI View of row_image_slider.xml and Handle clicks
        ModelImageSlider modelImageSlider = imageSliderArrayList.get(position);

        //get Url of images
        String imageUrl = modelImageSlider.getImageUrl();

        String imageCount = (position + 1) +"/" +imageSliderArrayList.size();

        //set image Count
        holder.imageCountTv.setText(imageCount);

        //set images
        try {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GlideError", "Load failed", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(binding.imageIv);




//          Glide
//                  .with(context).load(imageUrl)
//                  .placeholder(R.drawable.baseline_downloading_24)
//                  .into(binding.imageIv);

        }catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ",e);
        }

        //Handle images click, open in full screen e.g ImageViewActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        //return the size of the imageSliderArrayList
        return imageSliderArrayList.size();
    }

    class HolderImageSlider extends RecyclerView.ViewHolder{

        //UI of row_image_slider.xml
        ShapeableImageView imageIv;
        TextView imageCountTv;

        public HolderImageSlider(@NonNull View itemView) {
            super(itemView);

            //init UI of row_image_slider.xml
            imageIv=binding.imageIv;
            imageCountTv=binding.imageCountTv;

        }
    }
}
