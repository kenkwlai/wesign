package org.wesignproject.stores.cached;

import java.util.List;
import javax.inject.Inject;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.IrrelevantCharacterCacheStore;
import org.wesignproject.stores.jpa.IrrelevantCharacterJpaStore;
import org.wesignproject.utils.CachedDatabaseStoreUtils;


public class IrrelevantCharacterCachedDatabaseStore extends AbstractCachedDatabaseStore<String, IrrelevantCharacter> {
  private static int LIST_SIZE = 0;

  @Inject
  public IrrelevantCharacterCachedDatabaseStore(IrrelevantCharacterCacheStore cacheStore, IrrelevantCharacterJpaStore jpaStore) {
    super(cacheStore, jpaStore);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StoreResult<IrrelevantCharacter> get(String key) {
    return CachedDatabaseStoreUtils.getFromCacheThenDatabase(key, _cacheStore, _jpaStore);
  }

  @Override
  public List<IrrelevantCharacter> getAll() {
    if (LIST_SIZE == 0) {
      LIST_SIZE = _jpaStore.getAll().size();
    }
    return CachedDatabaseStoreUtils.getAllFromCacheThenDatabase(_cacheStore, _jpaStore, LIST_SIZE);
  }
}
