package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;

import natour.issam.proyecto.es.proyecto_qiz.Tiempos.TiempoDeConsejos;

/**
 * Created by Issam on 28/01/2015.
 */
public class ConexionParse extends Application {
    TextView   tvTime;
    Context context;
    @Override

    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this,getString(R.string.ParseApId),getString(R.string.ParseClientKey));
        ParseFacebookUtils.initialize(getString(R.string.app_id));
        ParseTwitterUtils.initialize(getString(R.string.TwitterKey), getString(R.string.TwitterSecret));


    }
}
