package org.wesignproject.stores.cached;

import java.util.List;
import javax.inject.Inject;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.VocabularyCacheStore;
import org.wesignproject.stores.jpa.VocabularyJpaStore;
import org.wesignproject.utils.CachedDatabaseStoreUtils;


public class VocabularyCachedDatabaseStore extends AbstractCachedDatabaseStore<String, Vocabulary> {
  private static int LIST_SIZE = 0;

  @Inject
  public VocabularyCachedDatabaseStore(VocabularyCacheStore cacheStore, VocabularyJpaStore jpaStore) {
    super(cacheStore, jpaStore);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StoreResult<Vocabulary> get(String key) {
    return CachedDatabaseStoreUtils.getFromCacheThenDatabase(key, _cacheStore, _jpaStore);
  }

  @Override
  public List<Vocabulary> getAll() {
    if (LIST_SIZE == 0) {
      LIST_SIZE = _jpaStore.getAll().size();
    }
    return CachedDatabaseStoreUtils.getAllFromCacheThenDatabase(_cacheStore, _jpaStore, LIST_SIZE);
  }

  @Override
  public List<Vocabulary> getAllWithConstraint() {
    return _jpaStore.getAllWithConstraint();
  }
}
