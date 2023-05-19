package com.example.diancan.fragment;

import static com.example.diancan.comm.Funcs.stringToBitmap;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.diancan.R;
import com.example.diancan.activity.FoodAdapter;
import com.example.diancan.activity.ProdListActivity;
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
 * Use the {@link ProdListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View listView;
    private RecyclerView recyclerViewFood;
    private FoodAdapter adapter;

    public ProdListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProdListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdListFragment newInstance(String param1, String param2) {
        ProdListFragment fragment = new ProdListFragment();
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
        listView = inflater.inflate(R.layout.fragment_prod_list, container, false);
        recyclerViewFood = (RecyclerView) listView.findViewById(R.id.recyclerView_prodlist_id);
        select();

        return listView;
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewFood.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new FoodAdapter(getContext(), items);
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
}