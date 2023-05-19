package com.example.diancan.comm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class Funcs {

    public static Bitmap stringToBitmap(String s) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.getDecoder().decode(s);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            System.out.println("stringToBitmap error:");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String BitmapToString(Bitmap pic) {
        String picStr = null;
        ByteArrayOutputStream baos = null;

        try {
            if (pic != null) {
                baos = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                picStr = Base64.getEncoder().encodeToString(bitmapBytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return picStr;
    }

    public static String pic_data(Bitmap bitmap_uri) {
        String iB_string="";
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap_uri.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[] bytes=bStream.toByteArray();
        iB_string= Base64.getEncoder().encodeToString(bytes);
        return iB_string;
    }

    public static void hideFragment(FragmentManager fragmentManager) {
        List<Fragment> totalFrag = fragmentManager.getFragments();
        for(Fragment frag: totalFrag) {
            fragmentManager.beginTransaction().hide(frag).commit();
        }
    }

    public static boolean addFragment(FragmentManager fragmentManager, Fragment addFrag) {
        boolean isNewFrag = true;
        List<Fragment> totalFrag = fragmentManager.getFragments();
        for(Fragment frag: totalFrag) {
            if (frag.getId() == addFrag.getId()) {
                isNewFrag = false;
            }
        }
        Log.d("args", "isNewFrag=" + isNewFrag);
        if(isNewFrag) {
            fragmentManager.beginTransaction().add(addFrag,String.valueOf(addFrag.getId())).commit();
        }
        return isNewFrag;
    }

    public static void showFragment(FragmentManager fragmentManager, Fragment showFrag) {
        List<Fragment> totalFrag = fragmentManager.getFragments();
        FragmentTransaction trans = fragmentManager.beginTransaction();
        for(Fragment frag: totalFrag) {
            if (frag.getId() == showFrag.getId()) {
                trans.show(showFrag);
            } else {
                trans.hide(frag);
            }
        }
        trans.commit();
        Log.d("args", "showFrag=" + showFrag);
    }
}
