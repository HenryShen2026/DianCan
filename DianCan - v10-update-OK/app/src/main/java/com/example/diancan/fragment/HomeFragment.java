package com.example.diancan.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.diancan.activity.HomeFoodAdapter;
import com.example.diancan.comm.MySQLConnection;
import com.example.diancan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int REQUEST_PERMISSION_FOR_WRITE_EXTERNAL_STORAGE = 100;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private ListView listView_main;
    private RecyclerView recyclerView_main;
    private HomeFoodAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        //select();
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        //recyclerView_main = (RecyclerView) mainView.findViewById(R.id.recyclerView_home_id);
        return mainView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.TabLayout_home);
        ViewPager2 viewPager = view.findViewById(R.id.ViewPager2_home);
        PagerAdapter pagerAdapter = new PagerAdapter(this);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Food");
                            break;
                        case 1:
                            tab.setText("Drink");
                            break;
                        case 2:
                            tab.setText("Dessert");
                            break;
                    }
                }).attach();
    }

    private class PagerAdapter extends FragmentStateAdapter {

        private static final int NUM_PAGES = 3;

        public PagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SupplierFragment();
                case 1:
                    return new SProdListFragment();
                case 2:
                    return new BuyCartFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
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
//                    String sql = "SELECT `seqno`, `text`, `pic` FROM `dc_list`";
//                    PreparedStatement pst = con.prepareStatement(sql);
//                    ResultSet rs = pst.executeQuery();
//                    while (rs.next()) {
//                        Map<String, Object> item = new HashMap<String, Object>();
//                        item.put("_seqno", rs.getInt(1));
//                        item.put("_text", rs.getString(2));
//                        Bitmap bitmap = null;
//                        try {
//                            byte[] bitmapArray;
//                            bitmapArray = Base64.getDecoder().decode(rs.getString(3));
//                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        item.put("_pic", bitmap);
//                        items.add(item);
//                    }
//                    rs.close();
//                    con.close();
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView_main.setLayoutManager(new LinearLayoutManager(mainView.getContext()));
//                            adapter = new HomeFoodAdapter(mainView.getContext(), items);
//                            recyclerView_main.setAdapter(adapter);
//                        }
//                    });
//
//                } catch (SQLException e){
//                    e.printStackTrace();
//                } finally {
//
//                }
//            }
//        });
//        thread.start();
//    }
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