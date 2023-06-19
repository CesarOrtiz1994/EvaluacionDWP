package mx.edu.uteq.evaluacionidgs06.controllers;

import mx.edu.uteq.evaluacionidgs06.models.User;
import mx.edu.uteq.evaluacionidgs06.dao.IUsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//vinculamos el controlador con la ruta /api
@RequestMapping("/api")
public class AuthController {

    private final IUsuarioDao usuarioDao;

    @Autowired
    public AuthController(IUsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
     * Registra un usuario
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        // Validación de los datos
        if (usuarioDao.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El correo electrónico ya está registrado");
        }

        // Crear usuario
        user.setPassword(user.getPassword());
        usuarioDao.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario creado correctamente");
    }

    /**
     * Inicia sesión
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
        User existingUser = usuarioDao.findByEmail(username);

        if (existingUser == null || password == existingUser.getPassword()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Datos incorrectos");
        }

        // Generar token (puedes implementar tu propio método para generar tokens)
        String token = generateToken(existingUser);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", "cookie_token=" + token + "; Max-Age=1440; Path=/")
                .body("Bienvenido");
    }

    /**
     * Cierra sesión
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", "cookie_token=; Max-Age=0; Path=/")
                .body("Sesión cerrada");
    }

    /**
     * Perfil de usuario
     */
    @GetMapping("/user-profile")
    public ResponseEntity<Object> userProfile() {
        // Implementa tu lógica para obtener el usuario actual basado en el token o la sesión
        // Puedes utilizar una librería como JWT para generar y validar tokens

        return ResponseEntity.status(HttpStatus.OK)
                .body("Perfil de usuario");
    }

    // Método para generar el token
    private String generateToken(User user) {
        // Implementa tu lógica para generar el token aquí
        return "token";
    }
}
