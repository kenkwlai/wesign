package org.wesignproject.stores.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.StoreResult;
import play.Logger;
import play.db.jpa.JPAApi;


/**
 * JPA store for {@link Vocabulary}
 */
public class VocabularyJpaStore extends AbstractJpaStore<String, Vocabulary> {
  @Inject
  public VocabularyJpaStore(JPAApi jpaApi) {
    super(Vocabulary.class.getSimpleName(), jpaApi);
  }

  @Override
  public List<Vocabulary> getAll() {
    final String selectAllQuery = String.format("from %s", _tableName);
    return Optional.of(_jpaApi.em())
        .map(em -> em.createQuery(selectAllQuery, Vocabulary.class))
        .map(TypedQuery::getResultList)
        .orElse(Collections.emptyList());
  }

  @Override
  public List<Vocabulary> getAllWithConstraint() {
    final String selectAllQuery =
        String.format("from %s v where not exists (select word from %s i where v.word = i.word)",
            _tableName,
            IrrelevantCharacter.class.getSimpleName());
    return Optional.of(_jpaApi.em())
        .map(em -> em.createQuery(selectAllQuery, Vocabulary.class))
        .map(TypedQuery::getResultList)
        .orElse(Collections.emptyList());
  }

  @SuppressWarnings("unchecked")
  @Override
  public StoreResult<Vocabulary> get(String key) {
    final String selectQuery = String.format("from %s v where v.word = :word", _tableName);
    Logger.info(String.format("Getting %s", key));
    return Optional.of(_jpaApi.em())
        .map(em -> em.createQuery(selectQuery, Vocabulary.class))
        .map(query -> query.setParameter("word", key))
        .map(TypedQuery::getResultList)
        .filter(list -> list.size() > 0)
        .map(list -> list.get(0))
        .map(StoreResult::success)
        .orElse(StoreResult.notFound());
  }
}
