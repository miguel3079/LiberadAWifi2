package natour.issam.proyecto.es.proyecto_qiz;

import com.parse.ParseFile;

import java.io.Serializable;

/**
 * Created by Issam on 21/01/2015.
 */
public class Usuario implements Serializable {
    String nombre;
    String contraseña;
    String email;
    float monedas;
    float diamantes;
    int nivel;
    float puntos;

    ParseFile image;

    private static final long serialVersionUID = 1L;



    public Usuario(String nombre,String contraseña,String email,float monedas,float diamantes,int nivel,ParseFile  imagen,float puntos){
this.nombre=nombre;
        this.contraseña=contraseña;
        this.email=email;
        this.monedas=monedas;
        this.diamantes=diamantes;
        this.nivel=nivel;
        this.image=imagen;
        this.puntos=puntos;
    }

    public float getPuntos() {
        return puntos;
    }

    public void setPuntos(float puntos) {
        this.puntos = puntos;
    }

    public ParseFile  getImage() {
        return image;
    }

    public void setImage(ParseFile  image) {
        this.image = image;
    }


    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getDiamantes() {
        return diamantes;
    }

    public void setDiamantes(float diamantes) {
        this.diamantes = diamantes;
    }

    public float getMonedas() {
        return monedas;
    }

    public void setMonedas(float monedas) {
        this.monedas = monedas;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
