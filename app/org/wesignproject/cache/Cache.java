package org.wesignproject.cache;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;


public interface Cache<K, V> {
  Optional<V> getIfPresent(@Nonnull K key);

  Map<K, V> getAllPresent();

  void put(K key, V value);

  int size();

  void invalidate(Set<K> keys);
}