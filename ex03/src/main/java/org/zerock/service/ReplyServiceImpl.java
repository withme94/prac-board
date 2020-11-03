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

@Service//�ش� Ŭ������ �������� �˸�~~
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
		
		
		log.info("�������� Ȯ��");
		
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		//��� ó�� �߰� reply mapper�� board mapper�� �߰�����
		
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		log.info("�о����");
		
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("�����ϱ�!");
		return mapper.update(vo);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		log.info("�����ϱ�");
		ReplyVO vo = mapper.read(rno);
		
		boardMapper.updateReplyCnt(vo.getBno(), -1);
		//��� ó�� �߰� reply mapper�� board mapper�� �߰�����
		return mapper.delete(rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		log.info("������ ��� ����Ʈ �ҷ�����! " + bno);
		return mapper.getListWithPaging(cri, bno);
	}
	
	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		return new ReplyPageDTO(
		mapper.getCountByBno(bno),
		mapper.getListWithPaging(cri, bno));
	}
	

}