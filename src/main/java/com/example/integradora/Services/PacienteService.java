package com.example.integradora.Services;
import com.example.integradora.Models.Paciente;
import com.example.integradora.Repositores.PacienteRepository;
import java.io.IOException;
import java.util.List;

public class PacienteService {
    private PacienteRepository repo = new PacienteRepository();

    public void registrar(Paciente p) throws IOException {
        validar(p, true);
        List<Paciente> lista = repo.findAll();
        lista.add(p);
        repo.saveAll(lista);
    }
    public void eliminarFisico(String curp) throws IOException {
        List<Paciente> lista = repo.findAll();
        lista.removeIf(p -> p.getCurp().equals(curp));
        repo.saveAll(lista);
    }

    public void actualizar(Paciente p) throws IOException {
        validar(p, false);
        List<Paciente> lista = repo.findAll();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCurp().equals(p.getCurp())) {
                lista.set(i, p);
                break;
            }
        }
        repo.saveAll(lista);
    }

    public void cambiarEstatus(String curp) throws IOException {
        List<Paciente> lista = repo.findAll();
        for (Paciente p : lista) {
            if (p.getCurp().equals(curp)) {
                p.setEstatus(p.getEstatus().equals("ACTIVO") ? "INACTIVO" : "ACTIVO");
                break;
            }
        }
        repo.saveAll(lista);
    }

    private void validar(Paciente p, boolean esNuevo) throws IOException {
        if (p.getCurp().isBlank() || p.getNombre().isBlank()) throw new IllegalArgumentException("Campos vacios.");
        if (p.getNombre().length() < 5) throw new IllegalArgumentException("Nombre con un minimo de 5 caracteres.");
        if (p.getEdad() < 0 || p.getEdad() > 120) throw new IllegalArgumentException("Edad fuera de rango.");
        if (!p.getTelefono().matches("\\d{10,}")) throw new IllegalArgumentException("Teléfono no valido (minimo 10 dígitos).");

        if (esNuevo) {
            for (Paciente ex : repo.findAll())
                if (ex.getCurp().equals(p.getCurp())) throw new IllegalArgumentException("CURP duplicada.");
        }
    }
}
