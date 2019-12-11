package com.flyer.chat.base;

/**
 * Created by mike.li on 2018/4/27.
 */

public class BaseContract {
    public interface BaseView{
        void showLoadingDialog();
        void showLoadingDialog(String message);
        void closeLoadingDialog();
    }
    public interface BasePresenter{
    }
}
