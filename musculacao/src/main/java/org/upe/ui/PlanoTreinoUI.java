package org.upe.ui;

import org.upe.model.Exercicio;
import org.upe.model.PlanoTreino;
import org.upe.business.ExercicioBusiness;
import org.upe.business.PlanoDeTreinoBusiness;

public class PlanoTreinoUI {

    private PlanoDeTreinoBusiness planoDeTreinoBusiness;
    private ExercicioBusiness exercicioBusiness;
    private InputHandler inputHandler;

    public PlanoTreinoUI(PlanoDeTreinoBusiness planoDeTreinoBusiness, ExercicioBusiness exercicioBusiness, InputHandler inputHandler) {
        this.planoDeTreinoBusiness = planoDeTreinoBusiness;
        this.exercicioBusiness = exercicioBusiness;
        this.inputHandler = inputHandler;
    }

    public void gerenciarPlanosDeTreino() {
        while (true) {
            System.out.println("\n--- Gerenciar Planos de Treino ---");
            System.out.println("1. Cadastrar Plano de Treino");
            System.out.println("2. Listar Planos de Treino");
            System.out.println("3. Atualizar Plano de Treino");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    cadastrarPlanoDeTreino();
                    break;
                case 2:
                    listarPlanosDeTreino();
                    break;
                case 3:
                    atualizarPlanoDeTreino();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarPlanoDeTreino() {
        System.out.print("ID do Exercício: ");
        long exercicioId = inputHandler.readLongInput();
        if (exercicioBusiness.buscarExercicioPorId(exercicioId) == null) {
            System.out.println("Exercício não encontrado.");
            return;
        }
        System.out.print("Séries: ");
        int series = inputHandler.readIntInput();
        System.out.print("Repetições: ");
        int repeticoes = inputHandler.readIntInput();
        System.out.print("Carga (kg): ");
        int carga = inputHandler.readIntInput();

        planoDeTreinoBusiness.cadastrarPlanoTreino(exercicioId, series, repeticoes, carga);
        System.out.println("Plano de treino cadastrado com sucesso!");
    }

    private void listarPlanosDeTreino() {
        System.out.println("\n--- Lista de Planos de Treino ---");
        planoDeTreinoBusiness.listarTodosPlanosDeTreino().forEach(p -> {
            Exercicio exercicioOpt = exercicioBusiness.buscarExercicioPorId(p.getExercicioId());
            String nomeExercicio = exercicioOpt.getNome();
            System.out.println("ID: " + p.getId() + ", " + "Exercício: " + nomeExercicio + ", " + "(ID: " + p.getExercicioId() + "), " + "Séries: " + p.getSeries() + ", " + "Repetições: " + p.getRepeticoes() + ", " + " Carga: " + p.getCarga() + "kg");
        });
    }

    private void atualizarPlanoDeTreino() {
        System.out.print("ID do plano de treino para atualizar: ");
        long id = inputHandler.readLongInput();
        PlanoTreino planoOpt = planoDeTreinoBusiness.buscarPlanoDeTreino(id);

        if (planoOpt != null) {
            PlanoTreino plano = planoOpt;
            System.out.print("Novo ID do Exercício (atual: " + plano.getExercicioId() + "): ");
            String exercicioIdStr = inputHandler.readLine();
            if (!exercicioIdStr.isEmpty()) {
                long newExercicioId = inputHandler.readLongInput();
                if (exercicioBusiness.buscarExercicioPorId(newExercicioId) == null) {
                    System.out.println("Exercício não encontrado. Mantendo o ID anterior.");
                } else {
                    plano.setExercicioId(newExercicioId);
                }
            }

            System.out.print("Novas Séries (atual: " + plano.getSeries() + "): ");
            String seriesStr = inputHandler.readLine();
            if (!seriesStr.isEmpty()) plano.setSeries(inputHandler.readIntInput());

            System.out.print("Novas Repetições (atual: " + plano.getRepeticoes() + "): ");
            String repeticoesStr = inputHandler.readLine();
            if (!repeticoesStr.isEmpty()) plano.setRepeticoes(inputHandler.readIntInput());

            System.out.print("Nova Carga (atual: " + plano.getCarga() + "): ");
            String cargaStr = inputHandler.readLine();
            if (!cargaStr.isEmpty()) plano.setCarga(inputHandler.readIntInput());

            planoDeTreinoBusiness.atualizarPlanoTreino(plano);
            System.out.println("Plano de treino atualizado com sucesso!");
        } else {
            System.out.println("Plano de treino não encontrado.");
        }
    }
}
