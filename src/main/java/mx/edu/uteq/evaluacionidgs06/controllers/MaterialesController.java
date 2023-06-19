package mx.edu.uteq.evaluacionidgs06.controllers;

import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Materiales material) {
        Materiales existingMaterial = materialesDao.findById(id).orElse(null);
        if (existingMaterial == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Material no encontrado");
        }

        existingMaterial.setNombre(material.getNombre());
        existingMaterial.setDescripcion(material.getDescripcion());
        materialesDao.save(existingMaterial);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Material actualizado correctamente");
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