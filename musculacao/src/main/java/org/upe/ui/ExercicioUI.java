package org.upe.ui;

import org.upe.model.Exercicio;
import org.upe.business.ExercicioBusiness;

import java.util.Optional;

public class ExercicioUI {

    private ExercicioBusiness exercicioBusiness;
    private InputHandler inputHandler;

    public ExercicioUI(ExercicioBusiness exercicioBusiness, InputHandler inputHandler) {
        this.exercicioBusiness = exercicioBusiness;
        this.inputHandler = inputHandler;
    }

    public void gerenciarExercicios() {
        while (true) {
            System.out.println("\n--- Gerenciar Exercícios ---");
            System.out.println("1. Cadastrar Exercício");
            System.out.println("2. Listar Exercícios");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    cadastrarExercicio();
                    break;
                case 2:
                    listarExercicios();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarExercicio() {
        System.out.print("Nome do Exercício: ");
        String nome = inputHandler.readLine();
        System.out.print("Descrição: ");
        String descricao = inputHandler.readLine();
        System.out.print("Caminho do GIF: ");
        String gifPath = inputHandler.readLine();

        exercicioBusiness.cadastrarExercicio(nome, descricao, gifPath);
        System.out.println("Exercício cadastrado com sucesso!");
    }

    private void listarExercicios() {
        System.out.println("\n--- Lista de Exercícios ---");
        exercicioBusiness.listarTodosExercicios().forEach(e -> {
            System.out.println(String.format("ID: %d, Nome: %s, Descrição: %s, GIF: %s",
                    e.getId(), e.getNome(), e.getDescricao(), e.getGifPath()));
        });
    }

}