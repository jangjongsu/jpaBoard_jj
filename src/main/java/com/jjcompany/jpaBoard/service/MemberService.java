package com.jjcompany.jpaBoard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jjcompany.jpaBoard.entity.SiteMember;
import com.jjcompany.jpaBoard.repository.SiteMemberRepository;

@Service
public class MemberService {
	
	@Autowired
	private SiteMemberRepository siteMemberRepository;
	
	public SiteMember memberJoin(String userid, String userpw, String email) {
		
		SiteMember siteMember = new SiteMember();
		siteMember.setUserid(userid);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		siteMember.setUserpw(passwordEncoder.encode(userpw));
		//유저가 입력한 암호를 hash화 하여 암호문을 db에 저장
		
		// siteMember.setUserpw(userpw);
		siteMember.setEmail(email);
		
		siteMemberRepository.save(siteMember);
		
		return siteMember;
		
	}
}
