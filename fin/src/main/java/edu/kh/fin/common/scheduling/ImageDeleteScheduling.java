package edu.kh.fin.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.fin.board.model.service.BoardService;

@Component // Bean 등록
public class ImageDeleteScheduling {
	
	/*
	 * * cron 속성 : UNIX계열 잡 스케쥴러 표현식으로 작성 - cron="초 분 시 일 월 요일 [년도]" - 요일 : 1(SUN) ~ 7(SAT) 
	 * ex) 2019년 9월 16일 월요일 10시 30분 20초 cron="20 30 10 16 9 2" // 연도 생략 가능
	 * 
	 * - 특수문자 * : 모든 수. 
	 * - : 두 수 사이의 값. ex) 10-15 -> 10이상 15이하 
	 * , : 특정 값 지정. ex) 3,4,7 -> 3,4,7 지정 
	 * / : 값의 증가. ex) 0/5 -> 0부터 시작하여 5마다 
	 * ? : 특별한 값이 없음. (월, 요일만 해당) 
	 * L : 마지막. (월, 요일만 해당)
	 */
	
	@Autowired
	private ServletContext servletContext; // 서블릿 컨테이너
	
	@Autowired
	private BoardService boardService;
	
	//         cron = "초 분 시 일 월 요일 [년도]"
	@Scheduled(cron = "0 * * * * *") // 모든 년월일시분,요일 0초 == 매 분 0초마다
//	@Scheduled(cron = "0 0 * * * *") // 모든 년월일시분,요일 0분 0초 == 매 시간 0분 0초마다 == 정시 마다
//	@Scheduled(cron = "0 0 0 * * *") // 모든 년월일시분,요일 0시 0분 0초 == 매 시간 0시 0분 0초마다 == 자정 마다
	public void imageDelete() { 
		
		String serverPath = servletContext.getRealPath("/resources/images/board");
		// -> 이미지가 저장되어 있는 실제 경로
		
		// 지정된 경로에 있는 모든 파일 리스트를 File 배열로 반환
		// File 객체 : 파일을 참조할 수 있는 객체
		File[] imgArr = new File(serverPath).listFiles();
		
		// 배열을 리스트로 변환
		List<File> serverImgList = Arrays.asList(imgArr);
		
		// 이미지가 저장된 폴더에 있는 파일 목록을 잘 가져왔는지 확인
//		for(File img : serverImgList){
//			System.out.println(img);
//		}
		
		// ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ----
		// DB에서 파일명 목록 조회
		List<String> dbImgList = boardService.selectImgList();
		
		// serverImgList : 서버에 저장된 파일 목록
		// dbImgList : DB에 저장된 파일명 목록
		if(!serverImgList.isEmpty() && !dbImgList.isEmpty()) {
			// 서버에 저장된 파일 목록을 순차 접근하여
			for(File img : serverImgList) {
				// 접근한 파일의 파일명만 추출한 후 
				String serverImgName = img.toString().substring(serverPath.length() + 1);
				// String serverImgName = img.toString().substring(img.toString().lastIndexOf("\\") + 1);
				
				// DB 파일명 목록에 서버 파일명과 같은 이름이 없다면 --> 해당 파일 삭제
				if(dbImgList.indexOf(serverImgName) == -1) {
					System.out.println(serverImgName + " 삭제");
					img.delete();
				}
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
