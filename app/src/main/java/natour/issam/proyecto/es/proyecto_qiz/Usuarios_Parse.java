package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class Usuarios_Parse {

    public Usuarios_Parse(){

    }

    public void CrearUser(Activity activity,Usuario usuario){
        Thread crearusuario=new CrearUserParse(activity,usuario);
        crearusuario.start();

    }

class CrearUserParse extends Thread{
    Activity activity;
    Usuario usuario;

    public CrearUserParse(Activity activity,Usuario usuario){
        this.activity=activity;
        this.usuario=usuario;

    }

    public void run(){
        CrearUsuario(activity,usuario);
    }
    public void CrearUsuario(final Activity activity, Usuario usuario){

        if(Validaremail(usuario.getEmail())) { // if validar email es valido true


            final ParseUser user = new ParseUser();
            user.setUsername(usuario.getNombre());
            user.setPassword(usuario.getContraseña());
            user.setEmail(usuario.getEmail());
            user.put("Monedas", usuario.getMonedas());
            user.put("Diamantes", usuario.getDiamantes());
            user.put("Nivel", usuario.getNivel());
            user.put("Imagen", usuario.getImage());
            user.put("puntos",usuario.getPuntos());
            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("name", usuario.getNombre());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            user.put("profile",jsonObj);


            try {
                usuario.getImage().save();
            } catch (ParseException e) {
                e.printStackTrace();
            }



         final Usuario usuariocreado= usuario;
final ParseUser userfinal = user;
            user.signUpInBackground(new SignUpCallback() {
                public void done(final ParseException e) {
                    if (e == null) {
                        Log.i("USUARIOS", "usuario creado");
                        activity.runOnUiThread(new Runnable() {
                            public void run() {

                                MetodosSqlite metodosSqlite = new MetodosSqlite(activity.getBaseContext());
                                metodosSqlite.crearUsuarioLite(usuariocreado.getNombre(),userfinal.getObjectId());
                                metodosSqlite.mostrarUsuario();
                                Toast.makeText(activity.getBaseContext(), "Usuario creado ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Log.i("USUARIOS", "usuario NO CREADO");
                        activity.runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(activity.getBaseContext(), "Error :" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });



        }   else{
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getBaseContext(), "Email no valido", Toast.LENGTH_SHORT).show();
                }
            });
        } //fin validar email
    }
}

    public  boolean Validaremail(String email) {
        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailreg);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public void LogearUsuario(final Activity activity,String usuario,String contraseña){


        ParseUser.logInInBackground(usuario, contraseña, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    final Intent intent = new Intent();
                    intent.setClass(activity.getApplicationContext(),Panel_Inicio.class);
                    activity.startActivity(intent);
                    Toast.makeText(activity.getBaseContext(),"Usuario logeado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity.getBaseContext(),"Login fallido" +e.toString()+" codigo error:"+e.getCode(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    public void ContraseñaOlvidada(final Activity activity,String email){
        if(Validaremail(email)) {
            ParseUser.requestPasswordResetInBackground(email,
                    new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(activity.getBaseContext(), "El email fue enviado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity.getBaseContext(), "Hubo un fallo" + e.toString() + " codigo error:" + e.getCode(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else{
            Toast.makeText(activity.getBaseContext(), "Email incorrecto", Toast.LENGTH_LONG).show();   // end validar email
        }
    }



}
