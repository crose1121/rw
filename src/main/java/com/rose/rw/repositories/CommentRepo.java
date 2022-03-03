package com.rose.rw.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rose.rw.models.Comment;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {
	List<Comment> findAll();
}
