package org.wesignproject.stores.ehcache;

import javax.inject.Inject;
import org.wesignproject.cache.InMemoryCache;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.utils.config.ConfigUtils;


public class IrrelevantCharacterCacheStore extends InMemoryCache<IrrelevantCharacter> {
  private static final String CACHE_EXPIRATION_TIME = "CACHE_EXPIRATION_TIME";
  private static final int EXPIRATION_TIME_SEC =
      ConfigUtils.getEnvVarWithDefault(CACHE_EXPIRATION_TIME, 600, Integer::parseInt);

  @Inject
  public IrrelevantCharacterCacheStore() {
    super(IrrelevantCharacter.class.getName(), EXPIRATION_TIME_SEC);
  }
}
