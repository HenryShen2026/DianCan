package com.example.diancan.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
        String sp_name = sp.getString("user_name","");
        String sp_pass = sp.getString("password","");

        boolean choseRemember = sp.getBoolean("remember",false);
        boolean choseAutoLogin = sp.getBoolean("autologin",false);

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
//            setResult(int_login_code,intent);
//            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到用戶輸入的email和passwd
                String email = editTextName.getText().toString();
                String passwd = editTextPassword.getText().toString();
                // 在UserCredentials陣列中尋找配對的用戶資訊
                boolean isMatched = false;
                for (UserCredentials userCredentials : userCredentialsArray) {
                    if (userCredentials.getEmail().equals(email) && userCredentials.getPasswd().equals(passwd))
                    {
                        // email和passwd匹配
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("user_name",email);
                        editor.putString("password",passwd);

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
                        isMatched = true;
                        break;
                    }
                }
                // 判斷核對結果
                if (isMatched) {
                    // 登入成功
                    // 在這裡處理登入成功的情況
                    Toast.makeText(getActivity(), "isMatched", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("user",1);
                    myListener.setUserLogin(1);
                    mainActivity = (MainActivity) getActivity();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .show(mainActivity.getProfileFrag())
                            .hide(mainActivity.getLoginFrag())
                            .commit();
//                    setResult(int_login_code,intent);
//                    finish();
                } else if (email.isEmpty() || passwd.isEmpty()) {
                    // 缺少email或passwd
                    // 在這裡處理缺少的email或passwd的情況
                    Toast.makeText(getActivity(),"please input email or passwd",Toast.LENGTH_SHORT).show();
                } else {
                    // email和passwd不正確
                    // 在這裡處理登入失敗的情況
                    Toast.makeText(getActivity(),"please check email or passwd",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Register onClick", Toast.LENGTH_SHORT).show();
                 mainActivity = (MainActivity) getActivity();
//                Intent intent = new Intent(getContext(), RegisterActivity.class);
//                startActivity(intent);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .show(mainActivity.getRegFrag())
                        .hide(mainActivity.getLoginFrag())
                        .commit();
            }
        });

        return mainView;
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
                    // 創建一個空的用戶資訊列表
                    List<UserCredentials> userCredentialsList = new ArrayList<>();
                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "SELECT email,passwd FROM dc_user";
                    PreparedStatement pst = con.prepareStatement(sql);
                    Log.d("main","pst = "+pst);
                    ResultSet rs = pst.executeQuery();
                    Log.d("main","rs = "+rs);
                    // 從MySQL資料庫中讀取資料並將其轉換為UserCredentials對象
                    while(rs.next()){
                        String email = rs.getString("email");
                        String passwd = rs.getString("passwd");
                        UserCredentials userCredentials = new UserCredentials(email, passwd);
                        userCredentialsList.add(userCredentials);
                    }
                    // 將用戶資訊列表轉換為UserCredentials陣列
                    userCredentialsArray = userCredentialsList.toArray(new UserCredentials[0]);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}