package com.example.demo.Service;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Entity.Board;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface BoardService {

    //글쓰기
    BoardDTO postfunc(BoardDTO dto);

    //게시물전달(Size:10)
    Page<Board> getBoardList(int page,int size);

    //게시물하나 받아오기
    Board getBoard(Long num);

    //조회수 증가
    void Upcount(Long id);

    //게시물 수정
    void updateboard(BoardDTO dto);

    //게시물 삭제
    void deleteboard(Long num);

    //DTO->Entity
    default Board dtoToEntity(BoardDTO dto){
        LocalDate now = LocalDate.now();
        //빌더패턴
        Board board = Board.builder()
                .content(dto.getContent())
                .subject(dto.getSubject())
                .email(dto.getEmail())
                .pwd(dto.getPwd())
                .regdate(now.toString())
                .count(0)
                .build();

        return board;

    }
}
