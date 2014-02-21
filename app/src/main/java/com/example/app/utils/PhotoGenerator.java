package com.example.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Alexander on 20.02.14.
 */
public class PhotoGenerator {

    private File mDirectory;

    public String mCurrentPhotoPath;

    private static PhotoGenerator mPhotoGenerator;

    private PhotoGenerator(String folderName) {
        // create folder in sdCard
        createDirectory(folderName);
    }

    public static PhotoGenerator getPhotoGenerator(String folderName) {
        if(mPhotoGenerator == null) {
            return new PhotoGenerator(folderName);
        }
            return mPhotoGenerator;
    }

    public void createDirectory(String folderName) {
        mDirectory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                folderName);
        if (!mDirectory.exists())
            mDirectory.mkdirs();
    }


    public Uri generateFileUri(int type) {
        File file = null;
        switch (type) {
            case Constants.REQUEST_IMAGE_CAPTURE:
                file = new File(mDirectory.getPath() + "/" + "photo_"
                        + System.currentTimeMillis() + ".jpg");
                break;
        }
        // save value
        mCurrentPhotoPath = file.getPath();
        return Uri.fromFile(file);
    }

    public Bitmap getPicture(int width, int height) {
        // Get the dimensions of the View
        int targetW = width;
        int targetH = height;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Log.d(Constants.MY_LOG, "DetailActivity.SetPic() PathFile =  " + mCurrentPhotoPath);
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }
}
