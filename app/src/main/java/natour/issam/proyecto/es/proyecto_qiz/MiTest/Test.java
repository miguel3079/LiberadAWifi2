package natour.issam.proyecto.es.proyecto_qiz.MiTest;

import java.sql.Date;
import java.util.ArrayList;

public class Test {
    int id;
    String nombre;
    int  casilla;
    float puntuacion;
    Date fecha;
    int monedas;
    int diamantes;
    float experiencia;
    ArrayList<Pregunta> preguntas;

    public Test(){

    }

    public Test(int id,String nombre,int  casilla,float puntuacion,int monedas,int diamantes,float experiencia, Date fecha,ArrayList<Pregunta> preguntas){
        this.id=id;
        this.nombre=nombre;
        this.casilla=casilla;
        this.puntuacion=puntuacion;
        this.monedas=monedas;
        this.diamantes=diamantes;
        this.experiencia=experiencia;
        this.fecha=fecha;
        this.preguntas=preguntas;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(ArrayList<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCasilla() {
        return casilla;
    }

    public void setCasilla(int casilla) {
        this.casilla = casilla;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public float getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(float experiencia) {
        this.experiencia = experiencia;
    }

    public int getDiamantes() {
        return diamantes;
    }

    public void setDiamantes(int diamantes) {
        this.diamantes = diamantes;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    }
