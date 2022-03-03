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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rose.rw.models.Category;
import com.rose.rw.models.Comment;
import com.rose.rw.models.Form;
import com.rose.rw.models.LoginUser;
import com.rose.rw.models.Story;
import com.rose.rw.models.User;
import com.rose.rw.services.UserService;


@Controller
public class HomeController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("newUser",new User());
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}
	
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
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		Long id = (Long) session.getAttribute("loggedInUserID");
		
		if(id==null) {
			return "redirect:/";
		}
		
		User loggedInUser = userService.findOneUser(id);
		List<Category> allCategories = userService.findAllCategories();
		model.addAttribute("allCategories", allCategories);
		model.addAttribute("loggedInUser", loggedInUser);
		model.addAttribute("allStories",userService.findAllStories());
		return "dashboard.jsp";
	}
	
	@GetMapping("/story/new")
	public String newStory(Model model) {
		model.addAttribute("story", new Story());
//		model.addAttribute("category", new Category());

		// newstory.jsp needs to know about all the categories
		List<Category> allCategories = userService.findAllCategories();
		model.addAttribute("allCategories",allCategories);
		System.out.println("Logging allCategories object");
		System.out.println(allCategories);
		return "newstory.jsp";
	}
	
	@PostMapping("/story/create")
	public String createStory(@Valid @ModelAttribute("story") Story story, BindingResult result, Model model, HttpSession session, HttpServletRequest request) {
		String[] selectedCategoryIds = request.getParameterValues("selected");
		if (selectedCategoryIds==null) {
			result.rejectValue("categories", "categories", "Please select at least 1 category.");

		}
		if (result.hasErrors()){
			System.out.println(result);
			List<Category> allCategories = userService.findAllCategories();
			model.addAttribute("allCategories",allCategories);
			
			return "newstory.jsp";
		}
		else {
					
			//we get back a list of strings which are category ids. Use for loop
			//to loop through each category ID, cast it as a long, retrieve the story's
			//current list of categories, retrieve the selected category by its ID,
			//and add that category to the list
			
			
			for (String selectedCategoryId:selectedCategoryIds) {
				
				Long catId = (long) Integer.parseInt(selectedCategoryId);
				Category selectedCategory = userService.findOneCategory(catId);
				System.out.println("Logging the selected category: ");
				System.out.println(selectedCategory);
				//if the storey.getCategories is null, then create a new List<Category> that is empty and se the story.setCategories to be that empty list and add the category to the list 
				if (story.getCategories()==null) {
					List<Category> emptyCatsList = new ArrayList<Category>();
					story.setCategories(emptyCatsList);
				}
				story.getCategories().add(selectedCategory);
			}
			
			
			User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
			currentUser.getStories().add(story);
			story.setUser(currentUser);
			userService.createStory(story);
			return "redirect:/home";
		}
	}
	
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
	
	@PostMapping("/comment/create/{story_Id}")
	public String createComment(@PathVariable("story_Id") Long story_Id, @Valid @ModelAttribute("comment") Comment comment, BindingResult result, Model model, HttpSession session) {
		
		if (result.hasErrors()) {
			model.addAttribute("story", userService.findStoryById(story_Id));
			model.addAttribute("currentStoryComments", userService.findStoryById(story_Id).getComments());
			model.addAttribute("currentUser", userService.findOneUser((Long) session.getAttribute("loggedInUserID")));
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
	
	//method that acceps a route like /story/like/{id} where id is story id
	//get the logged in user using session id and store in a variable like "userobj"
	//get the story usin the id form the path - examplse: storyobj
	//storyobj.getuserlikes.add(userobj);
	//send the storyobj to service for update
	
	@GetMapping("/like/story/{id}")
	public String likeStory(@PathVariable("id") Long id, HttpSession session) {
		
		Story currentStory = userService.findStoryById(id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		currentStory.getUserLikes().add(currentUser);
		userService.updateStory(currentStory);
		return "redirect:/story/{id}";
	}
	
	@GetMapping("/dislike/story/{id}")
	public String dislikeStory(@PathVariable("id") Long id, HttpSession session) {
		
		Story currentStory = userService.findStoryById(id);
		User currentUser = userService.findOneUser((Long) session.getAttribute("loggedInUserID"));
		currentStory.getUserLikes().remove(currentUser);
		userService.updateStory(currentStory);
		return "redirect:/story/{id}";
	}
	
	
	@GetMapping("/stories/category/{id}")
	public String getStoriesByCategory(@PathVariable("id") Long id, Model model) {
		//we first need to pull the clicked category from the path variable and add to model
		Category currentCategory = userService.findOneCategory(id);
		model.addAttribute("currentCategory", currentCategory);
		
		// next we pull the current category's list of stories and add to model
		List<Story> categoryStories = currentCategory.getStories(); 
		model.addAttribute("categoryStories", categoryStories);
		
		
		
		//pull the stories' list of related categories, filtering out the clicked category/ add to model
//		List<Category> otherRelatedCategories = userService.filterStoryCategories(null)
//		model.addAttribute("otherRelatedCategories", otherRelatedCategories);
		
		
		return "categorystories.jsp";
	}
	
	
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
}