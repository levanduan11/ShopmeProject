package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	public static final int USER_PER_PAGE=4;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public User getByEmail(String email) {
		
		return userRepository.getByEmail(email);
	}

	public List<User> listAll() {

		return userRepository.findAll(Sort.by("firstName").ascending());

	}
	
	public Page<User> listByPage(int pageNum,String sortField,String sortDir,String keyword) {
		
		Sort sort=Sort.by(sortField);
		
		
		sort=sortDir.equals("asc")? sort.ascending():sort.descending();
		
		Pageable pageable=PageRequest.of(pageNum-1, USER_PER_PAGE,sort);
		if (keyword!=null) {
			return userRepository.findAll(keyword,pageable);
		}
		return userRepository.findAll(pageable);
		
	}

	public List<Role> listRoles() {

		return roleRepository.findAll();
	}

	public User save(User user) {
		boolean isUpdating = (user.getId() != null);
		if (isUpdating) {
			User existing = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existing.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		// user.setId(0);
		
		
		return userRepository.save(user);
	}
	public User updateAccount(User userInForm) {
		
		User userInDb=userRepository.findById(userInForm.getId()).get();
		
		if (!userInForm.getPassword().isEmpty()) {
			userInDb.setPassword(userInForm.getPassword());
			encodePassword(userInDb);
		}
		if (userInForm.getPhotos()!=null) {
			userInDb.setPhotos(userInForm.getPhotos());
			
		}
		userInDb.setFirstName(userInForm.getFirstName());
		userInDb.setLastName(userInForm.getLastName());
		
		return userRepository.save(userInDb);
	}

	private void encodePassword(User user) {

		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);

	}

	public boolean isEmailUnique(Integer id, String email) {

		User user = userRepository.getByEmail(email);
		if (user == null) {
			return true;
		}
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if (user != null) {
				return false;

			}
		} else {
			if (user.getId() != id) {
				return false;
			}
		}

		return true;

	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {

			throw new UserNotFoundException("could not find any user with id " + id);
		}

	}

	public void delete(Integer id) throws UserNotFoundException {
		Long c = userRepository.countById(id);
		if (c == null || c == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);

		}
		userRepository.deleteById(id);

	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {

		userRepository.updateEnabledSatus(id, enabled);

	}

}
