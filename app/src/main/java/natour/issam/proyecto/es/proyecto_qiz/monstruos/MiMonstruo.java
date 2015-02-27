package natour.issam.proyecto.es.proyecto_qiz.monstruos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

import natour.issam.proyecto.es.proyecto_qiz.HorizontalListView;
import natour.issam.proyecto.es.proyecto_qiz.MetodosSqlite;
import natour.issam.proyecto.es.proyecto_qiz.R;

public class MiMonstruo extends ActionBarActivity {

    MetodosSqlite metodosSqlite;
    TextView textmimonstruo;
    ImageView imagemimonster;
    Context context;
    ArrayList<Monstruos> todoslosmonstruosdeusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_monstruo);

        context=this;



        metodosSqlite = new MetodosSqlite(context);
        ParseUser currentuser = ParseUser.getCurrentUser();
        todoslosmonstruosdeusuario=metodosSqlite.getmonstruosdelusuarioActual(currentuser);

        textmimonstruo = (TextView) findViewById(R.id.textmimonstruo);
        imagemimonster = (ImageView) findViewById(R.id.imagemimonster);
        HorizontalListView listview = (HorizontalListView) findViewById(R.id.listViewAlMonsters);
        listview.setAdapter(mAdapter);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.i("long clicked", "pos: " + pos);

                String s =(String) ((TextView) arg1.findViewById(R.id.title)).getText();
                Log.i("long clicked", "pos: " + s);

                metodosSqlite.cambiarMonstruoactual(s);
                vaciarhabilidades();
                monstruoactual();
                cargarhabilidades();
                return true;
            }
        });


        monstruoactual();
        cargarhabilidades();
    }
public int imagendemonster(String pathmonstruo){
    int drawableResourceId = this.getResources().getIdentifier(pathmonstruo, "drawable", this.getPackageName());
   return  drawableResourceId;

}

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return todoslosmonstruosdeusuario.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            Log.i("veces","veces");
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemhorizontal, null);
            ImageView imagen = (ImageView) retval.findViewById(R.id.imageListaHorizontal);
            String pathmonstruo=todoslosmonstruosdeusuario.get(position).getImagen();
            imagen.setImageResource(imagendemonster(pathmonstruo));


            TextView title = (TextView) retval.findViewById(R.id.title);
            title.setText(todoslosmonstruosdeusuario.get(position).getNombre());



            return retval;
        }

    };

   public void monstruoactual(){
       ParseUser currenUser = ParseUser.getCurrentUser();
      int idmonster= metodosSqlite.getidmonstruoactual(currenUser);
       Monstruos monstruoactual=  metodosSqlite.monstruoactual(idmonster);
       imagemimonster.setImageResource(imagendemonster(monstruoactual.getImagen()));

       textmimonstruo.setText(monstruoactual.getNombre());
   }

    private void vaciarhabilidades(){
        ImageView imagenhabilidad1 = (ImageView) findViewById(R.id.imagenhabilidad1);
        imagenhabilidad1.setImageResource(android.R.color.transparent);
        TextView nombrehabilidad1 = (TextView) findViewById(R.id.nombrehabilidad1);
        nombrehabilidad1.setText("");
        TextView cantidadhabilidad1 = (TextView) findViewById(R.id.cantidadhabilidad1);
        cantidadhabilidad1.setText("");

        ImageView imagenhabilidad2 = (ImageView) findViewById(R.id.imagenhabilidad2);
        imagenhabilidad2.setImageResource(android.R.color.transparent);
        TextView nombrehabilidad2 = (TextView) findViewById(R.id.nombrehabilidad2);
        nombrehabilidad2.setText("");
        TextView cantidadhabilidad2 = (TextView) findViewById(R.id.cantidadhabilidad2);
        cantidadhabilidad2.setText("");

        ImageView imagenhabilidad3 = (ImageView) findViewById(R.id.imagenhabilidad3);
        imagenhabilidad3.setImageResource(android.R.color.transparent);
        TextView nombrehabilidad3 = (TextView) findViewById(R.id.nombrehabilidad3);
        nombrehabilidad3.setText("");
        TextView cantidadhabilidad3 = (TextView) findViewById(R.id.cantidadhabilidad3);
        cantidadhabilidad3.setText("");

        ImageView imagenhabilidad4 = (ImageView) findViewById(R.id.imagenhabilidad4);
        imagenhabilidad4.setImageResource(android.R.color.transparent);
        TextView nombrehabilidad4 = (TextView) findViewById(R.id.nombrehabilidad4);
        nombrehabilidad4.setText("");
        TextView cantidadhabilidad4 = (TextView) findViewById(R.id.cantidadhabilidad4);
        cantidadhabilidad4.setText("");

    }

public void cargarhabilidades(){
    ParseUser currenUser = ParseUser.getCurrentUser();
    int idmonster= metodosSqlite.getidmonstruoactual(currenUser);
    ArrayList<Habilidades> habilidad = metodosSqlite.seleccionarhabilidadesdemonstruoid(idmonster);
    int tamañoarray=habilidad.size();

if(tamañoarray>=1) {
        ImageView imagenhabilidad1 = (ImageView) findViewById(R.id.imagenhabilidad1);
        imagenhabilidad1.setImageResource(imagendemonster(habilidad.get(0).getImagen()));
        TextView nombrehabilidad1 = (TextView) findViewById(R.id.nombrehabilidad1);
        nombrehabilidad1.setText(habilidad.get(0).getNombre());
        TextView cantidadhabilidad1 = (TextView) findViewById(R.id.cantidadhabilidad1);

    Log.i("habilidad",currenUser.getObjectId());
    Log.i("habilidad",String.valueOf(habilidad.get(0).getId()));
    int cantidad =metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(0).getId());
    cantidadhabilidad1.setText(String.valueOf(cantidad));
    }

    if(tamañoarray>=2) {
        ImageView imagenhabilidad2 = (ImageView) findViewById(R.id.imagenhabilidad2);
        imagenhabilidad2.setImageResource(imagendemonster(habilidad.get(1).getImagen()));
        TextView nombrehabilidad2 = (TextView) findViewById(R.id.nombrehabilidad2);
        nombrehabilidad2.setText(habilidad.get(1).getNombre());
        TextView cantidadhabilidad2 = (TextView) findViewById(R.id.cantidadhabilidad2);
        cantidadhabilidad2.setText(String.valueOf(metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(1).getId())));
    }

    if(tamañoarray>=3) {
        ImageView imagenhabilidad3 = (ImageView) findViewById(R.id.imagenhabilidad3);
        imagenhabilidad3.setImageResource(imagendemonster(habilidad.get(2).getImagen()));
        TextView nombrehabilidad3 = (TextView) findViewById(R.id.nombrehabilidad3);
        nombrehabilidad3.setText(habilidad.get(2).getNombre());
        TextView cantidadhabilidad3 = (TextView) findViewById(R.id.cantidadhabilidad3);
        cantidadhabilidad3.setText(String.valueOf(metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(2).getId())));
    }
    if(tamañoarray>=4) {
        ImageView imagenhabilidad4 = (ImageView) findViewById(R.id.imagenhabilidad4);
        imagenhabilidad4.setImageResource(imagendemonster(habilidad.get(3).getImagen()));
        TextView nombrehabilidad4 = (TextView) findViewById(R.id.nombrehabilidad4);
        nombrehabilidad4.setText(habilidad.get(3).getNombre());
        TextView cantidadhabilidad4 = (TextView) findViewById(R.id.cantidadhabilidad4);
        cantidadhabilidad4.setText(String.valueOf(metodosSqlite.getCantidadFromUserAndIdhabilidad(currenUser,habilidad.get(3).getId())));
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mi_monstruo, menu);
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
}
