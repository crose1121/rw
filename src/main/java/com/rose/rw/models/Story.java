package com.rose.rw.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="stories")
public class Story{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(updatable=false)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;
    
	@PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
    
    @NotEmpty(message="Your story needs a title.")
    @Size(max=50, message="Story title cannot be greater than 2 characters")
    private String title;
    
    @Size(min=200, message="Story must be at least 200 characters")
    @Size(max=2000, message="Story cannot exceed 2000 characters")
    private String details;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    
    @OneToMany(mappedBy="story", fetch = FetchType.LAZY) 
    private List<Comment> comments;
    
    //many to many relationship for likes
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "story_likes", 
        joinColumns = @JoinColumn(name = "story_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userLikes;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "stories_categories", 
        joinColumns = @JoinColumn(name = "story_id"), 
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
    
    //constructors
    public Story () {
    	
    }
    
    public Story(String title, String details) {
    	this.title = title;
    	this.details = details;
    }
    
    //getters and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<User> getUserLikes() {
		return userLikes;
	}
	public void setUserLikes(List<User> userLikes) {
		this.userLikes = userLikes;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
    
    
}
