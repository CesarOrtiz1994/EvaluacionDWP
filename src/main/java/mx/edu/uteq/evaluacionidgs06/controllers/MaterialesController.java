package mx.edu.uteq.evaluacionidgs06.controllers;

import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/materiales")
public class MaterialesController {

    private final IMaterialesDao materialesDao;

    @Autowired
    public MaterialesController(IMaterialesDao materialesDao) {
        this.materialesDao = materialesDao;
    }

    /**
     * Index muestra todos los materiales
     */
    @GetMapping
    public ResponseEntity<Object> index() {
        Iterable<Materiales> materiales = materialesDao.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(materiales);
    }

    /**
     * Store crea un nuevo material
     */
    @PostMapping
    public ResponseEntity<Object> store(@RequestBody Materiales material) {
        materialesDao.save(material);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Material creado correctamente");
    }

    /**
     * Add stock incrementa el stock de un material
     */
    @PostMapping("/{id}/add_stock")
    public ResponseEntity<Object> addStock(@PathVariable Long id, @RequestParam int stock) {
        Materiales material = materialesDao.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Material no encontrado");
        }

        material.setStock(material.getStock() + stock);
        materialesDao.save(material);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Stock incrementado correctamente");
    }

    /**
     * Show muestra un material
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) {
        Materiales material = materialesDao.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Material no encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(material);
    }

    /**
     * Update actualiza un material
     */
    @PostMapping("/editar")
    public String update(@RequestParam Long id, @RequestParam String nombre, @RequestParam String descripcion) {
        System.out.println("guardando material...");
        // Obtener el material existente
        Materiales material = materialesDao.findById(id).orElse(null);
        if (material == null) {
            return "redirect:/error"; // Redirigir a la lista de materiales
        }

        // Actualizar los datos del material
        material.setNombre(nombre);
        material.setDescripcion(descripcion);

        // Guardar los cambios en la base de datos
        materialesDao.save(material);

        return "redirect:/list-materiales"; // Redirigir a la lista de usuarios
    }

    /**
     * Remove the specified resource from storage.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        Materiales material = materialesDao.findById(id).orElse(null);
        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Material no encontrado");
        }

        materialesDao.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Material eliminado correctamente");
    }
}