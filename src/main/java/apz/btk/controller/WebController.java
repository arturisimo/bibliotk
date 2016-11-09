package apz.btk.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import apz.btk.repository.LibroRepository;


@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Autowired
	LibroRepository libroRepository;
	
	@GetMapping("/")
	@Secured({ "ROLE_demo" })
	public ModelAndView list(Map<String, Object> data, ViewControllerRegistry registry) {
		data.put("date", new Date());
		data.put("title" , "Lista de libros");
		return new ModelAndView("libros", data);
	}
	
	@RequestMapping("/login")
    public String login() {
	return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
	return "login";
    }

    @RequestMapping("/fragments/{resource}")
    public String angularHtmlTemplate(@PathVariable("resource") String resource) {
    	return "fragments/" + resource;
    }
	
	
}
