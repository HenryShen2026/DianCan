package com.example.diancan.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.R;

import java.util.List;
import java.util.Map;

public class MenuFoodAdapter extends RecyclerView.Adapter<MenuFoodAdapter.ViewHolder> {

    private final Context mainContext;
    private final List<Map<String, Object>> mainFoodList;

    public MenuFoodAdapter(Context context, List<Map<String, Object>> itemList){
        mainContext = context;
        mainFoodList = itemList;
    }

    @NonNull
    @Override
    public MenuFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainContext).inflate(R.layout.rc_item_menufood_layout, parent, false);
        ViewHolder viewHolder = new MenuFoodAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuFoodAdapter.ViewHolder holder, int position) {
        Map<String, Object> data = mainFoodList.get(position);
        int index = position;

        String food_name = data.get("_text").toString();
        Bitmap picId =(Bitmap) data.get("_pic");

        holder.foodName.setText(food_name);
        holder.foodImage.setImageBitmap(picId);

    }

    @Override
    public int getItemCount() {
        return mainFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView foodImage;
        private final TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = (ImageView) itemView.findViewById(R.id.menufood_imageView_pic);
            foodName = (TextView) itemView.findViewById(R.id.menufood_TextView_name);
        }
    }
}
