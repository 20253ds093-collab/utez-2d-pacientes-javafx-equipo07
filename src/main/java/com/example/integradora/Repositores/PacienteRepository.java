package com.example.integradora.Repositores;

import com.example.integradora.Models.Paciente;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {
    private final Path path = Paths.get("data", "pacientes.csv");

    public List<Paciente> findAll() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        List<String> lines = Files.readAllLines(path);
        List<Paciente> lista = new ArrayList<>();
        for (String l : lines) {
            if (l.isBlank()) continue;
            String[] d = l.split(",");
            lista.add(new Paciente(d[0], d[1], Integer.parseInt(d[2]), d[3], d[4], d[5]));
        }
        return lista;
    }

    public void saveAll(List<Paciente> lista) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Paciente p : lista) {
            lines.add(p.toCsv());
        }
        Files.write(path, lines);
    }
}