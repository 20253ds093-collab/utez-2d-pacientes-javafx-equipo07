package com.example.integradora.Repositores;

import com.example.integradora.Models.Paciente;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {
    private final Path filePath = Paths.get("data", "pacientes.csv");

    public List<Paciente> findAll() throws IOException {
        if (Files.notExists(filePath)) Files.createFile(filePath);
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        List<Paciente> lista = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] p = line.split(",");
            lista.add(new Paciente(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], p[5]));
        }
        return lista;
    }

    public void saveAll(List<Paciente> pacientes) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Paciente p : pacientes) lines.add(p.toCsv());
        Files.write(filePath, lines, StandardOpenOption.TRUNCATE_EXISTING);
    }
}