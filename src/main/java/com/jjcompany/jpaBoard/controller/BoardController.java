package com.jjcompany.jpaBoard.controller;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.jjcompany.jpaBoard.dto.AnswerForm;
import com.jjcompany.jpaBoard.dto.MemberForm;
import com.jjcompany.jpaBoard.dto.QuestionForm;
import com.jjcompany.jpaBoard.entity.Answer;
import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.entity.SiteMember;
import com.jjcompany.jpaBoard.repository.QuestionRepository;
import com.jjcompany.jpaBoard.service.AnswerService;
import com.jjcompany.jpaBoard.service.MemberService;
import com.jjcompany.jpaBoard.service.QuestionService;

import oracle.net.aso.m;
import oracle.net.aso.q;

@Controller
public class BoardController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/")
	public String home() {
		return "redirect:questionList";
	}
	@RequestMapping(value = "/index")
	public String index() {
		return "redirect:questionList";
	}
	
	@RequestMapping(value = "/questionForm")
	public String questionForm() {
		return "questionForm";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/questionCreate")
	public String create(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		String subject = questionForm.getSubject();
		String content = questionForm.getContent();
		
		if(bindingResult.hasErrors()) {//에러발생하면 참
			return "questionForm";
		} else{
			//principal.getName() > 현재 로그인 중인 유저의 useranme를 가져와라
			SiteMember siteMember = memberService.getMember(principal.getName());
			
			questionService.questionCreate(subject, content, siteMember);
		}
		return "redirect:questionList";
	}
	
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
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
	
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
	@RequestMapping(value = "/answerCreate/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		
		
		Question question = questionService.getQuestion(id);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "questionView";
		}else {
			SiteMember siteMember = memberService.getMember(principal.getName());
			
			answerService.answerCreate(answerForm.getContent(), question, siteMember);
		}
		
		return String.format("redirect:/questionContentView/%s",id);
	}
	
	@GetMapping(value = "/memberJoin")
	public String memberJoinForm(MemberForm memberForm) {
		
		return "member_join";
	}
	@PostMapping(value = "/memberJoin")
	public String memberJoinOk(@Valid MemberForm memberForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "member_join";
		}
		if(!memberForm.getUserpw1().equals(memberForm.getUserpw2())) { //비밀번호 확인 실패
			bindingResult.rejectValue("userpw2", "passwordCheckInCorrect","비밀번호 확인란에 비밀번호가 일치하지 않습니다.");
			return "member_join";
		}
		try {
			memberService.memberJoin(memberForm.getUsername(), memberForm.getUserpw1(), memberForm.getEmail());
		}catch(DataIntegrityViolationException e){
			e.printStackTrace(); //콘솔창에 에러 이유를 출력
			bindingResult.reject("idRegFail","이미 등록된 아이디 입니다.");
			return "member_join";
		}catch (Exception e) {
			e.printStackTrace(); //콘솔창에 에러 이유를 출력
			bindingResult.reject("Fail", e.getMessage()); //해당오류 메시지를 에러로 전송
			return "member_join";
		}
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/login")
	public String login() {
		return "login_form";
	}
	
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
	@GetMapping(value = "/questionModify/{id}")
	public String questionModify(@PathVariable("id") Integer id, Principal principal, QuestionForm questionForm, BindingResult bindingResult) {
		
		Question question = questionService.getQuestion(id);

		if(!question.getWriter().getUsername().equals(principal.getName())) {
			//글쓴이와 현재 로그인 중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"해당 질문에 대한 수정 권한이 없습니다.");
			//에러를 던진다
		}
		
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		
		
		return "questionForm";
	}
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
	@PostMapping(value = "/questionModify/{id}")
	public String questionModifyOk(@PathVariable("id") Integer id, Principal principal, @Valid QuestionForm questionForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "questionForm";
		}
		Question question = questionService.getQuestion(id);
		
		if(!question.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 질문에 대한 수정 권한이 없습니다.");
		}
		
		questionService. questoinModify(question, questionForm.getSubject(), questionForm.getContent());
		
		return String.format("redirect:/questionContentView/%s",id);
	}
	
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
	@GetMapping(value = "/answerModify/{id}")
	public String answerModify(@PathVariable("id") Integer id, Principal principal, AnswerForm answerForm, BindingResult bindingResult) {
		
		Answer answer = answerService.getAnswer(id);
		
		if(!answer.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 질문에 대한 수정 권한이 없습니다.");
		}
		
		answerForm.setContent(answer.getContent());//answerForm에 
		
		return "answer_form";
	}
	
	@PreAuthorize("isAuthenticated()") // 인증이 필요하면 참
	@PostMapping(value = "/answerModify/{id}")
	public String answerModifyOk(@PathVariable("id") Integer id, Principal principal, @Valid AnswerForm answerForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "questionForm";
		}
		Answer answer = answerService.getAnswer(id);
		
		if(!answer.getWriter().getUsername().equals(principal.getName())) {
			//해당 질문의 글쓴이와 현재 로그인중인 유저의 아이디가 다르면
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 질문에 대한 수정 권한이 없습니다.");
		}
		
		answerService. answerModify(answer, answerForm.getContent());
		
		return String.format("redirect:/questionContentView/%s", answer.getQuestion().getId());
	}
	
	
	
}
