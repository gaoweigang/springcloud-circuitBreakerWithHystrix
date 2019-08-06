package com.gwg.springcloud.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    //姓名
    private String name;
    //年龄
    private int age;


}
