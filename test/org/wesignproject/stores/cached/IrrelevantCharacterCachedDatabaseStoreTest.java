package org.wesignproject.stores.cached;

import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.ehcache.IrrelevantCharacterCacheStore;
import org.wesignproject.stores.jpa.IrrelevantCharacterJpaStore;
import org.wesignproject.stores.jpa.SynonymJpaStore;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class IrrelevantCharacterCachedDatabaseStoreTest {
  private static final String WORD = "label";
  private static final String OTHER_WORD = "otherLabel";
  private static final IrrelevantCharacter IRRELEVANT_CHARACTER;
  private static final IrrelevantCharacter OTHER_IRRELEVANT_CHARACTER;

  static {
    IRRELEVANT_CHARACTER = new IrrelevantCharacter();
    IRRELEVANT_CHARACTER.setWord(WORD);

    OTHER_IRRELEVANT_CHARACTER = new IrrelevantCharacter();
    OTHER_IRRELEVANT_CHARACTER.setWord(OTHER_WORD);
  }


  @Mock
  private IrrelevantCharacterJpaStore _mockJpaStore;
  @Mock
  private IrrelevantCharacterCacheStore _mockCacheStore;

  private IrrelevantCharacterCachedDatabaseStore _storeUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _mocksCollection = new Object[] {_mockJpaStore, _mockCacheStore};
    _storeUnderTest = new IrrelevantCharacterCachedDatabaseStore(_mockCacheStore, _mockJpaStore);
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
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.of(IRRELEVANT_CHARACTER));
    } else {
      when(_mockCacheStore.getIfPresent(isA(String.class))).thenReturn(Optional.empty());
      when(_mockJpaStore.get(isA(String.class))).thenReturn(new StoreResult<>(IRRELEVANT_CHARACTER, StoreResult.Status.SUCCESS));
    }
    StoreResult<IrrelevantCharacter> result = _storeUnderTest.get(WORD);
    assertEquals(result.getStatus(), status);

    verify(_mockCacheStore).getIfPresent(isA(String.class));
    if (isInCache) {
      verifyZeroInteractions(_mockJpaStore);
    } else {
      verify(_mockJpaStore).get(isA(String.class));
      verify(_mockCacheStore).put(isA(String.class), isA(IrrelevantCharacter.class));
    }
  }
}
