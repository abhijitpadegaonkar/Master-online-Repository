package com.ap.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ap.demo.model.Company;
import com.ap.demo.respository.CompanyRepository;

@Controller
public class AppController {

	@Autowired
	private CompanyRepository userRepo;

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@PostMapping("/logout")
	public String logout() {

		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new Company());

		return "signup_form";
	}

	@PostMapping("/process_register")
	public String processRegister(Company user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepo.save(user);

		return "users";
	}

	@GetMapping("/users")
	public String listUsers(Model model) {
		List<Company> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

//	@GetMapping("/add-company")
//	public String showAddCompanyForm(Model model) {
//		model.addAttribute("user", new User());
//
//		return "add-company";
//	}
//
//	@PostMapping("/add-company")
//	public String processAddCompanyRequest(User user) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String encodedPassword = passwordEncoder.encode(user.getPassword());
//		user.setPassword(encodedPassword);
//		user.setUsername(GeneralUtility.generateUsername(user.getCompName()));
//
//		userRepo.save(user);
//
//		return "company-master";
//	}

//	@GetMapping("/add-companies")
//	public String showAddCompaniesForm() {
//		return "add-companies";
//	}
//
//	@PostMapping("/add-companies")
//	public String addCompanies(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//		System.out.println("File is " + file.getName());
//
//		if (file.isEmpty()) {
//			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//			return "redirect:company-master";
//		}
//
//		List<User> companies = FileUtil.readAndValidateFile(file);
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		for (User company : companies) {
//			company.setPassword(passwordEncoder.encode(company.getPassword()));
//			company.setUsername(GeneralUtility.generateUsername(company.getCompName()));
//			long createdComapayId = userRepo.save(company);
//		}
//
//		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename()
//				+ " and added " + companies.size() + " users");
//
//		return "redirect:/company-master";
//	}

}
