package org.upe.interfaces;

import java.util.List;

import org.upe.model.Exercicio;

public interface ExercicioInterface {
    Exercicio salvar(Exercicio exercicios);
    Exercicio encontrarExercicio(long id);
    List<Exercicio> carregar();
}
