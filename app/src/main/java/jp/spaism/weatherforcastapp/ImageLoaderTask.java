package jp.spaism.weatherforcastapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by spaism on 4/6/16.
 */
public class ImageLoaderTask extends AsyncTask<ImageView, Void, Bitmap> {

    private final Context context;

    Exception exception;
    ImageView imageView;

    public ImageLoaderTask(Context context){
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(ImageView... params) {
        imageView = params[0];
        String url = (String)imageView.getTag();
        try{
            return getImage(context, url);
        }catch(IOException e){
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    private static Bitmap getImage(Context context, String uri) throws IOException{

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);
        connection.connect();

        InputStream is = connection.getInputStream();
        return BitmapFactory.decodeStream(is);

    }
}
