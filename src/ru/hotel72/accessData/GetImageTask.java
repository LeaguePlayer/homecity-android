package ru.hotel72.accessData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import ru.hotel72.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 27.01.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class GetImageTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private ImageView view;
    private int id;
    private int w;
    private int h;
    private Bitmap bitmap;
    private String loadUrl;

    public GetImageTask(Context context, ImageView view, int id, int w, int h) {
        this.context = context;
        this.view = view;
        this.id = id;
        this.w = w;
        this.h = h;
        loadUrl = "http://hotel72.ru/index.php/api/GetFile?filename=%s&for=%s";
//        loadUrl = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&h=%d&zc=1&q=90";
    }

    @Override
    protected Void doInBackground(String... strings) {
        String imgName = strings[0];
        if(imgName == null){
            return null;
        }

        File img = getFile(id, imgName);
        if(img.exists()){
            bitmap = loadImage(img);
            view.setImageBitmap(bitmap);
        }
        else {

            try {
                URL url = new URL(String.format(loadUrl, imgName, "original"));
//                URL url = new URL(String.format(loadUrl, imgName, w, h));

                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    saveImage(bitmap, img);
                    view.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    private File getFile(int id, String imgName){
        String path = "/hotel72/img/%d/%s";
        path = String.format(path, id, imgName);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path);
    }

    private void saveImage(Bitmap bmp, File file) {
        try {
            file.mkdirs();
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImage(File file){
        return BitmapFactory.decodeFile(file.getPath());
    }
}
