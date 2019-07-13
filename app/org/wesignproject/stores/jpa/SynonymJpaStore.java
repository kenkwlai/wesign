package org.wesignproject.stores.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import org.wesignproject.models.Synonym;
import play.db.jpa.JPAApi;


/**
 * JPA store for {@link Synonym}
 */
public class SynonymJpaStore extends AbstractJpaStore<String, Synonym> {
  @Inject
  public SynonymJpaStore(JPAApi jpaApi) {
    super(Synonym.class.getSimpleName(), jpaApi);
  }

  @Override
  public List<Synonym> getAll() {
    final String selectAllQuery = String.format("from %s", _tableName);
    return Optional.of(_jpaApi.em())
        .map(em -> em.createQuery(selectAllQuery, Synonym.class))
        .map(TypedQuery::getResultList)
        .orElse(Collections.emptyList());
  }
}
