package natour.issam.proyecto.es.proyecto_qiz;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseImageView;

public class ListaDePuntuacionesAdapter extends BaseAdapter{

    protected Activity activity;
    protected ArrayList<ListaDePuntuaciones> items;

    public ListaDePuntuacionesAdapter(Activity activity, ArrayList<ListaDePuntuaciones> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;


        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlista, null);
        }

        ListaDePuntuaciones dir = items.get(position);

        ParseImageView foto = (ParseImageView) v.findViewById(R.id.foto);
        foto.setParseFile(dir.getFoto());
        foto.loadInBackground();

        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        nombre.setText(dir.getNombre());

        TextView Puntos = (TextView) v.findViewById(R.id.puntos);
        Puntos.setText(dir.getPuntos());

        TextView top = (TextView) v.findViewById(R.id.texttop);
        top.setText(dir.getTop());


        return v;
    }
}