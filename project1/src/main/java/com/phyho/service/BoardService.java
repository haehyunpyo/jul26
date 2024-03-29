package com.phyho.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phyho.DAO.BoardDAO;
import com.phyho.DTO.BoardDTO;
import com.phyho.DTO.PageDTO;
import com.phyho.util.Util;

@Service("boardService")
public class BoardService {
	
	@Inject
	@Named("boardDAO")
	private BoardDAO boardDAO;
	
	@Autowired
	private Util util;
	
	// 보드 리스트 불러오는 메소드
	public List<BoardDTO> boardList(PageDTO page){
		return boardDAO.boardList(page);
	}

	public BoardDTO detail(BoardDTO dto2) {
		// 좋아요수 +1하기 기능을 넣어주겠습니다.
		boardDAO.likeUp(dto2);
	
		BoardDTO dto = boardDAO.detail(dto2);
		// System.out.println(dto);
		// System.out.println(dto.getBno());
		// System.out.println(dto.getBip());
		if(dto != null) {	// 내 글이 아닐때는 null들어옵니다. 즉, null이 아닐떄라고 검사해주세요.
			// 검사 : .이 없거나, null이면 실행하지 않게 해주세요.
			if(dto.getBip() != null && dto.getBip().indexOf(".") != -1) {
				// 여기서 ip뽑아올 수 있죠?
				String ip = dto.getBip();
				// ip 중간에 하트 넣을 수 있죠?
				String[] arr =ip.split("[.]");
				String str = arr[1];
				str = str.replace(arr[1], "**"); 
				arr[1] = str;
				str = String.join(".", arr);
				
				// 그거 다시 ip에 저장할 수 있죠?
				dto.setBip(str);
				// 끝
			}
		} 
		return dto;
	}

	public void write(BoardDTO dto) {
		
		// btitle을 꺼내줍니다.
		String str = dto.getBtitle();
		
		// 값을 변경하겠습니다. replace()  <  =>  &lt;	  >  =>  &gt;
//		if (str.contains("<")) {
//			str = str.replaceAll("<", "&lt;");
//		}
//		if (str.contains(">")){
//			str = str.replaceAll(">", "&gt;");
//		}

		// 다시 저장해주세요.
		dto.setBtitle(util.exchange(str));
		
		dto.setBip(util.getIp()); // 얻어온 ip도 저장해서 데이터베이스로 보내겠습니다.
		
		boardDAO.write(dto); // 실행만 시키고 결과를 받지 않습니다.
		// select를 제외한 나머지는 영향받은 행의 수(int)를 받아오기도 합니다.
	}

	public void delete(BoardDTO dto) {
		boardDAO.delete(dto);
	}

	public void edit(BoardDTO dto) {
		boardDAO.edit(dto);
	}

	public int totalCount() {
		return boardDAO.totalCount();
	}
}
 

