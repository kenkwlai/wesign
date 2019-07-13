package org.wesignproject.stores.cached;

import java.util.List;
import javax.inject.Inject;
import org.wesignproject.models.Synonym;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.SynonymCacheStore;
import org.wesignproject.stores.jpa.SynonymJpaStore;
import org.wesignproject.utils.CachedDatabaseStoreUtils;


public class SynonymCachedDatabaseStore extends AbstractCachedDatabaseStore<String, Synonym> {
  private static int LIST_SIZE = 0;

  @Inject
  public SynonymCachedDatabaseStore(SynonymCacheStore cacheStore, SynonymJpaStore jpaStore) {
    super(cacheStore, jpaStore);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StoreResult<Synonym> get(String key) {
    return CachedDatabaseStoreUtils.getFromCacheThenDatabase(key, _cacheStore, _jpaStore);
  }

  @Override
  public List<Synonym> getAll() {
    if (LIST_SIZE == 0) {
      LIST_SIZE = _jpaStore.getAll().size();
    }
    return CachedDatabaseStoreUtils.getAllFromCacheThenDatabase(_cacheStore, _jpaStore, LIST_SIZE);
  }
}
