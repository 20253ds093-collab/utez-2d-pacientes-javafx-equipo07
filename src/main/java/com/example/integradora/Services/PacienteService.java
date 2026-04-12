package com.example.integradora.Services;

import com.example.integradora.Models.Paciente;
import com.example.integradora.Repositores.PacienteRepository;
import java.io.IOException;
import java.util.List;

public class PacienteService {
    private PacienteRepository repo = new PacienteRepository();

    public void registrar(Paciente p) throws IOException {
        validar(p, "");
        List<Paciente> lista = repo.findAll();
        lista.add(p);
        repo.saveAll(lista);
    }

    public void actualizar(String curpAnterior, Paciente nuevo) throws IOException {
        validar(nuevo, curpAnterior);
        List<Paciente> lista = repo.findAll();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCurp().equals(curpAnterior)) {
                lista.set(i, nuevo);
                break;
            }
        }
        repo.saveAll(lista);
    }

    public void borradoLogico(String curp) throws IOException {
        List<Paciente> lista = repo.findAll();
        for (Paciente p : lista) {
            if (p.getCurp().equals(curp)) {
                p.setEstatus("INACTIVO");
                break;
            }
        }
        repo.saveAll(lista);
    }

    private void validar(Paciente p, String curpAnterior) throws IOException {
        if (p.getCurp().isBlank() || p.getNombre().isBlank() || p.getTelefono().isBlank() || p.getAlergias().isBlank()) {
            throw new IllegalArgumentException("Ningun campo puede estar vacío.");
        }
        if (p.getNombre().length() < 5) throw new IllegalArgumentException("Nombre min de 5 caracteres.");
        if (p.getEdad() < 0 || p.getEdad() > 120) throw new IllegalArgumentException("Edad inválida.");
        if (!p.getTelefono().matches("\\d{10,}")) throw new IllegalArgumentException("Teléfono invalido (min 10 digitos).");

        if (!p.getCurp().equals(curpAnterior)) {
            for (Paciente ex : repo.findAll()) {
                if (ex.getCurp().equals(p.getCurp())) throw new IllegalArgumentException("CURP ya existe.");
            }
        }
    }
}