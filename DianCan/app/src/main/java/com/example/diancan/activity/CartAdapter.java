package com.example.diancan.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private final Context mainContext;
    private final List<Map<String, Object>> mainCartList;
    private View parentView;


    public CartAdapter(View cartView, List<Map<String, Object>> items)  {
        parentView = cartView;
        mainContext = cartView.getContext();
        mainCartList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mainContext).inflate(R.layout.rc_item_cart_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> data = mainCartList.get(position);
        int index = position;

        String name = data.get("_text").toString();
        int price = (int) data.get("_price");
        int qty = (int) data.get("_qty");
        int chkbox = (int) data.get("_checked");
        if (chkbox == 0) {
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
        }
        holder.foodname.setText(name);
        holder.qty.setText(String.valueOf(qty));
        holder.foodprice.setText(String.valueOf(price));
    }



    @Override
    public int getItemCount() {
        return mainCartList.size();
    }
    public List<Map<String, Object>> getData(){
        return mainCartList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView qty,foodname,foodprice;
        private final ImageView qty_minus,qty_plus;
        private final CheckBox checkBox;
        private final TextView orderContent;
        private final TextView totalPrice;
        private int qtyNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qty = (TextView) itemView.findViewById(R.id.cart_textView_rc_item_qty);
            qty_minus = (ImageView) itemView.findViewById(R.id.cart_imageView_item_minus);
            qty_plus = (ImageView) itemView.findViewById(R.id.cart_imageView_item_plus);
            foodname = (TextView) itemView.findViewById(R.id.cart_textView_rc_item_foodname);
            foodprice = (TextView) itemView.findViewById(R.id.cart_textView_rc_item_price);
            checkBox = (CheckBox) itemView.findViewById(R.id.cart_checkBox_id);

            orderContent = (TextView) parentView.findViewById(R.id.car_textView_orderdetail);
            totalPrice = (TextView) parentView.findViewById(R.id.cart_textView_totalprice);
            Log.d("main","orderDetail="+orderContent.getText().toString());

            qty_plus.setOnClickListener(new QtyClick());
            qty_minus.setOnClickListener(new QtyClick());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    Toast.makeText(itemView.getContext(), "check index="+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Map<String, Object> data = mainCartList.get(getAdapterPosition());
                    if (isChecked) {
                        data.put("_checked", 1);
                    } else {
                        data.put("_checked", 0);
                    }
                    getDetail();
                }
            });
        }

        private void getDetail() {
            List<Map<String, Object>> currentOrder = mainCartList;
            orderContent.setText("");
            int sum = 0;
            int count = 0;
            for(int i=0; i< currentOrder.size(); i++) {
                Map<String, Object> currData = currentOrder.get(i);
                int qty = (int) currData.get("_qty");
                int chkFlag = (int) currData.get("_checked");
                if (chkFlag>0 && qty > 0) {
                    count++;
                    String name = currData.get("_text").toString();
                    int price = (int) currData.get("_price");
                    sum += price*qty;
                    orderContent.append(name + ":" + price +"x"+qty+"=$"+price*qty+"\n");
                }
            }
            orderContent.append("共"+count+"項");
            totalPrice.setText(String.valueOf(sum));
        }

        private class QtyClick implements View.OnClickListener {
            @Override
            public void onClick(View v) {

                int index = getAdapterPosition();
                Map<String, Object> data = mainCartList.get(index);
                if (qty.length() > 0) {
                    qtyNo = (int) data.get("_qty");
                }

                switch (v.getId()) {
                    case R.id.cart_imageView_item_minus:
                        if (qtyNo > 0) {
                            qtyNo--;
                            if (qtyNo == 0) {
                                checkBox.setChecked(false);
                                data.put("_checked", 0);
                            }
                        }
                        break;
                    case R.id.cart_imageView_item_plus:
                        qtyNo++;
                        break;
                }

                data.put("_qty", qtyNo);
                qty.setText(String.valueOf(qtyNo));
                getDetail();
            }
        }


    }



}
