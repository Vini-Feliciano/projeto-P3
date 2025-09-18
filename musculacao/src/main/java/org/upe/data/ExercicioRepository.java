package org.upe.data;
import org.upe.interfaces.ExercicioInterface;
import org.upe.model.Exercicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExercicioRepository implements ExercicioInterface{
    private static final String CAMINHO_ARQUIVO = "exercicios.csv";

    public ExercicioRepository() {
        criarArquivo();
    }

    public void criarArquivo() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (Exception e) {
            System.out.println("Erro criando arquivo csv " + e.getMessage());
        }
    }   

    public String getCaminhoCsv() {
        return CAMINHO_ARQUIVO;
    }

    @Override
    public Exercicio salvar(Exercicio exercicio) {
        List<Exercicio> exercicios = carregar();

        if (exercicio.getId() == 0) {
            long maiorId = 0;
            for ( Exercicio exercicioDaLista : exercicios) {
                if ( exercicioDaLista.getId() > maiorId) {
                    maiorId = exercicioDaLista.getId();
                }
            }
            long novoId = maiorId + 1;
            exercicio.setId(novoId);
            exercicios.add(exercicio);
        } else {
            for (int i = 0; i < exercicios.size(); i++) {
                if (exercicios.get(i).getId() == exercicio.getId()) {
                    exercicios.set(i, exercicio);
                }
            }
        }

        salvarVarios(exercicios);
        return exercicio;
    }

    @Override
    public Exercicio encontrarExercicio(long id) {
        List<Exercicio> exercicios = carregar();

        for ( int i = 0; i < exercicios.size(); i++ ) {
            if (exercicios.get(i).getId() == id) {
                return exercicios.get(i);
            }
        }

        return null;
    }
    
    public void salvarVarios(List<Exercicio> exercicios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Exercicio ex : exercicios) {
                writer.write(ex.getId() + "," + ex.getNome() + "," + ex.getDescricao() + "," + ex.getGifPath());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar exercícios: " + e.getMessage());
        }
    }

    public List<Exercicio> carregar() {
        List<Exercicio> exercicios = new ArrayList<>();

        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado!");
            return exercicios;
        } 

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 4) {
                    Exercicio ex = new Exercicio(Long.parseLong(partes[0]), partes[1], partes[2], partes[3]);
                    exercicios.add(ex);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar exercícios: " + e.getMessage());
        }
        
        return exercicios;
    }
}