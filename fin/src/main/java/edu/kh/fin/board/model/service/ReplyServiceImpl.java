package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.fin.board.model.dao.ReplyDAO;
import edu.kh.fin.board.model.vo.Reply;
import edu.kh.fin.common.Util;

@Service // 비즈니스 로직 처리하는 Service임을 알려줌 + Bean 등록
public class ReplyServiceImpl implements ReplyService{
	
	@Autowired
	private ReplyDAO dao;

	// 댓글 목록 조회
	@Override
	public List<Reply> selectList(int boardNo) {
		return dao.selectList(boardNo);
	}

	// 댓글 삽입
	@Override
	public int insertReply(Reply reply) {
		
		reply.setReplyContent(Util.XSS(reply.getReplyContent()));
		reply.setReplyContent(Util.changeNewLine(reply.getReplyContent()));
		
		return dao.insertReply(reply);
	}

	// 댓글 수정
	@Override
	public int updateReply(Reply reply) {
		
		reply.setReplyContent(Util.XSS(reply.getReplyContent()));
		reply.setReplyContent(Util.changeNewLine(reply.getReplyContent()));
		
		return dao.updateReply(reply);
	}

	// 댓글 삭제
	@Override
	public int deleteReply(int replyNo) {
		return dao.deleteReply(replyNo);
	}
	
	
	
	
	
	
}
