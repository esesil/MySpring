package com.kh.eunsilc.board.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.eunsilc.board.model.domain.Board;
import com.kh.eunsilc.board.model.service.BoardService;

@Controller
public class BoardController {
	@Autowired
	private BoardService bService;

	public static final int LIMIT = 5;

	// 게시글 작성 페이지
	@RequestMapping(value = "/writerForm.do", method = RequestMethod.GET)
	public String boardInsertForm(ModelAndView mv) {
		// public string, ModelAndView 둘 다 들어올 수 있는데 모델은 데이터를 가져오는게 있을때 씀.
		// 얘는 form이니까 뭐 가져가는게 있을리 없으니 그냥 string으로
		return "board/writeForm"; // jsp파일. View페이지에서 작성 후 form action="bInsert.do"로 들어오도록 함.
	}

	// 작성된 글을 insert
	@RequestMapping(value = "/bInsert.do", method = RequestMethod.POST)
	public ModelAndView boardInsert(Board b, @RequestParam(name = "upfile") MultipartFile report,
			HttpServletRequest request, ModelAndView mv) {

		// 첨부파일 저장
		if (report != null && !report.equals("")) {
			saveFile(report, request);
		}

		b.setBoard_file(report.getOriginalFilename());
		// 저장된 파일명을 vo에 set

		bService.insertBoard(b);
		mv.setViewName("redirect:bList.do");
		// insertBoard에 성공했다면 !!! View페이지로 이동하는 것이 아니라 컨트롤러 url 중 "게시글 리스트 select로 이동"하는
		// "bList.do" . redirect:라는 접두어 붙여주면 뷰가 아니라 컨트롤러 url 찾아 들어가게됨.
		return mv;
		// 실패했다면
		// mv.setViewName(""); //errorPage로 이동
	}

	// 게시글 리스트 select
	@RequestMapping(value = "/bList.do", method = RequestMethod.GET)
	public ModelAndView boardListService(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, ModelAndView mv) {
		try {
			int currentPage = page;
			// 한 페이지당 출력할 목록 갯수
			int listCount = bService.listCount();
			int maxPage = (int) ((double) listCount / LIMIT + 0.9);

			if (keyword != null && !keyword.equals(""))
				mv.addObject("list", bService.searchList(keyword));
			else
				mv.addObject("list", bService.selectList(currentPage, LIMIT));
			mv.addObject("currentPage", currentPage);
			mv.addObject("maxPage", maxPage);
			mv.addObject("listCount", listCount);
			mv.setViewName("board/blist"); // board/blist View페이지가 보여짐
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("errorPage");
		}
		return mv;
	}

	@RequestMapping(value = "bDetail.do", method = RequestMethod.GET)
	public ModelAndView boardDetail(@RequestParam(name = "board_num") String board_num,
			@RequestParam(name = "page", defaultValue = "1") int page, ModelAndView mv) {
		try {
			int currentPage = page;
			// 한 페이지당 출력할 목록 갯수
			mv.addObject("board", bService.selectBoard(0, board_num));
			// mv.addObject("commentList", brService.selectList(board_num));
			mv.addObject("currentPage", currentPage);
			mv.setViewName("board/boardDetail");
		} catch (Exception e) {
			mv.addObject("msg", e.getMessage());
			mv.setViewName("errorPage");
		}
		return mv;
	}

	// 파일
	private void saveFile(MultipartFile report, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "\\uploadFiles";
		String filePath = null;
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdir(); // 폴더가 없다면 생성한다.
		}
		try {
			// 파일 저장
			System.out.println(report.getOriginalFilename() + "을 저장합니다.");
			System.out.println("저장 경로 : " + savePath);
			filePath = folder + "\\" + report.getOriginalFilename();

			report.transferTo(new File(filePath)); // 파일을 저장한다
			System.out.println("파일 명 : " + report.getOriginalFilename());
			System.out.println("파일 경로 : " + filePath);
			System.out.println("파일 전송이 완료되었습니다.");
		} catch (Exception e) {
			System.out.println("파일 전송 에러 : " + e.getMessage());
		}
	}
}