package com.example.lojadecarros.service;

import com.example.lojadecarros.model.Veiculo;
import com.example.lojadecarros.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SistemaService {

    private final VeiculoRepository repository;

    public SistemaService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        return repository.save(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return repository.findAll();
    }

    public Optional<Veiculo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Veiculo atualizar(Long id, Veiculo dadosAtualizados) {
        return repository.findById(id).map(veiculo -> {
            veiculo.setMarca(dadosAtualizados.getMarca());
            veiculo.setModelo(dadosAtualizados.getModelo());
            return repository.save(veiculo);
        }).orElse(null);
    }

    public boolean remover(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean login(String usuario, String senha) {
        return "admin".equals(usuario) && "1234".equals(senha);
    }
}