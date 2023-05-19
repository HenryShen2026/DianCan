package com.example.diancan.fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.diancan.comm.Funcs.hideFragment;
import static com.example.diancan.comm.Funcs.pic_data;
import static com.example.diancan.comm.Funcs.stringToBitmap;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SProdAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SProdAddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private EditText t1,t2;
    private ImageView picb;
    private static final int MESSAGE_READ = 1;
    private Button b1;
    private Bitmap bitmap_uri;
    private final String s_id="S00001";
    int PICK_CONTACT_REQUEST=1;
    private Intent intent;

    public SProdAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SProdAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SProdAddFragment newInstance(String param1, String param2) {
        SProdAddFragment fragment = new SProdAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_s_prod_add, container, false);
        t1 = (EditText) view.findViewById(R.id.editText_add_desc);
        t2 = (EditText) view.findViewById(R.id.editText_add_price);
        picb=(ImageView) view.findViewById(R.id.imageView_add_pic);
        picb.setImageResource(R.drawable.ic_launcher_background);
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
        
        //新增
        b1=(Button) view.findViewById(R.id.button_add);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img_String="";
                if(bitmap_uri!=null){
                    img_String=pic_data(bitmap_uri);
                }
                add(t1.getText().toString(),t2.getText().toString(),img_String);
//                select();
            }
        });
        return view;
    }

    private void add(String t, String n, String img_string) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "INSERT INTO `dc_list`(`s_id`, `text`, `price`, `pic`) VALUES (?,?,?,?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1,s_id);
                    pst.setString(2,t);
                    pst.setInt(3,Integer.parseInt(n));
                    if(img_string.toString()!=""){
                        pst.setString(4,img_string.toString());
                    }else {
                        pst.setString(4,"");
                    }
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
}