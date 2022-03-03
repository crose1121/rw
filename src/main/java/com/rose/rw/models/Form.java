package com.rose.rw.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Form {

    @NotEmpty(message="Your story needs a title.")
    @Size(min=2, message="Story title must be at least 2 characters")
    private String title;
    
    @NotEmpty(message="Story details can't be blank!")
    @Size(min=200, message="Story must be at least 200 characters")
    @Size(max=2000, message="Story cannot exceed 2000 characters")
    private String details;
    
    @NotEmpty(message="Category name cannot be empty")
    private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
    
    
}