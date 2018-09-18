package com.tasalparslan.techconfscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tasalparslan.techconfscheduler.model.Talk;
import com.tasalparslan.techconfscheduler.service.TalkService;

@Controller
public class PageController {

	@Autowired
	private TalkService service;

	@GetMapping("registration-page")
	public String getRegistrationPage(Model model) {
		model.addAttribute("talk", new Talk());
		return "registration-page";
	}

	@GetMapping("agenda-page")
	public String getAgendaPage(Model model) {
		model.addAttribute("tracks", service.getTracks());
		return "agenda-page";
	}

	@PostMapping("create-talk")
	public String saveProduct(@ModelAttribute Talk talk) {
		service.registerNewTalk(talk);
		return "success-page";
	}

}