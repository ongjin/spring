package edu.kh.fin.member.controller;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.common.Util;
import edu.kh.fin.member.model.service.MemberService;
import edu.kh.fin.member.model.vo.Member;

// Controller : 요청에 따라 알맞은 서비스를 호출하고 결과에 따라 알맞은 응답을 제어하는 역할

// @RequestMapping : 요청 주소, 전달 방식에 따라 연결되는 클래스 또는 메소드를 지정하는 어노테이션
//					-> 클래스 + 메소드에 동시 작성하여 하나의 주소로 해석할 수 있음.

/* @RequestMapping 규칭
 * 1. () 안에 작성되는 매개변수가 1개인 경우 
 * 	--> 매핑할 요청 주소로 해석(== value)
 * 
 * 2. () 안에 작성되는 매개변수가 2개 이상인 경우 
 * 	--> 각 값을 해석하기 위해 value = , method = 과 같은 key를 작성한다.
 * 
 * 3. method 키 미작성 시 get/post 관계없이 모두 처리한다.
*/


@Controller // 프레젠테이션 레이어, 웹앱에 전달된 요청과 응답을 처리하는 역할임을 명시 + Bean 등록

@RequestMapping("/member/*") // /fin/member/ 로 시작하는 모든 요청을 받아서 처리하는 컨트롤러

@SessionAttributes({"loginMember"}) // Model 속성 중 loginMember가 request -> session 범위 변경
public class MemberController {
	
	// @Autowired
	// - component-scan(servlet-context.xml)을 통해
	//	Bean으로 등록된 객체 중 타입이 같거나 상속 관계인 객체를 찾아
	// 자동으로 의존성주입(DI)을 하는 어노테이션
	@Autowired
	private MemberService service;
	
	
	
	// 1. HttpServletRequest를 이용한 파리마트 전달 받기
	// @RequestMapping("login") 
	// @RequestMapping(value = "login", method = RequestMethod.POST) // /fin/member/login 요청을 처리하는 메소드
	public String login(HttpServletRequest req) {
		// -> 컨트롤러 메소드에 원하는 객체의 타입을 매개변수로 작성하면 
		// 요청, 응답 관련 객체를 얻어오거나 새로운 객체를 생성해서 의존성 주입(DI) 해줌
		
		System.out.println(req.getParameter("memberId"));
		System.out.println(req.getParameter("memberPw"));
		
		// 로그인은 JSP를 이용한 응답 페이지를 만드는 forward 보다 
		// 이미 만들어진 요청-응답 처리 메소드를 재요청하는 redirect를 사용하는 것이 맞다
		
		return "redirect:/"; // 최상위주소(/fin/)로 재요청
	}
	
	
	
	// (주위) 요청 매핑 주소 중복되지 않게 주의하자!
	
	// 2. @RequestParam 어노테이션을 이용한 파라미터 전달 받기
	// - 메소드 매개변수 앞에 작성
	// --> @RequestParam 뒤쪽에 작성된 매개변수에 파라미터가 저장됨
	
	// @RequestParam 속성
	// value = 전달 받을 input 태그의 name 속성 값 (매개변수 1개일 때 기본 값)
	// requierd = 파라미터가 필수로 전달되어야 하는 지 여부(기본값 : true == 필수)
	// -> requierd=true 일 때 파라미터가 없으면 : 400 Bad Request(잘못된 요청) 발생
	
	// defaultValue : 전달 받은 파라미터 값이 없을 때 지정할 기본값
	// @RequestMapping(value = "login", method = RequestMethod.POST) // /fin/member/login 요청을 처리하는 메소드
	public String login2(	@RequestParam("memberId") String id, @RequestParam("memberPw") String pw, 
							@RequestParam(value="test", required = false, defaultValue = "기본값") String t) {
		
		System.out.println(id);
		System.out.println(pw);
		System.out.println(t);
		
		return "redirect:/";
	}
	
	
	
	
	// 3. @RequestParam 생략
	// - input 태그의 name 속성 값과 파라미터를 지정할 매개 변수명이 같으면 생략 가능
	
	// @RequestMapping(value = "login", method = RequestMethod.POST) 
	public String login3(String memberId, String memberPw) {
		System.out.println(memberId);
		System.out.println(memberPw);
		return "redirect:/";
	}
	
	
	// 4. @ModelAttribute
	// - 요청 시 전달 받은 피라미터를 객체 형태로 매핑하는 역할을 하는 어노테이션
	//  -> 객체로 매핑하기 위한 조건
	// 1) input 태그 name 속성 값과 객체의 멤버변수(필드) 명이 같아야 함
	// 2) 객체로 만들어질 클래스에 기본 생성자가 있어야 함.
	//	-> 스프링이 객체를 자동 생성할 때 사용
	// 3) setter가 작성되어 있어야 함.
	
	
	// - 메소드/매개 변수 레벨로 사용 가능
	
	// @ModelAttribute를 이용해 파라미터가 세팅된 객체 == 커맨드 객체
	
	// @RequestMapping(value = "login", method = RequestMethod.POST) 
	public String login4(@ModelAttribute Member member ) {
		System.out.println(member.getMemberId());
		System.out.println(member.getMemberPw());
		
		return "redirect:/";
	}
	
	
	
	// 5. @ModelAttribute 생략
	
	@RequestMapping(value = "login", method = RequestMethod.POST) 
	public String login5( Member member, Model model, RedirectAttributes ra, 
			@RequestParam(value="save", required = false) String save, HttpServletRequest req,
			HttpServletResponse resp) {
		
		System.out.println(member.getMemberId());
		System.out.println(member.getMemberPw());
		
		Member loginMember = service.login(member);
		
		// Service 수행 결과에 따라 응답 처리
		if(loginMember != null) {
			
			// loginMember Session 추가하기
			
			// Model : 데이터를 K:V 형태로 담아서 전달하는 용도의 객체
			// -> page, request, session, application고 사용 방법이 유사함.
			// --> Controller 위쪽에 @SessionAttributes 어노테이션을 작성하면
			// key가 일치하는 model의 속성을 session 범위로 이동 시킴
			model.addAttribute("loginMember", loginMember);
			
			//******************************************************************************
			// 아이디 저장 Cookie 생성
			
			// 1. Cookie 객체 생성(K:V 형태로 저장할 값 지정)
			Cookie cookie = new Cookie("saveId", loginMember.getMemberId());
			
			// 2. 쿠키 유지 시간 지정
			if(save != null) {
				cookie.setMaxAge(60 * 60 * 24 * 30); // 초 단위
			}else {
				cookie.setMaxAge(0); // 0초
				// -> 쿠키가 생성 되자마자 사라짐 --> 기존 쿠키를 새로 생성된 쿠키가 덮어 씌우고 사라지게 됨 == 삭제
			}
			
			// 3. 쿠키 적용 경로(범위)
			// 최상위 경로 지정 --> 어디서든 쿠키 사용 가능
			cookie.setPath(req.getContextPath());
			
			// 4. 응답 객체에 생성한 쿠키를 추가하여 클라이언트로 전달
			resp.addCookie(cookie);
			
			
			
			
			//******************************************************************************
			
		}else {
			
			// RedirectAttributes : 리다이렉트 시 request 범위로 값을 전달할 수 있는 객체
			
			// RedirectAttributes에 값 세팅 : request scope
			// 리다이렉트 시 : request -> session으로 잠시 이동
			// 응답 완료 시 : session -> request 복귀
			
			ra.addFlashAttribute("message", "아이디 또는 비밀번호를 확인해 주세요");
			
		}
		
		return "redirect:/";
	}
	
	
	
	// 로그아웃 : 세션에 있는 회원 정보를 제거 -> 세션 자체를 없애는 것이 효율적
	@RequestMapping("logout")
	public String logout(SessionStatus status) {
		
		// session.invalidate();
		// -> req.getSession()을 통해서 얻어온 Session만 만료됨
		// --> @SessionAttributes + model.addAttribute() 작성된 세션은 별도 만료 방법이 존재함.
		
		// SessionStatus : 세션 상태 관리 객체
		// -> @SessionAttributes를 통해 등록된 session을 관리할 수 있음.
		
		status.setComplete(); // 세션 만료
		
		
		return "redirect:/";
	}
	
	
	// 회원가입 페이지 전환
	@RequestMapping(value = "signUp", method = RequestMethod.GET)
	public String signUp() {
		
		return "member/signUp";
	}
	
	
	
	/* @ResponseBody
	 * - 메소드에서 반환되는 값이 forward를 위한 jsp 이름, redirect 주소가 아닌
	 * 값 자체임을 알려주는 어노테이션 --> ajax를 이용한 데이터 응답 시 사용
	*/
	// 아이디 중복 검사(ajax)
	@RequestMapping(value = "idDupCheck", method = RequestMethod.GET)
	@ResponseBody
	public int idDUpCheck(String inputId) {
		
		// @RequestPAram 생략을 원하는 경우 피라미터가 null이 넘어오는지 확인
		// -> 안넘어오면 생략 가능
		
		// 아이디 중복 검사 Service 호출 후 결과 반환 받기
		// int result = service.idDupCheck(inputId);
		
		
		return service.idDupCheck(inputId);
	}
	
	
	@RequestMapping(value= "emailDupCheck", method=RequestMethod.GET)
	@ResponseBody
	public int emailDupCheck(String inputEmail) {
		return service.emailDupCheck(inputEmail);
	}
	
	
	// 회원가입
	@RequestMapping(value = "signUp", method = RequestMethod.POST)
	public String signUp(Member member, RedirectAttributes ra) {
						// -> 커맨드객체   -> RedirectAttributes : redirect 시 데이터 전달용 객체
		int result = service.signUp(member);
		
		// 메세지 전달용 변수 선언
		String title;
		String text;
		String icon;
		String button;
		
		if(result > 0) {
			title = "회원 가입 성공";
			text = member.getMemberName() + "님의 회원 가입을 환영합니다";
			icon = "success";
			button = "확인";
		}else {
			title = "회원 가입 실패";
			text = "관리자에게 문의해주세요.";
			icon = "error";
			button = "실패";
		}
		
		ra.addFlashAttribute("title", title);
		ra.addFlashAttribute("text", text);
		ra.addFlashAttribute("icon", icon);
		ra.addFlashAttribute("button", button);
		
		return "redirect:/";
	}
	
	
	
	
	/* Spring 예외 처리 방법
	 * 											| 우선 순위
	 * 1. 메소드별 try-catch / throws 예외처리		(1순위)
	 * 2. 컨트롤러 별로 예외 처리(@ExceptionHandler)	(2순위)
	 * 		-> DispatcherServlet(servlet-context.xml)에 <annotaion-driven/>이 수행되어야 사용 가능
	 * 3. 전역(모든 클래스)에서 발생하는 예외를 하나의 클래스에서 처리
	 * 		@ControllerAdvice 					(3순위)
	*/
	
	
	// @ExceptionHandler(처리할 예외.class)
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		
		// Model : 데이터 전달용 객체(Map 형식, 기본 값 : request)
		model.addAttribute("errorMessage", "회원관련 서비스 이용중 문제가 발생했습니다.");
		model.addAttribute("e", e);
		
		return "common/error";
	}
	
	@RequestMapping(value="myPage", method= RequestMethod.GET)
	public String myPage() {
		
		return "member/myPage";
	}
	
	// 회원 정보 수정
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@ModelAttribute("loginMember") Member loginMember,
			@RequestParam Map<String, String> param, // 모든 파라미터를 Map형식으로 저장
			Member member, // 비어있는 Member 객체 생성
			RedirectAttributes ra
			) {
		
		// 회원 정보 수정 시 필요한 값
		// 1. 입력된 파라미터(이메일, 전화번호, 주소)
		// 2. 로그인한 회원의 회원 번호(Session에서 얻어옴)
		
		// @ModelAttribute 이용 방식
		// 1. 피라미터를 객체에 set하는 역할 -> 커맨드 객체 생성
		// 2. @SessionAttributes를 이용해 등록된 Session 데이터를 얻어오는 역할
		// 	-> @ModelAttribute("Session키값")
		
		// member에 회원번호, 수정된 피라미터를 모두 담기
		member.setMemberNo(loginMember.getMemberNo());
		member.setMemberEmail(param.get("updateEmail"));
		member.setMemberPhone(param.get("updatePhone"));
		member.setMemberAddress(param.get("updateAddress"));
		
		// 회원 정보 수정 Service 호출 후 결과 반환 받기
		int result = service.updateMember(member);
		
		String title = null;
		String text = null;
		String icon = null;
		String button = null;
		
		if(result > 0) {
			
			// Session 로그인 회원 정보를 DB와 동기화
			// -> Session에 저장된 회원 정보 객체(Member)를 참조하는 loginMember 활용
			loginMember.setMemberEmail(param.get("updateEmail"));
			loginMember.setMemberPhone(param.get("updatePhone"));
			loginMember.setMemberAddress(param.get("updateAddress"));
			
			
			title = "회원 정보 수정 성공";
			text = loginMember.getMemberName() + "님의 회원 정보가 수정되었습니다.";
			icon = "success";
			button = "확인";
		}else {
			title = "회원 정보 수정 실패";
			text = "관리자에게 문의해주세요.";
			icon = "error";
			button = "실패";
		}
		ra.addFlashAttribute("title", title);
		ra.addFlashAttribute("text", text);
		ra.addFlashAttribute("icon", icon);
		ra.addFlashAttribute("button", button);
		
		return "redirect:myPage/";
	}
	
	
	// 비밀번호 수정 화면 전환
	@RequestMapping(value = "updatePw", method = RequestMethod.GET)
	public String updatePw() {
		return "member/updatePw";
	}
	
	// 비밀번호 수정
	@RequestMapping(value = "updatePw", method = RequestMethod.POST)
	public String updatePw(@ModelAttribute("loginMember") Member loginMember,
			String currentPw, String newPw1, RedirectAttributes ra) {
		
		// 비밀번호 수정 흐름
		// (Controller)
		// 1. 회원번호 + 현재 비밀번호 + 새 비밀번호 서비스 호출
		
		
		// (Service)
		// 2. 회원 번호를 이용해서 DB에 저장된 비밀번호를 조회
		// 3. DB 저장된 비밀번호와 입력된 현재 비밀번호 비교(matches() 사용)
		
		// 4. 일치하면 새 비밀번호를 암호화 -> 비밀번호 변경 DAO 호출
		// 5. 일치하지 않으면 Controller로 0 반환
		
		// (Controller)
		// 6. 성공 여부에 따라 출력 메세지 지정 -> 리다이렉트
		
		// 회원번호 + 파라미터 저장용 Map 생성
		Map<String, String> map = new HashMap<String, String>();
		map.put("memberNo", loginMember.getMemberNo() + "");
		map.put("currentPw", currentPw);
		map.put("newPw", newPw1);
		
		int result = service.updatePw(map);
		
		if(result > 0) {
			Util.swalSetMessage("비밀번호 변경 성공", loginMember.getMemberName() + "님의 비밀번호가 수정되었습니다.", "success", "확인", ra);
		}else {
			Util.swalSetMessage("비밀번호 변경 실패", "비밀번호가 일치하지 않습니다.", "error", "확인", ra);
		}
		
		return "redirect:myPage";
	}
	
	@RequestMapping(value = "secession", method = RequestMethod.GET)
	public String secession() {
		return "member/secession";
	}
	
	@RequestMapping(value = "secession", method = RequestMethod.POST)
	public String secession(@ModelAttribute("loginMember") Member loginMember, String currentPw,
			RedirectAttributes ra, SessionStatus status) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("currentPw", currentPw);
		map.put("memberNo", loginMember.getMemberNo() + "");
		
		int result = service.secession(map);
		
		String temp = "redirect:/";
		
		if(result > 0) {
			Util.swalSetMessage("회원탈퇴 성공", "", "success", "확인", ra);
			status.setComplete();
		}else {
			Util.swalSetMessage("회원탈퇴 실패", "비밀번호가 일치하지 않습니다.", "error", "확인", ra);
			temp = "redirect:secession";
		}
		
		return temp;
	}
	
	
	
	
	
	
	
	
	
}
