package org.wesignproject.stores.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import org.wesignproject.models.IrrelevantCharacter;
import play.db.jpa.JPAApi;


/**
 * JPA store for {@link IrrelevantCharacter}
 */
public class IrrelevantCharacterJpaStore extends AbstractJpaStore<String, IrrelevantCharacter> {
  @Inject
  public IrrelevantCharacterJpaStore(JPAApi jpaApi) {
    super(IrrelevantCharacter.class.getSimpleName(), jpaApi);
  }

  @Override
  public List<IrrelevantCharacter> getAll() {
    final String selectAllQuery = String.format("from %s", _tableName);
    return Optional.of(_jpaApi.em())
        .map(em -> em.createQuery(selectAllQuery, IrrelevantCharacter.class))
        .map(TypedQuery::getResultList)
        .orElse(Collections.emptyList());
  }
}
