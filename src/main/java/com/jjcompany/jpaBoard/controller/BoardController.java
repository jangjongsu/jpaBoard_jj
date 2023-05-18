package com.jjcompany.jpaBoard.controller;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.repository.QuestionRepository;

@Controller
public class BoardController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@RequestMapping(value = "/")
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
		
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());// 서버의 현재시간
		
		questionRepository.save(question); // insert(질문글 저장)
		
		return "redirect:questionList";
	}
	@RequestMapping(value = "/questionList")
	public String questionList(Model model) {
		
		List<Question> questionList  = questionRepository.findAll();
		
		model.addAttribute("questionList", questionList);
		
		return "questionList";
	}
}
