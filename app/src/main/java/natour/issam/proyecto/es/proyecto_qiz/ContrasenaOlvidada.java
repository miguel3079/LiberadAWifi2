package natour.issam.proyecto.es.proyecto_qiz;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ContrasenaOlvidada extends ActionBarActivity {
    Usuarios_Parse MetodosUsuariosParse;
    EditText textoEmail;
    Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena_olvidada);
        textoEmail = (EditText) findViewById(R.id.textomailforgotpass);
        MetodosUsuariosParse = new Usuarios_Parse();
        buttonBack = (Button) findViewById(R.id.VolverContrasenaOlvidada);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contrasena_olvidada, menu);
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

    public void EnviarEmailContraseñaOlvidada(View view) {
        String emailRecogido="";
        if (textoEmail.getText()!=null){
           emailRecogido = textoEmail.getText().toString();
    }
        MetodosUsuariosParse.ContraseñaOlvidada(this,emailRecogido);
    }

}
