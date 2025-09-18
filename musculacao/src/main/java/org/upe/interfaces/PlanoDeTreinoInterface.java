package org.upe.interfaces;

import org.upe.model.PlanoTreino;

import java.util.List;
import java.util.Optional;

public interface PlanoDeTreinoInterface {
    PlanoTreino salvar(PlanoTreino planoTreino);
    PlanoTreino encontrarPlano(long id);
    List<PlanoTreino> carregar();
}
