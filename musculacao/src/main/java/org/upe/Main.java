                                                                                                                                                                                                                                package org.upe;
import org.upe.model.Usuario;
import org.upe.data.*;
import org.upe.business.*;
import org.upe.ui.*;
import org.upe.util.*;

import java.util.Scanner;

public class Main {

    private static UsuarioBusiness usuarioBusiness;
    private static ExercicioBusiness exercicioBusiness;
    private static PlanoDeTreinoBusiness planoDeTreinoBusiness;
    private static SecaoTreinoBusiness secaoTreinoBusiness;
    private static IndicadorBiomedicoBusiness indicadorBiomedicoBusiness;

    private static Usuario usuarioLogado = null;
    private static Scanner scanner;
    private static InputHandler inputHandler;

    private static UsuarioUI usuarioUI;
    private static ExercicioUI exercicioUI;
    private static PlanoTreinoUI planoDeTreinoUI;
    private static SecaoTreinoUI secaoTreinoUI;
    private static IndicadorBiomedicoUI indicadorBiomedicoUI;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inputHandler = new InputHandler(scanner);

        // Manual Dependency Injection
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        ExercicioRepository exercicioRepository = new ExercicioRepository();
        PlanoDeTreinoRepository planoDeTreinoRepository = new PlanoDeTreinoRepository();
        SecaoDeTreinoRepository secaoTreinoRepository = new SecaoDeTreinoRepository(planoDeTreinoRepository);
        IndicadorBiomedicoRepository indicadorBiomedicoRepository = new IndicadorBiomedicoRepository();
    
        usuarioBusiness = new UsuarioBusiness(usuarioRepository);
        exercicioBusiness = new ExercicioBusiness(exercicioRepository);
        planoDeTreinoBusiness = new PlanoDeTreinoBusiness(planoDeTreinoRepository);
        secaoTreinoBusiness = new SecaoTreinoBusiness(secaoTreinoRepository, exercicioRepository, planoDeTreinoRepository);
        indicadorBiomedicoBusiness = new IndicadorBiomedicoBusiness(indicadorBiomedicoRepository);

        usuarioUI = new UsuarioUI(usuarioBusiness, inputHandler);
        exercicioUI = new ExercicioUI(exercicioBusiness, inputHandler);
        planoDeTreinoUI = new PlanoTreinoUI(planoDeTreinoBusiness, exercicioBusiness, inputHandler);

        PopulateExercicios populateExercicios = new PopulateExercicios(exercicioBusiness);

        if (usuarioBusiness.listarTodosUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário encontrado. Criando usuário administrador inicial...");
            usuarioBusiness.cadastrarUsuario("Admin", "admin", "admin123", true);
            System.out.println("Usuário administrador 'admin' criado com sucesso. Senha: admin123");
        }

        if (exercicioBusiness.listarTodosExercicios().isEmpty()) {
            populateExercicios.popularExerciciosIniciaisSeNecssarios();
        }

        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        while (true) {
            if (usuarioLogado == null) {
                System.out.println("\n--- Menu Principal ---");
                System.out.println("1. Login");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = inputHandler.readIntInput();

                switch (opcao) {
                    case 1:
                        fazerLogin();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                secaoTreinoUI = new SecaoTreinoUI(secaoTreinoBusiness, planoDeTreinoBusiness, inputHandler, usuarioLogado.getId());
                indicadorBiomedicoUI = new IndicadorBiomedicoUI(indicadorBiomedicoBusiness, inputHandler, usuarioLogado.getId());

                if (usuarioLogado.getAdmin()) {
                    exibirMenuAdmin();
                } else {
                    exibirMenuUsuario();
                }
            }
        }
    }

    private static void fazerLogin() {
        System.out.print("Login: ");
        String login = inputHandler.readLine();
        System.out.print("Senha: ");
        String senha = inputHandler.readLine();

        Usuario usuario = usuarioBusiness.autenticarUsuario(login, senha);
        if (usuario != null) {
            usuarioLogado = usuario;
            System.out.println("Login realizado com sucesso! Bem-vindo, " + usuarioLogado.getNome() + "!");
        } else {
            System.out.println("Login ou senha inválidos.");
        }
    }

    private static void exibirMenuAdmin() {
        while (true) {
            System.out.println("\n--- Menu Administrador ---");
            System.out.println("1. Gerenciar Usuários");
            System.out.println("2. Gerenciar Exercícios");
            System.out.println("0. Logout");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    usuarioUI.gerenciarUsuarios();
                    break;
                case 2:
                    exercicioUI.gerenciarExercicios();
                    break;
                case 0:
                    usuarioLogado = null;
                    System.out.println("Logout realizado.");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void exibirMenuUsuario() {
        while (true) {
            System.out.println("\n--- Menu Usuário ---");
            System.out.println("1. Gerenciar Planos de Treino");
            System.out.println("2. Gerenciar Seções de Treino");
            System.out.println("3. Gerenciar Indicadores Biomédicos");
            System.out.println("0. Logout");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    planoDeTreinoUI.gerenciarPlanosDeTreino();
                    break;
                case 2:
                    secaoTreinoUI.gerenciarSecoesDeTreino();
                    break;
                case 3:
                    indicadorBiomedicoUI.gerenciarIndicadoresBiomedicos();
                    break;
                case 0:
                    usuarioLogado = null;
                    System.out.println("Logout realizado.");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
