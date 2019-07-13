package org.wesignproject.stores.ehcache;

import javax.inject.Inject;
import org.wesignproject.cache.InMemoryCache;
import org.wesignproject.models.Synonym;
import org.wesignproject.utils.config.ConfigUtils;


public class SynonymCacheStore extends InMemoryCache<Synonym> {
  private static final String CACHE_EXPIRATION_TIME = "CACHE_EXPIRATION_TIME";
  private static final int EXPIRATION_TIME_SEC =
      ConfigUtils.getEnvVarWithDefault(CACHE_EXPIRATION_TIME, 600, Integer::parseInt);

  @Inject
  public SynonymCacheStore() {
    super(Synonym.class.getName(), EXPIRATION_TIME_SEC);
  }
}
