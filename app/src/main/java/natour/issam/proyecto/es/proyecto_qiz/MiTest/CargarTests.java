package natour.issam.proyecto.es.proyecto_qiz.MiTest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;

import natour.issam.proyecto.es.proyecto_qiz.ConnectSQLite;


public class CargarTests extends AsyncTask<LinkedList<Test>, Integer, LinkedList<Test>> {
    SQLiteDatabase db;
    ConnectSQLite connectSQLite;
    LinkedList<Test> Tests ;

    public CargarTests(Context context){
        connectSQLite = new ConnectSQLite(context);
        db=  connectSQLite.getWritableDatabase();
        Tests = new LinkedList<Test>();
    }


    public LinkedList<Test> cargandotests() {
        SQLiteDatabase database = db;
        Test test = null;
        String sqltest="Select * FROM TEST";
        Cursor cursorTest = database.rawQuery(sqltest,null);

        ArrayList<Pregunta> ListaPreguntas = null;
        if(cursorTest.moveToFirst()){
            do{
               ListaPreguntas = new ArrayList<Pregunta>();
                String SQLrespupreguntas="Select * FROM PREGUNTAS WHERE fid_preguntas="+cursorTest.getInt(cursorTest.getColumnIndex("id"));
                Cursor cursorPreguntas = database.rawQuery(SQLrespupreguntas,null);



        if (cursorPreguntas.moveToFirst()) {
            do {
                ArrayList<Respuestas> ListaRespuestas = new ArrayList<Respuestas>();
                String SQLrespuestas="Select * FROM RESPUESTAS WHERE id_preguntas="+cursorPreguntas.getInt(cursorPreguntas.getColumnIndex("id_preguntas"));


                Cursor cursorRespuestas = database.rawQuery(SQLrespuestas,null);

                if(cursorRespuestas.moveToFirst()){
                    do {

                        int id=cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("id_respuestas"));
                        String respuestatitulo=cursorRespuestas.getString(cursorRespuestas.getColumnIndex("respuesta"));
                        int solucionarespuesta= cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("solucion"));
                        int id_pregunta=cursorRespuestas.getInt(cursorRespuestas.getColumnIndex("id_preguntas"));

                        Respuestas respuestas = new Respuestas(id,respuestatitulo,solucionarespuesta,id_pregunta);
                        ListaRespuestas.add(respuestas);

                    }while (cursorRespuestas.moveToNext());
                    cursorRespuestas.close();
                }

                int id_pregunta=cursorPreguntas.getInt(cursorPreguntas.getColumnIndex("id_preguntas"));
                String titulopregunta = cursorPreguntas.getString(cursorPreguntas.getColumnIndex("titulo"));
                String categoriapregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("categoria"));
                String imagenpregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("imagen"));
                String tipopregunta=cursorPreguntas.getString(cursorPreguntas.getColumnIndex("tipo"));
                String consejo = cursorPreguntas.getString(cursorPreguntas.getColumnIndex("Consejo"));
                Pregunta pregunta = new Pregunta(id_pregunta,titulopregunta,categoriapregunta,imagenpregunta,tipopregunta,consejo,ListaRespuestas);
                ListaPreguntas.add(pregunta);

                Log.i("IDPREGUNTA",String.valueOf(pregunta.getId()));
            }while (cursorPreguntas.moveToNext());
        }

                cursorPreguntas.close();

                int idTest=cursorTest.getInt(cursorTest.getColumnIndex("id"));
                String nombre=cursorTest.getString(cursorTest.getColumnIndex("nombre"));;
                int  casilla=cursorTest.getInt(cursorTest.getColumnIndex("casilla"));;
                float puntuacion=cursorTest.getFloat(cursorTest.getColumnIndex("puntuacion"));
                int monedas=cursorTest.getInt(cursorTest.getColumnIndex("monedas"));;
                int diamantes=cursorTest.getInt(cursorTest.getColumnIndex("diamantes"));;
                float experiencia=cursorTest.getFloat(cursorTest.getColumnIndex("experiencia"));
                Date fecha=null;

                Test cargandotest = new Test(idTest,nombre,casilla,puntuacion,monedas,diamantes,experiencia,fecha,ListaPreguntas);
                Tests.add(cargandotest);
            } while (cursorTest.moveToNext());

        }

        cursorTest.close();
        database.close();

        return Tests;
    }


    @Override
    protected LinkedList<Test> doInBackground(LinkedList<Test>... linkedLists) {
        LinkedList<Test> testCargados=cargandotests();

        return testCargados;
    }

    @Override
    protected void onPostExecute(LinkedList<Test> tests) {
        tests=Tests;
        super.onPostExecute(tests);
    }
}
