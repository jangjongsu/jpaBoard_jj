package com.jjcompany.jpaBoard.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jjcompany.jpaBoard.entity.Answer;
import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.repository.AnswerRepository;
import com.jjcompany.jpaBoard.repository.QuestionRepository;


@Service
public class AnswerService {

	@Autowired
	private AnswerRepository answerRepository;
	
	public void answerCreate(String content, Question question) {
		
		Answer answer = new Answer();
		
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		
		answerRepository.save(answer);
		
	}
}
