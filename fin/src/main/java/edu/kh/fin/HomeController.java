package edu.kh.fin;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.kh.fin.member.model.vo.Member;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	// Logger : 로그를 작성할 수 있게하는 객체
	// LoggerFactory : Logger객체를 만드는 객체
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	// 최상위 주소 ("/" == "/fin/") 요청 시 이를 제어하는 컨트롤러
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String home(Locale locale, Model model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addAttribute("serverTime", formattedDate );
//		
//		return "home";
//	}
	
	// GET 방식 "/" 요청이 전달되었을 때 처리하는 메소드
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String mainForward() {
		
		// 로그 남기기
//		logger.debug("메인 페이지 접속");
//		logger.info("메인 페이지 접속");
//		logger.warn("메인 페이지 접속");
//		logger.error("메인 페이지 접속");
		
		return "common/main";
	}
	
	
	// POST 방식의 /loginTest 요청을 받아서 처리하는 컨트롤러
	@RequestMapping(value = "/loginTest", method = RequestMethod.POST)
	public String loginTest(Member member) {
		System.out.println(member);
		return null;
	}
	
	
}
