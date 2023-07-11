package com.example.diancan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diancan.R;
import com.example.diancan.activity.CartAdapter;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View cartView;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView textViewResult;
    private Button button_Order;
    private TextView totalPrice;

    public BuyCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyCartFragment newInstance(String param1, String param2) {
        BuyCartFragment fragment = new BuyCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cartView = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = (RecyclerView) cartView.findViewById(R.id.recyclerView_cart_id);
        textViewResult = (TextView) cartView.findViewById(R.id.car_textView_orderdetail);
        textViewResult.setText("");
        select();

        totalPrice = (TextView) cartView.findViewById(R.id.cart_textView_totalprice);
        button_Order = (Button) cartView.findViewById(R.id.cart_button_order);
        button_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, Object>> orderItem = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> newOrder = adapter.getData();
                textViewResult.setText("");
                int sum = 0;
                int count = 0;
                for(int i=0; i< newOrder.size(); i++) {
                    Map<String, Object> data = newOrder.get(i);
                    int qty = (int) data.get("_qty");
                    int chkFlag = (int) data.get("_checked");
                    if (chkFlag>0 && qty > 0) {
                        count++;
                        String name = data.get("_text").toString();
                        int price = (int) data.get("_price");
                        sum += price*qty;
                        textViewResult.append(name + ":" + price +"x"+qty+"=$"+price*qty+"\n");
                        addOrderList(orderItem, data); //copy data到orderItem
                    }
                }
                textViewResult.append("共"+count+"項");
                totalPrice.setText(String.valueOf(sum));
                String orderResult = textViewResult.getText().toString();

                //確認是否結帳
                AlertDialog.Builder dialog = new AlertDialog.Builder(cartView.getContext());
                //dialog.setIcon(R.drawable.enable_icon);
                dialog.setTitle("訂餐資訊");
                dialog.setMessage(orderResult+" 金額："+sum);
                int finalSum = sum;
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        add(orderItem, finalSum);
                        for(int i=0;i <newOrder.size();i++) {
                            Map<String, Object> data = newOrder.get(i);
                            data.put("_checked", 0);
                        }
                        textViewResult.setText("共0項");
                        totalPrice.setText("0");
                        adapter.notifyDataSetChanged();

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();


            }

            private void addOrderList(List<Map<String, Object>> orderItem, Map<String, Object> data) {
                //更新orderItem
                Map<String, Object> item = data;
                item.put("_bid", data.get("_bid"));
                item.put("_datetime", data.get("_datetime"));
                item.put("_seqno", data.get("_seqno"));
                item.put("_sid", data.get("_sid"));
                item.put("_text", data.get("_text"));
                item.put("_price", data.get("_price"));
                item.put("_qty", data.get("_qty"));
                item.put("_pic", data.get("_pic"));
                item.put("_checked", 1);
                orderItem.add(item);
            }
        });

        return cartView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        select();
    }

    private void select() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("main", "cartFrament");
                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `b_id`, `datetime`, `seqno`, `s_id`, `text`, `price`, `qty`, `pic`, `ordertime`, `orderprice` FROM `dc_car` where ordertime is null";
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("_bid", rs.getString(1));
                        item.put("_datetime", rs.getString(2));
                        item.put("_seqno", rs.getInt(3));
                        item.put("_sid", rs.getString(4));
                        item.put("_text", rs.getString(5));
                        item.put("_price", rs.getInt(6));
                        item.put("_qty", rs.getInt(7));
                        Bitmap bitmap = null;
                        try {
                            byte[] bitmapArray;
                            bitmapArray = Base64.getDecoder().decode(rs.getString(8));
                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        item.put("_pic", bitmap);
                        item.put("_checked", 0);
                        items.add(item);
                    }
                    rs.close();
                    con.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(cartView.getContext()));
                            adapter = new CartAdapter(cartView, items);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void add(List<Map<String, Object>> orderItem,int sum) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "update dc_car set ordertime = ?, orderprice = ? , orderqty = ? where b_id = ? and datetime = ?";

                    for(int i=0; i < orderItem.size(); i++) {
                        Map<String, Object> item = orderItem.get(i);
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setString(1,formattedDateTime); //ordertime
                        pst.setString(2, String.valueOf(sum)); //orderprice
                        pst.setString(3,String.valueOf(item.get("_qty"))); //orderqty
                        pst.setString(4,String.valueOf(item.get("_bid"))); //seqno
                        pst.setString(5, String.valueOf(item.get("_datetime")));
                        Log.d("sql", pst.toString());
                        pst.executeUpdate();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "結帳完成！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (SQLException e){
                    Log.d("sql",e.getMessage());
                }
            }
        });
        thread.start();
    }
}