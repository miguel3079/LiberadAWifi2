package natour.issam.proyecto.es.proyecto_qiz;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import natour.issam.proyecto.es.proyecto_qiz.monstruos.Monstruos;


public class HorizontalListViewDemo extends Activity {
    Context context;
    MetodosSqlite metodosSqlite;
    public String [] mismonstruos;
    ArrayList<Monstruos> todoslosmonstruos;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_horizontal_list_view);
        context=this;


        metodosSqlite = new MetodosSqlite(context);
        mismonstruos= metodosSqlite.getNombreDeMonstruos();
        todoslosmonstruos = metodosSqlite.getMonstruos();



        HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
        listview.setAdapter(mAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("hola","Hola");
            }
        });


    }



    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return mismonstruos.length;
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
            Drawable myDrawable = cargarimagendemonstruos(position);
            imagen.setImageDrawable(myDrawable);



            TextView title = (TextView) retval.findViewById(R.id.title);
            title.setText(mismonstruos[position]);



            return retval;
        }

    };

        public  ArrayList<Monstruos> MisMonstruos(){
            ArrayList<Monstruos> monsters = metodosSqlite.getMonstruos();
            return  monsters;
        }


    private Drawable cargarimagendemonstruos(int posicion){
        Drawable seleccion=null;
        ArrayList<Monstruos> getAllmonsters = MisMonstruos();


        boolean monstruosEstado =  intToBoolean(getAllmonsters.get(posicion).isEscomprado());
        switch (posicion){
            case 0:
                   if(monstruosEstado){
                       seleccion = getResources().getDrawable(R.drawable.mimonstruo2);
                   } else{
                       seleccion = getResources().getDrawable(R.drawable.mimonstruo1);
                   }

                break;
            case 1:
                if(monstruosEstado){
                    seleccion = getResources().getDrawable(R.drawable.mimonstruo2);
                } else{
                    seleccion = getResources().getDrawable(R.drawable.mimonstruo1);
                }

                break;
            case 2:

                if(monstruosEstado){
                    seleccion = getResources().getDrawable(R.drawable.mimonstruo2);
                } else{
                    seleccion = getResources().getDrawable(R.drawable.mimonstruo1);
                }

                break;

            default:
                seleccion = getResources().getDrawable(R.drawable.mimonstruo1);
                break;
        }
    return  seleccion;
    }

    private  boolean intToBoolean(int tipo){
        boolean valor=false;

        if(tipo==1){
            valor=true;
        } else{
            valor=false;
        }
        return  valor;
    }

}