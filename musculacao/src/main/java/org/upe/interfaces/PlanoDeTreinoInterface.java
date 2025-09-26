package org.upe.interfaces;

import java.util.List;

import org.upe.model.PlanoTreino;

public interface PlanoDeTreinoInterface {
    PlanoTreino salvar(PlanoTreino planoTreino);
    PlanoTreino encontrarPlano(long id);
    List<PlanoTreino> carregar();
}
