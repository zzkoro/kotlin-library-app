package com.group.libraryapp.dto.user.request;

import org.jetbrains.annotations.Nullable;

public class UserCreateRequest {

  private String name;
  private Integer age;


  public UserCreateRequest(String name, Integer age) {
    this.name = name;
    this.age =  age;
  }

  public String getName() {
    return name;
  }

  @Nullable
  public Integer getAge() {
    return age;
  }

}
