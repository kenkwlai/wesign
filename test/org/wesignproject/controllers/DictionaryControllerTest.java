package org.wesignproject.controllers;

import java.util.Collections;
import org.apache.http.HttpStatus;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.StoreResult;
import org.wesignproject.stores.cached.VocabularyCachedDatabaseStore;
import play.mvc.Http;
import play.mvc.Result;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class DictionaryControllerTest {
  private static final String WORD = "word";
  private static final Vocabulary VOCABULARY;

  static {
    VOCABULARY = new Vocabulary();
    VOCABULARY.setWord(WORD);
  }

  @Mock
  private VocabularyCachedDatabaseStore _mockCachedDatabaseStore;

  private DictionaryController _controllerUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _mocksCollection = new Object[] {_mockCachedDatabaseStore};
    _controllerUnderTest = new DictionaryController(_mockCachedDatabaseStore);
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @Test
  public void testGetResponse() throws Exception {
    when(_mockCachedDatabaseStore.get(anyString())).thenReturn(StoreResult.success(VOCABULARY));

    Result result =_controllerUnderTest.getResponse(WORD);
    assertEquals(result.status(), Http.Status.OK);

    verify(_mockCachedDatabaseStore).get(isA(String.class));
  }

  @Test
  public void testGetAll() throws Exception {
    when(_mockCachedDatabaseStore.getAllWithConstraint()).thenReturn(Collections.singletonList(VOCABULARY));
    assertEquals(_controllerUnderTest.getAll().status(), HttpStatus.SC_OK);
    verify(_mockCachedDatabaseStore).getAllWithConstraint();
  }
}
