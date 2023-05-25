package com.jjcompany.jpaBoard.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jjcompany.jpaBoard.entity.Answer;
import com.jjcompany.jpaBoard.entity.Question;
import com.jjcompany.jpaBoard.entity.SiteMember;
import com.jjcompany.jpaBoard.exception.DataNotFoundException;
import com.jjcompany.jpaBoard.repository.AnswerRepository;
import com.jjcompany.jpaBoard.repository.QuestionRepository;


@Service
public class AnswerService {

	@Autowired
	private AnswerRepository answerRepository;
	
	public void answerCreate(String content, Question question, SiteMember writer) {
		
		Answer answer = new Answer();
		
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setWriter(writer);
		
		answerRepository.save(answer);
		
	}
	
	public Answer getAnswer(Integer id) {
		Optional<Answer> optAnswer = answerRepository.findById(id);
		
		if(optAnswer.isPresent()) {
			return optAnswer.get();
		}else{
			throw new DataNotFoundException("선택하신답변은 없는 글 입니다.");
		}
	}
	
	public void answerModify(Answer answer, String content) {
		answer.setContent(content);
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());//현재 시간 가져와 답변 수정시간으로 입력
		
		answerRepository.save(answer);
		
	}
	public void answerDelete(Integer id) {
		answerRepository.deleteById(id);
	}
}
