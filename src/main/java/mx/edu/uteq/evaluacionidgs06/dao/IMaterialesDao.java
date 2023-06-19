package mx.edu.uteq.evaluacionidgs06.dao;

import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMaterialesDao extends JpaRepository<Materiales, Long>{
}
