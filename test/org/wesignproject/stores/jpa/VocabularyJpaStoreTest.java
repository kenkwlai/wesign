package org.wesignproject.stores.jpa;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.StoreBaseTest;
import org.wesignproject.stores.StoreResult;
import play.db.jpa.JPAApi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class VocabularyJpaStoreTest extends StoreBaseTest {
  private static final long ID = 1;
  private static final String WORD = "word";
  private static final Vocabulary VOCABULARY;

  static {
    VOCABULARY = new Vocabulary();
    VOCABULARY.setId(ID);
    VOCABULARY.setWord(WORD);
  }

  @Mock
  private JPAApi _mockJPAApi;
  @Mock
  private EntityManager _mockEntityManager;
  @Mock
  private TypedQuery _mockTypedQuery;
  @Mock
  private Query _mockQuery;

  private Object[] _mocksCollection;

  private VocabularyJpaStore _storeUnderTest;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _storeUnderTest = new VocabularyJpaStore(_mockJPAApi);
    _mocksCollection = new Object[] {_mockJPAApi, _mockEntityManager, _mockTypedQuery, _mockQuery};
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @DataProvider
  public Object[][] articles() {
    return new Object[][] {
        {Collections.singletonList(VOCABULARY), StoreResult.Status.SUCCESS},
        {null, StoreResult.Status.NOT_FOUND}
    };
  }

  @SuppressWarnings("unchecked")
  @Test(dataProvider = "articles")
  public void testGet(List<Vocabulary> resultList, StoreResult.Status status) {
    when(_mockJPAApi.em()).thenReturn(_mockEntityManager);
    when(_mockEntityManager.createQuery(anyString(), eq(Vocabulary.class))).thenReturn(_mockTypedQuery);
    when(_mockTypedQuery.setParameter(anyString(), any())).thenReturn(_mockTypedQuery);
    when(_mockTypedQuery.getResultList()).thenReturn(resultList);

    StoreResult<Vocabulary> storeResult = _storeUnderTest.get(WORD);
    assertEquals(storeResult.getStatus(), status);

    verify(_mockJPAApi).em();
    verify(_mockEntityManager).createQuery(anyString(), eq(Vocabulary.class));
    verify(_mockTypedQuery).setParameter(anyString(), any());
    verify(_mockTypedQuery).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetAll() {
    when(_mockJPAApi.em()).thenReturn(_mockEntityManager);
    when(_mockEntityManager.createQuery(anyString(), eq(Vocabulary.class))).thenReturn(_mockTypedQuery);
    when(_mockTypedQuery.getResultList()).thenReturn(Collections.singletonList(VOCABULARY));

    List<Vocabulary> resultList = _storeUnderTest.getAll();
    assertEquals(resultList, Collections.singletonList(VOCABULARY));

    verify(_mockJPAApi).em();
    verify(_mockEntityManager).createQuery(anyString(), eq(Vocabulary.class));
    verify(_mockTypedQuery).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetAllWithConstraint() {
    when(_mockJPAApi.em()).thenReturn(_mockEntityManager);
    when(_mockEntityManager.createQuery(anyString(), eq(Vocabulary.class))).thenReturn(_mockTypedQuery);
    when(_mockTypedQuery.getResultList()).thenReturn(Collections.singletonList(VOCABULARY));

    List<Vocabulary> resultList = _storeUnderTest.getAllWithConstraint();
    assertEquals(resultList, Collections.singletonList(VOCABULARY));

    verify(_mockJPAApi).em();
    verify(_mockEntityManager).createQuery(anyString(), eq(Vocabulary.class));
    verify(_mockTypedQuery).getResultList();
  }
}
