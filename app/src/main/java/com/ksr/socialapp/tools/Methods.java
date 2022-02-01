package com.ksr.socialapp.tools;

import android.app.Activity;
import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ksr.socialapp.R;


public class Methods {

    public static void hideSoftKeyboard(Activity paramActivity) {
        InputMethodManager localInputMethodManager = (InputMethodManager) paramActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View localView = paramActivity.getCurrentFocus();
        if (localView != null)
            Log.e("boolean", String.valueOf(localInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 0)));
    }


    public static boolean togglePasswordVisibility(Context context, MotionEvent event, EditText passwordET) {
        boolean eventConsumed = false;

        if (context == null || passwordET == null || event == null)
            return eventConsumed;


        final int DRAWABLE_RIGHT = 2;
        try {
            if (event.getAction() == MotionEvent.ACTION_UP && passwordET.getCompoundDrawables() != null && passwordET.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                if (event.getRawX() >= (passwordET.getRight() - passwordET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (passwordET.getTransformationMethod() instanceof PasswordTransformationMethod) {
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                        passwordET.setTransformationMethod(null);
                    } else {
                        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                        passwordET.setTransformationMethod(new PasswordTransformationMethod());
                    }

                    event.setAction(MotionEvent.ACTION_CANCEL);
                    eventConsumed = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventConsumed;
    }

}
