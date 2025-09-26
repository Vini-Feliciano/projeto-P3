                                                                                                                                                                                                                                package org.upe;
import java.util.Scanner;

import org.upe.business.ExercicioBusiness;
import org.upe.business.IndicadorBiomedicoBusiness;
import org.upe.business.PlanoDeTreinoBusiness;
import org.upe.business.SecaoTreinoBusiness;
import org.upe.business.UsuarioBusiness;
import org.upe.data.ExercicioRepository;
import org.upe.data.IndicadorBiomedicoRepository;
import org.upe.data.PlanoDeTreinoRepository;
import org.upe.data.SecaoDeTreinoRepository;
import org.upe.data.UsuarioRepository;
import org.upe.model.Usuario;
import org.upe.ui.ExercicioUI;
import org.upe.ui.IndicadorBiomedicoUI;
import org.upe.ui.InputHandler;
import org.upe.ui.PlanoTreinoUI;
import org.upe.ui.SecaoTreinoUI;
import org.upe.ui.UsuarioUI;
import org.upe.util.PopulateExercicios;

public class Main {

    private static UsuarioBusiness usuarioBusiness;
    private static PlanoDeTreinoBusiness planoDeTreinoBusiness;
    private static SecaoTreinoBusiness secaoTreinoBusiness;
    private static IndicadorBiomedicoBusiness indicadorBiomedicoBusiness;

    private static Usuario usuarioLogado = null;
    private static InputHandler inputHandler;

    private static UsuarioUI usuarioUI;
    private static ExercicioUI exercicioUI;
    private static PlanoTreinoUI planoDeTreinoUI;
    private static SecaoTreinoUI secaoTreinoUI;
    private static IndicadorBiomedicoUI indicadorBiomedicoUI;
    
    public static void main(String[] args) {
        System.Logger logger = System.getLogger(Main.class.getName());
        Scanner scanner = new Scanner(System.in);
        inputHandler = new InputHandler(scanner);

        // Manual Dependency Injection
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        ExercicioRepository exercicioRepository = new ExercicioRepository();
        PlanoDeTreinoRepository planoDeTreinoRepository = new PlanoDeTreinoRepository();
        SecaoDeTreinoRepository secaoTreinoRepository = new SecaoDeTreinoRepository(planoDeTreinoRepository);
        IndicadorBiomedicoRepository indicadorBiomedicoRepository = new IndicadorBiomedicoRepository();
    
        usuarioBusiness = new UsuarioBusiness(usuarioRepository);
        ExercicioBusiness exercicioBusiness = new ExercicioBusiness(exercicioRepository);
        planoDeTreinoBusiness = new PlanoDeTreinoBusiness(planoDeTreinoRepository);
        secaoTreinoBusiness = new SecaoTreinoBusiness(secaoTreinoRepository, exercicioRepository, planoDeTreinoRepository);
        indicadorBiomedicoBusiness = new IndicadorBiomedicoBusiness(indicadorBiomedicoRepository);

        usuarioUI = new UsuarioUI(usuarioBusiness, inputHandler);
        exercicioUI = new ExercicioUI(exercicioBusiness, inputHandler);
        planoDeTreinoUI = new PlanoTreinoUI(planoDeTreinoBusiness, exercicioBusiness, inputHandler);

        PopulateExercicios populateExercicios = new PopulateExercicios(exercicioBusiness);

        if (usuarioBusiness.listarTodosUsuarios().isEmpty()) {
            logger.log(System.Logger.Level.INFO, "Nenhum usuário encontrado. Criando usuário administrador inicial...");
            usuarioBusiness.cadastrarUsuario("Admin", "admin", "admin123", true);
            logger.log(System.Logger.Level.INFO, "Usuário administrador 'admin' criado com sucesso. Senha: admin123");
        }

        if (exercicioBusiness.listarTodosExercicios().isEmpty()) {
            populateExercicios.popularExerciciosIniciaisSeNecssarios();
        }

        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        System.Logger logger = System.getLogger(Main.class.getName());
        while (true) {
            if (usuarioLogado == null) {
                logger.log(System.Logger.Level.INFO, "--- Menu Principal ---");
                logger.log(System.Logger.Level.INFO, "1. Login");
                logger.log(System.Logger.Level.INFO, "0. Sair");
                logger.log(System.Logger.Level.INFO, "Escolha uma opção: ");
                
                int opcao = inputHandler.readIntInput();

                switch (opcao) {
                    case 1:
                        fazerLogin();
                        break;
                    case 0:
                        logger.log(System.Logger.Level.INFO, "Saindo...");
                        return;
                    default:
                        logger.log(System.Logger.Level.WARNING, "Opção inválida. Tente novamente.");
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
        System.Logger logger = System.getLogger(Main.class.getName());
        logger.log(System.Logger.Level.INFO, "Login: ");
        String login = inputHandler.readLine();
        logger.log(System.Logger.Level.INFO, "Senha: ");
        String senha = inputHandler.readLine();

        Usuario usuario = usuarioBusiness.autenticarUsuario(login, senha);
        if (usuario != null) {
            usuarioLogado = usuario;
            logger.log(System.Logger.Level.INFO, "Login realizado com sucesso! Bem-vindo, " + usuarioLogado.getNome() + "!");
        } else {
            logger.log(System.Logger.Level.WARNING, "Login ou senha inválidos.");
        }
    }

    private static void exibirMenuAdmin() {
        System.Logger logger = System.getLogger(Main.class.getName());
        while (true) {
            logger.log(System.Logger.Level.INFO, "--- Menu Administrador ---");
            logger.log(System.Logger.Level.INFO, "1. Gerenciar Usuários");
            logger.log(System.Logger.Level.INFO, "2. Gerenciar Exercícios");
            logger.log(System.Logger.Level.INFO, "0. Logout");
            logger.log(System.Logger.Level.INFO, "Escolha uma opção: ");

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
                    logger.log(System.Logger.Level.INFO, "Logout realizado.");
                    return;
                default:
                    logger.log(System.Logger.Level.WARNING, "Opção inválida. Tente novamente.");
            }
        }
    }

    private static void exibirMenuUsuario() {
        System.Logger logger = System.getLogger(Main.class.getName());
        while (true) {
            logger.log(System.Logger.Level.INFO, "--- Menu Usuário ---");
            logger.log(System.Logger.Level.INFO, "1. Gerenciar Planos de Treino");
            logger.log(System.Logger.Level.INFO, "2. Gerenciar Seções de Treino");
            logger.log(System.Logger.Level.INFO, "3. Gerenciar Indicadores Biomédicos");
            logger.log(System.Logger.Level.INFO, "0. Logout");
            logger.log(System.Logger.Level.INFO, "Escolha uma opção: ");
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
                    logger.log(System.Logger.Level.INFO, "Logout realizado.");
                    return;
                default:
                    logger.log(System.Logger.Level.WARNING, "Opção inválida. Tente novamente.");
            }
        }
    }
}
