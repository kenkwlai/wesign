package org.wesignproject.stores;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Ken Lai on 7/1/2017.
 */
public interface Store<K, V> {
  default List<V> getAll() {
    throw new UnsupportedOperationException("getAll not implemented");
  }

  default List<V> getAllWithConstraint() {
    throw new UnsupportedOperationException("getAllWithConstraint not implemented");
  }

  default StoreResult<V> get(K key) {
    throw new UnsupportedOperationException("get not implemented");
  }

  default Map<K, V> get(Set<K> key) {
    throw new UnsupportedOperationException("batch get not implemented");
  }

  default void store(K key, V value) {
    throw new UnsupportedOperationException("write not implemented");
  }

  default void store(Map<K, V> keyValuePairs) {
    throw new UnsupportedOperationException("batch write not implemented");
  }

  default void remove(K key) {
    throw new UnsupportedOperationException("delete not implemented");
  }
}
