package com.example.diancan;

import static com.example.diancan.comm.Funcs.hideFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diancan.fragment.BuyCartFragment;
import com.example.diancan.fragment.EventFragment;
import com.example.diancan.fragment.HomeFragment;
import com.example.diancan.fragment.LoginFragment;
import com.example.diancan.fragment.OrderFragment;
import com.example.diancan.fragment.ProdListFragment;
import com.example.diancan.fragment.ProfileFragment;
import com.example.diancan.fragment.RegisterFragment;
import com.example.diancan.fragment.SProdAddFragment;
import com.example.diancan.fragment.SProdModFragment;
import com.example.diancan.fragment.StoreFragment;
import com.example.diancan.fragment.SupplierFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.IListener {

    protected ImageButton imagebuttomProfile,imagebuttonOrder,imagebuttonHome,imagebuttonStore,imagebuttonEvent;
    private ProfileFragment profileFrag;
    private HomeFragment homeFrag;
    private OrderFragment orderFrag;
    private StoreFragment storeFrag;
    private EventFragment eventFrag;
    private Button buttonRegister;
    private FrameLayout framelayout;

    private BuyCartFragment cartFrag;
    private LoginFragment loginFrag;
    private RegisterFragment regFrag;
    private TextView navProfile,navHome,navOrder,navAct,navCart;
    private final int int_login_code = 10;
    private int user_login=0;

    private final int RC_SIGN_IN = 20;
    private ProdListFragment prodListFrag;
    private SupplierFragment supplierFrag;



    private SProdModFragment sProdModFrag;
    private SProdAddFragment sProdAddFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        imagebuttomProfile = (ImageButton) findViewById(R.id.btn_profile);

        navProfile = (TextView) findViewById(R.id.nav_profile);
        navOrder = (TextView) findViewById(R.id.nav_order);
        navHome = (TextView)findViewById(R.id.nav_home);
        navAct = (TextView) findViewById(R.id.nav_act);
        navCart = (TextView) findViewById(R.id.nav_cart);

        framelayout = (FrameLayout)findViewById(R.id.framelayout);

//        ActionBar bar = getSupportActionBar();
//        bar.setDisplayHomeAsUpEnabled(true);

        profileFrag = new ProfileFragment();
        orderFrag = new OrderFragment();
        homeFrag = new HomeFragment();
        storeFrag = new StoreFragment();
//        eventFrag = new EventFragment();
        cartFrag = new BuyCartFragment();
        loginFrag = new LoginFragment();
        regFrag = new RegisterFragment();
        prodListFrag = new ProdListFragment();
        supplierFrag = new SupplierFragment();
        sProdModFrag = new SProdModFragment();
        sProdAddFrag = new SProdAddFragment();



        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout,profileFrag,"First Fragment")
                .add(R.id.framelayout,orderFrag,"Second Fragment")
                .add(R.id.framelayout,homeFrag,"Third Fragment")
                .add(R.id.framelayout,supplierFrag,"Supplier Fragment")
//                .add(R.id.framelayout,storeFrag,"Four Fragment")
//                .add(R.id.framelayout,eventFrag,"Five Fragment")
                .add(R.id.framelayout,cartFrag,"Five Fragment")
                .add(R.id.framelayout,loginFrag, "Login Fragment")
                .add(R.id.framelayout,regFrag, "RegisterFragment")
                .add(R.id.framelayout,prodListFrag, "ProdListFrag")
                .add(R.id.framelayout,sProdModFrag, "SProdModFrag")
                .add(R.id.framelayout,sProdAddFrag,"SProdAddFrag")
                .hide(sProdAddFrag)
                .hide(supplierFrag)
                .hide(prodListFrag)
                .hide(regFrag)
                .hide(profileFrag)
                .hide(orderFrag)
                .hide(storeFrag)
//                .hide(eventFrag)
                .hide(cartFrag)
                .hide(loginFrag)
                .hide(sProdModFrag)
                .commit();



        navProfile.setOnClickListener(new FuncButton());
        navOrder.setOnClickListener(new FuncButton());
        navHome.setOnClickListener(new FuncButton());
        navAct.setOnClickListener(new FuncButton());
        navCart.setOnClickListener(new FuncButton());
    }

    @Override
    public void setUserLogin(int value) {
        user_login = value;
        Log.d("main", "user_login="+user_login);
    }

    private class FuncButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            hideFragment(fragmentManager);
            switch (v.getId()){

                case R.id.nav_profile:
                    if(user_login==0){
                        Toast.makeText(MainActivity.this, "user_login=0", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction()
                                .show(loginFrag).commit();
                    }else {
                        Toast.makeText(MainActivity.this, "user_logined", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction()
                                .show(profileFrag).commit();
                    }
                    break;
                case R.id.nav_order:
                    if (user_login == 0) {
                        getSupportFragmentManager().beginTransaction()
                                .show(loginFrag).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(orderFrag).commit();
                    }
                    break;

                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction()
                            .show(homeFrag).commit();
                    break;

                case R.id.nav_act:
                    getSupportFragmentManager().beginTransaction()
                            .show(supplierFrag).commit();
                    break;

                case R.id.nav_cart:
                    getSupportFragmentManager().beginTransaction()
                            .show(cartFrag).commit();
                    break;

            }
        }
    }

    public ProfileFragment getProfileFrag() {
        return profileFrag;
    }

    public HomeFragment getHomeFrag() {
        return homeFrag;
    }

    public OrderFragment getOrderFrag() {
        return orderFrag;
    }

    public StoreFragment getStoreFrag() {
        return storeFrag;
    }

    public EventFragment getEventFrag() {
        return eventFrag;
    }

    public Button getButtonRegister() {
        return buttonRegister;
    }

    public FrameLayout getFramelayout() {
        return framelayout;
    }

    public BuyCartFragment getCartFrag() {
        return cartFrag;
    }

    public LoginFragment getLoginFrag() {
        return loginFrag;
    }


    public RegisterFragment getRegFrag() {
        return regFrag;
    }
    public ProdListFragment getProdListFrag() {
        return prodListFrag;
    }

    public SupplierFragment getSupplierFrag() {
        return supplierFrag;
    }

    public SProdModFragment getsProdModFrag() {
        return sProdModFrag;
    }

    public SProdAddFragment getsProdAddFrag() {
        return sProdAddFrag;
    }
}