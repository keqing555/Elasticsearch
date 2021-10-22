package com.search.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
   // @TableId
    private int rid;
    private String rname;
    private String area;
    private int age;
    private String sex;
    private String star;
    private String elementalForce;
}
