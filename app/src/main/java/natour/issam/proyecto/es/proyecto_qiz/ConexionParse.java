package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;

/**
 * Created by Issam on 28/01/2015.
 */
public class ConexionParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this,getString(R.string.ParseApId),getString(R.string.ParseClientKey));
        ParseFacebookUtils.initialize(getString(R.string.app_id));
        ParseTwitterUtils.initialize(getString(R.string.TwitterKey), getString(R.string.TwitterSecret));

    }
}
