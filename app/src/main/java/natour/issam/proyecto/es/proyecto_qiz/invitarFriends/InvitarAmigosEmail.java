package natour.issam.proyecto.es.proyecto_qiz.invitarFriends;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Otros.Enviar_email;
import natour.issam.proyecto.es.proyecto_qiz.R;

public class InvitarAmigosEmail extends ActionBarActivity {


    EditText textoPara;
    EditText textoAsunto;
    EditText textoMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar_amigos_email);


        textoPara=(EditText) findViewById(R.id.mailTextPara);
        textoAsunto = (EditText) findViewById(R.id.mailTextAsunto);
        textoMensaje=(EditText) findViewById(R.id.mailTextMensaje);
        rellenarDatosEmail();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitar_amigos_email, menu);
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

    public void enviarEmail(View view){
        String mailReceptor = textoPara.getText().toString();
        String asunto = textoAsunto.getText().toString();
        String mensaje = textoMensaje.getText().toString();
        Thread enviarEmail= new Enviar_email(mailReceptor,asunto,mensaje);
        enviarEmail.start();
        Toast.makeText(this.getApplicationContext(),
                "Mensaje enviado",
                Toast.LENGTH_SHORT).show();
        finish();
    }


    public void rellenarDatosEmail(){
        ParseUser currenUser= ParseUser.getCurrentUser();

        String nombreUsuario=null;
        JSONObject ProfileJSON=currenUser.getJSONObject("profile");
        try {
            nombreUsuario= ProfileJSON.get("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String asunto=getString(R.string.asuntomailinvitar);

        String Mensaje=getString(R.string.mensajemailinvitar)+" "+nombreUsuario;


        textoMensaje.setText(Mensaje);
        textoAsunto.setText(asunto);

    }
}
