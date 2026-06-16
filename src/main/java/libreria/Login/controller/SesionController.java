package libreria.Login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping
    public ResponseEntity<Sesion> crear(@RequestBody Sesion sesion) {
        Sesion guardada = sesionService.guardar(sesion);
        return ResponseEntity.ok(guardada);
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

    @PutMapping("/{id}")
    public ResponseEntity<Sesion> actualizar(@PathVariable Long id, @RequestBody Sesion sesion) {
        try {
            Sesion actualizada = sesionService.actualizar(id, sesion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sesionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
