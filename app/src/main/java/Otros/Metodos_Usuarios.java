package Otros;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.mindrot.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import natour.issam.proyecto.es.proyecto_qiz.Conexion;

public class Metodos_Usuarios extends Conexion {
    Connection connection=null;
    Context context;
    Conexion conexion;
    public Metodos_Usuarios(Context context){
        this.context=context;
        conexion = new Conexion();
        conexion.execute();
    }

    public void llamarUsuario(Activity activity,String nombre,String contraseña,String email){
        Thread crearusuario= new Crearusuario(activity,nombre,contraseña,email);
        crearusuario.start();
    }


    class Crearusuario extends  Thread{
        String nombre,contraseña,email;

        Activity activity;
        public Crearusuario(Activity activity,String nombre,String contraseña,String email){
            this.nombre=nombre;
            this.contraseña=contraseña;
            this.email=email;
            this.activity=activity;
        }
        public void run(){
            CrearUsuario(nombre,contraseña,email);
        }
        public void CrearUsuario(String nombre,String contraseña,String email){

       String contraseñaHash;


            if(Validaremail(email)){
                if(ComprobarUsuario(activity,nombre)) {
                    if(ComprobarEmail(activity, email)) {


                            contraseñaHash = generarHASHpassword(contraseña);

                            Thread crearusuario = new CrearUser(nombre, contraseñaHash, email);
                            crearusuario.start();
                            activity.runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(activity.getBaseContext(), "Usuario creado!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }


            }else{

                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(activity.getBaseContext(),"Email no valido, introduce uno correcto",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }



        public Boolean ComprobarEmail(final Activity activity,String email) {


            String sqlgetid = "SELECT email FROM USUARIOS where email='"+email+"'";
            boolean Nodisponibles = false;
            ResultSet rs = null;
            Statement st = null;
          String emailrecogido = null;


            try {

                try {
                    st = conexion.get().createStatement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                rs = st.executeQuery(sqlgetid);
                while (rs.next()) {
                    emailrecogido=rs.getString("email");

                }
                final String emailresultado=emailrecogido;



                if(emailresultado==null){
                    Nodisponibles=true;
                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(activity.getBaseContext(),"Email disponible",Toast.LENGTH_SHORT).show();          }
                    });

                }else{
                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(activity.getBaseContext(),"El Email no esta disponible",Toast.LENGTH_SHORT).show();            }
                    });

                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
            return Nodisponibles;

        }

        public Boolean ComprobarUsuario(final Activity activity,String nombre) {
            String sqlgetid = "SELECT nombre FROM USUARIOS where nombre='"+nombre+"'";
            boolean Nodisponibles = false;
            ResultSet rs = null;
            Statement st = null;
            String nombrerecogido = null;

            try {
                try {
                    st = conexion.get().createStatement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                rs = st.executeQuery(sqlgetid);
                while (rs.next()) {
                    nombrerecogido=rs.getString("nombre");

                }


                if(nombrerecogido==null){

                    Nodisponibles=true;
                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(activity.getBaseContext(),"Nombre disponible",Toast.LENGTH_SHORT).show();          }
                    });
                }else{
                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(activity.getBaseContext(),"Nombre no disponible",Toast.LENGTH_SHORT).show();          }
                    });
                }

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return Nodisponibles;

        }

    public  boolean Validaremail(String email) {
        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailreg);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public String generarHASHpassword(String contraseña) {

        String generatedSecuredPasswordHash = BCrypt.hashpw(contraseña,
                BCrypt.gensalt(12));
        return generatedSecuredPasswordHash;

    }

    public boolean checkearValidacion(String usuario){
        boolean isValidado=false;
        String sqlgetid = "SELECT validado FROM USUARIOS where nombre='"+usuario+"'";
        ResultSet rs = null;
        Statement st = null;

        try {
            try {
                st = conexion.get().createStatement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            rs = st.executeQuery(sqlgetid);
            while (rs.next()) {
                isValidado = rs.getBoolean("validado");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isValidado;
    }



    public Boolean ComprobarUsuarioDelLogin(final Activity activity,String nombre) {
        String sqlgetid = "SELECT nombre FROM USUARIOS where nombre='"+nombre+"'";
        boolean Nodisponibles = false;
        ResultSet rs = null;
        Statement st = null;
        String nombrerecogido = null;

        try {
            try {
                st = conexion.get().createStatement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            rs = st.executeQuery(sqlgetid);
            while (rs.next()) {
                nombrerecogido=rs.getString("nombre");

            }


            if(nombrerecogido==null){

                Nodisponibles=true;

            }else {
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Nodisponibles;

    }

    public void llamarLogin(Activity activity,String usuario,String contraseña){
        if(ComprobarUsuarioDelLogin(activity,usuario)==false){
            Thread login = new Login(activity,usuario,contraseña);
            login.start();
        } else{
            Toast.makeText(activity.getBaseContext(),"El usuario no existe",Toast.LENGTH_SHORT).show();
        }

    }

class Login extends Thread{
    String usuario;
    String contraseña;
    Activity activity;
    public Login(Activity activity,String usuario,String contraseña){
        this.activity=activity;
        this.usuario=usuario;
        this.contraseña=contraseña;
    }
    public void run(){
        Login(usuario,contraseña);
    }

    public void Login(String usuario,String contraseña){

        if(checkearValidacion(usuario)){
            activity.runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(context,"Usuario validado",Toast.LENGTH_SHORT).show();                }
            });
            if(ComprobarPassword(usuario,contraseña)){
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(context,"Contraseña correcta, Login Ok",Toast.LENGTH_SHORT).show();                }
                });
            }else{
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(context,"Contraseña incorrecta,NO LOGEADO",Toast.LENGTH_SHORT).show();                }
                });
            }
        }else{

            activity.runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(context,"Usuario no validado, valida tu email",Toast.LENGTH_SHORT).show();                }
            });

        }
    }
}

    public  boolean ComprobarPassword(String usuario,String contraseña){
        String sqlgetid = "SELECT contraseña FROM USUARIOS where nombre='"+usuario+"'";
        String passwordHASH=null;
        ResultSet rs = null;
        Statement st = null;
        try {
            try {
                st = conexion.get().createStatement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            rs = st.executeQuery(sqlgetid);

            while (rs.next()) {
                passwordHASH = rs.getString("contraseña");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean matched = BCrypt.checkpw(contraseña, passwordHASH);
        return matched;
    }


    class CrearUser extends Thread{
        String nombre,contraseña,email;
        PreparedStatement ps = null;

        public CrearUser(String nombre,String contraseña,String email){
            this.nombre=nombre;this.contraseña=contraseña;this.email=email;
        }

        public void run(){
            CrearUsuario();
        }

        public void CrearUsuario(){

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String sqlPreguntas = "insert into USUARIOS(nombre,contraseña,email,nivel,fecha,validado) values (?,?,?,?,?,?)";

            try {
                try {
                    ps = conexion.get().prepareStatement(sqlPreguntas);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                ps.setString(1, nombre);
                ps.setString(2, contraseña);
                ps.setString(3, email);
                ps.setInt(4, 1);
                ps.setDate(5, sqlDate);
                ps.setBoolean(6, false);

                ps.executeUpdate();
                ps.close();

                Log.i("ENVIO OK","ENVIO OK");
            //   Enviar_email enviarmail=new Enviar_email(email, "Bienvenido a Quizmonster "+nombre,nombre,contraseña);
               // enviarmail.start();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("FALLO",e.toString());
            }
        }

    }



}


