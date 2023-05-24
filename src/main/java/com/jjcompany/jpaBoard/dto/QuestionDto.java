package com.jjcompany.jpaBoard.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.jjcompany.jpaBoard.entity.Answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
	
	private Integer id;
	private String subject;
	private String content;
	private LocalDateTime createtime;
	
	private List<Answer> answer;
}
