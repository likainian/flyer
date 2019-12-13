package com.flyer.chat.fragment;

import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.base.BaseContract;
import com.flyer.chat.bean.MapUser;

import java.util.List;

/**
 * Created by mike.li on 2018/11/7.
 */

public class HomeContract {
    interface HomeView extends BaseContract.BaseView{
        void showUserInfo(User user);
        void showMapUser(List<MapUser> mapUsers);
    }
    interface HomePresenter extends BaseContract.BasePresenter{
        void getUserInfo();
        void updateMapUser(String mapId,double latitude,double longitude);
        void searchMapUser(double latitude,double longitude);
    }
}
