package com.flyer.chat.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

/**
 * Created by mike.li on 2018/5/7.
 */

public abstract class BaseFragment extends Fragment{

    protected void addChildFragment(Fragment fragment, @IdRes int layoutFragmentId) {
        if (null == fragment) {
            return;
        }
        this.getChildFragmentManager().beginTransaction().add(layoutFragmentId, fragment).commit();
    }

    public void showLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showLoadingDialog();
        }
    }

    public void showLoadingDialog(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showLoadingDialog(message);
        }
    }

    public void closeLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.closeLoadingDialog();
        }
    }
}
