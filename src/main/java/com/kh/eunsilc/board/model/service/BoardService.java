package com.kh.eunsilc.board.model.service;

import java.util.List;

import com.kh.eunsilc.board.model.domain.Board;

public interface BoardService {
	public int listCount();

	public int insertBoard(Board b);
	
	public List<Board> selectList(int startPage, int limit);
	
	//public Board selectOne(String board_num);
	
	public Board selectBoard(int chk, String board_num);
	
	//public int addReadCount(String board_num);
	
	public List<Board> searchList(String keyword);
	
	public Board updateBoard(Board b);
	
	public int deleteBoard(String board_num);
	
}
