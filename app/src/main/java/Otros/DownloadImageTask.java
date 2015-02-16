package Otros;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import natour.issam.proyecto.es.proyecto_qiz.Metodos;




public class DownloadImageTask extends AsyncTask<String, Void, ParseFile> {

    ParseFile photoFile;
    Metodos metodos;
    public DownloadImageTask(ParseFile photoFile) {
        //Parsefile nula
        this.photoFile=photoFile;
        metodos=new Metodos();
    }

    protected ParseFile doInBackground(String... urls) {

        // url es la url introducida en downloadImageTask.execute de getuser()
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            // lo leo
            InputStream in = new java.net.URL(urldisplay).openStream();
            //paso a Bitmap , Bytearray, Byte y YA TENGO MI PARSEFILE IMAGE DEL USUARIO DE facebook! yeah
            mIcon11 = BitmapFactory.decodeStream(in);
            Bitmap miredondo = metodos.redondearImagen(mIcon11);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            miredondo.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] image = stream.toByteArray();

            photoFile = new ParseFile("bananadance.gif",image);
            photoFile.getData();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return photoFile;
    }

    protected void onPostExecute(ParseFile result) {
        photoFile=result;
    }
}