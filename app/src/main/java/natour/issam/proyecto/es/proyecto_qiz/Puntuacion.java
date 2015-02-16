package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;


import Otros.DownloadImageTask;


public class Puntuacion extends Activity {
    Button back;
    ListView VistaDePuntuaciones;
    ArrayList<ListaDePuntuaciones> arraydir ;
    ListaDePuntuacionesAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_puntuacion);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // listview de la vista de puntos
        VistaDePuntuaciones = (ListView) findViewById(R.id.listadirectivos);
        //array al que añado cada user, luego limpio el array por si tiene algo
        arraydir =  new ArrayList<ListaDePuntuaciones>();
        arraydir.clear();
        // adaptador PERSONALIZADO en layout itemlista.xml creado en ListadepuntuacionesAdapter
        adaptador = new ListaDePuntuacionesAdapter(this,arraydir);
        // adaptador inicializado
        ListaDePuntuacionesAdapter adapter = new ListaDePuntuacionesAdapter(this, arraydir);


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
        finish();

            }
        });

        getUsers();
    }
/*
Metodo esencial by issam para recoger los usuarios TOP X donde x es Setlimit(), ORDERBY puntos descendent,
Cojo los nombres, las fotos, los puntos en la query, y lo inserto en el array y en el adaptador personalizado
TIP: si el usuario es un user de facebook NO TIENE imagen PARSE, por eso si PARSEFILE fileobject == null,
tengo que llamar a la clase DownloadImageTask y coger la imagen.

 */
    public void getUsers(){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByDescending("puntos");
        query.setLimit(5);

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                DescargarimagenFace descargas;
                if (e == null) {
                    //inicializo el json para recoger los datos de la columna profile de Parse
                JSONObject jsonObj = new JSONObject();

                    String nombre = null;
                    ListaDePuntuaciones listaDePuntos;
                    Metodos metodos= new Metodos();
                    for(int i=0;i<objects.size();i++){
                        // seleciono el profile
                        jsonObj =objects.get(i).getJSONObject("profile");
                        try {
                            // cojo el nombre del usuario actual i del top i
                             nombre= jsonObj.get("name").toString();
                            Log.i("JSON",nombre);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        ParseFile fileObject = (ParseFile) objects.get(i).getParseFile("Imagen");
                        if(fileObject==null) {
                          Log.i("Fbid","objeto nulo");
                                try {
                                    // si el fileobject == null recojo su id de facebook
                                    String faceBookid = jsonObj.get("facebookId").toString();
                                    Log.i("Fbid", faceBookid);
                                    // ParseFile vacio para llenar luego en DownloadimageTask
                                    ParseFile filenew = null;

                                    descargas= new DescargarimagenFace(filenew);

                                    fileObject=  descargas.execute("https://graph.facebook.com/" + faceBookid + "/picture?type=large").get();

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                } catch (ExecutionException e1) {
                                    e1.printStackTrace();
                                }

                        }

                        // relleno mi adaptador con: FOTO, Nombre, Puntos y TOP, donde el top es i+1
                        listaDePuntos = new ListaDePuntuaciones(fileObject,nombre,String.valueOf("Puntuacion: "+objects.get(i).getInt("puntos")),String.valueOf(i+1));
                        // añado al array el resultado
                        arraydir.add(listaDePuntos);

                        VistaDePuntuaciones.setAdapter(adaptador);
                        VistaDePuntuaciones.refreshDrawableState();
                    } // end for
                    // esto ya se sabe

                } else {

                }
            } // end done
        }); // end query
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puntuacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class DescargarimagenFace extends AsyncTask<String, Void, ParseFile> {

        ParseFile photoFile;
        Metodos metodos;
        public DescargarimagenFace(ParseFile photoFile) {
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

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.PNG, 100, stream);

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

}
