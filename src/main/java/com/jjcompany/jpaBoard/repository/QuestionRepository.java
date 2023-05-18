package com.jjcompany.jpaBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jjcompany.jpaBoard.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

}
