package com.tasalparslan.techconfscheduler.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tasalparslan.techconfscheduler.model.Talk;

public interface TalkRepository extends CrudRepository<Talk, Long> {

	List<Talk> findAll();

}
