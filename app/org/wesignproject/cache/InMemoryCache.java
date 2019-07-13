package org.wesignproject.cache;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;


public class InMemoryCache<V extends Serializable> implements Cache<String, V> {
  // This is meant to be an in memory cache. If a larger size is needed, consider using other storage implementations
  public static final int MAX_ELEMENTS = 10000;
  private static CacheManager CACHE_MANAGER = CacheManager.create();

  private final net.sf.ehcache.Cache _cache;

  public InMemoryCache(String name, long ttlInSeconds) {
    Preconditions.checkArgument(StringUtils.isNotBlank(name), "name cannot be blank");
    Preconditions.checkArgument(ttlInSeconds >= 0, "ttlInSeconds cannot be negative");

    if (!CACHE_MANAGER.cacheExists(name)) {
      CACHE_MANAGER.addCache(new net.sf.ehcache.Cache(name,
          MAX_ELEMENTS,
          true,
          ttlInSeconds == 0,
          ttlInSeconds,
          0));
    }
    _cache = CACHE_MANAGER.getCache(name);
  }

  @Override
  public Optional<V> getIfPresent(@Nonnull String key) {
    Preconditions.checkArgument(StringUtils.isNotBlank(key), "key cannot be blank");
    return Optional.ofNullable(_cache.get(key))
        .map(Element::getObjectValue)
        .map(obj -> (V) obj);
  }

  @Override
  public Map<String, V> getAllPresent() {
    return _cache.getAll(_cache.getKeys())
        .values()
        .stream()
        .collect(Collectors.toMap(element -> (String) element.getObjectKey(), element -> (V) element.getObjectValue()));
  }

  @Override
  public void put(String key, V value) {
    _cache.put(new Element(key, value));
  }

  @Override
  public int size() {
    return _cache.getSize();
  }

  @Override
  public void invalidate(Set<String> keys) {
    _cache.removeAll(keys);
  }
}
