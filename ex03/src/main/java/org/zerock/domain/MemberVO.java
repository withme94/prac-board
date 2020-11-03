package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO {

	private String userid;
	private String userpw;
	private String userName;
	private boolean enabled;
	
	private Date regDate;
	private Date updateDate;
	private List<AuthVO> authList; // 멤버에 권한 추가 하나의 컬럼 속성값이 여러개일 수 있기 떄문에 auth VO를 만듬
}
