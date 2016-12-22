package com.teamfingo.android.fingo.mypage;

import android.net.Uri;

import java.io.File;

/**
 * Created by taewon on 2016-12-16.
 */

public interface setProfileImage {

    void takePhoto();
    void getGallery();
    void getFacebookImage();
    void deleteImage();

    void sendImage(File file);
    void sendFacebookImage(String url);

    String getRealPathFromURI(Uri contentUri);

}
