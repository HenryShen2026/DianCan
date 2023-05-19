package com.example.diancan.fragment;

import static com.example.diancan.comm.Funcs.BitmapToString;
import static com.example.diancan.comm.Funcs.hideFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.activity.HomeFoodAdapter;
import com.example.diancan.activity.SFoodAdapter;
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

public class SupplierFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View mainView;
    private RecyclerView recyclerView_main;
    private SFoodAdapter adapter;

    public SupplierFragment() {
        // Required empty public constructor
    }

    public static SupplierFragment newInstance(String param1, String param2) {
        SupplierFragment fragment = new SupplierFragment();
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
        setHasOptionsMenu(true);
        select();
        mainView = inflater.inflate(R.layout.fragment_supplier, container, false);
        recyclerView_main = (RecyclerView) mainView.findViewById(R.id.recyclerView_sprod_id);
        return mainView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        } else {
            select();
        }
    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `seqno`, `text`, `pic`, `price` FROM `dc_list`";
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("_seqno", rs.getInt(1));
                        item.put("_text", rs.getString(2));
                        Bitmap bitmap = null;
                        try {
                            byte[] bitmapArray;
                            bitmapArray = Base64.getDecoder().decode(rs.getString(3));
                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        item.put("_pic", bitmap);
                        item.put("_price", rs.getInt(4));
                        items.add(item);
                    }
                    rs.close();
                    con.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView_main.setLayoutManager(new LinearLayoutManager(mainView.getContext()));
                            adapter = new SFoodAdapter(mainView.getContext(), items);
                            recyclerView_main.setAdapter(adapter);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.s_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_prod_add:
                MainActivity mainActivity = (MainActivity) getActivity();
                SProdAddFragment spaf = mainActivity.getsProdAddFrag();
                hideFragment(mainActivity.getSupportFragmentManager());
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .show(spaf).commit();
                break;
        }
        return true;
    }

    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        // 檢查收到的權限要求編號是否和我們送出的相同
//        if (requestCode == REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getActivity(), "取得 WRITE_EXTERNAL_STORAGE 權限",
//                                Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//    private void askForWriteExternalStoragePermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED) {
//            // 這項功能尚未取得使用者的同意
//            // 開始執行徵詢使用者的流程
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                    getActivity(),
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                AlertDialog.Builder altDlgBuilder =
//                        new AlertDialog.Builder(getActivity());
//                altDlgBuilder.setTitle("提示");
//                altDlgBuilder.setMessage("App需要讀寫SD卡中的資料。");
//                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
//                altDlgBuilder.setCancelable(false);
//                altDlgBuilder.setPositiveButton("確定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // 顯示詢問使用者是否同意功能權限的對話盒
//                                // 使用者答覆後會執行onRequestPermissionsResult()
//                                ActivityCompat.requestPermissions(getActivity(),
//                                        new String[]{
//                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE);
//                            }
//                        });
//                altDlgBuilder.show();
//
//                return;
//            } else {
//                // 顯示詢問使用者是否同意功能權限的對話盒
//                // 使用者答覆後會執行onRequestPermissionsResult()
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE);
//
//                return;
//            }
//        }
//    }
}