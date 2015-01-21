package natour.issam.examen.es.proyecto_qiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.util.concurrent.ExecutionException;


public class Registro extends ActionBarActivity {

EditText texto_nombre;
EditText texto_contraseña;
EditText texto_email;

    Metodos_Usuarios metodos_usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);




        texto_nombre = (EditText) findViewById(R.id.editText);
        texto_contraseña = (EditText) findViewById(R.id.editText2);
        texto_email = (EditText) findViewById(R.id.editText3);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CrearRegistro(View view){
        String nombre=texto_nombre.getText().toString();
        String contraseña=texto_contraseña.getText().toString();
        String email=texto_email.getText().toString();

       Metodos_Usuarios metodos_usuarios = new Metodos_Usuarios(this);


        metodos_usuarios.llamarUsuario(this,nombre,contraseña,email);
        Toast.makeText(this, "Comprobando datos....", Toast.LENGTH_SHORT).show();
    }

    public void finishactivity(View view){
        finish();
    }
}
