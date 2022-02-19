package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser,
								Model model) {
		
		String email=loggedUser.getUsername();
		User user=service.getByEmail(email);
		model.addAttribute("user", user);
		return "users/account_form";
	}
	
	@PostMapping("/update")
	public String saveDetails(@ModelAttribute("user") User user, RedirectAttributes di,
			@AuthenticationPrincipal ShopmeUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser = service.updateAccount(user);
			String uploadDir = "user-photos/" + saveUser.getId();

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			// user.setPhotos(null);
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.updateAccount(user);

		}
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());

		di.addFlashAttribute("message", "yout account detail has been updated");

		return  "redirect:/account";
	}
	
	
}
