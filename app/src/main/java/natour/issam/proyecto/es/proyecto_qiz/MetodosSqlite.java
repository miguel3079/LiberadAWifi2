package natour.issam.proyecto.es.proyecto_qiz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import natour.issam.proyecto.es.proyecto_qiz.monstruos.Monstruos;


public class MetodosSqlite {
    SQLiteDatabase db;
    ConnectSQLite connectSQLite;

    public MetodosSqlite(Context context) {
        connectSQLite = new ConnectSQLite(context);
        db = connectSQLite.getWritableDatabase();
    }

    public void crearUsuarioLite(String usuario,String id) {

        db.beginTransaction();

        db.execSQL("INSERT INTO USUARIOS "
                       + "VALUES " + "(null,'"+usuario+"','"+id+"')");
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public ArrayList<Monstruos> getMonstruos(){
        ArrayList<Monstruos> mismonstruos = new ArrayList<Monstruos>();
        String SQLrespuestas = "Select * FROM MONSTRUOS ";
        int posicion=0;


        Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);
        String [] nombreMonstruos=new String[cursormonstruos.getCount()];
        if (cursormonstruos.moveToFirst()) {
            do {
                int id=cursormonstruos.getInt(cursormonstruos.getColumnIndex("id"));
                String nombre= cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));
                int monedas= cursormonstruos.getInt(cursormonstruos.getColumnIndex("monedas"));
                int diamantes =    cursormonstruos.getInt(cursormonstruos.getColumnIndex("diamantes"));
                int escomprado = cursormonstruos.getInt(cursormonstruos.getColumnIndex("escomprado"));

                Monstruos monstruo = new Monstruos(id,nombre,monedas,diamantes,escomprado);
                mismonstruos.add(monstruo);

                nombreMonstruos[posicion]=cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));

                posicion++;
            } while (cursormonstruos.moveToNext());
            cursormonstruos.close();

        }

return mismonstruos;

    }

    public String[] getNombreDeMonstruos(){
        String SQLrespuestas = "Select * FROM MONSTRUOS ";
       int posicion=0;

        Cursor cursormonstruos = db.rawQuery(SQLrespuestas, null);
        String [] nombreMonstruos=new String[cursormonstruos.getCount()];
        if (cursormonstruos.moveToFirst()) {
            do {

                nombreMonstruos[posicion]=cursormonstruos.getString(cursormonstruos.getColumnIndex("nombre"));

                posicion++;
            } while (cursormonstruos.moveToNext());
            cursormonstruos.close();

        }

        return nombreMonstruos;
    }

    public void mostrarUsuario() {
        String SQLrespuestas = "Select * FROM USUARIOS ";


        Cursor cursorGetuser = db.rawQuery(SQLrespuestas, null);

        if (cursorGetuser.moveToFirst()) {
            do {

                Log.i("NOMBRE",cursorGetuser.getString(cursorGetuser.getColumnIndex("nombre"))+" "+cursorGetuser.getString(cursorGetuser.getColumnIndex("objectId")));
            } while (cursorGetuser.moveToNext());
            cursorGetuser.close();

        }

    }
}
