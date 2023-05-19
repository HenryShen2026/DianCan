package com.example.diancan.fragment;

import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diancan.R;
import com.example.diancan.activity.HomeFoodAdapter;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SProdListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SProdListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private RecyclerView recyclerView_main;
    private HomeFoodAdapter adapter;

    public SProdListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SProdListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SProdListFragment newInstance(String param1, String param2) {
        SProdListFragment fragment = new SProdListFragment();
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
        select();
        mainView = inflater.inflate(R.layout.fragment_s_prod_list, container, false);
        recyclerView_main = (RecyclerView) mainView.findViewById(R.id.recyclerView_home_id);
        return mainView;

    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `seqno`, `text`, `pic` FROM `dc_list`";
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
                        items.add(item);
                    }
                    rs.close();
                    con.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView_main.setLayoutManager(new LinearLayoutManager(mainView.getContext()));
                            adapter = new HomeFoodAdapter(mainView.getContext(), items);
                            recyclerView_main.setAdapter(adapter);
                        }
                    });

                } catch (SQLException | java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}