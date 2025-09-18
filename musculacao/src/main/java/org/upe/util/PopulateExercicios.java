package org.upe.util;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.upe.business.ExercicioBusiness;
import org.upe.model.Exercicio;

public class PopulateExercicios {
  private ExercicioBusiness exercicioBusiness;

  public PopulateExercicios(ExercicioBusiness exercicioBusiness) {
    this.exercicioBusiness = exercicioBusiness;
  }

  public void popularExerciciosIniciaisSeNecssarios() {
    if (exercicioBusiness.listarTodosExercicios().isEmpty()) {
      System.out.println("Nenhum exercício encontrado. Criando exercício da base...");

      File diretorioGifs = new File("musculacao/src/main/resources/gifs");

      if ( diretorioGifs.exists() && diretorioGifs.isDirectory() ) {
        File[] arquivoGif = diretorioGifs.listFiles((dir, name) -> name.toLowerCase().endsWith(".gif"));

        if ( arquivoGif != null ) {
          long cont = 0;
          for ( File arquivo : arquivoGif ) {
            String nomeFormado = formatarNomeExercicio(arquivo.getName());

            cont += 1;
            Exercicio novoExercicio = new Exercicio(cont, nomeFormado, nomeFormado, arquivo.getAbsolutePath());
            novoExercicio.setNome(nomeFormado);

            exercicioBusiness.cadastrarExercicio(nomeFormado, nomeFormado, arquivo.getAbsolutePath());
            System.out.println("Exercicio adicionado -> " + nomeFormado);          }
        }
      }
    }
  }

  private String formatarNomeExercicio(String nomeArquivo) {
    String nomeSemExtensao = nomeArquivo.substring(0, nomeArquivo.lastIndexOf('.'));

    String[] palavras = nomeSemExtensao.split("(?=\\p{Lu})");

    String nomeFinal = Arrays.stream(palavras)
      .map(String::toLowerCase)
      .collect(Collectors.joining(" "));

    return nomeFinal.substring(0, 1).toUpperCase() + nomeFinal.substring(1);
  }
}