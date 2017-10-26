package com.trekr.Common;

import android.support.v4.app.Fragment;

import com.trekr.App;

public class BaseFragment extends Fragment{
    @Override
    public void onDetach() {
        super.onDetach();
        App.getInstance().apiManager.cancelRequests();
    }
}
