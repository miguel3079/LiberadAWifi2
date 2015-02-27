package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import natour.issam.proyecto.es.proyecto_qiz.invitarFriends.InvitarAmigosEmail;




public class Invitar_Amigos extends ActionBarActivity {

    private  static  final String URLIMAGENAPP="http://natour.myqnapcloud.com:8082/share.cgi?ssid=0UWTxmb&fid=0UWTxmb&ep=LS0tLQ==&open=normal";
    Activity activity ;
    private UiLifecycleHelper uiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar__amigos);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        activity=this;
    }


    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invitar__amigos, menu);
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

    public void invitaramigosemail(View view){
        Intent intent = new Intent(this, InvitarAmigosEmail.class);
        startActivity(intent);

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {

            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }


    public void onClickMapa(View v) {
        float latitude = 40.3736f;
        float longitude = -3.919848f;
        String url = String.format("geo:%f, %f", latitude, longitude);
        Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(i);
    }

    public void onClickLlamada(View v) {

        Intent i = new Intent(android.content.Intent.ACTION_DIAL,
                Uri.parse("tel:+902232350")); //
        startActivity(i);
    }
    public void CompartirTwitter(View v){
        if(isNetworkAvailable()) {
            String tweetUrl =
                    String.format("https://twitter.com/intent/tweet?text=%s&url=%s&hashtags=%s",
                            urlEncode(getString(R.string.twittertextoinvitar)), urlEncode(getString(R.string.Linkglobalapk)), urlEncode("QIZITMONSTER"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));



            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    intent.setPackage(info.activityInfo.packageName);
                }
            }

            startActivity(intent);

        } else{
            Toast.makeText(activity.getApplicationContext(),
                    "No hay internet",
                    Toast.LENGTH_SHORT).show();
        }

    }



    public void invitaramigosfacebook(View v){
        Bundle params = new Bundle();

        params.putString("name", getString(R.string.tituloIntentFace));
        params.putString("caption", getString(R.string.captionIntentFace));
        params.putString("description", getString(R.string.descripcionIntentFace));
        params.putString("link",  getString(R.string.Linkglobalapk));
        params.putString("picture", URLIMAGENAPP);

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(activity,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {

                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(activity,
                                        "Publicacion realizada con exito! " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(activity.getApplicationContext(),
                                        "Publicacion cancelada",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {

                            Toast.makeText(activity.getApplicationContext(),
                                    "Publicacion cancelada",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(activity.getApplicationContext(),
                                    "Hubo un error publicando",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }


    public void invitarWhatsapp(View view){
        Intent whatsappIntent = new  Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.textoinvitarwhatsapp) +"  "+getString(R.string.Linkglobalapk));
        try {
            activity.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }
}
