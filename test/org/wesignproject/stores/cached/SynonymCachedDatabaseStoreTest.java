package org.wesignproject.stores.cached;

import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wesignproject.models.Synonym;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.SynonymCacheStore;
import org.wesignproject.stores.jpa.SynonymJpaStore;
import org.wesignproject.stores.jpa.VocabularyJpaStore;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class SynonymCachedDatabaseStoreTest {
  private static final String WORD = "label";
  private static final String OTHER_WORD = "otherLabel";
  private static final Synonym SYNONYM;
  private static final Synonym OTHER_SYNONYM;

  static {
    SYNONYM = new Synonym();
    SYNONYM.setWord(WORD);

    OTHER_SYNONYM = new Synonym();
    OTHER_SYNONYM.setWord(OTHER_WORD);
  }


  @Mock
  private SynonymJpaStore _mockJpaStore;
  @Mock
  private SynonymCacheStore _mockCacheStore;

  private SynonymCachedDatabaseStore _storeUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _mocksCollection = new Object[] {_mockJpaStore, _mockCacheStore};
    _storeUnderTest = new SynonymCachedDatabaseStore(_mockCacheStore, _mockJpaStore);
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @DataProvider
  public Object[][] getResults() {
    return new Object[][] {
        {true, StoreResult.Status.SUCCESS},
        {false, StoreResult.Status.SUCCESS}
    };
  }

  @Test(dataProvider = "getResults")
  public void testGet(boolean isInCache, StoreResult.Status status) {
    if (isInCache) {
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.of(SYNONYM));
    } else {
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.empty());
      when(_mockJpaStore.get(isA(String.class))).thenReturn(new StoreResult<>(SYNONYM, StoreResult.Status.SUCCESS));
    }
    StoreResult<Synonym> result = _storeUnderTest.get(WORD);
    assertEquals(result.getStatus(), status);

    verify(_mockCacheStore).getIfPresent(isA(String.class));
    if (isInCache) {
      verifyZeroInteractions(_mockJpaStore);
    } else {
      verify(_mockJpaStore).get(isA(String.class));
      verify(_mockCacheStore).put(isA(String.class), isA(Synonym.class));
    }
  }
}
