package libreria.Login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import libreria.Login.model.Sesion;
import libreria.Login.model.enums.EstadoSesion;
import libreria.Login.repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Transactional
    public Sesion iniciarSesion(Long idUsuario) {
        Optional<Sesion> activa = sesionRepository.findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa);
        if (activa.isPresent()) {
            throw new RuntimeException("El usuario ya tiene una sesion activa");
        }
        Sesion sesion = new Sesion();
        sesion.setIdUsuario(idUsuario);
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setEstado(EstadoSesion.Activa);
        return sesionRepository.save(sesion);
    }

    @Transactional
    public Sesion cerrarSesion(Long idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesion no encontrada"));
        sesion.setFechaFin(LocalDateTime.now());
        sesion.setEstado(EstadoSesion.Cerrada);
        return sesionRepository.save(sesion);
    }

    public Optional<Sesion> obtenerPorId(Long id) {
        return sesionRepository.findById(id);
    }

    public Optional<Sesion> obtenerSesionActivaPorUsuario(Long idUsuario) {
        return sesionRepository.findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa);
    }

    public List<Sesion> obtenerTodas() {
        return sesionRepository.findAll();
    }

    @Transactional
    public void eliminar(Long id) {
        sesionRepository.deleteById(id);
    }
}
