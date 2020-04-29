package com.delav.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface BaseMapper<T>
{
  public abstract T select(T paramT);
  
  public abstract List<T> selectList(T paramT);
  
  public abstract int insert(T paramT);
  
  public abstract int update(T paramT);
}