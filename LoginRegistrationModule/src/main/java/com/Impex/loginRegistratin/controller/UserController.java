package com.Impex.loginRegistratin.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.Impex.loginRegistratin.entities.User;
import com.Impex.loginRegistratin.repos.UserRepository;
import com.Impex.loginRegistratin.services.SecurityService;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private SecurityService securityService;

	@GetMapping("/")
	public String viewHomePage() {
		return "index";
	}

	@RequestMapping("/showReg")
	public String showRegistrationPage() {

		return "registerUser";
	}

	@RequestMapping(value = "registerUser", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "login";

	}

	@RequestMapping("/loginPage")
	public String loginPage() {
		return "login";
	}

	@RequestMapping("/logout-success")
	public String logoutPage() {
		return "login";

	}

	/*@RequestMapping("/forgot")
	public String forgotPassword() {

		return "forgotPassword";
	}*/

	@RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
	public String updatePassword(@RequestParam("email") String email, @RequestParam("lastName") String LastName,
			ModelMap modelmap) {
		User user = userRepository.findByEmail(email);
		if (user.getLastName().equals(LastName)) {
			modelmap.addAttribute("user", user);
			return "login/updatePassword";
		} else {
			modelmap.addAttribute("msg", "Invalid User name or Password. Please Try Again!!!");
			return "login/forgotPassword";
		}

	}

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public String updateUser(@RequestParam("password") String password, @ModelAttribute("user") User user,
			ModelMap modelmap) {
		user.setPassword(password);
		userRepository.save(user);
		modelmap.addAttribute("msg", "updated Successfully");
		return "login/updatePassword";

	}
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			ModelMap modelMap) {
		boolean loginResponse = securityService.login(email, password);
		if (loginResponse) {
			if (email.equalsIgnoreCase("shalinisinghrajput96@gmail.com")) {
				List<User> listUser = userRepository.findAll();
				modelMap.addAttribute("listUser",listUser);
				return "adminPage";
			} else {
				User user = userRepository.findByEmail(email);
				modelMap.addAttribute("user",user);
				return "userPage";
			}
		}else {
		modelMap.addAttribute("msg", "Invalid User name or Password. Please Try Again!!!");
		return "login";
		}
	}
	
	
	@RequestMapping("/edit/{id}")
    public ModelAndView editProfile(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("new");
        User user = userRepository.findById(id).get();
        mav.addObject("user", user);
        return mav;
        
    }
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String updateProfile(@ModelAttribute("user") User updatedUser, ModelMap modelMap) {
		User user1 = userRepository.findById(updatedUser.getId()).get();
		user1.setAge(updatedUser.getAge());
		user1.setMobile(updatedUser.getMobile());
		user1.setNationality(updatedUser.getNationality());
		user1.setGender(updatedUser.getGender());
		user1.setResume(updatedUser.getResume());
		
		/*Path path = Paths.get("uploads/");
		try {
			InputStream inputStream= resume.getInputStream();
			Files.copy(inputStream, path.resolve(resume.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
		}catch(Exception e) {
			
		}*/
		User user = userRepository.save(user1);
		modelMap.addAttribute("user",user);
		return "userPage";
	}
	
	
	
	
	@GetMapping("/403")
    public String error403() {
        return "login";
    }

}
