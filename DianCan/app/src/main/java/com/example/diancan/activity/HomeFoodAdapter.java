package com.example.diancan.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.R;

import java.util.List;
import java.util.Map;

public class HomeFoodAdapter extends RecyclerView.Adapter<HomeFoodAdapter.ViewHolder> {
    private final Context mainContext;
    private final List<Map<String, Object>> mainFoodList;

    public HomeFoodAdapter(Context context, List<Map<String, Object>> items) {
        mainContext = context;
        mainFoodList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainContext).inflate(R.layout.rc_item_home_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> data = mainFoodList.get(position);
        int index = position;
        String name = data.get("_text").toString();
        int seqno = (int) data.get("_seqno");
        Bitmap picId = (Bitmap) data.get("_pic");

        //Log.d("main", index+") food_name="+name+"-seqno="+seqno+"-picId="+ picId);
        holder.text.setText(name);
        holder.imageView.setImageBitmap(picId);
        holder.seqNO.setText(String.valueOf(seqno));
    }

    @Override
    public int getItemCount() {
        return mainFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView seqNO;
        private final TextView text;
//        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_main_pic);
            seqNO = (TextView) itemView.findViewById(R.id.textView_main_seqno);
            text = (TextView) itemView.findViewById(R.id.textView_main_text);
//            layout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_home_id);
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), getAdapterPosition()+". constraintLayout click", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }
}
