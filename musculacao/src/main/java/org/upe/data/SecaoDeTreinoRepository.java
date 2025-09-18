package org.upe.data;

import org.upe.model.PlanoTreino;
import org.upe.model.SecaoTreino;
import org.upe.interfaces.PlanoDeTreinoInterface;
import org.upe.interfaces.SecaoDeTreinoInterface;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecaoDeTreinoRepository implements SecaoDeTreinoInterface {

    protected String CAMINHO_ARQUIVO = "secoes_treino.csv";
    private PlanoDeTreinoInterface planoDeTreinoInterface;

    public SecaoDeTreinoRepository(PlanoDeTreinoInterface planoDeTreinoInterface) {
        this.planoDeTreinoInterface = planoDeTreinoInterface;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }

    protected String getCAMINHO_ARQUIVO() {
        return CAMINHO_ARQUIVO;
    }

    @Override
    public SecaoTreino salvar(SecaoTreino secaoTreino) {
        List<SecaoTreino> secoes = carregar();

        if (secaoTreino.getId() == 0) {
            long maiorId = 0;

            for ( SecaoTreino secao : secoes ) {
            if (secao.getId() > maiorId) { 
                maiorId = secao.getId();
            }
            }
            long novoId = maiorId + 1;
            secaoTreino.setId(novoId);
            secoes.add(secaoTreino);
        } else {
            for (int i = 0; i < secoes.size(); i++) {
                if (secoes.get(i).getId() == secaoTreino.getId()) {
                    secoes.set(i, secaoTreino);
                }
            }
        }

        salvarVarios(secoes);
        return secaoTreino;
    }

    @Override
    public List<SecaoTreino> encontrarPorUsuario(long usuarioId) {
        List<SecaoTreino> secoes = carregar();

        List<SecaoTreino> secoesDoUsuario = new ArrayList<SecaoTreino>();

        for ( int i = 0; i < secoes.size(); i++ ) {
            if (secoes.get(i).getUsuarioId() == usuarioId) {
                secoesDoUsuario.add(secoes.get(i));
            }
        }

        return secoesDoUsuario;
    }

    @Override
    public List<SecaoTreino> carregar() {
        List<SecaoTreino> secoes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length >= 3) { 
                    long id = Long.parseLong(record[0]);
                    long usuarioId = Long.parseLong(record[1]);
                    String nome = record[2];
                    List<PlanoTreino> planosDeTreino = new ArrayList<>();
                    if (record.length > 3 && !record[3].isEmpty()) {
                        List<Long> planoIds = Arrays.stream(record[3].split(";"))
                                .map(Long::parseLong)
                                .collect(Collectors.toList());
                        for (Long planoId : planoIds) {
                            PlanoTreino plano = planoDeTreinoInterface.encontrarPlano(planoId);
                            if (plano != null) {
                              planosDeTreino.add(plano);
                            }
                        } 
                    }
                    secoes.add(new SecaoTreino(id, usuarioId, nome, planosDeTreino));
                }
            }
        } catch (IOException e) {
        }
        return secoes;
    }

    @Override
    public SecaoTreino encontrarTreino(long id) {
        List<SecaoTreino> secoes = carregar();

        for ( SecaoTreino secao : secoes ) {
            if ( secao.getId() == id ) {
                return secao;
            }
        }

        return null;
    }

    private void salvarVarios(List<SecaoTreino> secoes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (SecaoTreino secao : secoes) {
              StringBuilder sb = new StringBuilder();
              List<PlanoTreino> planos = secao.getPlanoTreinos();
              for ( int i = 0; i < planos.size(); i++) {
                sb.append(planos.get(i).getId());
                if ( i < planos.size() - 1 ) {
                  sb.append(";");
                }
              }
              String planoIds = sb.toString();
              writer.write(secao.getId() + "," +
                            secao.getUsuarioId() + "," +
                            secao.getNome() + "," +
                            planoIds);
              writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
