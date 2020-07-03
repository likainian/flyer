package com.flyer.sharesdk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mike.li on 2019/1/9.
 */

public class ShareEntity implements Parcelable{
    private String shareTitle;
    private String shareUrl;
    private String shareContent;
    private String shareImgUrl;
    private String shareImgPath;

    public ShareEntity() {
    }

    protected ShareEntity(Parcel in) {
        shareTitle = in.readString();
        shareUrl = in.readString();
        shareContent = in.readString();
        shareImgUrl = in.readString();
        shareImgPath = in.readString();
    }

    public static final Creator<ShareEntity> CREATOR = new Creator<ShareEntity>() {
        @Override
        public ShareEntity createFromParcel(Parcel in) {
            return new ShareEntity(in);
        }

        @Override
        public ShareEntity[] newArray(int size) {
            return new ShareEntity[size];
        }
    };

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareImgUrl() {
        return shareImgUrl;
    }

    public void setShareImgUrl(String shareImgUrl) {
        this.shareImgUrl = shareImgUrl;
    }

    public String getShareImgPath() {
        return shareImgPath;
    }

    public void setShareImgPath(String shareImgPath) {
        this.shareImgPath = shareImgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shareTitle);
        dest.writeString(shareUrl);
        dest.writeString(shareContent);
        dest.writeString(shareImgUrl);
        dest.writeString(shareImgPath);
    }

    @Override
    public String toString() {
        return "ShareEntity{" +
                "shareTitle='" + shareTitle + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", shareImgUrl='" + shareImgUrl + '\'' +
                ", shareImgPath='" + shareImgPath + '\'' +
                '}';
    }
}
