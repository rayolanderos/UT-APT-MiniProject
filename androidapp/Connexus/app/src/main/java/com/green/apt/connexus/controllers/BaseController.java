package com.green.apt.connexus.controllers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by memo on 16/10/17.
 * Extend from this class if you create a new Controller.
 * Controllers manage both the models and the views (user actions)
 * Push common logic among controllers to this BaseController as you see fit.
 */

public abstract class BaseController {

    private static final String BASE_URL = "https://apt-miniproject-greenteam-v2.appspot.com/";
    private static final String API_URL = "https://apt-miniproject-greenteam-v2.appspot.com/api/";

    private Activity activity;

    public BaseController(Activity activity) {
        this.activity = activity;
    }

    protected Activity getActivity() {
        return activity;
    }

    protected String getAbsoluteAPIUrl(String relativeUrl) {
        return API_URL + relativeUrl;
    }

    protected String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void hideKeyboard(){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
