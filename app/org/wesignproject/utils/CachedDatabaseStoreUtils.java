package org.wesignproject.utils;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.wesignproject.cache.InMemoryCache;
import org.wesignproject.models.BaseModel;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.jpa.AbstractJpaStore;
import play.Logger;


/**
 * Util class providing convenient API to perform R/W operation to cache
 */
public enum CachedDatabaseStoreUtils {
  INSTANCE;

  public static <K, V extends BaseModel> StoreResult<V> getFromCacheThenDatabase(@Nonnull K key,
      @Nonnull InMemoryCache<V> cacheStore,
      @Nonnull AbstractJpaStore<K, V> jpaStore) {
    Preconditions.checkNotNull(key, "id cannot be null");
    Preconditions.checkNotNull(cacheStore, "cacheStore cannot be null");
    Preconditions.checkNotNull(jpaStore, "jpaStore cannot be null");
    long start = System.currentTimeMillis();
    return Stream.of(getFromCache(key, cacheStore), getFromDatabaseAndWriteToCache(key, cacheStore, jpaStore))
        .map(Supplier::get)
        .filter(StoreResult::isSuccess)
        .peek(result -> Logger.info(String.format("Time used to read: %d ms", System.currentTimeMillis() - start)))
        .findFirst()
        .orElse(StoreResult.notFound());
  }

  public static <K, V extends BaseModel> List<V> getAllFromCacheThenDatabase(@Nonnull InMemoryCache<V> cacheStore,
      @Nonnull AbstractJpaStore<K, V> jpaStore,
      int listSize) {
    Preconditions.checkNotNull(cacheStore, "cacheStore cannot be null");
    Preconditions.checkNotNull(jpaStore, "jpaStore cannot be null");
    long start = System.currentTimeMillis();
    List<V> resultList = new ArrayList<>(cacheStore.getAllPresent().values());
    if (resultList.size() == 0 || resultList.size() < listSize) {
      List<V> jpaResultList = getAllFromDatabaseAndWriteToCache(cacheStore, jpaStore);
      Logger.info(String.format("Time used to read: %d ms", System.currentTimeMillis() - start));
      return jpaResultList;
    } else {
      Logger.info(String.format("Time used to read: %d ms", System.currentTimeMillis() - start));
      return resultList;
    }
  }

  private static <K, V extends BaseModel> Supplier<StoreResult<V>> getFromCache(K key, InMemoryCache<V> cacheStore) {
    return () -> Optional.of(key)
        .map(id -> cacheStore.getIfPresent((String) id))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(StoreResult::success)
        .orElse(StoreResult.notFound());
  }

  private static <K, V extends BaseModel> Supplier<StoreResult<V>> getFromDatabaseAndWriteToCache(K key, InMemoryCache<V> cacheStore, AbstractJpaStore<K, V> jpaStore) {
    return () -> Optional.of(key)
        .map(jpaStore::get)
        .filter(StoreResult::isSuccess)
        .map(result -> storeInCacheAndReturn(key, result, cacheStore))
        .orElse(StoreResult.notFound());
  }

  private static <K, V extends BaseModel> List<V> getAllFromDatabaseAndWriteToCache(InMemoryCache<V> cacheStore, AbstractJpaStore<K, V> jpaStore) {
    return jpaStore.getAll()
        .stream()
        .peek(data -> cacheStore.put(data.getKey().toString(), data))
        .collect(Collectors.toList());
  }

  private static <K, V extends BaseModel> StoreResult<V> storeInCacheAndReturn(K key, StoreResult<V> result, InMemoryCache<V> cacheStore) {
    cacheStore.put(key.toString(), result.getDataOnSuccess());
    return result;
  }
}
