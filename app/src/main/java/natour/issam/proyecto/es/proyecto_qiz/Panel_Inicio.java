package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.MiTest.CargarTests;
import natour.issam.proyecto.es.proyecto_qiz.MiTest.Test;


public class Panel_Inicio extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static int RESULT_LOAD_IMAGE = 1;

   Metodos metodos;

    SQLiteDatabase db;

    ProfilePictureView userProfilePictureView;
    ParseImageView ImagenParseUser;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    Context context = this;
    ImageView buttonPuntuacion;
    private MediaPlayer mediaPlayer;
    TextView textoNombre;
    TextView textonivel;
    private CharSequence mTitle;
    TextView monedas;
    TextView diamantes;
    ParseTwitterUtils utils;
    SharedPreferences appPrefs;
    SharedPreferences.Editor editablepref;
    ProgressBar barra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tomamenu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
            metodos = new Metodos();
        appPrefs = context.
                getSharedPreferences("Opciones",
                        Context.MODE_PRIVATE);
        editablepref= appPrefs.edit();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        ConnectSQLite connectSQLite=new ConnectSQLite(this);

         db = connectSQLite.getWritableDatabase();


        connectSQLite.createDataBase();
        connectSQLite.openDataBase();

        CargarTests cargarTests = new CargarTests(this);
        cargarTests.execute();
        try {
            LinkedList<Test> Tests = cargarTests.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textonivel=(TextView ) findViewById(R.id.textViewnivel);
        textoNombre=(TextView ) findViewById(R.id.textViewNombre);
        monedas=(TextView ) findViewById(R.id.textViewMonedas);
        diamantes=(TextView ) findViewById(R.id.textViewDiamantes);
        barra = (ProgressBar) findViewById(R.id.progressBar1);
        buttonPuntuacion = (ImageView) findViewById(R.id.imageViewRanking);
        mTitle = getTitle();



        //imagen de facebook del user logeado
        userProfilePictureView = (ProfilePictureView) findViewById(R.id.imageFb);

        ImagenParseUser =(ParseImageView ) findViewById(R.id.imageUser);


    // compruebo la session de facebook
        boolean usuarioTwitterLinked=false;
        ParseUser currentUser = ParseUser.getCurrentUser();
        Session session = ParseFacebookUtils.getSession();
        if(ParseTwitterUtils.getTwitter()==null) {
             usuarioTwitterLinked = ParseTwitterUtils.isLinked(currentUser);
        }

        comprobarYcargarusuario(currentUser,session,usuarioTwitterLinked);

        reproducirMusica();


        buttonPuntuacion.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final Intent intent = new Intent();
                intent.setClass(context,Puntuacion.class);
                startActivity(intent);
            }
        });


    }


    public void ensenarTablas(){
        String[] columnNames;
        Cursor c = db.rawQuery("SELECT * FROM mitable ",null);
        try {
           columnNames = c.getColumnNames();
        } finally {
            c.close();
        }

        for (int i=0;i<columnNames.length;i++){
            Log.i("Tabla",columnNames[i]);
        }
    }



 public void comprobarYcargarusuario(ParseUser currentUser,Session session,boolean usuarioTwitterLinked){
     // si es user de FACEBUK hacer lo siguiente:
     if (session != null && session.isOpened() && usuarioTwitterLinked==false) {
         refreshuser();
         makeMeRequest();
         ImagenParseUser.setVisibility(View.INVISIBLE);



     }
     // si es user DE PARSE hacer lo siguiente:
     if(currentUser != null && session==null && usuarioTwitterLinked==false){
         cargarusuarioNormal();
         userProfilePictureView.setVisibility(View.INVISIBLE);
         refreshuser();
        Calcularexp();
     }
      // si es user de Twitter hacer lo siguiente:
     if(currentUser != null && session==null && usuarioTwitterLinked==true){
         userProfilePictureView.setVisibility(View.INVISIBLE);
     }
 }
    public void refreshuser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        metodos.actualizarnivelconExp(currentUser);

        currentUser.fetchInBackground();
       int level = currentUser.getInt("Nivel");
      ;
        textonivel.setText(String.valueOf(level));

        currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseUser currUser = (ParseUser) object;
                    int level = currUser.getInt("Nivel");
                    Calcularexp();
                    textonivel.setText(String.valueOf(level));
                } else {
                }
            }
        });
    }


    public void Calcularexp(){
        ParseUser currentUser = ParseUser.getCurrentUser();

        int exp = currentUser.getInt("experiencia");

        int experienciaMaxima = metodos.getexpmaximadenivel(currentUser);
        int experienciaminima=metodos.getexpminimadenivel(currentUser);


        int actualexperienciaMAXIMA= experienciaMaxima-experienciaminima;
        int actualexperienciaACTUAL = exp-experienciaminima;

        Log.i("EXPACTUAL",String.valueOf(exp));
        Log.i("EXP MAX",String.valueOf(actualexperienciaMAXIMA));
        Log.i("EXP MIN",String.valueOf(metodos.getexpminimadenivel(currentUser)));

        barra.setMax(actualexperienciaMAXIMA);
        barra.setProgress(actualexperienciaACTUAL);


    }



public void reproducirMusica(){

Log.i("Dentro musica","dentro");
    boolean musicaActivada = appPrefs.getBoolean ("sonido", true);
    if(musicaActivada){
        mediaPlayer = MediaPlayer.create(this, R.raw.intheend);
        mediaPlayer.start();


}
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panel__inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/* Metodo-evento para cambiar la foto del perfil de  perfil y seleccionar una de la camara

 */
    @Override
    protected void onResume() {
        super.onResume();
        Session session = ParseFacebookUtils.getSession();
        ParseUser currentUser = ParseUser.getCurrentUser();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reproducirMusica();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null)
            mediaPlayer.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
        mediaPlayer.stop();
    }

    public void CerrarSesion(View v) {

        ParseUser.logOut();

        finish();
    }
    public void CambiarImagen(View view){

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);

    }
    /*
    Metodo que recibe la imagen selecionada en CambiarImagen() y procesa la imagen para subirla y guardarla en el Parse
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            ImagenParseUser.setImageBitmap(yourSelectedImage);

            byte[] image = stream.toByteArray();
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseFile photoFile = new ParseFile("fotoperfil"+currentUser.getUsername()+".gif",image);

            currentUser.put("Imagen", photoFile);
            currentUser.saveInBackground();
        }

    }
// si el usuario es un usario de facebook tengo que cargar sus datos con esto
    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {

                            JSONObject userProfile = new JSONObject();
                            try {

                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                if (user.getProperty("gender") != null) {
                                    userProfile.put("gender", user.getProperty("gender"));
                                }
                                if (user.getProperty("email") != null) {
                                    userProfile.put("email", user.getProperty("email"));
                                }


                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                              // muestro la informacion
                                updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d("TAG", "Error parsing returned user data. " + e);
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                    (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {

                                logout();
                            } else {

                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }




    private void logout() {
        // cerrar sesion usuario actual y cierra la pantalla
        ParseUser.logOut();
        finish();
    }
// muestra la informacion cargada en MAKEMEARQUEST
    private void updateViewsWithProfileInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.has("profile")) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.has("facebookId")) {
                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureView.setProfileId(null);
                }

                if (userProfile.has("name")) {
                    textoNombre.setText(userProfile.getString("name"));
                } else {
                    textoNombre.setText(userProfile.getString(""));
                }

                textonivel.setText(currentUser.get("Nivel").toString());
                monedas.setText(currentUser.get("Monedas").toString());
                diamantes.setText(currentUser.get("Diamantes").toString());

                userProfilePictureView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                Log.d("TAG", "Error parsing saved user data.");
            }
        }
    }
    // carga datos del usuario PARSE normal no facebook
    public void cargarusuarioNormal(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        ImagenParseUser.setParseFile(currentUser.getParseFile("Imagen"));

        textonivel.setText(currentUser.get("Nivel").toString());
        textoNombre.setText(currentUser.getUsername());
        monedas.setText(currentUser.get("Monedas").toString());
        diamantes.setText(currentUser.get("Diamantes").toString());
        ImagenParseUser.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

            }
        });


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
        }
    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tomamenu, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Panel_Inicio) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

public void gotoHorizontal(View view){
    final Intent intent = new Intent();
    intent.setClass(context,HorizontalListViewDemo.class);
    startActivity(intent);
}

}
