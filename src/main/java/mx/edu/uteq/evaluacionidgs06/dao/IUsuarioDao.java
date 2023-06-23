package mx.edu.uteq.evaluacionidgs06.dao;

import  mx.edu.uteq.evaluacionidgs06.models.User;
import  org.springframework.data.jpa.repository.JpaRepository;
public interface IUsuarioDao  extends  JpaRepository<User, Long>{
    User findByEmail(String email);

    boolean existsByEmail(String email);
}