package org.upe.business;

import java.util.List;

import org.upe.interfaces.ExercicioInterface;
import org.upe.model.Exercicio;

public class ExercicioBusiness {

    //Tornei a interface final
    private final ExercicioInterface exercicioInterface;

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
