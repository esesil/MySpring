package com.kh.eunsilc.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.eunsilc.board.model.dao.BoardDao;
import com.kh.eunsilc.board.model.domain.Board;

@Service("bService")
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDao bDao;

	@Override
	public int listCount() {
		return bDao.listCount();
	}

	@Override
	public int insertBoard(Board b) {
		return bDao.insertBoard(b);
	}

	@Override
	public List<Board> selectList(int startPage, int limit) {
		return bDao.selectList(startPage, limit);
	}

	@Override
	 public Board selectBoard(int chk, String board_num) {
	 if (chk == 0) bDao.addReadCount(board_num);
	 return bDao.selectOne(board_num);
	 }
	
//	@Override
//	 public Board selectOne(String board_num) {
//	 return bDao.selectOne(board_num);
//	 }

//	@Override
//	public int addReadCount(String board_num) {
//		return bDao.addReadCount(board_num);
//	}

	@Override
	public List<Board> searchList(String keyword) {
		return bDao.searchList(keyword);
	}

	@Override
	public Board updateBoard(Board b) {
		int result = bDao.updateBoard(b);
		if (result > 0) {
			b = bDao.selectOne(b.getBoard_num());
		} else {
			b = null;
		}
		return b;
	}

	@Override
	public int deleteBoard(String board_num) {
		return bDao.deleteBoard(board_num);
	}

}
