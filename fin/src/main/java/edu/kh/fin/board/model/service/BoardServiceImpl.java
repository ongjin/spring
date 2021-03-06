package edu.kh.fin.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.fin.board.model.dao.BoardDAO;
import edu.kh.fin.board.model.exception.InsertBoardFailException;
import edu.kh.fin.board.model.exception.UpdateBoardFailException;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.BoardImage;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.board.model.vo.Search;
import edu.kh.fin.common.Util;

@Service // Service임을 알려줌 + Bean 등록
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDAO dao;

	// 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
	@Override
	public Pagination getPagination(int cp) {
		
		// 1. 전체 게시글 수 count
		int listCount = dao.getListCount();
		
		// 2. 페이징 처리에 필요한 값 계산 + 반환
		return new Pagination(listCount, cp);
	}

	@Override
	public List<Board> selectBoardList(Pagination pagination) {
		return dao.selectBoardList(pagination);
	}

	@Override
	public Board selectBoard(int boardNo, int memberNo) {
		Board board = dao.selectBoard(boardNo);
		
		if(board != null && board.getBoardNo() != memberNo) {
			int result = dao.increaseReadCount(boardNo);
		}
		
		return board;
				
	}

	// 카테고리 목록 조회
	@Override
	public List<Category> selectCategory() {
		return dao.selectCategory();
	}

	// 게시글 삽입 + 이미지 삽입
	@Transactional // 예외 발생 시 모두 롤백
	@Override
	public int insertBoard(Board board, List<MultipartFile> images, String webPath, String serverPath) {
		
		// 1) 제목, 내용에 XSS 처리 + 내용에 개행문자 변경 처리
		board.setBoardTitle(Util.XSS(board.getBoardTitle()));
		board.setBoardContent(Util.XSS(board.getBoardContent()));
		
		board.setBoardContent(Util.changeNewLine(board.getBoardContent()));
		
		// 2) board 부분 DB 삽입 DAO 수행 후 삽입된 행의 boardNo 얻어오기
		// -> Mybatis에서 제공하는 insert 후 PK 컬럼값을 얻어오는 useGeneratedKeys, <selectKey> 사용
		int boardNo = dao.insertBoard(board);
		
		// 3) 이미지 삽입
		if(boardNo > 0) { // 게시글 삽입 성공 시
			
			// 실제 업로드된 이미지를 분별하여 List<BoardImage> imgList에 담기
			List<BoardImage> imgList = new ArrayList<BoardImage>();
			
			for(int i = 0; i < images.size(); i++) {
				// i == images의 인덱스 == 업로드된 파일의 level
				
				// 각 인덱스 요소에 파일이 업로드 되었는지 검사
				if(!images.get(i).getOriginalFilename().equals("") ) {
					// 업로드가 된 경우 MultipartFile에서 DB저장에 필요한 데이터만을 추출하여
					// BoardImage 객체에 담은 후 imgList에 추가
					
					BoardImage img = new BoardImage();
					img.setImgPath(webPath); // 웹 접근 경로
					img.setImgOriginal(images.get(i).getOriginalFilename()); // 원본 파일명
					img.setImgName(Util.fileRename(img.getImgOriginal())); // 변경된 파일명
					img.setImgLevel(i); // 이미지 레벨
					img.setBoardNo(boardNo); // DAO 수행 결과로 반환 받은 boardNo
					
					imgList.add(img);
				}
			}
			
			// 4) imgList에 업로드된 이미지 정보가 있다면 DAO 호출
			if(!imgList.isEmpty()) {
				int result = dao.insertImgList(imgList);
				
				// 5) 삽입 성공한 행의 개수와 imgList 개수가 같을 경우 파일을 서버에 저장
				// 1순위로 확인 할 것! : servers -> fin server -> OverView 
				// -> serve modules without publishing 체크 -> 저장되는 파일 경로를 현재 프로젝트로 지정할 수 있음
				
				if(result == imgList.size()) { // 성공 ==> 파일 저장
					
					// images  : MultipartFile List, 실제 파일 자체 + 정보
					// imgList : BoardImageList, Db에 저장할 파일 정보
					for(int i = 0; i < result; i++) {
						
						// 업로드된 파일이 있는 images의 인덱스 요소를 얻어와 
						// 지정된 경로와 이름으로 파일로 변환하여 저장
						try {
							images.get(imgList.get(i).getImgLevel()).transferTo(new File(serverPath + "/" + imgList.get(i).getImgName() ));
						} catch (Exception e) {
							e.printStackTrace();
							
							// 파일 변환이 실패할 경우 사용자 정의 예외 발생
							throw new InsertBoardFailException("파일 변환 중 문제 발생");
						}
					}
					
				}else {
					// 업로드된 이미지 수와 삽입된 행의 수가 다를 경우 사용자 정의 예외 발생
				}
			}
		}
		return boardNo;
	}
	
	// 수정 화면 전환용 게시글 상세 조회
	@Override
	public Board selectBoard(int boardNo) {
		Board board = dao.selectBoard(boardNo);
		
		board.setBoardContent(Util.changeNewLine2(board.getBoardContent()));
		
		return board;
	}

	// 게시글 수정
	@Transactional
	@Override
	public int updateBoard(Board board, List<MultipartFile> images, String webPath, String serverPath,
			String deleteImages) {
		
		// 1) 게시글 제목/내용 XSS, 개행문자 처리
		board.setBoardTitle(Util.XSS(board.getBoardTitle()));
		board.setBoardContent(Util.XSS(board.getBoardContent()));
		board.setBoardContent(Util.changeNewLine(board.getBoardContent()));
		
		// 2) 게시글 부분 수정 진행
		int result = dao.updateBoard(board);
		
		// 3) 기존에 있었던 이미지 delete 처리
		if(result > 0) {
			// 마이바티스는 SQL 수행 시 파라미터를 1개만 받을 수 있다!
			// 전달할 파라미터가 다수인 경우 Map과 같은 컬렉션 객체를 이용하면 된다!
			
			if(!deleteImages.equals("")) { // 삭제할 이미지가 있을 경우
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("boardNo", board.getBoardNo());
				map.put("deleteImages", deleteImages);
				
				result = dao.deleteImages(map);
			}
		}
		
		// 4) images에 담겨있는 파일 정보 중 업로드된 파일 정보를 imgList에 옮겨 담기
		if(result > 0) {
			List<BoardImage> imgList = new ArrayList<>();
			
			for(int i = 0; i < images.size(); i++) {
				
				// 업로드된 파일이 있는 경우
				if(!images.get(i).getOriginalFilename().equals("")) {
					BoardImage img = new BoardImage();
					img.setImgPath(webPath);
					img.setImgName(Util.fileRename(images.get(i).getOriginalFilename()));
					img.setImgOriginal(images.get(i).getOriginalFilename());
					img.setImgLevel(i);
					img.setBoardNo(board.getBoardNo());
					
					imgList.add(img);
				}
			}
			
			// 5) imgList가 비어있지 않을경우 imgList에 있는 내용을 update 또는 insert
			if(!imgList.isEmpty()) {
				for(BoardImage img : imgList) {
					result = dao.updateBoardImage(img);
					
					
					// 결과 1 -> 기존에 저장된 이미지가 수정됨
					// 결과 0 -> 기존에 저장되지 않은 이미지가 추가됨 -> INSERT 수행
					if(result == 0) {
						result = dao.insertBoardImage(img);
						
						if(result == 0)	{
							// 사용자 정의 예외
							throw new UpdateBoardFailException("이미지 삽입 중 문제 발생");
						}
					}
				}
			} 
			
			// 6) 전달 받은 images 중 업로드된 파일이 있는 부분을 실제 파일 저장
			if(!imgList.isEmpty()) {
				try {
					for(int i = 0; i < imgList.size(); i++) {
						
						images.get(imgList.get(i).getImgLevel()).transferTo(new File(serverPath + "/" + imgList.get(i).getImgName()));
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					// 사용자 정의 예외 (RuntimeException)
					throw new UpdateBoardFailException();
				}
			}
			
		} // if end
		
		return result;
	}

	@Override
	public int deleteBoard(int boardNo) {
		return dao.deleteBoard(boardNo);
	}

	@Override
	public Pagination getPagination(int cp, Search search) {
		
		int searchListCount = dao.getSearchListCount(search);
		
		return new Pagination(searchListCount, cp);
	}

	// 검색 조건에 맞는 게시글 목록 조회
	@Override
	public List<Board> selectBoardList(Pagination pagination, Search search) {
		return dao.selectSearchBoard(pagination, search);
	}

	// 이미지 파일명 목록 조회
	@Override
	public List<String> selectImgList() {
		return dao.selectImgList();
	}
	
	
	
	

	
	


	
	

	

	
	
	
	
	
	

	
	
	
	
	
	
}
