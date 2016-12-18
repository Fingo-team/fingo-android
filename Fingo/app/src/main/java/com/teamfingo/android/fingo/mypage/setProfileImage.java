package com.teamfingo.android.fingo.mypage;

import android.net.Uri;

/**
 * Created by taewon on 2016-12-16.
 */

public interface setProfileImage {

    void takePhoto();
    void getGallery();
    void getFacebookImage();
    void deleteImage();

    String getRealPathFromURI(Uri contentUri);

}
