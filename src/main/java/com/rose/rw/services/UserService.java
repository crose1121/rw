package com.rose.rw.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.rose.rw.models.Category;
import com.rose.rw.models.Comment;
import com.rose.rw.models.LoginUser;
import com.rose.rw.models.Story;
import com.rose.rw.models.User;
import com.rose.rw.repositories.CategoryRepo;
import com.rose.rw.repositories.CommentRepo;
import com.rose.rw.repositories.StoryRepo;
import com.rose.rw.repositories.UserRepo;

@Service
public class UserService {
	@Autowired
	UserRepo userRepo;
	@Autowired
	StoryRepo storyRepo;
	@Autowired
	CommentRepo commentRepo;
	@Autowired
	CategoryRepo categoryRepo;
	
	//first two Service methods are always REGISTER AND LOGIN!!!!
	
	//these will take 2 arguments, user object and errors
	public User register(User newUser, BindingResult result) {
		Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());
		if(potentialUser.isPresent()) {
			result.rejectValue("email", "EmailTaken", "This email is already in use. Login?");
		}
		if(!newUser.getPassword().equals(newUser.getConfirm())) {
			result.rejectValue("confirm", "NoMatchy", "Passwords don't match");
		}
		if(result.hasErrors()) {
			return null;
		}
		else {
			String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
			newUser.setPassword(hashed);
			return userRepo.save(newUser);
		}
	}
	
	public User login(LoginUser loginObj, BindingResult result) {
		Optional<User> potentialUser = userRepo.findByEmail(loginObj.getEmail());
		if(!potentialUser.isPresent()) {
			result.rejectValue("email", "EmailNotFound", "This email does not exist yet. Register?");
		}
		else {
			if(!BCrypt.checkpw(loginObj.getPassword(), potentialUser.get().getPassword())) {
				result.rejectValue("password", "NoMatchy","Invalid Password");
			}
		}
		if(result.hasErrors()) {
			return null;
		}
		else {
			return potentialUser.get();
		}
	}
	
	public User findOneUser(Long id) {
		return userRepo.findById(id).orElse(null);
	}
	
	public Story createStory(Story story) {
		return storyRepo.save(story);
	}
	
	public List<Story> findAllStories() {
		return storyRepo.findAll();
	}
	
	public Story findStoryById(Long id) {
		return storyRepo.findById(id).orElse(null);
	}
	
	public Comment createComment(Comment comment) {
		return commentRepo.save(comment);
	}
	
	public Story updateStory(Story story) {
		return this.storyRepo.save(story);
	}
	
	public List<Category> findAllCategories() {
		return categoryRepo.findAll();
	}
	
	public List<Category> filterStoryCategories(Story story) {
		return categoryRepo.findByStoriesNotContaining(story);
	}
	
	public Category findOneCategory(Long id) {
		return categoryRepo.findById(id).orElse(null);
	}
	
}
