package libreria.Login.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import libreria.Login.model.Sesion;
import libreria.Login.repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Transactional
    public Sesion guardar(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    public Optional<Sesion> obtenerPorId(Long id) {
        return sesionRepository.findById(id);
    }

    public List<Sesion> obtenerTodas() {
        return sesionRepository.findAll();
    }

    @Transactional
    public Sesion actualizar(Long id, Sesion sesion) {
        Sesion existente = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesion no encontrada"));

        existente.setIdUsuario(sesion.getIdUsuario());
        existente.setFechaInicio(sesion.getFechaInicio());
        existente.setFechaFin(sesion.getFechaFin());
        existente.setEstado(sesion.getEstado());

        return sesionRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        sesionRepository.deleteById(id);
    }
}
