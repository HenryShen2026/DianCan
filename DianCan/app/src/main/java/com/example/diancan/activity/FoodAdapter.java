package com.example.diancan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.R;

import java.util.List;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private final Context mainContext;
    private final List<Map<String, Object>> mainFoodList;

    public FoodAdapter(Context context, List<Map<String, Object>> foodList) {
        mainContext = context;
        mainFoodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainContext).inflate(R.layout.rc_item_layout, parent, false);
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
        private final TextView foodQry;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = (ImageView) itemView.findViewById(R.id.imageView_rc_foodpic);
            foodName = (TextView) itemView.findViewById(R.id.textView_rc_foodname);
            foodPrice = (TextView) itemView.findViewById(R.id.textView_rc_foodprice);
            foodQry = (TextView) itemView.findViewById(R.id.textView_rc_foodqty);

            foodLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_rc_item);
            foodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
//                    Toast.makeText(mainContext, "food click ["+index+"]", Toast.LENGTH_SHORT).show();

                    Map<String, Object> data = mainFoodList.get(index);
                    Log.d("main", "_s_id"+ data.get("_s_id"));
                    Intent intent = new Intent(mainContext, ProdItemsActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("seqNo", (int) data.get("_seqno"));
                    intent.putExtra("foodName", (String) data.get("_text"));
                    intent.putExtra("foodPrice",(int) data.get("_price"));
                    intent.putExtra("foodPic",(Bitmap) data.get("_pic"));
                    intent.putExtra("foodQty", (int) data.get("_qty"));
                    intent.putExtra("sId",(String) data.get("_s_id"));
                    mainContext.startActivity(intent);

                }
            });
        }
    }
}
