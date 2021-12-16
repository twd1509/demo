package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="tbl_board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    @Column
    private String email;
    @Column
    private String subject;
    @Column
    private String content;
    @Column(columnDefinition ="date")
    private String regdate;
    @Column
    private String pwd;
    @Column
    private int count;

}








