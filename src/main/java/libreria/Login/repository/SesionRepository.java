package libreria.Login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import libreria.Login.model.Sesion;
import libreria.Login.model.enums.EstadoSesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    List<Sesion> findByIdUsuario(Long idUsuario);
    Optional<Sesion> findByIdUsuarioAndEstado(Long idUsuario, EstadoSesion estado);
}
