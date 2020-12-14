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

	// �Խñ� �ۼ� ������
	@RequestMapping(value = "/writerForm.do", method = RequestMethod.GET)
	public String boardInsertForm(ModelAndView mv) {
		// public string, ModelAndView �� �� ���� �� �ִµ� ���� �����͸� �������°� ������ ��.
		// ��� form�̴ϱ� �� �������°� ������ ������ �׳� string����
		return "board/writeForm"; // jsp����. View���������� �ۼ� �� form action="bInsert.do"�� �������� ��.
	}

	// �ۼ��� ���� insert
	@RequestMapping(value = "/bInsert.do", method = RequestMethod.POST)
	public ModelAndView boardInsert(Board b, @RequestParam(name = "upfile") MultipartFile report,
			HttpServletRequest request, ModelAndView mv) {

		// ÷������ ����
		if (report != null && !report.equals("")) {
			saveFile(report, request);
		}

		b.setBoard_file(report.getOriginalFilename());
		// ����� ���ϸ��� vo�� set

		bService.insertBoard(b);
		mv.setViewName("redirect:bList.do");
		// insertBoard�� �����ߴٸ� !!! View�������� �̵��ϴ� ���� �ƴ϶� ��Ʈ�ѷ� url �� "�Խñ� ����Ʈ select�� �̵�"�ϴ�
		// "bList.do" . redirect:��� ���ξ� �ٿ��ָ� �䰡 �ƴ϶� ��Ʈ�ѷ� url ã�� ���Ե�.
		return mv;
		// �����ߴٸ�
		// mv.setViewName(""); //errorPage�� �̵�
	}

	// �Խñ� ����Ʈ select
	@RequestMapping(value = "/bList.do", method = RequestMethod.GET)
	public ModelAndView boardListService(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "keyword", required = false) String keyword, ModelAndView mv) {
		try {
			int currentPage = page;
			// �� �������� ����� ��� ����
			int listCount = bService.listCount();
			int maxPage = (int) ((double) listCount / LIMIT + 0.9);

			if (keyword != null && !keyword.equals(""))
				mv.addObject("list", bService.searchList(keyword));
			else
				mv.addObject("list", bService.selectList(currentPage, LIMIT));
			mv.addObject("currentPage", currentPage);
			mv.addObject("maxPage", maxPage);
			mv.addObject("listCount", listCount);
			mv.setViewName("board/blist"); // board/blist View�������� ������
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
			// �� �������� ����� ��� ����
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

	// ����
	private void saveFile(MultipartFile report, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "\\uploadFiles";
		String filePath = null;
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdir(); // ������ ���ٸ� �����Ѵ�.
		}
		try {
			// ���� ����
			System.out.println(report.getOriginalFilename() + "�� �����մϴ�.");
			System.out.println("���� ��� : " + savePath);
			filePath = folder + "\\" + report.getOriginalFilename();

			report.transferTo(new File(filePath)); // ������ �����Ѵ�
			System.out.println("���� �� : " + report.getOriginalFilename());
			System.out.println("���� ��� : " + filePath);
			System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
		} catch (Exception e) {
			System.out.println("���� ���� ���� : " + e.getMessage());
		}
	}
}