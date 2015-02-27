package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.CargarTests;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;
import natour.issam.proyecto.es.proyecto_qiz.Tiempos.Premios_Aciertos;
import natour.issam.proyecto.es.proyecto_qiz.monstruos.Habilidades;


public class Intent_Test_Juego extends ActionBarActivity {
    MetodosSqlite metodosSqlite;
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
    Context context;
    int myProgress;

    /* Comprobar variables    */
    boolean istimeused=false;
    boolean isfiftyfiftyused=false;
    boolean isconsejoused=false;

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
        context=this;
        metodosSqlite = new MetodosSqlite(context);
        sumartiempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),3)>0){
                    sumartiempo.setEnabled(false);
                    barratime.setMax(TIEMPOLIMITE + TIEMPOAUMENTADO);
                    issumartiempo = true;
                    metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 3);
                    istimeused=true;
                    cargarHabilidades();
            }else {
                    Toast.makeText(context, "NO TIENES MAS TIME", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonconsejo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           if(metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),1)>0) {
               cargarConsejo(idTest);
               botonconsejo.setEnabled(false);
               isconsejoused=true;
               metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 1);
               cargarHabilidades();
           }else {
               Toast.makeText(context, "NO TE QUEDAN CONSEJOS", Toast.LENGTH_SHORT).show();
           }
            }
        });

        fiftyfifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(metodosSqlite.getCantidadFromUserAndIdhabilidad(ParseUser.getCurrentUser(),2)>0){
                    ayudamitadrespuesta(idTest);
                    metodosSqlite.restarhabilidaddeusuario(ParseUser.getCurrentUser(), 2);
                    isfiftyfiftyused=true;
                    cargarHabilidades();
                }else{
                    Toast.makeText(context, "NO TE QUEDAN FIFTYFIFTY", Toast.LENGTH_SHORT).show();
                }

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

        cargarHabilidades();
    }

    private void parartiempo(){
        Cancelar=true;
        tiempo.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        parartiempo();
    }

    private void TestCorrecto(){
        int tiempousado = myProgress;

        Intent intent = new Intent(this, Premios_Aciertos.class);
        intent.putExtra("tiempousado", tiempousado);
        intent.putExtra("istimeused", istimeused);
        intent.putExtra("isfiftyfiftyused", isfiftyfiftyused);
        intent.putExtra("isconsejoused", isconsejoused);
        startActivity(intent);
        finish();

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
            TestCorrecto();
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
            TestCorrecto();
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
            TestCorrecto();
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
            TestCorrecto();
        }else{
            Toast.makeText(this, "Respuesta fallida", Toast.LENGTH_SHORT).show();
            parartiempo();
            finish();

        }

    }

    public void cargarHabilidades(){
        ParseUser currenUser = ParseUser.getCurrentUser();
        int idmonster= metodosSqlite.getidmonstruoactual(currenUser);
        ArrayList<Habilidades> habilidad = metodosSqlite.seleccionarhabilidadesdemonstruoid(idmonster);



        sumartiempo.setVisibility(View.GONE);
        botonconsejo.setVisibility(View.GONE);
        fiftyfifty.setVisibility(View.GONE);

        for(int i=0;i<habilidad.size();i++){
            if(habilidad.get(i).getId()==1){

                TextView consejoview = (TextView)findViewById(R.id.textocantidaddeconsejos);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());

                consejoview.setText(String.valueOf(cantidad));
                botonconsejo.setVisibility(View.VISIBLE);
            } else if(habilidad.get(i).getId()==2){
                TextView fiftyfiftyview = (TextView)findViewById(R.id.textocantidaddefiftyfifty);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());

                fiftyfiftyview.setText(String.valueOf(cantidad));
                fiftyfifty.setVisibility(View.VISIBLE);
            }else if((habilidad.get(i).getId()==3)){
                TextView textotimeview = (TextView)findViewById(R.id.textocantidaddetiempo);
                int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(i).getId());
                sumartiempo.setVisibility(View.VISIBLE);
                textotimeview.setText(String.valueOf(cantidad));
            }

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
     class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {
        Activity actividad;


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
                if (isCancelled())
                    break;

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
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            barratime.setProgress(values[0]);
        }

    }




}

