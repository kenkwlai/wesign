package org.wesignproject.stores.cached;

import java.io.Serializable;
import org.wesignproject.cache.InMemoryCache;
import org.wesignproject.stores.Store;
import org.wesignproject.stores.jpa.AbstractJpaStore;


/**
 * Store to handle query to cache or database
 */
public class AbstractCachedDatabaseStore<K, V extends Serializable> implements Store<K, V> {
  protected InMemoryCache<V> _cacheStore;
  protected AbstractJpaStore<K, V> _jpaStore;

  public AbstractCachedDatabaseStore(InMemoryCache<V> cacheStore, AbstractJpaStore<K, V> jpaStore) {
    _cacheStore = cacheStore;
    _jpaStore = jpaStore;
  }
}
