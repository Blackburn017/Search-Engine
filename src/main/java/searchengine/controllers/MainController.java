package searchengine.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/admin")
    public String admin() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "testpage";
    }

}
