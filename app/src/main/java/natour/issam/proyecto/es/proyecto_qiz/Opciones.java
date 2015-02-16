package natour.issam.proyecto.es.proyecto_qiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.ToggleButton;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.util.concurrent.ExecutionException;

import Otros.DownloadImageTask;


public class Opciones extends ActionBarActivity {

    private static int RESULT_LOAD_IMAGE = 1;

Context context;

    ToggleButton sonido;
    ToggleButton notificaciones;
    EditText nombreuser;
    ParseImageView uacebookpicture;
    ParseImageView userpicture;
    SharedPreferences appPrefs;
    SharedPreferences.Editor editablepref;


    Metodos metodos;
    public static final boolean SONIDO = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context=this;
        appPrefs = context.getSharedPreferences("Opciones",
        Context.MODE_PRIVATE);
        editablepref= appPrefs.edit();


        sonido = (ToggleButton) findViewById(R.id.togglesonido);
        notificaciones = (ToggleButton) findViewById(R.id.togglenotificaciones);

        nombreuser = (EditText) findViewById(R.id.editText4);
        uacebookpicture = (ParseImageView) findViewById(R.id.perfilfacebookopciones);

        userpicture =(ParseImageView ) findViewById(R.id.perfiluseropciones);

        // cargar datos del usuario
        ParseUser currentUser = ParseUser.getCurrentUser();
        Session session = ParseFacebookUtils.getSession();
        boolean usuarioTwitterLinked= ParseTwitterUtils.isLinked(currentUser);
        comprobarYcargarusuario(currentUser,session,usuarioTwitterLinked);
        cargarpreferencias();





    }

    public void comprobarYcargarusuario(ParseUser currentUser,Session session,boolean usuarioTwitterLinked){
        // si es user de Facebook hacer lo siguiente:
        if (session != null && session.isOpened() && usuarioTwitterLinked==false) {
            userpicture.setVisibility(View.INVISIBLE);
            ActualizarInfoFacebook();
        }
        // si es user de Parse hacer lo siguiente:
        if(currentUser != null && session==null && usuarioTwitterLinked==false ){
            uacebookpicture.setVisibility(View.INVISIBLE);
            cargarusuarioNormal();
        }
        // si es user de Twitter hacer lo siguiente:
        if(currentUser != null && session==null && usuarioTwitterLinked==true){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
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


public void cargarpreferencias(){
    boolean sonidoOK = appPrefs.getBoolean ("sonido", true);
    sonido.setChecked(sonidoOK);
    Log.i("ESTADO SONIDO", String.valueOf(sonidoOK));

    boolean notificacionesestado = appPrefs.getBoolean("notificaciones",true);
    notificaciones.setChecked(notificacionesestado);
    Log.i("ESTADO NOTIFICACIONES", String.valueOf(sonidoOK));
}
    public void getSonido(View view){
       boolean estadosonido = sonido.isChecked();
         editablepref.putBoolean("sonido",estadosonido);
         editablepref.apply();

    }

    public void getNotificaciones(View view){
        boolean estadosonido = notificaciones.isChecked();
        editablepref.putBoolean("notificaciones",estadosonido);
        editablepref.apply();
    }

    public void cargarusuarioNormal(){
        metodos=new Metodos();

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseFile filefoto=currentUser.getParseFile("Imagen");

        Bitmap bitmap=null;
        try {
            byte [] bitedeFoto=filefoto.getData();
            bitmap = BitmapFactory.decodeByteArray(bitedeFoto,0,bitedeFoto.length);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userpicture.setImageBitmap(metodos.redondearImagen(bitmap));

        nombreuser.setText(currentUser.getUsername());

        userpicture.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {

            }
        });
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
            metodos=new Metodos();
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
            userpicture.setImageBitmap(metodos.redondearImagen(yourSelectedImage));

            byte[] image = stream.toByteArray();
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseFile photoFile = new ParseFile("fotoperfil"+currentUser.getUsername()+".gif",image);

            currentUser.put("Imagen", photoFile);
            currentUser.saveInBackground();
        }

    }

    private void logout() {
        // cerrar sesion usuario actual y cierra la pantalla
        ParseUser.logOut();
        finish();
    }
    // muestra la informacion cargada en MAKEMEARQUEST
    private void ActualizarInfoFacebook() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {


                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if(currentUser.has("profile"))

                        {
                            JSONObject userProfile = currentUser.getJSONObject("profile");
                            try {

                                if (userProfile.has("facebookId")) {

                                    ParseFile filenew = null;
                                    DownloadImageTask downloadImageTask = new DownloadImageTask(filenew);
                                    // recojo la imagen de la url de facebook, el parametro facebookid es del usuario topx
                                    // en el momento (i) del for
                                    String iduser = userProfile.getString("facebookId");
                                    Log.i("iduser", iduser);
                                    downloadImageTask.execute("https://graph.facebook.com/" + iduser + "/picture?type=large");
                                    ParseFile mIcon11 = downloadImageTask.get();

                                   nombreuser.setText(userProfile.getString("name"));


                                    uacebookpicture.setParseFile(mIcon11);
                                    uacebookpicture.loadInBackground();


                                } else {
                                    // Show the default, blank user profile picture
                                    uacebookpicture.setImageBitmap(null);
                                }
                                uacebookpicture.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                Log.d("TAG", "Error parsing saved user data.");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }




}
