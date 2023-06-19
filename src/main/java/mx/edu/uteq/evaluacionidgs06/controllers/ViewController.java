package mx.edu.uteq.evaluacionidgs06.controllers;

import mx.edu.uteq.evaluacionidgs06.dao.IMaterialesDao;
import mx.edu.uteq.evaluacionidgs06.models.Materiales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    @Autowired
    IMaterialesDao materialesDao;


    @RequestMapping("/")
    public String index() {
        return "index";
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
        return "private/registro";
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

    @GetMapping("/agregar-material")
    public String agregarMaterial(Model model, Materiales material){
        model.addAttribute("operacion", "Agregar Material");
        return "private/materiales/frm-material";
    }

}