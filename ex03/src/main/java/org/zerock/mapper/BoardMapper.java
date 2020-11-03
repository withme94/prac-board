package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	/* @Select("select * from tbl_board where bno > 0") */
	public List<BoardVO> getList();
	
	public void insert(BoardVO board);
	
	public void insertSelectKey(BoardVO board);
	
	public BoardVO read(Long bno);
	
	public int delete(Long bno);
	
	public int update(BoardVO board);
	
	public List<BoardVO> getListWithPaging(Criteria cri);
	//페이징을 위한 맵퍼 sql문 딤
	public int getTotalCount(Criteria cri);
	//전체
	
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	//답글 추가 DB수정 후
	
}
