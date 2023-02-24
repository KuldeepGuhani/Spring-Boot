package com.smartContactmanager.Controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.aspectj.bridge.Message;
import org.aspectj.lang.reflect.CatchClauseSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartContactmanager.Entities.Contact;
import com.smartContactmanager.Entities.User;
import com.smartContactmanager.Helpers.MessageHelper;
import com.smartContactmanager.dao.ContactRepository;
import com.smartContactmanager.dao.UserRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/index")
	public String dashboard(Model m,Principal principal) {
		m.addAttribute("title","Dashboard Page");
		String username=principal.getName();
		
		User user=this.userRepository.getUserByUserName(username);
		m.addAttribute("user",user);
		return "normal/user_dashboard";
	}
	
	@GetMapping("/contactForm")
	public String openAddContactForm(Model m) {
		
		m.addAttribute("title","Add Contact");
		m.addAttribute("contact",new Contact());
		return "normal/addForm";
	}
	
	@ModelAttribute
	public void addCommonData(Model m,Principal principal) {
		
		
		String user=principal.getName();
		User user2=this.userRepository.getUserByUserName(user);
		m.addAttribute("user", user2);
	}
	
	@PostMapping("/formProcess")
	public String formProcess(@ModelAttribute Contact contact,Principal principal,@RequestParam("profileImage") MultipartFile file) {
try {
	

		String username=principal.getName();
		User user=userRepository.getUserByUserName(username);
		
		//process and upload file
		if(file.isEmpty()) {
			
		}else {
			contact.setImage(file.getOriginalFilename());
			File filename=new ClassPathResource("static/image").getFile();
			Path path=Paths.get(filename.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("file upload");
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);		
		
	
	}catch(Exception e){
		e.printStackTrace();
	}
		return "normal/addForm";	
	}
	
	// for showing contacts handler
	@GetMapping("/show_contacts/{page}")
	public String  showCotacts(@PathVariable("page") Integer page,Model m,Principal principal) {
		
		String email=principal.getName();
		User user=this.userRepository.getUserByUserName(email);
		Pageable pageable=PageRequest.of(page,2);
	Page<Contact> contacts=this.contactRepository.findContactsByUser(user.getId(),pageable);
	   
		m.addAttribute("contacts",contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		m.addAttribute("title","Show Contacts");
		return "normal/show_contacts";
	}
	
	//handler for showing single contact
	@GetMapping("/{cId}/contact")
	public String showSingleContact(@PathVariable("cId") Integer cId,Model m,Principal principal) {
	Optional<Contact> contactOptional= this.contactRepository.findById(cId);
	Contact contact= contactOptional.get();
	String uName=principal.getName();
	User user=this.userRepository.getUserByUserName(uName);
	if(user.getId()==contact.getUser().getId()) {
		m.addAttribute("title","Show Single Contact");
		m.addAttribute("contactID",contact);
	}
		return "normal/contact_details";
	}
	
	//delete handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId,Principal principal,HttpSession session) {

		Contact contact=this.contactRepository.findById(cId).get();
		
		contact.setUser(null);
		String uName=principal.getName();
		User user=this.userRepository.getUserByUserName(uName);
		///if(user.getId()==contact.getUser().getId()) {
			
			user.getContacts().remove(contact);
		//}
		this.userRepository.save(user);
			session.setAttribute("message", new MessageHelper("Sucessfully Delete ","alert-success"));
						
		return "redirect:/user/show_contacts/0";
	}
	
	
//	Update handler
	@PostMapping("/update_contact/{cId}")
	public String updateContact(@PathVariable("cId") Integer cId,Model m) {
		m.addAttribute("title","Update Form");
		Contact contact=this.contactRepository.findById(cId).get();
		m.addAttribute("contact",contact);
	    return "normal/updateForm";
	}

	//update form process	
	@PostMapping("/updateProcess")
	public String updateProcessForm(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,HttpSession session,Model m,Principal principal ) {
		
		m.addAttribute("title","Update Process Form");
		try {
			//old complaint details
			Contact contact1=this.contactRepository.findById(contact.getCid()).get();
			if(!file.isEmpty()) {
				//delete old photo
				File deleteFile=new ClassPathResource("static/image").getFile();
				File file2=new File(deleteFile,contact1.getImage());
				file2.delete();
				
				//update new photo
				File saveFile=new ClassPathResource("static/image").getFile();
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				System.out.println("file upload");
			}else {
				contact.setImage(contact1.getImage());
			}
			
			
			System.out.println("CONTACT IOD:::::::::>"+contact.getCid());;
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message",new MessageHelper("Your Contact SucessFully Added", "success"));
			m.addAttribute("contact",contact);
		}catch(Exception exp) {
			exp.printStackTrace();
			System.out.println("EXP message :: "+exp.getMessage());
		}
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	
	
}
