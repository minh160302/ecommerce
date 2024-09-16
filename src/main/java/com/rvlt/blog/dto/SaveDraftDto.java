package com.rvlt.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDraftDto {
  private String id;
  private String title;
  private String content;
}
