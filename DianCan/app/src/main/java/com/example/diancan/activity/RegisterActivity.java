package com.example.diancan.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diancan.R;
import com.example.diancan.comm.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonNext;
    private EditText et_email,et_name,et_pwd,et_pwd2;
    private TextView tv_msg;
    private Button buttonCancel;
    private String pwdStr,pwdStr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = (EditText) findViewById(R.id.reg_editText_email);
        et_name = (EditText) findViewById(R.id.reg_editText_name);
        et_pwd = (EditText) findViewById(R.id.reg_editText_password);
        et_pwd2 = (EditText) findViewById(R.id.reg_editText_password2);
        tv_msg = (TextView) findViewById(R.id.reg_textView_msg);
        tv_msg.setText("");

        pwdStr = et_pwd.getText().toString();
        pwdStr2 = et_pwd2.getText().toString();

        buttonCancel = (Button) findViewById(R.id.btn_cancel);
        buttonNext = (Button)findViewById(R.id.btn_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.length()==0  || et_name.length()==0  || et_pwd.length()==0 || et_pwd2.length()==0) {
                    tv_msg.setText("未完整填寫,請填寫完整後送出,謝謝！");
                } else if (et_email.length()==0) {
                    tv_msg.setText("請輸入Email!");
                } else if (et_name.length()==0) {
                    tv_msg.setText("請輸入姓名!");
                } else if(et_pwd.length()==0) {
                    tv_msg.setText("請輸入密碼!");
                } else if(!pwdStr.equals(pwdStr2)){
                    Log.d("debug-pwd1", pwdStr);
                    Log.d("debug-pwd2", pwdStr2);
                    tv_msg.setText("("+pwdStr+"!=" +pwdStr2+ ")密碼錯誤！");
                } else {
                    String txtStr = "姓名："+et_name.getText().toString()+"\n" + "電子信箱："+et_email.getText().toString()+"\n";
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    //dialog.setIcon(R.drawable.enable_icon);
                    dialog.setTitle("註冊資訊");
                    dialog.setMessage(txtStr);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            add();
                        }
                    });

                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            buttonCancel.callOnClick();
                        }
                    });

                    dialog.show();
                }
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void add() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    MySQLConnection conn = new MySQLConnection();
                    Connection con = conn.connection();
                    String sql = "INSERT INTO `dc_user`(`id`, `name`, `email`, `type`, `passwd`) VALUES (?,?,?,?,?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1,"B00002");
                    pst.setString(2, et_name.getText().toString());
                    pst.setString(3, et_email.getText().toString());
                    pst.setString(4,"B");
                    pst.setString(5,et_pwd2.getText().toString());
                    pst.executeUpdate();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (SQLException e){
                    Log.d("sql",e.getMessage());
                }
            }
        });
        thread.start();
    }
}