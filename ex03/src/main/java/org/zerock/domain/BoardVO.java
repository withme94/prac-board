package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;
	private int replyCnt; //답글 추가 DB 수정 후
	private List<BoardAttachVO> attachList;//파일 업로드 BoardAttachVO생성 후
	
}
