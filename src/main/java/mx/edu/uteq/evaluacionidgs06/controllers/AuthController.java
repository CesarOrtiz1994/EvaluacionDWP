package mx.edu.uteq.evaluacionidgs06.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mx.edu.uteq.evaluacionidgs06.models.User;
import mx.edu.uteq.evaluacionidgs06.dao.IUsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
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
    public ResponseEntity<Object> register(@RequestParam String name, @RequestParam String email, @RequestParam String password, User user) {
        // Validación de los datos
        if (usuarioDao.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El correo electrónico ya está registrado");
        }

        // Crear usuario
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setType("1");
        usuarioDao.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/"); // tiene que ser la misma ruta que en ViewController
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/login")
    public String login(@Valid User user, BindingResult bindingResult ) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Get all field error messages
                return "public/login";

        }
        User existingUser = usuarioDao.findByEmail(user.getEmail());
        if (!existingUser.getPassword().equals(user.getPassword())) {
            bindingResult.rejectValue("email", "error.user", "Datos incorrectos");
            return "public/login";
        }
        return "redirect:/inicio";
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

    @PostMapping("/recuperar")
    public String validateUser(@RequestParam("email") String email, @RequestParam("secret") String secret, User user, HttpSession session, Model model) {
        if (email.isEmpty()) {
            model.addAttribute("error", "El correo electrónico es requerido.");
            return "public/recuperar";
        }
        else if (secret.isEmpty()) {
            model.addAttribute("error", "El código secreto es requerido.");

            return "public/recuperar";
        }
        else if (!usuarioDao.existsByEmail(email) || !secret.equals("secreto") ) {
            model.addAttribute("error", "Existe un error en los datos proporcionados.");
            return "public/recuperar";
        }
        session.setAttribute("loggedInUser", user);
        return "redirect:/recuperar_actualizar";
    }
    @PostMapping("/recuperar_actualizar")
    public String updatePassword( @RequestParam("newPassword") String newPassword,HttpSession session, Model model){
        if(newPassword.isEmpty()){
            model.addAttribute("error", "La contraseña es requerida.");
            return "public/recuperar_actualizar";
        }
        else if (newPassword.length() < 8 ){
            model.addAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            return "public/recuperar_actualizar";
        }

        User user = (User) session.getAttribute("loggedInUser");
        user = usuarioDao.findByEmail(user.getEmail());
        user.setPassword(newPassword);
        usuarioDao.save(user);
        session.setAttribute("loggedInUser", user);
        model.addAttribute("success", "La contraseña se actualizó correctamente.");
    return "public/volver_login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}