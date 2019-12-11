package com.flyer.chat.fragment;

import com.flyer.chat.network.CMSCallback;
import com.mob.cms.CMSSDK;
import com.mob.cms.Category;
import com.mob.cms.News;

import java.util.ArrayList;

/**
 * Created by mike.li on 2018/11/8.
 */

public class NotePresenter implements NoteContract.NotePresenter {
    private NoteContract.NoteView mView;

    public NotePresenter(NoteContract.NoteView mView) {
        this.mView = mView;
    }

    @Override
    public void getCategories() {

    }

    @Override
    public void getNews(Category category, String[] fields, int offset) {
        CMSSDK.getNews(category, News.ArticleType.NORMAL,null,offset,20,new CMSCallback<ArrayList<News>>(){
            @Override
            public void onSuccess(ArrayList<News> news) {
                super.onSuccess(news);
                mView.showNews(news);
            }
        });
    }
}
