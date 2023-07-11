package com.example.diancan.activity;

import static com.example.diancan.comm.Funcs.BitmapToString;
import static com.example.diancan.comm.Funcs.addFragment;
import static com.example.diancan.comm.Funcs.hideFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.fragment.SProdModFragment;
import com.example.diancan.fragment.SupplierFragment;

import java.util.List;
import java.util.Map;

public class SFoodAdapter extends RecyclerView.Adapter<SFoodAdapter.ViewHolder> {
    private final Context mainContext;
    private final List<Map<String, Object>> mainFoodList;

    public SFoodAdapter(Context context, List<Map<String, Object>> foodList) {
        mainContext = context;
        mainFoodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainContext).inflate(R.layout.rc_sprod_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> data = mainFoodList.get(position);
        int index = position;

        String food_name = data.get("_text").toString();
        int price = (int) data.get("_price");
        Bitmap picId =(Bitmap) data.get("_pic");

        holder.foodName.setText(food_name);
        holder.foodImage.setImageBitmap(picId);
        holder.foodPrice.setText(String.valueOf(price));

    }

    @Override
    public int getItemCount() {
        return mainFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView foodImage;
        private final TextView foodName,foodPrice;
        private final ConstraintLayout foodLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = (ImageView) itemView.findViewById(R.id.imageView_rc_s_pic);
            foodName = (TextView) itemView.findViewById(R.id.textView_rc_s_text);
            foodPrice = (TextView) itemView.findViewById(R.id.textView_rc_s_price);

            foodLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_rc_s_item);
            foodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Map<String, Object> data = mainFoodList.get(index);
                    MainActivity mainActivity = (MainActivity) mainContext;
                    SProdModFragment spmf = mainActivity.getsProdModFrag();
                    Bitmap picId =(Bitmap) data.get("_pic");
                    spmf = spmf.newInstance((String) data.get("_text"), (int) data.get("_price"), String.valueOf(data.get("_seqno")),BitmapToString(picId));

                    hideFragment(mainActivity.getSupportFragmentManager());
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .remove(spmf)
                            .add(R.id.framelayout,spmf, String.valueOf(spmf.getId()))
                            .show(spmf).commit();
                }
            });
        }
    }


}
