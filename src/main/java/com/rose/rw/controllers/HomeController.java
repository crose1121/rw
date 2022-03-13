package com.rose.rw.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rose.rw.models.Category;
import com.rose.rw.models.Comment;
import com.rose.rw.models.LoginUser;
import com.rose.rw.models.Story;
import com.rose.rw.models.User;
import com.rose.rw.services.UserService;


@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	//Login Registration Page
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("newUser",new User());
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}
	
	//Register
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model, HttpSession session) {
		
		User user = userService.register(newUser, result);
		
		if (result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}		
		session.setAttribute("loggedInUserID", user.getId());
		session.setAttribute("loggedInUserName", user.getUsername());
		return "redirect:/home";
	}
	
	//Login

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,BindingResult result, Model model, HttpSession session) {
		User user = userService.login(newLogin, result);
		
		if(result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "index.jsp";
		}
		session.setAttribute("loggedInUserID", user.getId());
		return "redirect:/home";
	}
	
	//Dashboard
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		
		Long id = (Long) session.getAttribute("loggedInUserID");
		
		//if session id is null redirect to login/registration
		if(id==null) {
			return "redirect:/";
		}
		
		//retrieve the logged in user and populate the dashboard with data
		User loggedInUser = userService.findOneUser(id);
		List<Category> allCategories = userService.findAllCategories();
		
		
		model.addAttribute("allCategories", allCategories);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("allStories",userService.findAllStories());
		model.addAttribute("allUsers", userService.findAllUsers());
		model.addAttribute("currentlyFollowing", loggedInUser.getFollowing());
		return "dashboard.jsp";
	}
	
	//Tell a new story 
	
	@GetMapping("/story/new")
	public String newStory(Model model, HttpSession session) {
		// newstory.jsp needs to know about all the categories
		List<Category> allCategories = userService.findAllCategories();
		model.addAttribute("allCategories",allCategories);
		model.addAttribute("story", new Story());
		// also needs to know about currentUser.getFollowing()
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		List<User> currentlyFollowing = currentUser.getFollowing();
		model.addAttribute("currentlyFollowing", currentlyFollowing);
		
		return "newstory.jsp";
	}
	
	//Tell a new story 
	//Note: We need HttpServletRequest here to receive multiple inputs from checkboxes. they come in as a list of strings
	//that we have to cast as longs to receive category id's.
	
	@PostMapping("/story/create")
	public String createStory(@Valid @ModelAttribute("story") Story story, BindingResult result, Model model, HttpSession session, HttpServletRequest request) {
		String[] selectedCategoryIds = request.getParameterValues("selectedCategories");
		String[] selectedUserIds = request.getParameterValues("selectedUsers");
		if (selectedCategoryIds==null) {
			result.rejectValue("categories", "categories", "Please select at least 1 category.");

		}
		if (result.hasErrors()){
			System.out.println(result);
			List<Category> allCategories = userService.findAllCategories();
			model.addAttribute("allCategories",allCategories);
			User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
			model.addAttribute("currentlyFollowing",currentUser.getFollowing());
			return "newstory.jsp";
		}
		else {
					
			//we get back a list of strings which are category ids. Loop through each category ID, cast it as a long, retrieve the story's
			//current list of categories, retrieve the selected category by its ID,
			//and add that category to the list
			
			
			for (String selectedCategoryId:selectedCategoryIds) {
				
				Long catId = (long) Integer.parseInt(selectedCategoryId);
				Category selectedCategory = userService.findOneCategory(catId);

				//if the story.getCategories is null, then create a new List<Category> that is empty and set the story.setCategories to be that empty list and add the category to the list 
				if (story.getCategories()==null) {
					List<Category> emptyCatsList = new ArrayList<Category>();
					story.setCategories(emptyCatsList);
				}
				story.getCategories().add(selectedCategory);
			}
			
			//repeat this same process but for tagged users instead of related categories
			if (selectedUserIds != null) {
				
				for (String selectedUserId:selectedUserIds) {
					
					Long userId = (long) Integer.parseInt(selectedUserId);
					User selectedUser = userService.findOneUser(userId);

					//if the story.getUsersTagged is null, then create a new List<User> that is empty and set the story.setTaggedUsers to be that empty list and add the user to the list 
					if (story.getUsersTagged()==null) {
						List<User> emptyUsersTagged = new ArrayList<User>();
						story.setUsersTagged(emptyUsersTagged);
					}
					story.getUsersTagged().add(selectedUser);
				}
			}
			
			User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
			currentUser.getStories().add(story);
			story.setUser(currentUser);
			userService.createStory(story);
			return "redirect:/home";
		}
	}
	
	//View story details
	
	@GetMapping("/story/{story_Id}")
	public String showStory(@PathVariable("story_Id") Long storyId, Model model,HttpSession session) {
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		Story currentStory = userService.findStoryById(storyId);
		List<Comment> currentStoryComments = currentStory.getComments();

		model.addAttribute("currentStoryComments", currentStoryComments);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("story", currentStory);
		model.addAttribute("comment", new Comment());
		return "storydetails.jsp";
	}
	
	//Leave a comment on a story
	
	@PostMapping("/comment/create/{story_Id}")
	public String createComment(@PathVariable("story_Id") Long story_Id, @Valid @ModelAttribute("comment") Comment comment, BindingResult result, Model model, HttpSession session) {
		
		if (result.hasErrors()) {
			User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
			model.addAttribute("story", userService.findStoryById(story_Id));
			model.addAttribute("currentStoryComments", userService.findStoryById(story_Id).getComments());
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("currentlyFollowing", currentUser.getFollowing());
			return "storydetails.jsp";
		}
		
		Story currentStory = userService.findStoryById(story_Id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		
		//set the comment's user and story attributes
		comment.setUser(currentUser);
		comment.setStory(currentStory);
		
		//create the comment
		userService.createComment(comment);
		
		//add the comment to story's list of comments
		currentStory.getComments().add(comment);
		
		//add the comment to the user's list of comments
		currentUser.getComments().add(comment);
		
		//send the story and user to the seervice for update now that their arraylists are updated
		
		
		return "redirect:/story/{story_Id}";
	}
	
	//Like a story
	
	@GetMapping("/like/story/{id}")
	public String likeStory(@PathVariable("id") Long id, HttpSession session) {
		
		Story currentStory = userService.findStoryById(id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		currentStory.getUserLikes().add(currentUser);
		userService.updateStory(currentStory);
		
		
		return "redirect:/story/{id}";
	}
	
	//Like a comment
	
	@GetMapping("/like/comment/{story_Id}/{comment_Id}")
	public String likeComment(@PathVariable("story_Id") Long story_Id, @PathVariable("comment_Id") Long comment_Id, HttpSession session) {

		Comment currentComment = userService.findOneComment(comment_Id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		
		//add the user to the current comment's list of users who liked it
		currentComment.getCommentLikes().add(currentUser);
		
		//add the comment to the current user's list of liked comments
//		currentUser.getCommentsLiked().add(currentComment);
		
		//we need to update the current comment and the current user to the db
		userService.updateUser(currentUser);
		userService.updateComment(currentComment);
		
		return "redirect:/story/{story_Id}";
	}
	
	//"Un-Like" a story
	
	@GetMapping("/dislike/story/{id}")
	public String dislikeStory(@PathVariable("id") Long id, HttpSession session) {
		
		Story currentStory = userService.findStoryById(id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		currentStory.getUserLikes().remove(currentUser);
		userService.updateStory(currentStory);
		return "redirect:/story/{id}";
	}
	
	//"Un-Like" a comment
	
	@GetMapping("/dislike/comment/{story_Id}/{comment_Id}")
	public String dislikeComment(@PathVariable("story_Id") Long story_Id, @PathVariable("comment_Id") Long comment_Id, HttpSession session) {
		
		Comment currentComment = userService.findOneComment(comment_Id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		
		currentComment.getCommentLikes().remove(currentUser);
		
		userService.updateUser(currentUser);
		userService.updateComment(currentComment);
		
		return "redirect:/story/{story_Id}";
	}
	
	//View stories by category
	
	@GetMapping("/stories/category/{id}")
	public String getStoriesByCategory(@PathVariable("id") Long id, Model model, HttpSession session) {
		//we first need to pull the clicked category from the path variable and add to model
		Category currentCategory = userService.findOneCategory(id);
		model.addAttribute("currentCategory", currentCategory);
		
		// next we pull the current category's list of stories and add to model
		List<Story> categoryStories = currentCategory.getStories(); 
		model.addAttribute("categoryStories", categoryStories);
		
		//need current user to populate followers and suggested following
		User loggedInUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		model.addAttribute("loggedInUser", loggedInUser);
		
		//need to populate the rest of the model attributes
		
		model.addAttribute("currentlyFollowing",loggedInUser.getFollowing());		
		model.addAttribute("allCategories", userService.findAllCategories());
		model.addAttribute("allUsers",userService.findAllUsers());
				
		return "categorystories.jsp";
	}
	
	//Follow a user
	
	@GetMapping("/follow/{user_Id}")
	public String followUser(@PathVariable("user_Id") Long user_Id, HttpSession session) {
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		User followUser = userService.findOneUser(user_Id);
		
		currentUser.getFollowing().add(followUser);
		
		userService.updateUser(currentUser);
		
		return "redirect:/user/{user_Id}";
	}
	
	//un-follow a user
	@GetMapping("/unfollow/{user_Id}")
	public String unfollowUser(@PathVariable("user_Id") Long user_Id, HttpSession session) {
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		User targetUser = userService.findOneUser(user_Id);
		
		currentUser.getFollowing().remove(targetUser);
		
		userService.updateUser(currentUser);
		return "redirect:/user/{user_Id}";
	}
	
	//View stories by user
	
	@GetMapping("/user/{user_Id}")
	public String userStories(@PathVariable("user_Id") Long user_Id, Model model, HttpSession session) {
		//get the selected user from path variable and add to model
		
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		List<User> currentlyFollowing = currentUser.getFollowing();
		
		model.addAttribute("loggedInUser", currentUser);
		model.addAttribute("selectedUser", userService.findOneUser(user_Id));
		model.addAttribute("allCategories", userService.findAllCategories());
		model.addAttribute("currentlyFollowing", currentlyFollowing);
		model.addAttribute("allUsers", userService.findAllUsers());
		
		return "userstory.jsp";
	}
	
	//delete a comment
	@GetMapping("/comment/delete/{story_Id}/{comment_Id}")
	public String deleteComment(@PathVariable("story_Id") Long story_Id, @PathVariable("comment_Id") Long comment_Id) {

		userService.deleteComment(comment_Id);
		return "redirect:/story/{story_Id}";
	}
	
	//delete a story
	@GetMapping("/story/delete/{story_Id}")
	public String deleteStory(@PathVariable("story_Id") Long story_Id) {
		userService.deleteStory(story_Id);
		return "redirect:/home";
		
	}
	

	
	//Logout, clear session
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
}