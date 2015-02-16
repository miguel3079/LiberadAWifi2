package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.CargarTests;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;


public class Intent_Test_Juego extends ActionBarActivity {

    private int TIEMPOLIMITE=15;
    private int TIEMPOAUMENTADO=1;

    TextView Pregunta;
    TextView textoconsejo;
    ImageView imagenPregunta;
    Button a, b, c, d;
    private final int NOTIFICATION_ID = 1010;

    LinkedList<Test> Tests;
    BackgroundAsyncTask tiempo;
    int idTest=0;
    ProgressBar barratime;
    Button sumartiempo;
    Button botonconsejo;
    Button fiftyfifty;
    boolean Cancelar;
    boolean issumartiempo;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent__test__juego);
        //Inicializacion//

        issumartiempo=false;
        Pregunta = (TextView) findViewById(R.id.textoPregunta);
        textoconsejo = (TextView) findViewById(R.id.textoconsejo);
        imagenPregunta = (ImageView) findViewById(R.id.imagenpregunta);
        sumartiempo= (Button) findViewById(R.id.buttonsumartiempo);
        botonconsejo = (Button) findViewById(R.id.botonconejo);
        fiftyfifty = (Button) findViewById(R.id.botonfiftyfifty);
        a = (Button) findViewById(R.id.botonpreguntaA);
        b = (Button) findViewById(R.id.botonpreguntaB);
        c = (Button) findViewById(R.id.botonpreguntaC);
        d = (Button) findViewById(R.id.botonpreguntaD);

        sumartiempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sumartiempo.setEnabled(false);
                barratime.setMax(TIEMPOLIMITE+TIEMPOAUMENTADO);
                issumartiempo=true;
            }
        });

        botonconsejo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

           cargarConsejo(idTest);
                botonconsejo.setEnabled(false);
            }
        });

        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ayudamitadrespuesta(idTest);
            }
        });
        Bundle b = getIntent().getExtras();
        idTest = b.getInt("id");

        Tests = cargartests();


        cargarpregunta(idTest);
        activity= this;
        barratime = (ProgressBar) findViewById(R.id.progressBar);
        barratime.setProgress(0);
        barratime.setMax(15);
        tiempo = new BackgroundAsyncTask(activity);
        tiempo.execute();

        Cancelar=false;
    }

    private void parartiempo(){
        Cancelar=true;
        tiempo.cancel(true);
    }

    private void ayudamitadrespuesta(int numerodetest){
        int contador=0;
        for (int i=0;i<4;i++){

         if(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(i).isSolucion()==0 && contador<2){
           String selecion1=Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(i).getRespuesta();
                 Log.i("seleccion",selecion1);
             if(a.getText().equals(selecion1)){
                 a.setEnabled(false);
             }else if(b.getText().equals(selecion1)){
                 b.setEnabled(false);
             }else if(c.getText().equals(selecion1)){
                 c.setEnabled(false);
             }else if(c.getText().equals(selecion1)){
                 c.setEnabled(false);
             }

           contador++;
         }
     }

    }

    private void cargarConsejo(int numerodetest){
        String consejo =Tests.get(numerodetest).getPreguntas().get(0).getConsejo();
        textoconsejo.setText(consejo);
    }


    private LinkedList<Test> cargartests(){
        LinkedList<Test> todosTest = new LinkedList<Test>();
        CargarTests cargarTests = new CargarTests(this);

        cargarTests.execute();

        try {
            todosTest = cargarTests.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  todosTest;
    }

    private void cargarpregunta(int numerodetest){
        if(Tests.get(numerodetest).getPreguntas().get(0).getTipo().equals("texto")){
            Pregunta.setText(Tests.get(numerodetest).getPreguntas().get(0).getTitulo());
            imagenPregunta.setVisibility(View.INVISIBLE);

        }else if(Tests.get(numerodetest).getPreguntas().get(0).getTipo().equals("imagen")){
            String nameofdrawable=Tests.get(numerodetest).getPreguntas().get(0).getImagen();
            String titulopregunta=Tests.get(numerodetest).getPreguntas().get(0).getTitulo();
            Pregunta.setText(titulopregunta);
            int drawableResourceId = this.getResources().getIdentifier(nameofdrawable, "drawable", this.getPackageName());
            imagenPregunta.setImageResource(drawableResourceId);
}
        a.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(0).getRespuesta());
        b.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(1).getRespuesta());
        c.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(2).getRespuesta());
        d.setText(Tests.get(numerodetest).getPreguntas().get(0).getRespuestas().get(3).getRespuesta());
    }

    public void ComprobarA(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(0).isSolucion()==1) {
            Log.i("Boton A","TRUE");
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            parartiempo();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            finish();parartiempo();
        }

    }
    public void ComprobarB(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(1).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton B","TRUE");
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            finish();parartiempo();
        }

    }
    public void ComprobarC(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(2).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton c","TRUE");
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            finish();parartiempo();
        }

    }
    public void ComprobarD(View view){
        if (Tests.get(idTest).getPreguntas().get(0).getRespuestas().get(3).isSolucion()==1) {
            parartiempo();
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            Log.i("Boton d","TRUE");
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            parartiempo();
            finish();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intent__test__juego, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {
        Activity actividad;
        int myProgress;

        int TIEMPOMAXIMO=TIEMPOLIMITE;
        public  BackgroundAsyncTask(Activity activity){
            this.actividad=activity;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            Toast.makeText(Intent_Test_Juego.this,
                    "tiempo acabado", Toast.LENGTH_LONG).show();
            actividad.finish();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Toast.makeText(Intent_Test_Juego.this,
                    "onPreExecute", Toast.LENGTH_LONG).show();

            myProgress = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            while(myProgress<TIEMPOMAXIMO){
                if(Cancelar==true){
                    myProgress=TIEMPOMAXIMO;
                }else {

                    if(issumartiempo==true){
                        TIEMPOMAXIMO=TIEMPOAUMENTADO+TIEMPOLIMITE;

                    }
                    myProgress++;

                    publishProgress(myProgress);


                SystemClock.sleep(1000);}
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            barratime.setProgress(values[0]);
        }

    }




}

