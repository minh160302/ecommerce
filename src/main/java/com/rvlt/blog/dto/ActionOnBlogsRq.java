package com.rvlt.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActionOnBlogsRq {
  private List<Long> blogs;
}
