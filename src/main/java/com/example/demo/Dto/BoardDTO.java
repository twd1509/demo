package com.example.demo.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDTO {
    private Long num;
    private String email;
    private String subject;
    private String content;
    private String regdate;
    private String pwd;
    private int count;

}
