package edu.kh.fin.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.member.model.dao.MemberDAO;
import edu.kh.fin.member.model.vo.Member;

@Service // Service 레이어, 비즈니스 로직을 가진 클래스임을 명시 + Bean 등록
public class MemberServiceImpl implements MemberService {
	
	@Autowired // bean으로 등록된 MemberDAO 객체 의존성 주입(DI)
	private MemberDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	// Bean으로 등록된 BCryptPasswordEncoder 객체를 의존성 주입(DI)

	// 로그인
	@Override
	public Member login(Member member) {
		
		System.out.println("서비스 bean 등록 및 DI 성공");
		System.out.println(member.getMemberPw());
		
		// 암호화된 비밀번호
		String encPw = encoder.encode(member.getMemberPw());
		
		System.out.println("암호호된 비밀번호 : " + encPw);
		
		// BCrypt 암호화 원리
		// - 평문에 추가적인 문자열을 임의로 붙여(salt)서 암호화를 진행
		// - 암호화된 결과가 계속 다르지만 이를 비교할 수 있는 별도 메소드를 같이 제공
		
		// String temp = "$2a$10$S5BSzi1gCP3J.fe1KR2WV.btSJe4f/fC38yG9qzuURdsxuCw0jBMa";
		// System.out.println( "결과는? : " + encoder.matches(member.getMemberPw(), temp) );
		
		// 로그인 DAO 호출
		Member loginMember = dao.login(member.getMemberId());
		
		System.out.println(loginMember);
		// 조회 성공 시 Member 객체, 실패 시 null
		
		// DB에 일치하는 아이디를 가진 회원이 있고, 입력받은 비밀번호와 암호화된 비밀번호가 같을 때 ==> 로그인 성공
		if(loginMember != null && encoder.matches(member.getMemberPw(), loginMember.getMemberPw())) {
			loginMember.setMemberPw(null);
		}else {
			loginMember = null;
		}
		
		
		return loginMember;
	}

	// 아이디 중복 체크
	@Override
	public int idDupCheck(String inputId) {
		return dao.idDupCheck(inputId);
	}

	// 이메일 중복 체크
	@Override
	public int emailDupCheck(String inputEmail) {
		return dao.emailDupCheck(inputEmail);
	}

	// 회원가입
	// rollbackFor 속성 : 어떤 예외 발생 시 롤백을 수행할 지 지정
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int signUp(Member member) {
		member.setMemberPw(encoder.encode(member.getMemberPw()));
		
		return dao.signUp(member);
		
		// 트랜잭션 처리 @Transcational
		
		/* 스프링에서 트랜잭션을 처리하는 방법
		 * 1. 코드 기반 처리 방법 (기존 commit, rollback)
		 * 2. 선언적 트랜잭션 처리 방법
		 * 	1) <tx:advice> XML 방식
		 * 	2) @Transcational 어노테이션 방식
		 * 		- 트랜잭션 매니저가 Bean으로 등록 되어있어야 함
		 * 		- @Transcational 어노테이션을 인식하기 위한 <tx:annotation-driven/> 태그가 존재해야 함.
		 * 
		 * @Transcational 어노테이션은 rollback을 위한 어노테이션
		 * 왜 커밋은 안하지? 커넥션 반환 시 아무런 트랜잭션 처리가 되어있지 않다면 자동 commit
		 * 
		 * @Transcational 어노테이션은 RuntimeException이 발생했을 때 Rollback을 수행함
		*/
	}

	// 회원 정보 수정
	// @Transactional // 트랜잭션 처리는 여러 DML 수행 시 사용한다.
	@Override
	public int updateMember(Member member) {
		return dao.updateMember(member);
	}

	// 비밀번호 수정
	@Override
	public int updatePw(Map<String, String> map) {
		
		String pw = dao.selectSavePw(map.get("memberNo") );
		
		int result = 0;
		if(encoder.matches(map.get("currentPw"), pw)) {
			
			map.put("newPw", encoder.encode(map.get("newPw")));
			result = dao.updatePw(map);
		}
		
		return result;
	}

	@Override
	public int secession(Map<String, String> map) {
		
		String pw = dao.selectSavePw( map.get("memberNo") );
		
		int result = 0;
		
		if(encoder.matches(map.get("currentPw"), pw)) {
			result = dao.secession( map.get("memberNo") );
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
