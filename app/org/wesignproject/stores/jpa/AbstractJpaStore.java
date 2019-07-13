package org.wesignproject.stores.jpa;

import org.wesignproject.stores.Store;
import play.db.jpa.JPAApi;

public class AbstractJpaStore<K, V> implements Store<K, V> {
  protected final String _tableName;
  protected final JPAApi _jpaApi;

  public AbstractJpaStore(String tableName, JPAApi jpaApi) {
    _tableName = tableName;
    _jpaApi = jpaApi;
  }
}
