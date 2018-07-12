package com.flyer.chat.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

/**
 * Created by mike.li on 2018/5/7.
 */

public abstract class BaseFragment extends Fragment implements BaseContract.BaseView {

    @Override
    public boolean isActive() {
        return !this.isDetached()&&this.isAdded();
    }

    protected void addChildFragment(Fragment fragment, @IdRes int layoutFragmentId) {
        if (null == fragment) {
            return;
        }
        this.getChildFragmentManager().beginTransaction().add(layoutFragmentId, fragment).commit();
    }

    @Override
    public void showLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showLoadingDialog();
        }
    }

    @Override
    public void showLoadingDialog(boolean isCancel, String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showLoadingDialog(isCancel,message);
        }
    }

    @Override
    public void closeLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.closeLoadingDialog();
        }
    }
}
