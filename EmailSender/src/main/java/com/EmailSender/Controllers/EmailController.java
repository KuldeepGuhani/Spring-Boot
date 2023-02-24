package com.EmailSender.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.EmailSender.Model.EmailRequest;
import com.EmailSender.Model.EmailResponse;
import com.EmailSender.Service.EmailService;




@RestController
@CrossOrigin
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@GetMapping("/welcome")
public String welcome() {
	
	return "hello i am working with email api";
}
	
	@PostMapping("/sendEmail")
public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request){
	
		boolean result=this.emailService.sendEmail(request.getTo(),request.getSubject(),request.getMessage());
		
		if(result) {
			return ResponseEntity.ok(new EmailResponse("Email Sent SucessFully"));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new EmailResponse("Email Internal Server Error : 500"));
		}
		//System.out.println(request.toString());
		//return ResponseEntity.ok("DOne..");
		
		}

}
