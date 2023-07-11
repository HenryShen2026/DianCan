package com.example.diancan.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ListView listView_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView_car = (ListView)findViewById(R.id.listview_cart);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        select();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `b_id`, `datetime`, `seqno`, `s_id`, `text`, `price`, `qty`, `pic` FROM `dc_car`";
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
                        item.put("_qyt", rs.getInt(7));
                        Bitmap bitmap = null;
                        try {
                            byte[] bitmapArray;
                            bitmapArray = Base64.getDecoder().decode(rs.getString(8));
                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        item.put("_pic", bitmap);
                        items.add(item);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleAdapter SA=new SimpleAdapter(CartActivity.this,items, R.layout.listview_cart_layout,new String[]{"_bid","_datetime","_seqno","_sid","_text","_price","_qyt","_pic"},
                                    new int[]{R.id.textView_car_bid, R.id.textView_car_datetime, R.id.textView_car_seqno, R.id.textView_car_sid, R.id.textView_car_text, R.id.textView_car_price, R.id.textView_car_qty, R.id.imageView_car_pic});
                            SA.setViewBinder(new SimpleAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Object data, String textRepresentation) {
                                    if(view instanceof ImageView && data instanceof Bitmap){
                                        ImageView iv = (ImageView) view;
                                        iv.setImageBitmap((Bitmap) data);
                                        return true;
                                    }else {
                                        return false;
                                    }
                                }
                            });

                            listView_car.setAdapter(SA);
                        }
                    });

                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}