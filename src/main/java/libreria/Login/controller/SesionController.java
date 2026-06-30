package libreria.Login.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import libreria.Login.model.Sesion;
import libreria.Login.service.SesionService;

@RestController
@RequestMapping("/api/v1/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarSesion(@RequestBody Map<String, Long> body) {
        try {
            Long idUsuario = body.get("idUsuario");
            if (idUsuario == null) {
                return ResponseEntity.badRequest().body("idUsuario es requerido");
            }
            Sesion sesion = sesionService.iniciarSesion(idUsuario);
            return ResponseEntity.ok(sesion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarSesion(@PathVariable Long id) {
        try {
            Sesion sesion = sesionService.cerrarSesion(id);
            return ResponseEntity.ok(sesion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Sesion> obtenerSesionActiva(@PathVariable Long idUsuario) {
        return sesionService.obtenerSesionActivaPorUsuario(idUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Sesion>> obtenerTodas() {
        return ResponseEntity.ok(sesionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sesion> obtenerPorId(@PathVariable Long id) {
        return sesionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
