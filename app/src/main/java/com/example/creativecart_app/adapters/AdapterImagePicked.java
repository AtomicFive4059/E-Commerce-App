package com.example.creativecart_app.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.RowImagePickedBinding;
import com.example.creativecart_app.models.ModelImagePicked;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterImagePicked extends RecyclerView.Adapter<AdapterImagePicked.HolderImagePicked> {

    //View Binding
    private RowImagePickedBinding binding;

    //TAG show in logs in the Logcat
    private static final String TAG="IMAGES_TAG";

    //Context of the Activity/Fragment from where instance of the AdapterImagePicked class is created
    private Context context;

    //imagePickedArrayList the list of Images picked/captured/ from Gallery/Camera or from internet
    private ArrayList<ModelImagePicked> imagePickedArrayList;
    private String adsId;

    /*Constructor
    @param Context of the Activity/Fragment where instance of the AdapterImagePicked class is create
    @param imagePickedArrayList the list of Images picked/captured/ from Gallery/Camera or from internet
    @param adsId Id of the Ads
     */

    public AdapterImagePicked(Context context, ArrayList<ModelImagePicked> imagePickedArrayList,String adsId) {
        this.context = context;
        this.imagePickedArrayList = imagePickedArrayList;
        this.adsId=adsId;
    }

    @NonNull
    @Override
    public HolderImagePicked onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate and bind the row_image_picked.xml
        binding=RowImagePickedBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderImagePicked(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImagePicked holder, int position) {
        //get data from particular position of the list and set to UI View of row_image_picked.xml and clicks
        ModelImagePicked model=imagePickedArrayList.get(position);

        if (model.isFromInternet()){

            //Images is from internet/firebase DB. Get Uri of image to set in imageIv
            String imageUrl=model.getGetImageUrl();

            Log.d(TAG, "onBindViewHolder: ImageUrl "+imageUrl);

            //set the image in imageIv
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
                        .into(holder.imageIv);

            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: ",e);
            }

        }else {

            //imags is picked from gallery/camera. Get the images Uri of the images to set in imageIv
            Uri imageUri=model.getImageUri();

            Log.d(TAG, "onBindViewHolder: imageUrl "+imageUri);

            //set the image in imageIv
            try {

                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                Glide.with(context)
                        .load(imageUri)
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
                        .into(holder.imageIv);

            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: ",e);
            }
        }



        //Handle closeeBtn click, remove image from imagePickedArryList
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.isFromInternet()){
                    deleteImageFirebase(model,holder,position);
                }else {
                    imagePickedArrayList.remove(model);
                    notifyItemRemoved(position);
                }

            }
        });

    }

    private void deleteImageFirebase(ModelImagePicked model, HolderImagePicked holder, int position) {
        String imageId= model.getId();

        Log.d(TAG, "deleteImageFirebase: adsId "+adsId);
        Log.d(TAG, "deleteImageFirebase: imagesId "+imageId);

        //Ads-->AdsId-->Images-->ImageId
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Ads");
        reference.child(adsId).child("Images").child(imageId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Deleted successfully
                        Log.d(TAG, "onSuccess: Deleted");
                        Utils.toast(context,"Images Delete");

                        try {
                            //Remove from the imagePickedArrayList
                            imagePickedArrayList.remove(model);
                            notifyItemRemoved(position);
                        }catch (Exception e){
                            Log.e(TAG, "onSuccess: ",e);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Delete Failure
                        Log.e(TAG, "onFailure: ",e);
                        Utils.toast(context,"Failed To Delete Images Due To "+e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imagePickedArrayList.size();
    }

    //View Holder class to hold/init UI View of the row_image_picked.xml
    class HolderImagePicked extends RecyclerView.ViewHolder{

        //UI View of the row_image_picked.xml
        ImageView imageIv;
        ImageButton closeBtn;
        public HolderImagePicked(@NonNull View itemView) {
            super(itemView);

            //UI View of the row_image_picked.xml
            imageIv=binding.imageIv;
            closeBtn=binding.closeBtn;


        }
    }

}
