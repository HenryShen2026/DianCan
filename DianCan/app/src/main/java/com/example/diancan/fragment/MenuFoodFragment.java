package com.example.diancan.fragment;

import static com.example.diancan.comm.Funcs.stringToBitmap;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diancan.R;
import com.example.diancan.activity.FoodAdapter;
import com.example.diancan.activity.MenuFoodAdapter;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFoodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View fragView;
    private RecyclerView recyclerView;
    private MenuFoodAdapter adapter;

    public MenuFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFoodFragment newInstance(String param1, String param2) {
        MenuFoodFragment fragment = new MenuFoodFragment();
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
        fragView = inflater.inflate(R.layout.fragment_menu_food, container, false);
        recyclerView = (RecyclerView) fragView.findViewById(R.id.recyclerView_menufood_id);
        select();
        return fragView;
    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT `seqno`, `text`, `pic`, `price`, `s_id` FROM `dc_list` where type= 0";
                    PreparedStatement pst = con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("_text", rs.getString("text"));
                        Bitmap bitmap = stringToBitmap(rs.getString("pic"));
                        item.put("_pic", bitmap);
                        items.add(item);
                    }
                    rs.close();
                    con.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new MenuFoodAdapter(getContext(), items);
                            recyclerView.setAdapter(adapter);
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
}