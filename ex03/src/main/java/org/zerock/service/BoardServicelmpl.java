package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;


import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service

public class BoardServicelmpl implements BoardService {

	@Setter(onMethod_ = @Autowired )
	private BoardMapper mapper;
	
	//setter 값
	@Setter(onMethod_ = @Autowired )
	private BoardAttachMapper attachMapper;
	
	
	
	@Transactional
	@Override
	public void register(BoardVO board) {
		log.info("register......" + board);
		
		mapper.insertSelectKey(board);
	
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		board.getAttachList().forEach(attach ->{
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("get.........." + bno);
		return mapper.read(bno);
	}

	
	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		log.info("modify.........." + board);
		
		attachMapper.deleteAll(board.getBno());
	
		boolean modifyResult = mapper.update(board) == 1;
		
		if(modifyResult && board.getAttachList() != null & board.getAttachList().size() > 0) {
			
			board.getAttachList().forEach(attach ->{
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
				
			});
		}
		
		return modifyResult;
	}

	
/*	@Override
	public List<BoardVO> getList() {
		log.info("getList.........." );
		return mapper.getList();
		페이징 처리 전 get List 불러올 때 사용
		밑 파라미터로 criteria 오버라이딩
	}
*/
	
	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("getList with criteria" + cri );
		return mapper.getListWithPaging(cri);
	}
	
	
	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno){
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
	
	

	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("지우기 보드서비스" + bno);
		
		attachMapper.deleteAll(bno);
	
		return mapper.delete(bno) == 1;
	}
}
