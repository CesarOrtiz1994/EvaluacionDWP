package mx.edu.uteq.evaluacionidgs06.controllers;

import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import mx.edu.uteq.evaluacionidgs06.dao.IUsuarioDao;
import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import mx.edu.uteq.evaluacionidgs06.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    @Autowired
    IUsuarioDao usuarioDao;
    @Autowired
    IMaterialesDao materialesDao;


    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleError() {
        return "error"; // Nombre de la vista de error, por ejemplo, "error.html"
    }

    @RequestMapping("/about")
    public String about() {
        return "public/about";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "public/contact";
    }

    @RequestMapping("/login")
    public String login() {
        return "public/login";
    }

    @RequestMapping("/registro")
    public String register() {
        return "private/usuarios/registro";
    }

    @RequestMapping("/recuperar")
    public String recuperar() {
        return "public/recuperar";
    }

    @RequestMapping("/inicio")
    public String inicio() {
        return "private/inicio";
    }

    @GetMapping("/list-materiales")
    public String listaMateriales(Model model) {
        Iterable<Materiales> materiales = materialesDao.findAll();
        model.addAttribute("materiales", materiales);
        return "private/materiales/list-materiales";
    }

    @GetMapping("/invetario")
    public String inventario(Model model){
        Iterable<Materiales> materiales = materialesDao.findAll();
        model.addAttribute("materiales", materiales);
        return "private/materiales/inventario";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model){
        Iterable<User> usuarios = usuarioDao.findAll();
        model.addAttribute("usuarios", usuarios);
        return "private/usuarios/list-usuarios";
    }
}