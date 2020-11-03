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
	@GetMapping("/list")//����� get��ĸ�
	public void list(Model model) {
		log.info("list");
		model.addAttribute("list",service.getList());
	}
	����¡ ó���� �ٸ���Ʈ ����¡ ó���� �������̵� ���� �ؿ� �߰�
	*/
	

	@GetMapping("/list")//����� get��ĸ�
	public void list(Criteria cri ,Model model) {
		log.info("list" + cri);
		model.addAttribute("list",service.getList(cri));
		//model.addAttribute("pageMaker", new PageDTO(cri, 123)); ->�׽�Ʈ�� ��������
		//��ü ������ ����
		int total = service.getTotal(cri);
		log.info("total: "+ total);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		
		
		
	}
	
	@GetMapping("/register")//����Ʈ���� �۾��� ��ư�� Ŭ���� ��� ����
	@PreAuthorize("isAuthenticated()") //������ ����ڸ� �̿��� �� �ֵ��� ó��
	public void register(){
	
	
		
	}

	@PostMapping("/register")//DB�� �ڷ� ����
	@PreAuthorize("isAuthenticated()") //������ ����ڸ� �̿��� �� �ֵ��� ó��
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register"+ board);
		
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		
		//÷������ ����
		service.register(board);
		rttr.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list";
	}
	
	@GetMapping({"/get", "/modify"})//ȭ��� ����
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri")Criteria cri, Model model) {
		
		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
		
	}
	
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")//DB����
	public String modify(BoardVO board, Criteria cri,RedirectAttributes rttr) {
		log.info("modify" + board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		/*
		[Criteria�� UriComponentsBuilder ó����
		//���� ���� ó���� ��� �̵�
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		//�˻� ���� ó���� �߰�!!
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());
		]
		return "redirect:/board/list";
		*/
		return "redirect:/board/list" + cri.getListLink();
	}
	
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")//������ post��ĸ�
	public String remove(@RequestParam("bno") Long bno,Criteria cri, RedirectAttributes rttr, String writer) {
		log.info("remove,,,,,," + bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if(service.remove(bno)) {
			
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result", "success");
		}
		/*
		[Criteria�� UriComponentsBuilder ó����
		//���� ���� ó���� ��� �̵�
				rttr.addAttribute("pageNum", cri.getPageNum());
				rttr.addAttribute("amount", cri.getAmount());
				//�˻� ���� ó���� �߰�!!
				rttr.addAttribute("type", cri.getType());
				rttr.addAttribute("keyword", cri.getKeyword());
		
		return "redirect:/board/list";
		
		*/
		return "redirect:/board/list" + cri.getListLink();
	}
	
	//���� ÷��
	@GetMapping(value ="/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
		log.info("getAttachList" + bno);
	
			return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK); 
	}

	
	//delete ����
	
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
			log.error("delete ���� ����!!" +e.getMessage());
		}
		
	});
}

	
}
