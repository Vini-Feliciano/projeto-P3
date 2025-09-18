package org.upe.data;

import org.upe.model.PlanoTreino;
import org.upe.interfaces.PlanoDeTreinoInterface;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlanoDeTreinoRepository implements PlanoDeTreinoInterface {

    private static final String CAMINHO_ARQUIVO = "planos_treino.csv";

    public PlanoDeTreinoRepository() {
        init();
    }

    private void init() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo csv: " + e.getMessage());
        }
    }

    protected String getCAMINHO_ARQUIVO() {
        return CAMINHO_ARQUIVO;
    }

    @Override
    public PlanoTreino salvar(PlanoTreino planoTreino) {
        List<PlanoTreino> planos = carregar();
        
        if ( planoTreino.getId() == 0 ) {
            long maiorId = 0;
            for (PlanoTreino planoTreinoExistente : planos) {
                if (planoTreinoExistente.getId() > maiorId) {
                    maiorId = planoTreinoExistente.getId();
                }
            }
            long novoId = maiorId + 1;
            planoTreino.setId(novoId);
            planos.add(planoTreino);
        } else {
            for ( int i = 0; i < planos.size() ; i++ ) {
                if (planos.get(i).getId() == planoTreino.getId()) {
                    planos.set(i, planoTreino);
                }
            }
        }
        salvarVarios(planos);
        return planoTreino;
    }

    @Override
    public PlanoTreino encontrarPlano(long id) {
        List<PlanoTreino> planos = carregar();

        for ( int i = 0; i < planos.size(); i++ ) {
            if (planos.get(i).getId() == id) {
                return planos.get(i);
            }
        }

        return null;
    }

    @Override
    public List<PlanoTreino> carregar() {
        List<PlanoTreino> planos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parte = line.split(",");
                if (parte.length == 5) { 
                    planos.add(new PlanoTreino(
                            Long.parseLong(parte[0]),
                            Long.parseLong(parte[1]),
                            Integer.parseInt(parte[2]),
                            Integer.parseInt(parte[3]),
                            Integer.parseInt(parte[4])
                    ));
                }
            }
        } catch (IOException e) {
        }
        return planos;
    }

    private void salvarVarios(List<PlanoTreino> planos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (PlanoTreino plano : planos) {
                writer.write(plano.getId() + "," +
                             plano.getExercicioId() + "," +
                             plano.getSeries() + "," +
                             plano.getRepeticoes() + "," +
                             plano.getCarga());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo csv: " + e.getMessage());
        }
    }
}
