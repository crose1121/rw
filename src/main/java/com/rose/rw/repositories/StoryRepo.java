package com.rose.rw.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rose.rw.models.Category;
import com.rose.rw.models.Story;

@Repository
public interface StoryRepo extends CrudRepository<Story, Long>{
	List<Story> findAll();
	List<Category> findByCategoriesNotContaining(Category category);
}
