package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String listFirstPage(Model model) {

		return listByPage(1, model, "firstName", "asc", null);

	}
	

	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);

		List<User> listUsers = page.getContent();

		long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
		long endCount = pageNum * UserService.USER_PER_PAGE;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverSortDir", reverSortDir);
		model.addAttribute("keyword", keyword);

		return "users/users";

	}

	@GetMapping("/new")
	public String newUser(Model model) {
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create user");
		return "users/user_form";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute("user") User user, RedirectAttributes di,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser = userService.save(user);
			String uploadDir = "user-photos/" + saveUser.getId();

			FileUploadUtil.cleanDir(uploadDir);

			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			// user.setPhotos(null);
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			userService.save(user);

		}

		di.addFlashAttribute("message", "the user has been saved successfully");

		return getRedirectURLtoAffectecUser(user);
	}

	private String getRedirectURLtoAffectecUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/edit")
	public String editUser(@RequestParam("id") Integer id, Model model, RedirectAttributes di)
			throws UserNotFoundException {
		try {
			User user = userService.get(id);
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit user (Id : " + id + ")");
			return "users/user_form";
		} catch (UserNotFoundException e) {
			di.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted ");

		} catch (UserNotFoundException e) {

			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}
		return "redirect:/users";
	}

	@GetMapping("/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		userService.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "the user id " + id + " has been " + status;

		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}

	@GetMapping("/export/csv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		
		List<User> list = userService.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(list, response);
	}
	
	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		List<User> list = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(list, response);
	}
	
	@GetMapping("/export/pdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		
		List<User> list = userService.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(list, response);
	}
	

}
