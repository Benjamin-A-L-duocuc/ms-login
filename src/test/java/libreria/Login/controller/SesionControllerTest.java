package libreria.Login.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import libreria.Login.model.Sesion;
import libreria.Login.model.enums.EstadoSesion;
import libreria.Login.service.SesionService;

@WebMvcTest(SesionController.class)
class SesionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SesionService sesionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void iniciarSesionReturns200() throws Exception {
        Long idUsuario = 1L;
        Sesion sesion = new Sesion(1L, idUsuario, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionService.iniciarSesion(idUsuario)).thenReturn(sesion);

        mockMvc.perform(post("/api/v1/sesiones/iniciar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("idUsuario", idUsuario))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.estado").value("Activa"));
    }

    @Test
    void iniciarSesionSinIdUsuarioReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/sesiones/iniciar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("idUsuario es requerido"));
    }

    @Test
    void cerrarSesionReturns200() throws Exception {
        Long idSesion = 1L;
        Sesion sesion = new Sesion(idSesion, 1L, LocalDateTime.now(), LocalDateTime.now(), EstadoSesion.Cerrada);
        when(sesionService.cerrarSesion(idSesion)).thenReturn(sesion);

        mockMvc.perform(post("/api/v1/sesiones/{id}/cerrar", idSesion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idSesion))
                .andExpect(jsonPath("$.estado").value("Cerrada"));
    }

    @Test
    void obtenerSesionActivaPorUsuarioReturns200() throws Exception {
        Long idUsuario = 1L;
        Sesion sesion = new Sesion(1L, idUsuario, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionService.obtenerSesionActivaPorUsuario(idUsuario)).thenReturn(Optional.of(sesion));

        mockMvc.perform(get("/api/v1/sesiones/usuario/{idUsuario}", idUsuario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario));
    }

    @Test
    void obtenerSesionActivaPorUsuarioReturns404() throws Exception {
        Long idUsuario = 999L;
        when(sesionService.obtenerSesionActivaPorUsuario(idUsuario)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/sesiones/usuario/{idUsuario}", idUsuario))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerTodasReturns200() throws Exception {
        List<Sesion> sesiones = List.of(
            new Sesion(1L, 1L, LocalDateTime.now(), null, EstadoSesion.Activa),
            new Sesion(2L, 2L, LocalDateTime.now(), null, EstadoSesion.Cerrada)
        );
        when(sesionService.obtenerTodas()).thenReturn(sesiones);

        mockMvc.perform(get("/api/v1/sesiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void obtenerPorIdReturns200() throws Exception {
        Long id = 1L;
        Sesion sesion = new Sesion(id, 1L, LocalDateTime.now(), null, EstadoSesion.Activa);
        when(sesionService.obtenerPorId(id)).thenReturn(Optional.of(sesion));

        mockMvc.perform(get("/api/v1/sesiones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void obtenerPorIdReturns404() throws Exception {
        Long id = 999L;
        when(sesionService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/sesiones/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarReturns204() throws Exception {
        Long id = 1L;
        doNothing().when(sesionService).eliminar(id);

        mockMvc.perform(delete("/api/v1/sesiones/{id}", id))
                .andExpect(status().isNoContent());
    }
}
