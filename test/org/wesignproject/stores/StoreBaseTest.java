package org.wesignproject.stores;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import play.db.jpa.JPAApi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class StoreBaseTest {
  protected <K, V> void testPersist(JPAApi jpaApi, EntityManager entityManager, Store<K, V> store, V value, Object[] mocksCollection) {
    when(jpaApi.em()).thenReturn(entityManager);

    store.store(null, value);

    verify(jpaApi).em();
    verify(entityManager).persist(eq(value));
    verifyNoMoreInteractions(mocksCollection);
  }

  protected <K, V> void testUpdate(JPAApi jpaApi, EntityManager entityManager, Store<K, V> store, K key, V value, Object[] mocksCollection) {
    when(jpaApi.em()).thenReturn(entityManager);

    store.store(key, value);

    verify(jpaApi).em();
    verify(entityManager).merge(eq(value));
    verifyNoMoreInteractions(mocksCollection);
  }

  protected <K, V> void testDelete(JPAApi jpaApi, EntityManager entityManager, Query query, Store<K, V> store, K key, Object[] mocksCollection) {
    when(jpaApi.em()).thenReturn(entityManager);
    when(entityManager.createQuery(anyString())).thenReturn(query);
    when(query.setParameter(anyString(), any())).thenReturn(query);
    when(query.executeUpdate()).thenReturn(1);

    store.remove(key);

    verify(jpaApi).em();
    verify(entityManager).createQuery(anyString());
    verify(query).setParameter(anyString(), any());
    verify(query).executeUpdate();
    verifyNoMoreInteractions(mocksCollection);
  }
}
