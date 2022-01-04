package edu.kh.fin.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	
}
