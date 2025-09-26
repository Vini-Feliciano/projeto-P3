package org.upe.business;

import java.util.List;

import org.upe.interfaces.PlanoDeTreinoInterface;
import org.upe.model.PlanoTreino;

public class PlanoDeTreinoBusiness {
    //tornei a interface final
    private final PlanoDeTreinoInterface planoDeTreinoInterface;

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
