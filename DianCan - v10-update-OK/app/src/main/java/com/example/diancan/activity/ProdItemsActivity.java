package com.example.diancan.activity;

import static com.example.diancan.comm.Funcs.BitmapToString;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProdItemsActivity extends AppCompatActivity {

    private TextView tvFoodName,tvFoodPrice,tvPriceSum;
    private EditText tvFoodQty;
    private ImageView ivFoodPic;
    private ImageView qtyPlus,qtyMinus;
    private TextView addCart;
    private String b_id = "B00001";
    private int index,seqno,foodPrice, foodQty;
    private String foodName;
    private Bitmap foodPic;
    private static final int MESSAGE_READ = 1;
    private String supplier;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    Log.d("main", "MESSAGE_READ");
                    break;
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_items);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        tvFoodName = (TextView) findViewById(R.id.textView_item_foodname);
        tvFoodPrice = (TextView) findViewById(R.id.textView_item_price);
        tvFoodQty = (EditText) findViewById(R.id.editText_item_foodQty);
        ivFoodPic = (ImageView) findViewById(R.id.imageView_item_foodpic);
        tvPriceSum = (TextView) findViewById(R.id.textView_item_sum);

        qtyMinus = (ImageView) findViewById(R.id.imageView_item_minus);
        qtyPlus = (ImageView) findViewById(R.id.imageView_item_plus);
        qtyMinus.setOnClickListener(new QtyClick());
        qtyPlus.setOnClickListener(new QtyClick());

        addCart = (TextView) findViewById(R.id.textView_addToCar);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                Toast.makeText(ProdItemsActivity.this,"已加入購物車中",Toast.LENGTH_SHORT).show();
            }
        });

        index = intent.getIntExtra("index",0);
        seqno = intent.getIntExtra("seqNo",0);
        foodName = intent.getStringExtra("foodName");
        foodPrice = intent.getIntExtra("foodPrice", 99999);
        foodPic = (Bitmap) intent.getParcelableExtra("foodPic" );
        foodQty = intent.getIntExtra("foodQty", 0);
        supplier = intent.getStringExtra("sId");

        tvFoodName.setText(foodName);
        tvFoodPrice.setText(String.valueOf(foodPrice));
        ivFoodPic.setImageBitmap(foodPic);

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

    private class QtyClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            int qty = 0;
            int price = Integer.valueOf(tvFoodPrice.getText().toString());

            if (tvFoodQty.length() > 0) {
                qty = Integer.valueOf(tvFoodQty.getText().toString());
                Log.d("ProdItems","current.qty="+qty);
            }

            switch (v.getId()) {
                case R.id.imageView_item_minus:
                    if (qty > 1) {
                        qty--;
                        int total = price*qty;
//                        Log.d("ProdItems","qty--="+qty);
                        tvFoodQty.setText(String.valueOf(qty));
                        tvPriceSum.setText(String.valueOf(total));
                    }
                    break;

                case R.id.imageView_item_plus:
                    if (qty > 0) {
                        qty++;
                        int total = price*qty;
//                        Log.d("ProdItems","qty++="+qty);
                        tvFoodQty.setText(String.valueOf(qty));
                        tvPriceSum.setText(String.valueOf(total));
                    }
                    break;
            }
        }
    }

    private void add() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "INSERT INTO `dc_car`(`b_id`, `datetime`, `seqno`, `s_id`, `text`, `price`, `qty`, `pic`) VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1,b_id);
                    pst.setString(2,formattedDateTime);
                    pst.setString(3,String.valueOf(seqno));
                    pst.setString(4,String.valueOf(supplier)); //供應商編號
                    pst.setString(5,foodName);
                    pst.setInt(6,foodPrice);
                    pst.setInt(7,Integer.valueOf(tvFoodQty.getText().toString()));
                    if(foodPic.toString()!=""){
                        pst.setString(8,BitmapToString(foodPic));
                    }else {
                        pst.setString(8,"");
                    }
                    pst.executeUpdate();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
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