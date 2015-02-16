package natour.issam.proyecto.es.proyecto_qiz.MiTest;

/**
 * Created by Issam on 07/02/2015.
 */
public class Respuestas {
    int id_respuesta;
    String respuesta;
    int solucion;
    int id_pregunta;

    public Respuestas(int id_respuesta,String respuesta,int solucion,int id_pregunta){
        this.id_respuesta=id_respuesta;
        this.respuesta=respuesta;
        this.solucion=solucion;
        this.id_pregunta=id_pregunta;
    }

    public int getId_respuesta() {
        return id_respuesta;
    }

    public void setId_respuesta(int id_respuesta) {
        this.id_respuesta = id_respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public int isSolucion() {
        return solucion;
    }

    public void setSolucion(int solucion) {
        this.solucion = solucion;
    }

    public int getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(int id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

}
