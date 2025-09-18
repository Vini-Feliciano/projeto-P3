package org.upe.business;

import org.upe.model.Exercicio;
import org.upe.interfaces.ExercicioInterface;

import java.util.List;
import java.util.Optional;

public class ExercicioBusiness {

    private ExercicioInterface exercicioInterface;

    public ExercicioBusiness(ExercicioInterface exercicioInterface) {
        this.exercicioInterface = exercicioInterface;
    }

    public Exercicio cadastrarExercicio(String nome, String descricao, String gifPath) {
        Exercicio novoExercicio = new Exercicio(0, nome, descricao, gifPath);
        return exercicioInterface.salvar(novoExercicio);
    }

    public List<Exercicio> listarTodosExercicios() {
        return exercicioInterface.carregar();
    }

    public Exercicio buscarExercicioPorId(long id) {
        return exercicioInterface.encontrarExercicio(id);
    }

    public Exercicio atualizarExercicio(Exercicio exercicio) {
        return exercicioInterface.salvar(exercicio);
    }

}
