package org.upe.business;

import org.upe.model.IndicadorBiomedico;
import org.upe.interfaces.IndicadorBiomedicoInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

public class IndicadorBiomedicoBusiness {

    private IndicadorBiomedicoInterface indicadorBiomedicoInterface;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public IndicadorBiomedicoBusiness(IndicadorBiomedicoInterface indicadorBiomedicoInterface) {
        this.indicadorBiomedicoInterface = indicadorBiomedicoInterface;
    }

    public IndicadorBiomedico cadastrarIndicadorBiomedico(long usuarioId, LocalDate data, double peso, double altura, double percentualGordura, double percentualMassaMagra) {
        double imc = peso / ( altura * altura );
        long id = 0;
        IndicadorBiomedico novoIndicador = new IndicadorBiomedico(id, usuarioId, data, peso, altura, percentualGordura, percentualMassaMagra, imc);
        return indicadorBiomedicoInterface.salvar(novoIndicador);
    }

    public List<IndicadorBiomedico> listarIndicadoresPorUsuario(long usuarioId) {
        return indicadorBiomedicoInterface.encontrarPeloUsuario(usuarioId);
    }

    public IndicadorBiomedico atualizarIndicadorBiomedico(IndicadorBiomedico indicador) {
        indicador.setImc(indicador.getPeso() / (indicador.getAltura() * indicador.getAltura()));
        return indicadorBiomedicoInterface.salvar(indicador);
    }

    public List<IndicadorBiomedico> gerarRelatorioPorData(long usuarioId) {
        return indicadorBiomedicoInterface.encontrarPeloUsuario(usuarioId);
    }

    public String gerarRelatorioEvolucao(long usuarioId, LocalDate startDate, LocalDate endDate) {
        List<IndicadorBiomedico> indicadores = indicadorBiomedicoInterface.encontrarPorData(usuarioId, startDate, endDate);

        if (indicadores.isEmpty()) {
            return "Não há dados para o período selecionado.";
        }

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Relatório de Evolução de Indicadores Biomédicos (Período: ")
              .append(startDate.format(DATE_FORMATTER)).append(" a ").append(endDate.format(DATE_FORMATTER)).append(")\n");
        relatorio.append("-----------------------------------------------------------------------------------------------------\n");

        IndicadorBiomedico primeiro = indicadores.get(0);
        IndicadorBiomedico ultimo = indicadores.get(indicadores.size() - 1);

        relatorio.append(String.format("%-15s %-10s %-10s %-10s %-10s %-10s\n", "Indicador", "Inicial", "Final", "Diferença", "% "));
        relatorio.append("-----------------------------------------------------------------------------------------------------\n");

        appendEvolution(relatorio, "Peso (kg)", primeiro.getPeso(), ultimo.getPeso());
        appendEvolution(relatorio, "Altura (m)", primeiro.getAltura(), ultimo.getAltura());
        appendEvolution(relatorio, "Gordura (%)", primeiro.getPercentualGordura(), ultimo.getPercentualGordura());
        appendEvolution(relatorio, "Massa Magra (%)", primeiro.getPercentualMassaMagra(), ultimo.getPercentualMassaMagra());
        appendEvolution(relatorio, "IMC", primeiro.getImc(), ultimo.getImc());

        return relatorio.toString();
    }

    private void appendEvolution(StringBuilder relatorio, String label, double initial, double latest) {
        double difference = latest - initial;
        double percentage = (initial != 0) ? (difference / initial) * 100 : 0;
        relatorio.append(String.format("%-15s %-10.2f %-10.2f %-10.2f %-9.2f%%\n", label, initial, latest, difference, percentage));
    }

    public int importarIndicadoresCSV(long usuarioId, String filePath) throws IOException {
        int importedCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length == 5) {
                    try {
                        LocalDate data = LocalDate.parse(record[0], DATE_FORMATTER);
                        double peso = Double.parseDouble(record[1]);
                        double altura = Double.parseDouble(record[2]);
                        double percentualGordura = Double.parseDouble(record[3]);
                        double percentualMassaMagra = Double.parseDouble(record[4]);
                        cadastrarIndicadorBiomedico(usuarioId, data, peso, altura, percentualGordura, percentualMassaMagra);
                        importedCount++;
                    } catch (Exception e) {
                        System.err.println("Erro ao importar linha do CSV: " + Arrays.toString(record) + " - " + e.getMessage());
                    }
                }
            }
        }
        return importedCount;
    }

    public IndicadorBiomedico buscarIndicadorBiomedico(long id) {
        return indicadorBiomedicoInterface.buscarPorId(id);
    }
}