package org.upe.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.upe.interfaces.UsuarioInterface;
import org.upe.model.Usuario;

public class UsuarioRepository implements UsuarioInterface {
    private static final String CAMINHO_ARQUIVO = "usuarios.csv";

    public UsuarioRepository() {
        init();
    }

    public void init() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (Exception e) {
            System.out.println("Erro ao criar csv: " + e.getMessage());
        }
    }

    private String getCaminhoCsv() {
        return CAMINHO_ARQUIVO;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        List<Usuario> usuarios = carregar();

        if ( usuario.getId() == 0 ) {
            long maiorId = 0;
            for ( Usuario usuarioDaLista : usuarios) {
                if ( usuarioDaLista.getId() > maiorId) {
                    maiorId = usuarioDaLista.getId();
                }
            }
            long novoId = maiorId + 1;
            usuario.setId(novoId);
            usuarios.add(usuario);
        } else {
            for ( int i = 0; i < usuarios.size() ; i++ ) {
                if (usuarios.get(i).getId() == usuario.getId()) {
                    usuarios.set(i, usuario);
                }
            }
        }
        
        salvarVarios(usuarios);
        return usuario;
    }

    @Override
    public Usuario encontrarUsuario(String login) {
        List<Usuario> exercicios = carregar();

        for ( int i = 0; i < exercicios.size(); i++ ) {
            if (exercicios.get(i).getEmail().equals(login)) {
                return exercicios.get(i);
            }
        }

        return null;
    }

    @Override
    public List<Usuario> carregar() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(",");
                if (record.length == 5) { // Ensure correct number of fields
                    usuarios.add(new Usuario(
                            Long.parseLong(record[0]),
                            record[1],
                            record[2],
                            record[3],
                            Boolean.parseBoolean(record[4])
                    ));
                }
            }
        } catch (IOException e) {
            
        }
        return usuarios;
    }

    private void salvarVarios(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Usuario usuario : usuarios) {
                writer.write(String.valueOf(usuario.getId()) + "," +
                             usuario.getNome() + "," +
                             usuario.getEmail() + "," +
                             usuario.getSenha() + "," +
                             String.valueOf(usuario.getAdmin()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
