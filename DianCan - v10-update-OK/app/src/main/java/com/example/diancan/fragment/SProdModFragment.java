package com.example.diancan.fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.diancan.comm.Funcs.BitmapToString;
import static com.example.diancan.comm.Funcs.hideFragment;
import static com.example.diancan.comm.Funcs.pic_data;
import static com.example.diancan.comm.Funcs.showFragment;
import static com.example.diancan.comm.Funcs.stringToBitmap;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SProdModFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SProdModFragment extends Fragment {
    private static final int PICK_CONTACT_REQUEST = 1;
    private final String s_id="S00001";
    private static final String ARG_PARAM1 = "foodName";
    private static final String ARG_PARAM2 = "foodPrice";
    private static final String ARG_PARAM3 = "seqno";
    private static final String ARG_PARAM4 = "pic";

    private String foodName,foodPic,seqno;
    private int foodPrice;
    private View view;
    private EditText t1,t2;
    private ImageView picb;
    private Intent intent;
    private Button b2,b3,b4;
    private static final int MESSAGE_READ = 1;
    private Bitmap bitmap_uri;
    private TextView t3;

    public SProdModFragment() {
        // Required empty public constructor
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d("main", "handlMessage");
            switch (msg.what) {
                case MESSAGE_READ:
                    MainActivity mainActivity = (MainActivity) getActivity();
                    SupplierFragment supplierFrag = mainActivity.getSupplierFrag();
                    hideFragment(mainActivity.getSupportFragmentManager());
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .remove(supplierFrag)
                            .add(R.id.framelayout,supplierFrag)
                            .show(supplierFrag).commit();
                    break;
            }
            return true;
        }
    });


    public static SProdModFragment newInstance(String param1, int param2, String param3, String param4) {
        Log.d("args", "newInstance " );
        SProdModFragment fragment = new SProdModFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foodName = getArguments().getString(ARG_PARAM1);
            foodPrice = getArguments().getInt(ARG_PARAM2);
            seqno = getArguments().getString(ARG_PARAM3);
            foodPic = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_s_prod_mod, container, false);

        t1=(EditText) view.findViewById(R.id.editText_mod_desc);
        t2=(EditText) view.findViewById(R.id.editText_mod_price);
        t3=(TextView) view.findViewById(R.id.textView_mod_seqno);
        picb=(ImageView) view.findViewById(R.id.imageView_mod_pic);
        if (foodPic!= null && foodPic.length() >0) {
            picb.setImageBitmap(stringToBitmap(foodPic));
        } else {
            picb.setImageResource(R.drawable.ic_launcher_background);
        }

        picb.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        t1.setText(foodName);
        t2.setText(String.valueOf(foodPrice));
        t3.setText(seqno);

        //更新
        b2=(Button)view.findViewById(R.id.button_update);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t1.getText().length() == 0 || t2.getText().length()==0) {
                    Toast.makeText(getContext(), "完整填寫後才可送出!", Toast.LENGTH_SHORT).show();
                }
                Bitmap bup=((BitmapDrawable) picb.getDrawable()).getBitmap();
                update(seqno,t1.getText().toString(),t2.getText().toString(),bup);
//                select();
            }
        });

        //刪除
        b3=(Button)view.findViewById(R.id.button_delete);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del(seqno);
//                select();
                seqno=null;
            }
        });

        b4=(Button) view.findViewById(R.id.button_mod_cancel);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity mainActivity = (MainActivity) getActivity();
                SupplierFragment supplierFrag = mainActivity.getSupplierFrag();
                hideFragment(mainActivity.getSupportFragmentManager());
                mainActivity.getSupportFragmentManager().beginTransaction()
//                        .remove(supplierFrag)
//                        .add(R.id.framelayout,supplierFrag)
                        .show(supplierFrag).commit();
            }
        });
        return view;
    }

    private void update(String id_text, String t, String n, Bitmap bup) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "UPDATE `dc_list` SET `s_id`=?,`text`=?,`price`=?,`pic`=? WHERE `seqno`=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1,s_id);
                    pst.setString(2,t);
                    pst.setInt(3,Integer.parseInt(n));
                    if(bup!=null){
                        pst.setString(4,pic_data(bup));
                    }else {
                        pst.setString(4,"");
                    }
                    pst.setString(5,id_text);
                    pst.executeUpdate();
                    pst.close();
                    con.close();
                    Message msg = new Message();
                    msg.what=MESSAGE_READ;
                    mHandler.obtainMessage(msg.what).sendToTarget();
                } catch (SQLException e){
                    Log.d("sql",e.getMessage());

                }
            }
        });
        thread.start();
    }

    private void del(String id_text) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "DELETE FROM `dc_list` WHERE `seqno`=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1,id_text);
                    pst.executeUpdate();
                    pst.close();
                    con.close();
                    Message msg = new Message();
                    msg.what=MESSAGE_READ;
                    mHandler.obtainMessage(msg.what).sendToTarget();
                } catch (SQLException e){
                    Log.d("sql",e.getMessage());

                }
            }
        });
        thread.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_CONTACT_REQUEST){
            if(resultCode==RESULT_OK){
                Uri uri = data.getData();
                ContentResolver cr = getActivity().getContentResolver();
                try {
                    bitmap_uri = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    picb.setImageBitmap(bitmap_uri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
//    private void select() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
//                    MySQLConnection conn = new MySQLConnection();
//                    Connection con = conn.connection();
//                    String sql = "select * from dc_list";
//                    PreparedStatement pst = con.prepareStatement(sql);
//                    ResultSet rs = pst.executeQuery();
//                    while (rs.next()) {
//                        Map<String, Object> item = new HashMap<String, Object>();
//                        item.put("_seqno", rs.getInt(1));
//                        item.put("_sid", rs.getString(2));
//                        item.put("_text", rs.getString(3));
//                        item.put("_price", rs.getInt(4));
//                        Bitmap bitmap = null;
//                        try {
//                            byte[] bitmapArray;
//                            bitmapArray = Base64.getDecoder().decode(rs.getString(5));
//                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        item.put("_pic", bitmap);
//                        items.add(item);
//                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            SimpleAdapter SA=new SimpleAdapter(getContext(),items,R.layout.listlayout,new String[]{"_seqno","_sid","_text","_price","_pic"},new int[]{R.id.textView_seqno,R.id.textView_s_id,R.id.textView_text,R.id.textView_price,R.id.imageView_pic});
//                            SA.setViewBinder(new SimpleAdapter.ViewBinder() {
//                                @Override
//                                public boolean setViewValue(View view, Object data, String textRepresentation) {
//                                    if(view instanceof ImageView && data instanceof Bitmap){
//                                        ImageView iv = (ImageView) view;
//                                        iv.setImageBitmap((Bitmap) data);
//                                        return true;
//                                    }else {
//                                        return false;
//                                    }
//                                }
//                            });
//
//                            LV1.setAdapter(SA);
//                        }
//                    });
//
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }

}