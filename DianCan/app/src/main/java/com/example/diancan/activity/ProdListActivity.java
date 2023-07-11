package com.example.diancan.activity;

import static com.example.diancan.comm.Funcs.stringToBitmap;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFood;
    private String[] foodName;
    private int[] foodPrice;
    private TypedArray foodPic;
    private int[] foodQty;
    private List<Map<String, Object>> foodList;
    private FoodAdapter adapter;
    private List<Map<String, Object>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_list);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        recyclerViewFood = (RecyclerView) findViewById(R.id.recyclerView_prodlist_id_old);
        select();
    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `seqno`, `text`, `pic`, `price`, `s_id` FROM `dc_list`";
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("_seqno", rs.getInt("seqno"));
                        item.put("_text", rs.getString("text"));
                        item.put("_price", rs.getInt("price"));
                        item.put("_s_id", rs.getString("s_id"));
                        Log.d("main", "_s_id="+ rs.getString("s_id"));
                        item.put("_qty", 0);
                        Bitmap bitmap = stringToBitmap(rs.getString("pic"));
                        item.put("_pic", bitmap);
                        items.add(item);
                    }
                    rs.close();
                    con.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewFood.setLayoutManager(new LinearLayoutManager(ProdListActivity.this));
                            adapter = new FoodAdapter(ProdListActivity.this, items);
                            recyclerViewFood.setAdapter(adapter);
                        }
                    });

                } catch (SQLException e){
                    e.printStackTrace();
                } finally {

                }
            }
        });
        thread.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflate = getMenuInflater();
        menuInflate.inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_cart:
//                Toast.makeText(this, "購物車", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProdListActivity.this, CartActivity.class);
                startActivity(intent);
//                cartFragement = new CartFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.framelayout,cartFragement,"Cart Fragment").commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}