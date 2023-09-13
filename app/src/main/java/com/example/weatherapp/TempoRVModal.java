package com.example.weatherapp;

public class TempoRVModal {

    private String hora;
    private String temperatura;
    private String icone;
    private String velocidadeDoVento;

    public TempoRVModal(String hora, String temperatura, String icone, String velocidadeDoVento) {
        this.hora = hora;
        this.temperatura = temperatura;
        this.icone = icone;
        this.velocidadeDoVento = velocidadeDoVento;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getVelocidadeDoVento() {
        return velocidadeDoVento;
    }

    public void setVelocidadeDoVento(String velocidadeDoVento) {
        this.velocidadeDoVento = velocidadeDoVento;
    }
}
