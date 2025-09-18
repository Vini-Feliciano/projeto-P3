package org.upe.ui;

import org.upe.model.PlanoTreino;
import org.upe.model.SecaoTreino;
import org.upe.business.PlanoDeTreinoBusiness;
import org.upe.business.SecaoTreinoBusiness;

import java.util.ArrayList;
import java.util.List;

public class SecaoTreinoUI {

    private SecaoTreinoBusiness secaoTreinoBusiness;
    private PlanoDeTreinoBusiness planoDeTreinoBusiness;
    private InputHandler inputHandler;
    private long usuarioLogadoId; // Assuming we pass the logged in user's ID

    public SecaoTreinoUI(SecaoTreinoBusiness secaoTreinoBusiness, PlanoDeTreinoBusiness planoDeTreinoBusiness, InputHandler inputHandler, long usuarioLogadoId) {
        this.secaoTreinoBusiness = secaoTreinoBusiness;
        this.planoDeTreinoBusiness = planoDeTreinoBusiness;
        this.inputHandler = inputHandler;
        this.usuarioLogadoId = usuarioLogadoId;
    }

    public void gerenciarSecoesDeTreino() {
        while (true) {
            System.out.println("\n--- Gerenciar Seções de Treino ---");
            System.out.println("1. Cadastrar Seção de Treino");
            System.out.println("2. Listar Minhas Seções de Treino");
            System.out.println("3. Exibir Seção de Treino (Cartão)");
            System.out.println("4. Atualizar Seção de Treino");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    cadastrarSecaoDeTreino();
                    break;
                case 2:
                    listarMinhasSecoesDeTreino();
                    break;
                case 3:
                    exibirSecaoDeTreino();
                    break;
                case 4:
                    atualizarSecaoDeTreino();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarSecaoDeTreino() {
        System.out.print("Nome da Seção de Treino: ");
        String nome = inputHandler.readLine();

        System.out.println("Adicione Planos de Treino (digite 'fim' para parar): ");
        List<PlanoTreino> planosIds = new ArrayList<>();
        while (true) {
            System.out.print("ID do Plano de Treino (ou 'fim'): ");
            String input = inputHandler.readLine();
            if (input.equalsIgnoreCase("fim")) {
                break;
            }
            try {
                long planoId = Long.parseLong(input);
                if (planoDeTreinoBusiness.buscarPlanoDeTreino(planoId) != null) {
                    planosIds.add(planoDeTreinoBusiness.buscarPlanoDeTreino(planoId));
                    System.out.println("Plano adicionado.");
                } else {
                    System.out.println("Plano de treino não encontrado.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
            }
        }

        if (planosIds.isEmpty()) {
            System.out.println("Nenhum plano de treino adicionado. Seção não será criada.");
            return;
        }

        secaoTreinoBusiness.cadastrarSecaoTreino(usuarioLogadoId, nome, planosIds);
        System.out.println("Seção de treino cadastrada com sucesso!");
    }

    private void listarMinhasSecoesDeTreino() {
        System.out.println("\n--- Minhas Seções de Treino ---");
        secaoTreinoBusiness.listarSecoesTreinoPorUsuario(usuarioLogadoId).forEach(s -> {
            System.out.println(String.format("ID: %d, Nome: %s, Total de Planos: %d",
                    s.getId(), s.getNome(), s.getPlanoTreinos().size()));
        });
    }

    private void exibirSecaoDeTreino() {
        System.out.print("ID da Seção de Treino para exibir: ");
        long id = inputHandler.readLongInput();
        SecaoTreino secaoOpt = secaoTreinoBusiness.buscarSecaoTreino(id);

        if (secaoOpt != null) {
            SecaoTreino secao = secaoOpt;
            if (secao.getUsuarioId() != usuarioLogadoId) {
                System.out.println("Você não tem permissão para visualizar esta seção de treino.");
                return;
            }
            System.out.println(secaoTreinoBusiness.exibirSecaoTreino(secao));

            // Lógica para perguntar sobre atualização do plano
            System.out.print("Deseja atualizar algum plano de treino nesta seção? (s/n): ");
            if (inputHandler.readLine().equalsIgnoreCase("s")) {
                System.out.print("ID do Plano de Treino a ser atualizado: ");
                long planoId = inputHandler.readLongInput();

                List<PlanoTreino> planos = secao.getPlanoTreinos();
                PlanoTreino planoOriginalOpt = null;
                for ( PlanoTreino plano : planos) {
                  if ( plano.getId() == planoId ) {
                    planoOriginalOpt = plano;
                  }
                }

                if (planoOriginalOpt != null) {
                    PlanoTreino planoOriginal = planoOriginalOpt;
                    System.out.print("Repetições realizadas (atual: " + planoOriginal.getRepeticoes() + "): ");
                    int repeticoesRealizadas = inputHandler.readIntInput();
                    System.out.print("Carga utilizada (atual: " + planoOriginal.getCarga() + "): ");
                    int cargaUtilizada = inputHandler.readIntInput();

                    if (secaoTreinoBusiness.verificarAtualizacaoPlano(planoOriginal, repeticoesRealizadas, cargaUtilizada)) {
                        System.out.print("O plano de treino deve ser atualizado com os novos parâmetros? (s/n): ");
                        if (inputHandler.readLine().equalsIgnoreCase("s")) {
                            secaoTreinoBusiness.atualizarPlanoSeNecessario(planoOriginal, repeticoesRealizadas, cargaUtilizada);
                            System.out.println("Plano de treino atualizado com sucesso!");
                        } else {
                            System.out.println("Atualização do plano de treino cancelada.");
                        }
                    } else {
                        System.out.println("Nenhuma alteração significativa nos parâmetros do plano.");
                    }
                } else {
                    System.out.println("Plano de treino não encontrado nesta seção.");
                }
            }
          
        } else {
            System.out.println("Seção de treino não encontrada.");
        }
    }

    private void atualizarSecaoDeTreino() {
        System.out.print("ID da seção de treino para atualizar: ");
        long id = inputHandler.readLongInput();
        SecaoTreino secaoOpt = secaoTreinoBusiness.buscarSecaoTreino(id);

        if (secaoOpt != null) {
            SecaoTreino secao = secaoOpt;
            if (secao.getUsuarioId() != usuarioLogadoId) {
                System.out.println("Você não tem permissão para atualizar esta seção de treino.");
                return;
            }

            System.out.print("Novo nome da Seção (atual: " + secao.getNome() + "): ");
            String nome = inputHandler.readLine();
            if (!nome.isEmpty()) secao.setNome(nome);

            System.out.println("Adicionar/Remover Planos de Treino (digite 'fim' para parar): ");
            while (true) {
                System.out.print("ID do Plano de Treino para adicionar/remover (ou 'fim'): ");
                String input = inputHandler.readLine();
                if (input.equalsIgnoreCase("fim")) {
                    break;
                }
                try {
                    long planoId = Long.parseLong(input);
                    PlanoTreino planoOpt = planoDeTreinoBusiness.buscarPlanoDeTreino(planoId);
                    if (planoOpt != null) {
                        PlanoTreino plano = planoOpt;
                        if (secao.getPlanoTreinos().contains(plano)) {
                            System.out.print("Plano já existe. Remover? (s/n): ");
                            if (inputHandler.readLine().equalsIgnoreCase("s")) {
                                secao.getPlanoTreinos().remove(plano);
                                System.out.println("Plano removido.");
                            }
                        } else {
                            System.out.print("Plano não existe. Adicionar? (s/n): ");
                            if (inputHandler.readLine().equalsIgnoreCase("s")) {
                                secao.getPlanoTreinos().add(plano);
                                System.out.println("Plano adicionado.");
                            }
                        }}
                     else {
                        System.out.println("Plano de treino não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido.");
                }
            }

            secaoTreinoBusiness.atualizarSecaoTreino(secao);
            System.out.println("Seção de treino atualizada com sucesso!");
        } else {
            System.out.println("Seção de treino não encontrada.");
        }
    }
}
