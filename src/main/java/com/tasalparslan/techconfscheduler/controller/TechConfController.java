package com.tasalparslan.techconfscheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasalparslan.techconfscheduler.model.Talk;
import com.tasalparslan.techconfscheduler.repository.TalkRepository;

@RestController
@RequestMapping(path = "/api")
public class TechConfController {

	@Autowired
	TalkRepository repository;

	@RequestMapping("/talks")
	public List<Talk> getTalks() {
		return repository.findAll();
	}
}
