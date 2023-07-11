package com.example.diancan.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diancan.MainActivity;
import com.example.diancan.R;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mainView;
    private TextView textViewEmail,textViewPasswd,textViewID,textViewName;
    private int PICK_IMAGE_REQUEST;
    private TextView textViewEdit;
    private ImageView imageViewPic;
    private Button btn_logout;

    public ProfileFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    private LoginFragment.IListener myListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("fragMsg","onAttach");
        myListener = (LoginFragment.IListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textViewEdit = (TextView)view.findViewById(R.id.textView_edit);
        imageViewPic = (ImageView)view.findViewById(R.id.imageView_pic);

        textViewEmail = (TextView) view.findViewById(R.id.textView_email);
        textViewPasswd = (TextView)view.findViewById(R.id.textView_passwd);
        textViewID = (TextView) view.findViewById(R.id.textView_id);
        textViewName = (TextView)view.findViewById(R.id.textView_name);

        btn_logout = (Button) view.findViewById(R.id.btn_logout);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 啟動選擇圖片的 Intent
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 獲取選擇的圖片並顯示在 ImageView 中
            Uri selectedImageUri = data.getData();
            imageViewPic.setImageURI(selectedImageUri);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", 0);
        String email = sharedPreferences.getString("email", "");
        String passwd = sharedPreferences.getString("password", "");
        String id = sharedPreferences.getString("id", "");
        String name = sharedPreferences.getString("name", "");
        Log.d("main","pst = "+email+passwd+id+name);
        textViewEmail.setText(email);
        textViewPasswd.setText(passwd);
        textViewID.setText(id);
        textViewName.setText(name);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("userInfo", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("autologin",false);

                TextView navAct = (TextView) getActivity().findViewById(R.id.nav_act);
                navAct.setText("活動資訊");
                myListener.setUserLogin(0);
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
                getActivity().findViewById(R.id.nav_home).callOnClick();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}


