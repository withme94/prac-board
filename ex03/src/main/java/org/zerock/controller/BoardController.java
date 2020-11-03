package org.zerock.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	private BoardService service;
	
	/*
	@GetMapping("/list")//목록은 get방식만
	public void list(Model model) {
		log.info("list");
		model.addAttribute("list",service.getList());
	}
	페이징 처리전 겟리스트 페이징 처리후 오버라이딩 매핑 밑에 추가
	*/
	

	@GetMapping("/list")//목록은 get방식만
	public void list(Criteria cri ,Model model) {
		log.info("list" + cri);
		model.addAttribute("list",service.getList(cri));
		//model.addAttribute("pageMaker", new PageDTO(cri, 123)); ->테스트용 가데이터
		//전체 데이터 개수
		int total = service.getTotal(cri);
		log.info("total: "+ total);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		
		
		
	}
	
	@GetMapping("/register")//리스트에서 글쓰기 버튼을 클릭한 경우 매핑
	@PreAuthorize("isAuthenticated()") //인증된 사용자만 이용할 수 있도록 처리
	public void register(){
	
	
		
	}

	@PostMapping("/register")//DB에 자료 매핑
	@PreAuthorize("isAuthenticated()") //인증된 사용자만 이용할 수 있도록 처리
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register"+ board);
		
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		
		//첨부파일 이전
		service.register(board);
		rttr.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list";
	}
	
	@GetMapping({"/get", "/modify"})//화면상 수정
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri")Criteria cri, Model model) {
		
		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
		
	}
	
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")//DB수정
	public String modify(BoardVO board, Criteria cri,RedirectAttributes rttr) {
		log.info("modify" + board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		/*
		[Criteria에 UriComponentsBuilder 처리전
		//수정 삭제 처리후 목록 이동
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		//검색 조건 처리후 추가!!
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		]
		return "redirect:/board/list";
		*/
		return "redirect:/board/list" + cri.getListLink();
	}
	
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")//삭제는 post방식만
	public String remove(@RequestParam("bno") Long bno,Criteria cri, RedirectAttributes rttr, String writer) {
		log.info("remove,,,,,," + bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if(service.remove(bno)) {
			
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result", "success");
		}
		/*
		[Criteria에 UriComponentsBuilder 처리전
		//수정 삭제 처리후 목록 이동
				rttr.addAttribute("pageNum", cri.getPageNum());
				rttr.addAttribute("amount", cri.getAmount());
				//검색 조건 처리후 추가!!
				rttr.addAttribute("type", cri.getType());
				rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/list";
		
		*/
		return "redirect:/board/list" + cri.getListLink();
	}
	
	//파일 첨부
	@GetMapping(value ="/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
		log.info("getAttachList" + bno);
	
			return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK); 
	}

	
	//delete 파일
	
private void deleteFiles(List<BoardAttachVO> attachList) {
	
	if(attachList == null || attachList.size()==0) {
		
		return;
	}
	
	log.info("delete attach file.....................");
	log.info(attachList);

	attachList.forEach(attach -> {
		try {
			Path file = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
			Files.deleteIfExists(file);
			
			if(Files.probeContentType(file).startsWith("image")) {
				Path thumbNail = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
				
			Files.delete(thumbNail);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("delete 파일 에러!!" +e.getMessage());
		}
		
	});
}

	
}
