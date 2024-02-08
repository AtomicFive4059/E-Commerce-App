package com.example.creativecart_app.LoginOptionActivity;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.RowImagePickedBinding;
import com.example.creativecart_app.models.ModelImagePicked;

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

    //Constructor @param Context of the Activity/Fragment where instance of the AdapterImagePicked class is created
    //@param imagePickedArrayList the list of Images picked/captured/ from Gallery/Camera or from internet
    public AdapterImagePicked(Context context, ArrayList<ModelImagePicked> imagePickedArrayList) {
        this.context = context;
        this.imagePickedArrayList = imagePickedArrayList;
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

        //get Uri of image to set in imageIv
        Uri imageUri=model.getImageUri();

        Log.d(TAG, "onBindViewHolder: ImageUri "+imageUri);

        //set the image in imageIv
        try {
            Glide.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.book1)
                    .into(holder.imageIv);
        }catch (Exception e){
            Log.e(TAG, "onBindViewHolder: ",e);
        }

        //Handle closeeBtn click, remove image from imagePickedArryList
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickedArrayList.remove(model);
                notifyItemRemoved(position);
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
