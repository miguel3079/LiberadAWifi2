package natour.issam.proyecto.es.proyecto_qiz.monstruos;



public class Monstruos {

    int id;
    String nombre;
    int monedas;
    int diamantes;
    int escomprado;
    String imagen;
    public Monstruos(){

    }


    public Monstruos(int id,String nombre,int monedas,int diamantes,int escomprado,String imagen){
        this.id=id;
        this.nombre=nombre;
        this.monedas=monedas;
        this.diamantes=diamantes;
        this.escomprado=escomprado;
        this.imagen=imagen;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public int isEscomprado() {
        return escomprado;
    }

    public void setEscomprado(int escomprado) {
        this.escomprado = escomprado;
    }

    public int getDiamantes() {
        return diamantes;
    }

    public void setDiamantes(int diamantes) {
        this.diamantes = diamantes;
    }
}
