package com.jjcompany.jpaBoard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jjcompany.jpaBoard.entity.SiteMember;


public interface SiteMemberRepository extends JpaRepository<SiteMember, Integer>{
	
	public Optional<SiteMember> findByUsername(String username);

}
