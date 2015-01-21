package natour.issam.examen.es.proyecto_qiz;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Inicio extends Activity {
EditText Usuario;
    TextView msaludo;
   LoginButton loginButton;



   Facebook mFacebook;


    EditText Contraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mFacebook = new Facebook("598597040241505");
        loginButton = (LoginButton) findViewById(R.id.authButton);
        msaludo = (TextView) findViewById(R.id.mSaludo);

        Usuario = (EditText) findViewById(R.id.editText4);
        Contraseña = (EditText) findViewById(R.id.editText5);
        Log.i("creado","creado");


        ParseUser user = new ParseUser();
        user.setUsername("issam");
        user.setPassword("password");
        user.setEmail("issinatour90@gmail.com");

// other fields can be set just like with ParseObject
        user.put("telefono", "667477919");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }


/*
    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "natour.issam.examen.es.proyecto_qiz", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    */
    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
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

    public void RegistroButton(View view){
        Intent intent = new Intent();
        intent.setClass(this, Registro.class);
        startActivity(intent);

    }

    public void LogearButton(View view){
        String textoUsuario = Usuario.getText().toString();
        String textoPassword = Contraseña.getText().toString();

        Metodos_Usuarios metodos_usuarios = new Metodos_Usuarios(this);
        metodos_usuarios.llamarLogin(this,textoUsuario,textoPassword);
        Toast.makeText(this, "Comprobando datos...", Toast.LENGTH_SHORT).show();
    }


    public void onLOginFacebook(View v) {

        if(mFacebook.isSessionValid()){
//cerrar
        }else{
            //login
            AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner( mFacebook);
            mFacebook.authorize(this,new String[] { "email" },new Facebook.DialogListener() {
                @Override
                public void onComplete(Bundle values) {

                }

                @Override
                public void onFacebookError(FacebookError e) {

                }

                @Override
                public void onError(DialogError e) {

                }

                @Override
                public void onCancel() {

                }

            });
        }
    }




}
