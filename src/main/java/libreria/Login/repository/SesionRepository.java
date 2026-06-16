package libreria.Login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import libreria.Login.model.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
}
