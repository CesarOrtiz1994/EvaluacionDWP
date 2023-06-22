package mx.edu.uteq.evaluacionidgs06.controllers;

import jakarta.servlet.http.HttpSession;
import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import mx.edu.uteq.evaluacionidgs06.dao.IUsuarioDao;
import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import mx.edu.uteq.evaluacionidgs06.models.UpdatePassword;
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
    public String login(User user) {


        return "public/login";
    }

    @RequestMapping("/registro")
    public String register(HttpSession session){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "private/usuarios/registro";
    }
    @RequestMapping("/registro_material")
    public String materialRegister(HttpSession session){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "private/materiales/registro";
    }
    @RequestMapping("/recuperar")
    public String recuperar(User user, Model model)
    {
        model.addAttribute("udpate_password" , false );

        return "public/recuperar";
    }
    @RequestMapping("/recuperar_actualizar")
    public String recuperar_actualizar(UpdatePassword updatePassword)
    {
        return "public/recuperar_actualizar";
    }

    @RequestMapping("/inicio")
    public String inicio(HttpSession session, User user, Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        User admin = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user", admin);
        return "private/inicio";
    }

    @GetMapping("/list-materiales")
    public String listaMateriales(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Iterable<Materiales> materiales = materialesDao.findAll();
        model.addAttribute("materiales", materiales);
        return "private/materiales/list-materiales";
    }

    @GetMapping("/inventario")
    public String inventario(Model model, HttpSession session){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        Iterable<Materiales> materiales = materialesDao.findAll();
        model.addAttribute("materiales", materiales);
        return "private/materiales/inventario";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model, HttpSession session){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        Iterable<User> usuarios = usuarioDao.findAll();
        model.addAttribute("usuarios", usuarios);
        return "private/usuarios/list-usuarios";
    }

}