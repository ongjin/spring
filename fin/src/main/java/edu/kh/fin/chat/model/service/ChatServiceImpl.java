package edu.kh.fin.chat.model.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.chat.model.dao.ChatDAO;
import edu.kh.fin.chat.model.vo.ChatMessage;
import edu.kh.fin.chat.model.vo.ChatRoom;
import edu.kh.fin.chat.model.vo.ChatRoomJoin;
import edu.kh.fin.common.Util;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private ChatDAO dao;

	// 채팅방 목록 조회
	@Override
	public List<ChatRoom> chatRoomList() {
		return dao.chatRoomList();
	}

	// 채팅방 열기
	@Override
	public int openChatRoom(ChatRoom room) {
		
		int result = dao.openChatRoom(room);
		
		// dao 호출 전에는 chatRoomNo가 없었으나 호출 후에는 담겨있는 상태
		
		if(result > 0) {
			return room.getChatRoomNo();
		}else {
			return 0;
		}
	}

	// 채팅방 입장 + 채팅 내역(메세지) 조회
	@Override
	public List<ChatMessage> joinChatRoom(ChatRoomJoin join) {
		
		// 1. 파라미터로 전달 받은 방 번호와 일치하는 방이 DB에 있는지 검사
		int result = dao.existsChatRoom(join.getChatRoomNo());
		
		// 2-1. 방이 있는 경우
		if(result > 0) {
			
			// 2-1-1. CHAT_ROOM_JOIN 테이블에 삽입 DAO 호출
			try {
				dao.joinChatRoom(join);
				// 재입장 시 유니크 제약조건 위배 예외가 발생
			} catch (Exception e) {}// -> catch문에 아무것도 적지 않고 처리
			
			// 2-1-2. 해당 방 번호 일치하는 모든 메세지를 CHAT_MESSAGE 테이블에서 조회
			return dao.selectChatMessage(join.getChatRoomNo());
			
		}else {// 2-2. 방이 없는 경우
			return null;
		}
		
		
	}

	// 채팅 내용 삽입
	@Transactional
	@Override
	public int insertMessage(ChatMessage cm) {
		
		int result = 0;
		
		if(cm.getMessage() == null) {
			
			result = dao.insertMessage(cm);
			
			if(result > 0) {
				result = dao.exitChatRoom(cm);
			}
			
		}else {
			
			cm.setMessage(Util.XSS(cm.getMessage()));
			cm.setMessage(Util.changeNewLine(cm.getMessage()));
			result = dao.insertMessage(cm);
		}
		
		return result;
	}
	
	
	
	
	
	
}
