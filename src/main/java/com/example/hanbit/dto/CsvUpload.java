package com.example.hanbit.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CsvUpload {

    @CsvBindByName(column = "serviceid")
    private String serviceid;

    @CsvBindByName(column = "sevicecode")
    private String sevicecode;

    @CsvBindByName(column = "servicenum")
    private String servicenum;

    @CsvBindByName(column = "open")
    private boolean open;

    @CsvBindByName(column = "tel")
    private String tel;

    @CsvBindByName(column = "addnum")
    private String addnum;

    @CsvBindByName(column = "addo")
    private String addo;

    @CsvBindByName(column = "addn")
    private String addn;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "x")
    private Double x;

    @CsvBindByName(column = "y")
    private Double y;
}

