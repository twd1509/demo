package com.example.demo.Controller;

import com.example.demo.Dto.BoardDTO;
import com.example.demo.Dto.PageDTO;
import com.example.demo.Entity.Board;
import com.example.demo.Service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    @Autowired
    private final BoardService service;

    private boolean flag=false;


    @GetMapping("/post.do")
    public String PostPage(){
        return "post.html";
    }

    @PostMapping("/postproc.do")
    public String PostprocPage(BoardDTO dto, Model model){
        //파라미터받기
        
        //입력값검증
        if(dto.getSubject().isEmpty() || dto.getContent().isEmpty()||
            dto.getPwd().isEmpty()
        )
        {
            return "redirect:/list.do";
        }
        //서비스 호출
        
        //페이지 이동
        service.postfunc(dto);
        model.addAttribute("dto",dto);
        return "redirect:/list.do";     //JSP/Servlet 에서 response.sendredirect(URL)
    }

    @GetMapping("/list.do")
    public String ListPage(PageDTO dto, Model model){

        int nowPage=1; //기본 페이지

        log.info("현재페이지 : " + dto.getNowPage());
        if(dto.getNowPage()!=0)
            nowPage=dto.getNowPage();

        //게시물가져오기
        Page<Board> list =  service.getBoardList(nowPage-1,10); //페이지번호 0부터 시작


        //블럭 계산
        int pagePerBlock=15;                        //한페이지당 표시할 블럭수
        Long totalRecord=list.getTotalElements();   //전체 레코드 수
        int totalPage = list.getTotalPages();       //전체 페이지 수
        int nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock);//현재블럭 번호
        int totalBlock=(int)Math.ceil((double)totalPage/pagePerBlock);//전체블럭 개수

        //블럭에 표시할 StartNum,EndNum 계산
        int pageStart=(nowBlock -1)*pagePerBlock+1;
        int pageEnd=((pageStart+pagePerBlock)<=totalPage)?(pageStart+pagePerBlock):totalPage+1;

        //Model에 연결
        model.addAttribute("list",list);
        model.addAttribute("PS",pageStart);
        model.addAttribute("PE",pageEnd);
        model.addAttribute("pagePerBlock",pagePerBlock);
        model.addAttribute("nowBlock",nowBlock);
        model.addAttribute("totalBlock",totalBlock);
        model.addAttribute("nowPage",nowPage);

        return "list.html";
    }


    @GetMapping("/read.do")
    public String ReadPage(HttpServletRequest req,Model model){
        System.out.println("NUM : " + req.getParameter("num"));

        flag=Boolean.parseBoolean(req.getParameter("flag"));
        System.out.println("FLAG : " + flag);
        //현재페이지 값 저장
        int nowPage = Integer.parseInt(req.getParameter("nowPage"));

        //세션추출
        HttpSession session = req.getSession();

        //현재 읽고있는 게시물 정보 받기
        Long num =  Long.parseLong(req.getParameter("num"));
        //카운트 증가
        if(flag==true) {
            service.Upcount(num);
        }

        //게시물 받아오기
        Board board = service.getBoard(num);


        //세션에 읽고있는 게시물 넣기
        session.setAttribute("board",board);
        session.setAttribute("nowPage",nowPage);

        //모델에 추가(페이지로 전달)
        model.addAttribute("board",board);
        model.addAttribute("nowPage",nowPage);

        return "read.html";
    }

    //Test
    @PostMapping("/isupdate.do")
    public String isupdate(BoardDTO dto,Model model){
        model.addAttribute("dto",dto);
        return "isupdate.html";
    }

    @PostMapping("/update.do")
    public String updatepage(BoardDTO dto,HttpServletRequest req){

        log.info("UPDATE예정인 정보" + dto.toString());

        HttpSession session = req.getSession();
        Board board = (Board)session.getAttribute("board");
        int nowPage=(Integer)session.getAttribute("nowPage");
        log.info("Read중인 정보 : " + board.toString());

        //패스워드 일치 여부확인
        if(dto.getPwd().equals(board.getPwd()))
        {
            //dto에 값 추가
            dto.setEmail(board.getEmail());
            dto.setNum(board.getNum());
            //서비스 수정함수 사용
            service.updateboard(dto);
            //list.do 로 이동(읽고 있는 페이지로이동)
            return "redirect:/list.do?nowPage="+nowPage;
        }else{
            //read.do 로 이동(읽고 있는 게시물로)
            return "redirect:/read.do?num="+board.getNum()+"&nowPage="+nowPage;
        }
    }
    @GetMapping("/isdelete.do")
    public String isdeletepage(){
        return "isdelete.html";
    }
    @PostMapping("/delete.do")
    public String deletepage(BoardDTO dto, HttpServletRequest req){


        log.info("Delete예정인 정보" + dto.toString());
        HttpSession session = req.getSession();
        Board board = (Board)session.getAttribute("board");
        int nowPage=(Integer)session.getAttribute("nowPage");
        log.info("Read중인 정보 : " + board.toString());

        //패스워드 일치 여부확인
        if(dto.getPwd().equals(board.getPwd()))
        {
            //서비스 삭제함수 사용
            service.deleteboard(board.getNum());
            //list.do 로 이동(읽고 있는 페이지로이동)
            return "redirect:/list.do?nowPage="+nowPage;
        }else{
            //read.do 로 이동(읽고 있는 게시물로)
            return "redirect:/read.do?num="+board.getNum()+"&nowPage="+nowPage;
        }

    }



}
