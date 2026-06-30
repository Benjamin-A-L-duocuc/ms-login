package libreria.Login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import libreria.Login.model.Sesion;
import libreria.Login.model.enums.EstadoSesion;
import libreria.Login.repository.SesionRepository;

@ExtendWith(MockitoExtension.class)
class SesionServiceTest {

    @Mock
    private SesionRepository sesionRepository;

    @InjectMocks
    private SesionService sesionService;

    @Test
    void iniciarSesionSuccess() {
        Long idUsuario = 1L;
        when(sesionRepository.findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa))
                .thenReturn(Optional.empty());
        when(sesionRepository.save(any(Sesion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sesion sesion = sesionService.iniciarSesion(idUsuario);

        assertThat(sesion.getIdUsuario()).isEqualTo(idUsuario);
        assertThat(sesion.getEstado()).isEqualTo(EstadoSesion.Activa);
        assertThat(sesion.getFechaInicio()).isNotNull();
        verify(sesionRepository).findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa);
        verify(sesionRepository).save(any(Sesion.class));
    }

    @Test
    void iniciarSesionYaActivaThrows() {
        Long idUsuario = 1L;
        Sesion activa = new Sesion(1L, idUsuario, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionRepository.findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa))
                .thenReturn(Optional.of(activa));

        assertThatThrownBy(() -> sesionService.iniciarSesion(idUsuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El usuario ya tiene una sesion activa");

        verify(sesionRepository, never()).save(any());
    }

    @Test
    void cerrarSesionSuccess() {
        Long idSesion = 1L;
        Sesion sesion = new Sesion(idSesion, 1L, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionRepository.findById(idSesion)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(Sesion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Sesion result = sesionService.cerrarSesion(idSesion);

        assertThat(result.getEstado()).isEqualTo(EstadoSesion.Cerrada);
        assertThat(result.getFechaFin()).isNotNull();
        verify(sesionRepository).save(sesion);
    }

    @Test
    void cerrarSesionNotFoundThrows() {
        Long idSesion = 999L;
        when(sesionRepository.findById(idSesion)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sesionService.cerrarSesion(idSesion))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Sesion no encontrada");

        verify(sesionRepository, never()).save(any());
    }

    @Test
    void obtenerPorIdFound() {
        Long id = 1L;
        Sesion sesion = new Sesion(id, 1L, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionRepository.findById(id)).thenReturn(Optional.of(sesion));

        Optional<Sesion> result = sesionService.obtenerPorId(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    void obtenerPorIdNotFound() {
        Long id = 999L;
        when(sesionRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Sesion> result = sesionService.obtenerPorId(id);

        assertThat(result).isEmpty();
    }

    @Test
    void obtenerSesionActivaPorUsuario() {
        Long idUsuario = 1L;
        Sesion sesion = new Sesion(1L, idUsuario, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionRepository.findByIdUsuarioAndEstado(idUsuario, EstadoSesion.Activa))
                .thenReturn(Optional.of(sesion));

        Optional<Sesion> result = sesionService.obtenerSesionActivaPorUsuario(idUsuario);

        assertThat(result).isPresent();
        assertThat(result.get().getIdUsuario()).isEqualTo(idUsuario);
    }

    @Test
    void obtenerTodas() {
        List<Sesion> sesiones = List.of(
            new Sesion(1L, 1L, LocalDateTime.now(), null, EstadoSesion.Activa),
            new Sesion(2L, 2L, LocalDateTime.now(), null, EstadoSesion.Cerrada)
        );
        when(sesionRepository.findAll()).thenReturn(sesiones);

        List<Sesion> result = sesionService.obtenerTodas();

        assertThat(result).hasSize(2);
    }

    @Test
    void eliminar() {
        Long id = 1L;
        sesionService.eliminar(id);
        verify(sesionRepository).deleteById(id);
    }
}
