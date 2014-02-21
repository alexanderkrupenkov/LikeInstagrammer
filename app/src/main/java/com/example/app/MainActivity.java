package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.app.utils.Constants;
import com.example.app.utils.PhotoGenerator;


public class MainActivity extends ActionBarActivity {

    private PhotoGenerator mPhotoGenerator;

    private ImageView mDetailPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
             getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        mPhotoGenerator = PhotoGenerator.getPhotoGenerator("TestPhoto");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_send_email) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("application/image");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"alexanderkrupenkov@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mPhotoGenerator.mCurrentPhotoPath));
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.d(Constants.MY_LOG, "Intent is null"    );

                    Bitmap bitmap = mPhotoGenerator.getPicture(mDetailPhotoView.getWidth(),
                            mDetailPhotoView.getHeight());
                    mDetailPhotoView.setImageBitmap(bitmap);

                } else {
                    Log.d(Constants.MY_LOG, "Photo uri: " + data.getData());

                    Bundle bndl = data.getExtras();
                    if (bndl != null) {
                        Object obj = data.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            Log.d(Constants.MY_LOG, "bitmap " + bitmap.getWidth() + " x "
                                    + bitmap.getHeight());
                            mDetailPhotoView.setImageBitmap(bitmap);
                        }
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(Constants.MY_LOG, "Canceled");
            }
        }

    }

    public void onClickGetPhoto(View v) {
        PlaceholderFragment placeholderFragment = (PlaceholderFragment) getSupportFragmentManager()
            .findFragmentById(R.id.container);
        mDetailPhotoView = (ImageView) placeholderFragment.getView().findViewById(R.id.imageViewPhoto);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoGenerator.generateFileUri(Constants.REQUEST_IMAGE_CAPTURE));
        startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
        Log.d(Constants.MY_LOG, "Button Clicked = ");
    }

}
