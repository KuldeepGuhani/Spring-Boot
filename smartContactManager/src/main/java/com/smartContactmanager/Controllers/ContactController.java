package com.smartContactmanager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartContactmanager.Entities.User;
import com.smartContactmanager.Helpers.MessageHelper;
import com.smartContactmanager.dao.UserRepository;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ContactController {

	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/home")
	public String home(Model m) {
		m.addAttribute("title","Home Page");
		return "home";
	}
	
	@GetMapping("/signup")
	public String signup(Model m) {
		
		m.addAttribute("user",new User());
		m.addAttribute("title","Register Page");
		return "signup";
	}
	
	@PostMapping("/do-register")
	public String registerUser( @Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="agrement",defaultValue = "false") boolean agrement,Model m,HttpSession session) {
		
		try {
		if(!agrement) {
			System.out.println(" ::> please check agrement");
			throw new Exception("::>  please check agrement");
		}
		if(result.hasErrors()) {
			System.out.println("ERROR"+result.toString());
			m.addAttribute("user|",user);
			return "signup";
		}
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		User user2=this.userRepository.save(user);
			
		m.addAttribute("user",new User());
		
		session.setAttribute("message", new MessageHelper("sucess","alert-success"));
		return "signup";
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message", new MessageHelper("404 error"+e.getMessage(),"alert-danger"));
		}
		return "signup";
	}
	
	@GetMapping("/signin")
	public String login(Model m) {
		
		m.addAttribute("title","Login Page");
		return "login";
	}
	
}
