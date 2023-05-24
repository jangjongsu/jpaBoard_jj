package com.jjcompany.jpaBoard.controller;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jjcompany.jpaBoard.dto.AnswerForm;
import com.jjcompany.jpaBoard.dto.QuestionForm;
import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.repository.QuestionRepository;
import com.jjcompany.jpaBoard.service.AnswerService;
import com.jjcompany.jpaBoard.service.QuestionService;

import oracle.net.aso.m;

@Controller
public class BoardController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:questionList";
	}
	
	@RequestMapping(value = "/questionForm")
	public String questionForm() {
		return "questionForm";
	}
	
	@PostMapping(value = "/questionCreate")
	public String create(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		String subject = questionForm.getSubject();
		String content = questionForm.getContent();
		
		if(bindingResult.hasErrors()) {//에러발생하면 참
			return "questionForm";
		} else{
			questionService.questionCreate(subject, content);
		}
		return "redirect:questionList";
	}
	
	@GetMapping(value = "/questionCreate")
	public String questionCreate(QuestionForm questionForm) {
		return"questionForm";
	}
	
	
	@RequestMapping(value = "/questionList")
	public String questionList(Model model) {
		
//  	List<Question> questionList  = questionRepository.findAll();
		
		List<Question> questionList  = questionService.getQuestionList();
		
		model.addAttribute("questionList", questionList);
		
		return "questionList";
	}
	@RequestMapping(value = "/questionContentView/{id}")
	public String questionContentView(@PathVariable("id") Integer id, Model model, AnswerForm answerForm) {
		
		Question question = questionService.getQuestion(id);
		
		model.addAttribute("question", question);
		
		return "questionView";
	}
	
	@RequestMapping(value = "/answerCreate/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
		
		
		Question question = questionService.getQuestion(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "questionView";
		}else {
			answerService.answerCreate(answerForm.getContent(), question);
		}
		
		return String.format("redirect:/questionContentView/%s",id);
	}
}
