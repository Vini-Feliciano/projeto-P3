package org.upe.ui;

import org.upe.model.IndicadorBiomedico;
import org.upe.business.IndicadorBiomedicoBusiness;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class IndicadorBiomedicoUI {

    private final IndicadorBiomedicoBusiness indicadorBiomedicoBusiness;
    private final InputHandler inputHandler;
    private final long usuarioLogadoId; // Assuming we pass the logged in user's ID

    public IndicadorBiomedicoUI(IndicadorBiomedicoBusiness indicadorBiomedicoBusiness, InputHandler inputHandler, long usuarioLogadoId) {
        this.indicadorBiomedicoBusiness = indicadorBiomedicoBusiness;
        this.inputHandler = inputHandler;
        this.usuarioLogadoId = usuarioLogadoId;
    }

    public void gerenciarIndicadoresBiomedicos() {
        while (true) {
            System.out.println("\n--- Gerenciar Indicadores Biomédicos ---");
            System.out.println("1. Cadastrar Indicador Biomédico");
            System.out.println("2. Listar Meus Indicadores Biomédicos");
            System.out.println("3. Importar Indicadores Biomédicos (CSV)");
            System.out.println("4. Gerar Relatório por Data");
            System.out.println("5. Gerar Relatório de Evolução");
            System.out.println("6. Atualizar Indicador Biomédico");
            System.out.println("7. Deletar Indicador Biomédico");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            int opcao = inputHandler.readIntInput();

            switch (opcao) {
                case 1:
                    cadastrarIndicadorBiomedico();
                    break;
                case 2:
                    listarMeusIndicadoresBiomedicos();
                    break;
                case 3:
                    importarIndicadoresBiomedicos();
                    break;
                case 4:
                    gerarRelatorioPorData();
                    break;
                case 5:
                    gerarRelatorioEvolucao();
                    break;
                case 6:
                    atualizarIndicadorBiomedico();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarIndicadorBiomedico() {
        System.out.print("Data (AAAA-MM-DD): ");
        LocalDate data = inputHandler.readDateInput();
        System.out.print("Peso (kg): ");
        double peso = inputHandler.readDoubleInput();
        System.out.print("Altura (m): ");
        double altura = inputHandler.readDoubleInput();
        System.out.print("Percentual de Gordura (%): ");
        double percentualGordura = inputHandler.readDoubleInput();
        System.out.print("Percentual de Massa Magra (%): ");
        double percentualMassaMagra = inputHandler.readDoubleInput();

        try {
            indicadorBiomedicoBusiness.cadastrarIndicadorBiomedico(usuarioLogadoId, data, peso, altura, percentualGordura, percentualMassaMagra);
            System.out.println("Indicador biomédico cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarMeusIndicadoresBiomedicos() {
        System.out.println("\n--- Meus Indicadores Biomédicos ---");
        indicadorBiomedicoBusiness.listarIndicadoresPorUsuario(usuarioLogadoId).forEach(i -> {
            System.out.println(String.format("ID: %d, Data: %s, Peso: %.2f kg, Altura: %.2f m, Gordura: %.2f%%, Massa Magra: %.2f%%, IMC: %.2f",
                    i.getId(), i.getData(), i.getPeso(), i.getAltura(), i.getPercentualGordura(), i.getPercentualMassaMagra(), i.getImc()));
        });
    }

    private void importarIndicadoresBiomedicos() {
        System.out.print("Caminho do arquivo CSV para importar: ");
        String filePath = inputHandler.readLine();
        try {
            int importedCount = indicadorBiomedicoBusiness.importarIndicadoresCSV(usuarioLogadoId, filePath);
            System.out.println(importedCount + " indicadores importados com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao importar arquivo CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro de formato nos dados do CSV. Verifique se os números estão corretos.");
        }
    }

    private void gerarRelatorioPorData() {
        System.out.println("\n--- Relatório de Indicadores Biomédicos por Data ---");
        indicadorBiomedicoBusiness.listarIndicadoresPorUsuario(usuarioLogadoId).forEach(i -> {
            System.out.println(String.format("Data: %s, Peso: %.2f kg, Altura: %.2f m, Gordura: %.2f%%, Massa Magra: %.2f%%, IMC: %.2f",
                    i.getData(), i.getPeso(), i.getAltura(), i.getPercentualGordura(), i.getPercentualMassaMagra(), i.getImc()));
        });
    }

    private void gerarRelatorioEvolucao() {
        System.out.print("Data de Início (AAAA-MM-DD): ");
        LocalDate startDate = inputHandler.readDateInput();
        System.out.print("Data de Fim (AAAA-MM-DD): ");
        LocalDate endDate = inputHandler.readDateInput();

        String relatorio = indicadorBiomedicoBusiness.gerarRelatorioEvolucao(usuarioLogadoId, startDate, endDate);
        System.out.println(relatorio);
    }

    private void atualizarIndicadorBiomedico() {
        System.out.print("ID do indicador biomédico para atualizar: ");
        long id = inputHandler.readLongInput();
        IndicadorBiomedico indicadorOpt = indicadorBiomedicoBusiness.buscarIndicadorBiomedico(id);

        if (indicadorOpt != null) {
            IndicadorBiomedico indicador = indicadorOpt;
            if (indicador.getUsuarioId() != usuarioLogadoId) {
                System.out.println("Você não tem permissão para atualizar este indicador.");
                return;
            }

            System.out.print("Nova Data (atual: " + indicador.getData() + ") (AAAA-MM-DD): ");
            String dataStr = inputHandler.readLine();
            if (!dataStr.isEmpty()) indicador.setData(inputHandler.readDateInput());

            System.out.print("Novo Peso (atual: " + indicador.getPeso() + "): ");
            String pesoStr = inputHandler.readLine();
            if (!pesoStr.isEmpty()) indicador.setPeso(inputHandler.readDoubleInput());

            System.out.print("Nova Altura (atual: " + indicador.getAltura() + "): ");
            String alturaStr = inputHandler.readLine();
            if (!alturaStr.isEmpty()) indicador.setAltura(inputHandler.readDoubleInput());

            System.out.print("Novo Percentual de Gordura (atual: " + indicador.getPercentualGordura() + "): ");
            String gorduraStr = inputHandler.readLine();
            if (!gorduraStr.isEmpty()) indicador.setPercentualGordura(inputHandler.readDoubleInput());

            System.out.print("Novo Percentual de Massa Magra (atual: " + indicador.getPercentualMassaMagra() + "): ");
            String massaMagraStr = inputHandler.readLine();
            if (!massaMagraStr.isEmpty()) indicador.setPercentualMassaMagra(inputHandler.readDoubleInput());

            indicadorBiomedicoBusiness.atualizarIndicadorBiomedico(indicador);
            System.out.println("Indicador biomédico atualizado com sucesso!");
        } else {
            System.out.println("Indicador biomédico não encontrado.");
        }
    }
}
