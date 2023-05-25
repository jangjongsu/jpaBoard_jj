package com.jjcompany.jpaBoard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.entity.SiteMember;
import com.jjcompany.jpaBoard.exception.DataNotFoundException;
import com.jjcompany.jpaBoard.repository.QuestionRepository;

@Service
public class QuestionService {
	
		@Autowired
		private QuestionRepository questionRepository;
		
		
		public void questionCreate(String subject, String content, SiteMember writer) {
			
			Question question = new Question();
			question.setSubject(subject);
			question.setContent(content);
			question.setWriter(writer);
			question.setCreateDate(LocalDateTime.now());
			
			questionRepository.save(question);
		}
		
		public List<Question> getQuestionList() {
			
			List<Question> questionList  = questionRepository.findAll();
			
			return questionList;
		}
		
		
		
		public Question getQuestion(Integer id) {
			
			Optional<Question> qptQuestion  = questionRepository.findById(id);
			
			if(qptQuestion.isPresent()) {
				Question question = qptQuestion.get();
				return question;
			} else {
				throw new DataNotFoundException("선택하신 질문은 없는 글입니다.");
			}
		
			
		}
		public void questoinModify(Question question, String subject, String content) {
		
			question.setSubject(subject);
			question.setContent(content);
			question.setModifyDate(LocalDateTime.now());
			
			questionRepository.save(question);	
		}
	
}
