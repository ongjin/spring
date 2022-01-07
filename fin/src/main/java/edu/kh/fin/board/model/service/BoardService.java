package edu.kh.fin.board.model.service;

import java.util.List;

import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Pagination;

// Service 인터페이스 왜 사용할까?
// 1. 규칙성
// 2. 유지보수
// 3. 결합도 다운
// 4. AOP 사용

public interface BoardService {

	/** 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
	 * @param cp
	 * @return pagination
	 */
	Pagination getPagination(int cp);

	/** 지정된 범위의 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination);

	/** 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	Board selectBoard(int boardNo);

}
