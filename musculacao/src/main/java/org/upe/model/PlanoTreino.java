package org.upe.model;

import java.util.ArrayList;
import java.util.List;

public class PlanoTreino {
    private long id;
    private long exercicioId;
    private int series;
    private int repeticoes;
    private int carga;

    public PlanoTreino(long id, long exercicioId, int series, int repeticoes, int carga) {
        this.id = id;
        this.exercicioId = exercicioId;
        this.series = series;
        this.repeticoes = repeticoes;
        this.carga = carga;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExercicioId() {
        return exercicioId;
    }

    public void setExercicioId(long exercicioId) {
        this.exercicioId = exercicioId;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }
}