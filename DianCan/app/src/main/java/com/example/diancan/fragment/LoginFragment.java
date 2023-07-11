package com.example.diancan.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diancan.MainActivity;
import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;
import com.example.diancan.comm.UserCredentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private EditText editTextName,editTextPassword;
    private Button buttonLogin,buttonRegister;
    private TextView textViewResult;
    private RegisterFragment regFrag;
    private CheckBox checkBoxRemember,checkBoxAutoLogin;
    private SharedPreferences sp;
    private UserCredentials[] userCredentialsArray;
    private MainActivity mainActivity;
    private SharedPreferences.Editor editor;
    private TextView navAct;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public interface IListener{
        public void setUserLogin(int value);
    }
    private IListener myListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("fragMsg","onAttach");
        myListener = (IListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_login, container, false);
        editTextName = (EditText) mainView.findViewById(R.id.editText_login_nam);
        editTextPassword = (EditText) mainView.findViewById(R.id.editText_login_password);
        buttonLogin = (Button) mainView.findViewById(R.id.btn_login);
        buttonRegister = (Button) mainView.findViewById(R.id.btn_register);
        checkBoxRemember = (CheckBox) mainView.findViewById(R.id.checkBox_remember);
        checkBoxAutoLogin = (CheckBox) mainView.findViewById(R.id.checkBox_autologin);
        textViewResult = (TextView) mainView.findViewById(R.id.textView_result);

        sp = getActivity().getSharedPreferences("userInfo",0);
        String sp_name = sp.getString("email","");
        String sp_pass = sp.getString("password","");

        boolean choseRemember = sp.getBoolean("remember",false);
        boolean choseAutoLogin = sp.getBoolean("autologin",false);
        navAct = (TextView) getActivity().findViewById(R.id.nav_act);
//        UserCredentials user = new UserCredentials();
//        user.getEmail();
        //上次選了記住密碼
        if (choseRemember){
            editTextName.setText(sp_name);
            editTextPassword.setText(sp_pass);
            checkBoxRemember.setChecked(true);
        }

        if (choseAutoLogin){
//            Bundle bundle = new Bundle();
//            bundle.putInt("user",1);
            myListener.setUserLogin(1);
            navAct.setText("商品維護");
//            setResult(int_login_code,intent);
//            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到用戶輸入的email和passwd
                String edit_email = editTextName.getText().toString();
                String edit_passwd = editTextPassword.getText().toString();
                login(edit_email,edit_passwd);

            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity = (MainActivity) getActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .show(mainActivity.getRegFrag())
                        .hide(mainActivity.getLoginFrag())
                        .commit();
            }
        });

        return mainView;
    }
    private void login(String email, String password) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT * FROM dc_user WHERE email=? AND passwd=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, email);
                    pst.setString(2, password);
                    Log.d("main","pst = "+pst);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        Log.d("main", "User exists!");
                        // 如果需要記住信箱和密碼，則將它們保存到SharedPreferences中
                        editor = getActivity().getSharedPreferences("userInfo",0).edit();
                        editor.putString("email",email);
                        editor.putString("password",password);
                        editor.putString("id",rs.getString("id"));
                        editor.putString("name",rs.getString("name"));

                        //記住密碼
                        if (checkBoxRemember.isChecked()){
                            editor.putBoolean("remember",true);
                        }else{
                            editor.putBoolean("remember",false);
                        }
                        //自動登入
                        if (checkBoxAutoLogin.isChecked()){
                            editor.putBoolean("autologin",true);
                        }else{
                            editor.putBoolean("autologin",false);
                        }
                        editor.commit();
//
                        myListener.setUserLogin(1);
                        navAct.setText("商品維護");
                        mainActivity = (MainActivity) getActivity();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .show(mainActivity.getProfileFrag())
                                .hide(mainActivity.getLoginFrag())
                                .commit();
                    } else {
                        Log.d("main", "User does not exist!");
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        );
        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

//        select();
    }
//    private void select() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    MySQLConnection conn = new MySQLConnection();
//                    Connection con = conn.connection();
//                    String sql = "SELECT * FROM dc_user";
//                    PreparedStatement pst = con.prepareStatement(sql);
//                    Log.d("main","pst = "+pst);
//                    ResultSet rs = pst.executeQuery();
//                    Log.d("main","rs = "+rs);
//                    while(rs.next()){
//                        String id = rs.getString(1);
//                        String email = rs.getString(2);
//                        String password = rs.getString(3);
//                        String name = rs.getString(4);
//                        Log.d("main","id = "+id+", email = "+email+", password = "+password+", name = "+name);
//                    }
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
//    }
}
