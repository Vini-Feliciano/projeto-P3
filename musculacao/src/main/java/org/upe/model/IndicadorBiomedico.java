package org.upe.model;

import java.time.LocalDate;

public class IndicadorBiomedico {
    
    private long id;
    private long usuarioId;
    private LocalDate data;
    private double peso;
    private double altura;
    private double percentualGordura;
    private double percentualMassaMagra;
    private double imc;

    public IndicadorBiomedico(long id, long usuarioId, LocalDate data, double peso, double altura, double gordura, double massaMagra, double imc){
        this.id =id;
        this.usuarioId = usuarioId;
        this.data = data;
        this.peso = peso;
        this.altura = altura;
        this.percentualGordura = gordura;
        this.percentualMassaMagra = massaMagra;
        this.imc = imc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }


    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPercentualGordura() {
        return percentualGordura;
    }

    public void setPercentualGordura(double percentualGordura) {
        this.percentualGordura = percentualGordura;
    }

    public double getPercentualMassaMagra() {
        return percentualMassaMagra;
    }

    public void setPercentualMassaMagra(double percentualMassaMagra) {
        this.percentualMassaMagra = percentualMassaMagra;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }
}
