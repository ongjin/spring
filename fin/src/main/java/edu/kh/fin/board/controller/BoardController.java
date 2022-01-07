package edu.kh.fin.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.board.model.service.BoardService;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.common.Util;
import edu.kh.fin.member.model.vo.Member;

@Controller // 슉.슈슉.슉 컨트롤러임을 알려줌 + Bean 등록
@RequestMapping(value = "/board/*") // /board 로 시작하는 모오~~든 요청을 받는 프론트 컨트롤러
@SessionAttributes({"loginMember"}) 
// 1. Model 속성 추가 시 key값이 일치하는 값을 Session 영역으로 이동
// 2. Session에서 key가 일치하는 값을 얻어와 해당 컨트롤러 내에서 사용 가능하게 함
//	-> @ModelAttribute("loginMember")를 작성하여 얻어다 쓸 수 있다.
public class BoardController {
	
	
	@Autowired // Bean으로 등록된 객체 중 같은 타입 또는 상속 관계 객체를 자동으로 DI
	private BoardService service;
	
	// 게시글 목록 조회
	// -> 현재 페이지를 나타내는 파라미터 cp 전달 받기
	@RequestMapping("list")
	public String selectBoardList(@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
												// 파라미터중에     cp라는애가 있으면 전달받고 만약에 없으면 1을 받음
			Model model
			) {
		
		// 1. 페이징 처리용 객체 pagination 생성하기
		// 	-> 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
		Pagination pagination = service.getPagination(cp);
		System.out.println(pagination);
		
		// 2. 지정된 범위의 게시글 목록 조회
		List<Board> boardList = service.selectBoardList(pagination);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("boardList", boardList);
		
		return "board/boardList";
	}
	
	/* @PathVariable : URL 경로상에 있는 값을 파라미터로 사용할 수 있게하는 어노테이션
	 * 
	 * PathVariable 언제 사용할까?
	 * - 자원을 식별하는 용도 값(파라미터)
	 * 
	 * QueryString 언제 사용할까?
	 * - 필터링(검색, 정렬, 현재 페이지)
	*/
	// 게시글 상세 조회
	//@RequestMapping(value = "view/{boardNo}")
	@GetMapping("view/{boardNo}")
	public String selectBoard(@PathVariable("boardNo") int boardNo, Model model, RedirectAttributes ra,
			@RequestParam(value="cp", required = false, defaultValue = "1") int cp) {
		
		Board board = service.selectBoard(boardNo);
		
		String path = null;
		
		if(board != null) { // 조회 성공 시
			
			model.addAttribute("board", board);
			path = "board/boardView";
			
		}else { // 조회 실패 시
			
			Util.swalSetMessage("해당 글이 존재하지 않습니다.", null, "info", "확인", ra);
			path = "redirect:../list";
		}
		
		return path;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
