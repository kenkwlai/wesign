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
import org.testng.annotations.Test;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.stores.StoreBaseTest;
import play.db.jpa.JPAApi;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class IrrelevantCharacterJpaStoreTest extends StoreBaseTest {
  private static final long ID = 1;
  private static final String WORD = "word";
  private static final IrrelevantCharacter IRRELEVANT_CHARACTER;

  static {
    IRRELEVANT_CHARACTER = new IrrelevantCharacter();
    IRRELEVANT_CHARACTER.setId(ID);
    IRRELEVANT_CHARACTER.setWord(WORD);
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

  private IrrelevantCharacterJpaStore _storeUnderTest;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _storeUnderTest = new IrrelevantCharacterJpaStore(_mockJPAApi);
    _mocksCollection = new Object[] {_mockJPAApi, _mockEntityManager, _mockTypedQuery, _mockQuery};
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetAll() {
    when(_mockJPAApi.em()).thenReturn(_mockEntityManager);
    when(_mockEntityManager.createQuery(anyString(), eq(IrrelevantCharacter.class))).thenReturn(_mockTypedQuery);
    when(_mockTypedQuery.getResultList()).thenReturn(Collections.singletonList(IRRELEVANT_CHARACTER));

    List<IrrelevantCharacter> resultList = _storeUnderTest.getAll();
    assertEquals(resultList, Collections.singletonList(IRRELEVANT_CHARACTER));

    verify(_mockJPAApi).em();
    verify(_mockEntityManager).createQuery(anyString(), eq(IrrelevantCharacter.class));
    verify(_mockTypedQuery).getResultList();
  }
}
