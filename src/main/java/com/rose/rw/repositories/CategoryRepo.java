package com.rose.rw.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rose.rw.models.Category;
import com.rose.rw.models.Story;

@Repository
public interface CategoryRepo extends CrudRepository<Category, Long>{
	List<Category> findAll();
	List<Category> findByStoriesNotContaining(Story story);
}
