package mx.edu.uteq.evaluacionidgs06.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import mx.edu.uteq.evaluacionidgs06.models.Materiales;
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

    @Autowired
    IUsuarioDao usuarioDao;
    @Autowired
    IMaterialesDao materialesDao;

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
        headers.add("Location", "/usuarios"); // tiene que ser la misma ruta que en ViewController
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Registra un material
     */
    @PostMapping("/register_material")
    public ResponseEntity<Object> registerMaterial( @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,  Materiales material) {
        // Crear usuario
        material.setNombre(nombre);
        material.setDescripcion(descripcion);
        materialesDao.save(material);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/list-materiales");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
    @GetMapping("/eliminar_material/{id}")
    public String eliminarMaterial(@PathVariable("id") Long id) {
        // Obtener el usuario por su ID
        Materiales material = materialesDao.findById(id).orElse(null);

        // Verificar si el usuario existe
        if (material == null) {
            // Manejar el caso de usuario no encontrado
            // Puedes redirigir a una página de error o mostrar un mensaje de error en la vista
            return "redirect:/materiales"; // Redirigir a la lista de usuarios
        }

        // Eliminar el usuario
        materialesDao.delete(material);
        return "redirect:/list-materiales"; // Redirigir a la lista de usuarios
    }

    @PostMapping("/materiales/stock_update")
    public String stockUpdate(@RequestParam("id") Long id, @RequestParam("stock") String stock ){
        Materiales material = materialesDao.findById(id).orElse(null);
        material.setStock(Integer.parseInt(stock) + material.getStock());
        materialesDao.save(material);
        return "redirect:/inventario";
    }
    @PostMapping("/login")
    public String login(@Valid User user, BindingResult bindingResult, HttpSession session ){
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // Get all field error messages
            session.setAttribute("loggedInUser", user);
            return "public/login";

        }
        User existingUser = usuarioDao.findByEmail(user.getEmail());
        if (!existingUser.getPassword().equals(user.getPassword())) {
            bindingResult.rejectValue("email", "error.user", "Datos incorrectos");
            return "public/login";
        }

        session.setAttribute("loggedInUser", existingUser);
        return "redirect:/inicio";
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
    /**
     * editar usuario
     */
    @PostMapping("/usuarios/editar")
    public String editarUsuario(@RequestParam Long id, @RequestParam String nombre, @RequestParam String email, @RequestParam String password) {
        System.out.println("guardando usuario...");
        // Obtener el usuario existente
        User usuario = usuarioDao.findById(id).orElse(null);

        // Verificar si el usuario existe
        if (usuario == null) {
            // Manejar el caso de usuario no encontrado
            // Puedes redirigir a una página de error o mostrar un mensaje de error en la vista
            return "redirect:/usuarios"; // Redirigir a la lista de usuarios
        }

        // Actualizar los datos del usuario
        usuario.setName(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);

        // Guardar los cambios en la base de datos
        usuarioDao.save(usuario);

        return "redirect:/usuarios"; // Redirigir a la lista de usuarios
    }

    /**
     * obtener
     */
    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public User obtenerUsuario(@PathVariable("id") Long id) {
        // Obtener el usuario por su ID
        User usuario = usuarioDao.findById(id).orElse(null);
        return usuario;
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        // Obtener el usuario por su ID
        User usuario = usuarioDao.findById(id).orElse(null);

        // Verificar si el usuario existe
        if (usuario == null) {
            // Manejar el caso de usuario no encontrado
            // Puedes redirigir a una página de error o mostrar un mensaje de error en la vista
            return "redirect:/usuarios"; // Redirigir a la lista de usuarios
        }

        // Eliminar el usuario
        usuarioDao.delete(usuario);
        return "redirect:/usuarios"; // Redirigir a la lista de usuarios
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