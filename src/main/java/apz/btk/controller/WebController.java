package apz.btk.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import apz.btk.repository.LibroRepository;


@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Autowired
	LibroRepository libroRepository;
	
	@GetMapping("/")
	public ModelAndView list(Map<String, Object> data) {
		data.put("title" , "Lista de libros");
		return new ModelAndView("libros", data);
	}
	
	@GetMapping("/admin/libros")
	@Secured({ "ROLE_ADMIN" })
	public ModelAndView admin(Map<String, Object> data) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();

		data.put("date", new Date());
		data.put("title" , "Admin de libros");
		data.put("username" , name);
		return new ModelAndView("admin", data);
	}
	
	@RequestMapping("/fragments/{fragment}")
    public String fragment(@PathVariable("fragment") String fragment) {
    	return "fragments/" + fragment;
    }
	
}
