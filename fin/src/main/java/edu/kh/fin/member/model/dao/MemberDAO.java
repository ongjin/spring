package edu.kh.fin.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.member.model.vo.Member;

@Repository // Persistance layer, 영속성을 가지는 속성(파일, DB)을 제어하는 클래스 + Bean 등록
public class MemberDAO {
	
	@Autowired // bean으로 등록된 SqlSessionTemplate 객체 의존성 주입(DI)
	private SqlSessionTemplate sqlSession; // Mybatis를 이용한 DB연결, SQL 수행
	
	/** 로그인
	 * @param memberId
	 * @return loginMember
	 */
	public Member login(String memberId) {
		
		// sqlSession.selectOne("매퍼명.태그id", 전달할 파라미터)
		// -> 특정 매퍼에 있는 id가 일치하고 1행만 반환하는 select 문을 수행
		// + select문에서 파라미터 사용
		Member loginMember = sqlSession.selectOne("memberMapper.login", memberId);
		
		return loginMember;
	}
}