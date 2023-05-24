package com.jjcompany.jpaBoard.controller;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping(value = "/index")
	public String index() {
		return "redirect:questionList";
	}
	
	@RequestMapping(value = "/questionForm")
	public String questionForm() {
		return "questionForm";
	}
	
	@RequestMapping(value = "/questionCreate")
	public String create(HttpServletRequest request) {
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		questionService.questionCreate(subject, content);
		
		return "redirect:questionList";
	}
	@RequestMapping(value = "/questionList")
	public String questionList(Model model) {
		
//  	List<Question> questionList  = questionRepository.findAll();
		
		List<Question> questionList  = questionService.getQuestionList();
		
		model.addAttribute("questionList", questionList);
		
		return "questionList";
	}
	@RequestMapping(value = "/questionContentView/{id}")
	public String questionContentView(@PathVariable("id") Integer id, Model model) {
		
		Question question = questionService.getQuestion(id);
		
		model.addAttribute("question", question);
		
		return "questionView";
	}
	
	@RequestMapping(value = "/answerCreate/{id}")
	public String answerCreate(@PathVariable("id") Integer id, HttpServletRequest request) {
			
		String content = request.getParameter("content");
		
		Question question = questionService.getQuestion(id);
		
		answerService.answerCreate(content, question);
		
		
		return String.format("redirect:/questionContentView/%s",id);
	}
}
