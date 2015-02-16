package natour.issam.proyecto.es.proyecto_qiz;

import com.parse.ParseFile;

public class ListaDePuntuaciones {
    protected ParseFile  foto;
    protected String nombre;
    protected String puntos ;



    protected String top;


    public ListaDePuntuaciones(ParseFile  foto, String nombre, String puntos,String top) {
        super();
        this.foto = foto;
        this.nombre = nombre;
        this.puntos = puntos;
        this.top=top;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public ParseFile  getFoto() {
        return foto;
    }

    public void setFoto(ParseFile  foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }


}