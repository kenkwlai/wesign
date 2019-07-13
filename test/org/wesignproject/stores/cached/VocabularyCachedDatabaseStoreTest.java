package org.wesignproject.stores.cached;

import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.VocabularyCacheStore;
import org.wesignproject.stores.jpa.VocabularyJpaStore;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class VocabularyCachedDatabaseStoreTest {
  private static final String WORD = "label";
  private static final String OTHER_WORD = "otherLabel";
  private static final Vocabulary VOCABULARY;
  private static final Vocabulary OTHER_VOCABULARY;

  static {
    VOCABULARY = new Vocabulary();
    VOCABULARY.setWord(WORD);

    OTHER_VOCABULARY = new Vocabulary();
    OTHER_VOCABULARY.setWord(OTHER_WORD);
  }


  @Mock
  private VocabularyJpaStore _mockJpaStore;
  @Mock
  private VocabularyCacheStore _mockCacheStore;

  private VocabularyCachedDatabaseStore _storeUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _mocksCollection = new Object[] {_mockJpaStore, _mockCacheStore};
    _storeUnderTest = new VocabularyCachedDatabaseStore(_mockCacheStore, _mockJpaStore);
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
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.of(VOCABULARY));
    } else {
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.empty());
      when(_mockJpaStore.get(isA(String.class))).thenReturn(new StoreResult<>(VOCABULARY, StoreResult.Status.SUCCESS));
    }
    StoreResult<Vocabulary> result = _storeUnderTest.get(WORD);
    assertEquals(result.getStatus(), status);

    verify(_mockCacheStore).getIfPresent(isA(String.class));
    if (isInCache) {
      verifyZeroInteractions(_mockJpaStore);
    } else {
      verify(_mockJpaStore).get(isA(String.class));
      verify(_mockCacheStore).put(isA(String.class), isA(Vocabulary.class));
    }
  }
}
