package com.example.diancan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.activity.RegisterActivity;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private Button buttonNext;
    private EditText editTextRegisterID,editTextRegisterName,editTextRegisterEmail,editTextRegisterPassword;
    private MainActivity mainActivity;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        mainView = inflater.inflate(R.layout.fragment_register, container, false);

        buttonNext = (Button)mainView.findViewById(R.id.btn_next);
        editTextRegisterID = (EditText)mainView.findViewById(R.id.editText_register_id);
        editTextRegisterEmail = (EditText)mainView.findViewById(R.id.editText_register_email);
        editTextRegisterPassword = (EditText)mainView.findViewById(R.id.editTextText_register_password);
        editTextRegisterName = (EditText)mainView.findViewById(R.id.editTextText_register_name);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRegisterEmail.length()==0 || editTextRegisterPassword.length() == 0){
                    Toast.makeText(getActivity(), "Please input data", Toast.LENGTH_SHORT).show();
                } else {
                    insert(editTextRegisterID.getText().toString(),
                            editTextRegisterEmail.getText().toString(),
                            editTextRegisterPassword.getText().toString(),
                            editTextRegisterName.getText().toString());
                    mainActivity = (MainActivity) getActivity();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .show(mainActivity.getLoginFrag())
                            .hide(mainActivity.getRegFrag())
                            .commit();
                }

//                Intent intent = new Intent();
//                setResult(int_register_code,intent);
//                finish();
            }
        });
        return mainView;
    }

    private void insert(String id, String email, String password, String name) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "INSERT INTO dc_user (id, email, passwd, name) VALUES (?, ?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, email);
                    pst.setString(3, password);
                    pst.setString(4, name);
                    Log.d("main","pst = "+pst);
                    int rowsAffected = pst.executeUpdate();
                    Log.d("main","rowsAffected = "+rowsAffected);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        select();
    }

    private void select() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT * FROM dc_user";
                    PreparedStatement pst = con.prepareStatement(sql);
                    Log.d("main","pst = "+pst);
                    ResultSet rs = pst.executeQuery();
                    Log.d("main","rs = "+rs);
                    while(rs.next()){
                        String id = rs.getString(1);
                        String email = rs.getString(2);
                        String password = rs.getString(3);
                        String name = rs.getString(4);
                        Log.d("main","id = "+id+", email = "+email+", password = "+password+", name = "+name);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}