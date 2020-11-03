package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service//해당 클래스가 서비스임을 알림~~
@Log4j
@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	
	@Setter(onMethod_= @Autowired )
	private ReplyMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;
	
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		
		
		log.info("레지스터 확인");
		
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		//답글 처리 추가 reply mapper에 board mapper에 추가했음
		
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		log.info("읽어오기");
		
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("수정하기!");
		return mapper.update(vo);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		log.info("삭제하기");
		ReplyVO vo = mapper.read(rno);
		
		boardMapper.updateReplyCnt(vo.getBno(), -1);
		//답글 처리 추가 reply mapper에 board mapper에 추가했음
		return mapper.delete(rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		log.info("보드의 답글 리스트 불러오기! " + bno);
		return mapper.getListWithPaging(cri, bno);
	}
	
	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		return new ReplyPageDTO(
		mapper.getCountByBno(bno),
		mapper.getListWithPaging(cri, bno));
	}
	

}
