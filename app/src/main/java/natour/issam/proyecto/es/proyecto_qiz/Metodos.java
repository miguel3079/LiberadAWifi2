package natour.issam.proyecto.es.proyecto_qiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Issam on 03/02/2015.
 */
public class Metodos {

    public Metodos(){

    }

//Redondea imagen Bitmap
    public Bitmap redondearImagen(Bitmap scaleBitmapImage) {
        int targetWidth = 450;
        int targetHeight = 450;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public ParseFile BitmapToParseFile(Bitmap bitmap,String nombreImagen){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile imagen = new ParseFile(nombreImagen,image);


        return  imagen;
    }

    public int getexpmaximadenivel(ParseUser currentUser){

        int nivel = currentUser.getInt("Nivel");
        int expmaxima = 0;
        for (int i = 1; i <= nivel; i++) {
            expmaxima += i * 100;

        }

        Log.i("EXPMAX",String.valueOf(expmaxima));
        return  expmaxima;
    }

    public int getexpminimadenivel(ParseUser currentUser){

        int nivel = currentUser.getInt("Nivel");
        int expmaxima = 0;
        int expminima = 0;
        for (int i = 1; i <= nivel; i++) {
            expmaxima += i * 100;
            if(i!=0) {
                expminima = expmaxima - ((i * 100) - 1);
            }

        }

        Log.i("EXPMNIN",String.valueOf(expminima));
        return  expminima;
    }

    public boolean actualizarnivelconExp(ParseUser currentUser) {
        boolean isdone = false;
        int nivel = 100;

        int experienciaactual = currentUser.getInt("experiencia");
        int expMinima = 0;
        int expmaxima = 0;
        for (int i = 1; i <= nivel; i++) {
            expmaxima += i * 100;

            if(i!=0) {
                expMinima = expmaxima - ((i * 100) - 1);
            }
            Log.i("nivel ", String.valueOf(i));
            Log.i("experienciaminima ", String.valueOf(expMinima));
            Log.i("experienciamaxima ", String.valueOf(expmaxima));
            if (experienciaactual >= expMinima && experienciaactual <= expmaxima) {
                Log.i("dentro", "dentroactualizar " + i);
                currentUser.put("Nivel", i);
                try {
                    currentUser.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                isdone = true;
            }
        }

return  isdone;
    }
}

