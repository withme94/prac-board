package org.zerock.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;


import lombok.extern.log4j.Log4j;
import lombok.Setter;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")

public class BoardServiceTests {

	@Setter(onMethod_ = {@ Autowired} )
	private BoardService service;
	
	@Test
	public void testExist() {
		log.info(service);
		assertNotNull(service);
	}
	
	@Test
	public void testRegister() {
		
		BoardVO board = new BoardVO();
		board.setTitle("���� �ۼ��ϴ� ��");
		board.setContent("���� �ۼ��ϴ� ����");
		board.setWriter("����");
		service.register(board);
		log.info("������ �Խù��� ��ȣ: "+board.getBno());
	}
	
	@Test
	public void testGetList() {
		
		//service.getList().forEach(board -> log.info(board));
		//����¡ ó�� �� �ٸ���Ʈ �׽�Ʈ
		service.getList(new Criteria(2, 10)).forEach(board -> log.info(board));
		
	}
	
	@Test
	public void testDelete() {
		log.info("remove result:" + service.remove(2L));
	}
	
	@Test
	public void testUpdate() {
		BoardVO board = service.get(1L);
		
		if(board == null) {
			return;
		}
		board.setTitle("���� ����");
		log.info("ModiFY rEsult :"+ service.modify(board));
	}
	
	
}
