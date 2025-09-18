package org.upe.business;

import org.upe.model.Exercicio;
import org.upe.model.PlanoTreino;
import org.upe.model.SecaoTreino;
import org.upe.interfaces.ExercicioInterface;
import org.upe.interfaces.PlanoDeTreinoInterface;
import org.upe.interfaces.SecaoDeTreinoInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecaoTreinoBusiness {

    private SecaoDeTreinoInterface secaoTreinoInterface;
    private ExercicioInterface exercicioInterface;
    private PlanoDeTreinoInterface planoDeTreinoInterface;

    public SecaoTreinoBusiness(SecaoDeTreinoInterface secaoTreinoInterface, ExercicioInterface exercicioInterface, PlanoDeTreinoInterface planoDeTreinoInterface) {
        this.secaoTreinoInterface = secaoTreinoInterface;
        this.exercicioInterface = exercicioInterface;
        this.planoDeTreinoInterface = planoDeTreinoInterface;
    }

    public SecaoTreino cadastrarSecaoTreino(long usuarioId, String nome, List<PlanoTreino> planosDeTreino) {
        SecaoTreino novaSecao = new SecaoTreino(0, usuarioId, nome, planosDeTreino);
        return secaoTreinoInterface.salvar(novaSecao);
    }

    public List<SecaoTreino> listarSecoesTreinoPorUsuario(long usuarioId) {
        return secaoTreinoInterface.encontrarPorUsuario(usuarioId);
    }

    public SecaoTreino atualizarSecaoTreino(SecaoTreino secaoTreino) {
        return secaoTreinoInterface.salvar(secaoTreino);
    }

    public String exibirSecaoTreino(SecaoTreino secaoTreino) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Seção de Treino: ").append(secaoTreino.getNome()).append(" ---\n");
        for (PlanoTreino plano : secaoTreino.getPlanoTreinos()) {
            Exercicio exercicio = exercicioInterface.encontrarExercicio(plano.getExercicioId());
            if ( exercicio != null ) {
              sb.append(String.format("  - Exercício: %s\n", exercicio.getNome()));
              sb.append(String.format("    Descrição: %s\n", exercicio.getDescricao()));
              sb.append(String.format("    GIF: %s\n", exercicio.getGifPath()));
              sb.append(String.format("    Séries: %d, Repetições: %d, Carga: %.2f kg\n",
                      plano.getSeries(), plano.getRepeticoes(), plano.getCarga()));
            }
        }
        sb.append("---------------------------------------\n");
        return sb.toString();
    }

    public boolean verificarAtualizacaoPlano(PlanoTreino planoOriginal, int repeticoesRealizadas, double cargaUtilizada) {
        return planoOriginal.getRepeticoes() != repeticoesRealizadas || planoOriginal.getCarga() != cargaUtilizada;
    }

    public PlanoTreino atualizarPlanoSeNecessario(PlanoTreino planoOriginal, int repeticoesRealizadas, int cargaUtilizada) {
        if (verificarAtualizacaoPlano(planoOriginal, repeticoesRealizadas, cargaUtilizada)) {
            planoOriginal.setRepeticoes(repeticoesRealizadas);
            planoOriginal.setCarga(cargaUtilizada);
            return planoDeTreinoInterface.salvar(planoOriginal);
        }
        return planoOriginal;
    }

    public SecaoTreino buscarSecaoTreino(long id) {
        return secaoTreinoInterface.encontrarTreino(id);
    }
}
