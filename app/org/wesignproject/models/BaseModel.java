package org.wesignproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


public abstract class BaseModel<K> implements Serializable {
  private K key;

  @JsonIgnore
  abstract public K getKey();
}
