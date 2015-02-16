package natour.issam.proyecto.es.proyecto_qiz;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class Login extends Activity {
    EditText Usuario;
    TextView contraseñaolvidada;
    Context context = this;
    Activity activity=this;
    Usuarios_Parse MetodosUsuariosParse;
    EditText Contraseña;
    private Dialog progressDialog;
    ImageView butonLoginNormal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inicio);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        ParseUser currentUser = ParseUser.getCurrentUser();
// si el usuario del juego esta conectado se salta la pantalla de login (si es de facebook)
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            IraMenuDejuego();
        }

// si el usuario del juego esta conectado se salta la pantalla de login (si es usuario normal)
        if(currentUser != null){
            IraMenuDejuego();

        }



        butonLoginNormal = (ImageView) findViewById(R.id.imageViewLogin);
        MetodosUsuariosParse = new Usuarios_Parse();
        Usuario = (EditText) findViewById(R.id.editText);
       // contraseñaolvidada = (TextView) findViewById(R.id.contraseñaolvidada);

       Contraseña = (EditText) findViewById(R.id.editText2);
/*
        contraseñaolvidada.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(context,ContrasenaOlvidada.class);
                startActivity(intent);
            }
        });
*/
        /* boton de login, recoge los campos user y pass y los comprueba....
         si se pincha una vez se pone false el click, para no abrir muchas ventanas,
        cuando el intent se abre, esta pantalla se pone Onpause y se libera el boton login (true)
         */

        butonLoginNormal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String textoUsuario = Usuario.getText().toString();
                String textoPassword = Contraseña.getText().toString();

                MetodosUsuariosParse.LogearUsuario(activity, textoUsuario, textoPassword);


            }
        });




    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        butonLoginNormal.setEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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

    /*
    Abre la pantalla de Registro...
     */
    public void RegistroButton(View view) {
        Intent intent = new Intent();
        intent.setClass(this, Registro.class);
        startActivity(intent);
    }

public void LoginTwitter(View view){


    ParseTwitterUtils.logIn(this, new LogInCallback() {

        @Override
        public void done(ParseUser user, ParseException err) {
            if (user == null) {

                Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
            } else if (user.isNew()) {
                user.put("Nivel", 1);
                user.put("Monedas",100);
                user.put("Diamantes",20);
                user.put("puntos",0);

                String objectId = user.getObjectId();
                MetodosSqlite metodosSqlite = new MetodosSqlite(activity.getBaseContext());
                metodosSqlite.crearUsuarioLite(user.getUsername(),objectId);
                metodosSqlite.mostrarUsuario();

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("name", ParseTwitterUtils.getTwitter().getScreenName());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                user.put("profile",jsonObj);

                user.saveInBackground();
                IraMenuDejuego();
                Log.d("MyApp", "User signed up and logged in through Twitter!");
            } else {
                IraMenuDejuego();
                Log.d("MyApp", "User logged in through Twitter!");
            }
        }
    });
}

/*
Metodo resultado del login de facebook
 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    /*
    Metodo del evento del boton de login con facebook, pido permisos por primera vez... y luego va a la pantalla del menu
    Si no es usuario nuevo, voy directamente al menu de juego
     */
    public void onLoginClick(View v) {
        progressDialog = ProgressDialog.show(Login.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team


        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                progressDialog.dismiss();
                if (user == null) {
                    Log.d("TAG", "Uh oh. The user cancelled the Facebook login.");

                } else if (user.isNew()) {
                    Log.d("TAG", "User signed up and logged in through Facebook!");
                    user.put("Nivel", 1);
                    user.put("Monedas",100);
                    user.put("Diamantes",20);
                    user.put("puntos",0);


                    String objectId = user.getObjectId();
                    MetodosSqlite metodosSqlite = new MetodosSqlite(activity.getBaseContext());
                    metodosSqlite.crearUsuarioLite(user.getUsername(),objectId);
                    metodosSqlite.mostrarUsuario();
                    user.saveInBackground();


                    Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {

                                      String emailFace=  String.valueOf(user.getProperty("email"));
                                        ParseUser usuarioParse=ParseUser.getCurrentUser();
                                        usuarioParse.put("email",emailFace);
                                        usuarioParse.saveInBackground();
                                        Log.i("EMAILFACE",emailFace);
                                    } else if (response.getError() != null) {
                                        if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                                                (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {

                                        } else {

                                        }
                                    }
                                }
                            }
                    );request.executeAsync();

                    IraMenuDejuego();
                } else {
                    Log.d("TAG", "User logged in through Facebook!");
                    IraMenuDejuego();
                }
            }
        });
    }
// metodo para ir al menu de juego, simplemente para simplificar...
    private void IraMenuDejuego() {
        Intent intent = new Intent(this, Panel_Inicio.class);
        startActivity(intent);

    }

}
