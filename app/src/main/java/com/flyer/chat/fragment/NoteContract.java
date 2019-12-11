package com.flyer.chat.fragment;

import com.flyer.chat.base.BaseContract;
import com.mob.cms.Category;
import com.mob.cms.News;

import java.util.ArrayList;

/**
 * Created by mike.li on 2018/11/8.
 */

public class NoteContract {
    interface NoteView extends BaseContract.BaseView{
        void showCategories(ArrayList<Category> categories);
        void showNews(ArrayList<News> news);
    }
    interface NotePresenter extends BaseContract.BasePresenter{
        void getCategories();
        void getNews(Category category, String[] fields,int offset);
    }
}
