package natour.issam.proyecto.es.proyecto_qiz;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.ParseFile;


public class Registro extends ActionBarActivity {
    Metodos metodos;

    EditText texto_nombre;
    EditText texto_contraseña1;
    EditText texto_contraseña2;
    EditText texto_email;
    CheckBox terminosBox;
    Usuario usuario;
    Usuarios_Parse MetodosUsuariosParse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        MetodosUsuariosParse = new Usuarios_Parse();

        texto_nombre = (EditText) findViewById(R.id.usuarioregistro);
        texto_contraseña1 = (EditText) findViewById(R.id.contraseña1registro);
        texto_contraseña2 = (EditText) findViewById(R.id.contraseña2registro);
        texto_email = (EditText) findViewById(R.id.emailregistro);
        terminosBox = (CheckBox) findViewById(R.id.checkBoxAceptarTerminos);

        metodos = new Metodos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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
Crear registro de usuario Parse normal, recojo los campos, Creo una imagen como default(R.drawable.bananadance
La imagen se tiene que colocar como Parsefile, por eso cambio de Drawable->Bytearray->Byte->Parsefile,
tras esto creo un Object usuario y lo inserto con los parametros INICIALES
 */
    public void CrearRegistro(View view) {
         boolean checkBoxTerminos=false;

        checkBoxTerminos = terminosBox.isChecked();

        String nombre = texto_nombre.getText().toString();
        String contraseña = texto_contraseña1.getText().toString();
        String contraseña2 = texto_contraseña2.getText().toString();
        String email = texto_email.getText().toString();

        if (!nombre.isEmpty() && !contraseña.isEmpty() && !contraseña2.isEmpty() && !email.isEmpty()) {
            if(Validarpassword(contraseña,contraseña2)) {
                if(checkBoxTerminos) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bananadance);

                        ParseFile photoFile = metodos.BitmapToParseFile(bitmap,"fotoinicio.png");

                    //PARAMETROS INICIALES: nombre,contraseña,email,monedas,diamantes,nivel,fotografia,puntos
                    usuario = new Usuario(nombre, contraseña, email, 100, 20, 1, photoFile, 0);
                    MetodosUsuariosParse.CrearUser(this, usuario);
                } else{
                    Toast.makeText(this, "Debes Aceptar Términos y condiciones", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "No has rellenado todos los campos, intentalo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean Validarpassword(String contraseña1,String contraseña2){
        boolean todoOk=false;
        if(contraseña1.equals(contraseña2)){
            todoOk=true;
        }else{
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            todoOk=false;
        }
        return  todoOk;
    }

// un finish(), xD haodndpnsadoduhadandipsandpa
    public void finishactivity(View view){
        finish();
    }
}
