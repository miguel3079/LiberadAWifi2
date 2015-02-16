package natour.issam.proyecto.es.proyecto_qiz.MiTest;

import java.util.ArrayList;


public class Pregunta {
    int id;
    String titulo;
    String categoria;
    String imagen;
    String tipo;
    String consejo;
    ArrayList<Respuestas> respuestas;

    public  Pregunta(){

    }




    public Pregunta(int id,String titulo, String categoria, String imagen,String tipo,String consejo,ArrayList<Respuestas> respuestas) {

        this.titulo = titulo;
        this.categoria = categoria;
        this.imagen = imagen;
        this.respuestas=respuestas;
        this.id=id;
        this.tipo=tipo;
        this.consejo=consejo;
    }
    public String getConsejo() {
        return consejo;
    }

    public void setConsejo(String consejo) {
        this.consejo = consejo;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Respuestas> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<Respuestas> respuestas) {
        this.respuestas = respuestas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String toStringg() {
        return "Pregunta [Titulo= " + titulo + ", categoria= "+categoria+"]";
    }

}
