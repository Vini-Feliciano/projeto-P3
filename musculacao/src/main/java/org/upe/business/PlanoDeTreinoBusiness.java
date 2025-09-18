package org.upe.business;

import org.upe.model.PlanoTreino;
import org.upe.interfaces.PlanoDeTreinoInterface;

import java.util.List;
import java.util.Optional;

public class PlanoDeTreinoBusiness {

    private PlanoDeTreinoInterface planoDeTreinoInterface;

    public PlanoDeTreinoBusiness(PlanoDeTreinoInterface planoDeTreinoInterface) {
        this.planoDeTreinoInterface = planoDeTreinoInterface;
    }

    public PlanoTreino cadastrarPlanoTreino(long exercicioId, int series, int repeticoes, int carga) {
      long id = 0;
      PlanoTreino novoPlano = new PlanoTreino(id, exercicioId, series, repeticoes, carga);
      return planoDeTreinoInterface.salvar(novoPlano);
    }

    public PlanoTreino buscarPlanoDeTreino(long id) {
        return planoDeTreinoInterface.encontrarPlano(id);
    }

    public List<PlanoTreino> listarTodosPlanosDeTreino() {
        return planoDeTreinoInterface.carregar();
    }

    public PlanoTreino atualizarPlanoTreino(PlanoTreino PlanoTreino) {
        return planoDeTreinoInterface.salvar(PlanoTreino);
    }
}
