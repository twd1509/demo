package com.example.demo.Service;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Entity.Board;
import com.example.demo.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    @Autowired
    private final BoardRepository boardrepo;

    @Override
    public BoardDTO postfunc(BoardDTO dto) {
        Board board = dtoToEntity(dto); //DTO -> Entity 변환
        boardrepo.save(board);  //DB에 글쓰기
        return dto;
    }

    @Override
    public Page<Board> getBoardList(int page, int size) {
        //num열을 기준으로 내림차순 정렬
        Sort sort1 = Sort.by("num").descending();
        //페이지당 게시물의 개수
        Pageable pageable= PageRequest.of(page,size,sort1);
        //게시물가져오기
        Page<Board> list=boardrepo.findAll(pageable);
        return list;
    }

    @Override
    public Board getBoard(Long num) {

        Optional<Board> board = boardrepo.findById(num);
        return board.get();    //게시물꺼내오기
    }

    @Override
    public void Upcount(Long id) {
        Optional<Board> board = boardrepo.findById(id);
        board.get().setCount(board.get().getCount()+1);
        boardrepo.save(board.get());
    }

    @Override
    public void updateboard(BoardDTO dto) {
        LocalDate now = LocalDate.now();
        Optional<Board> board = boardrepo.findById(dto.getNum());
        board.get().setEmail(dto.getEmail());
        board.get().setContent(dto.getContent());
        board.get().setSubject(dto.getSubject());
        board.get().setRegdate(now.toString());
        boardrepo.save(board.get());
    }

    @Override
    public void deleteboard(Long num) {
        boardrepo.deleteById(num);
    }


}
