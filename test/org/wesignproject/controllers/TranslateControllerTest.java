package org.wesignproject.controllers;

import java.util.Collections;
import org.apache.http.HttpStatus;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.test.utils.TestUtils;
import org.wesignproject.translate.TranslateProcessor;
import org.wesignproject.translate.VideoSynthesizer;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


public class TranslateControllerTest {
  private static final String WORD = "word";
  private static final Vocabulary VOCABULARY;

  static {
    VOCABULARY = new Vocabulary();
    VOCABULARY.setWord(WORD);
  }

  @Mock
  private TranslateProcessor _mockProcessor;
  @Mock
  private VideoSynthesizer _mockSynthesizer;

  private TranslateController _controllerUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _controllerUnderTest = new TranslateController(_mockSynthesizer, _mockProcessor);
    _mocksCollection = new Object[] {_mockSynthesizer, _mockProcessor};
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @Test
  public void testTranslateToText() {
    Http.RequestBuilder requestBuilder = TestUtils.loadJsonRequest("sampleTranslateRequest.json");

    when(_mockProcessor.process(anyString(), anyString())).thenReturn(Collections.singletonList(VOCABULARY));

    Result result = Helpers.invokeWithContext(requestBuilder, Helpers.contextComponents(), () -> _controllerUnderTest.translateToText());
    assertEquals(result.status(), HttpStatus.SC_OK);

    verify(_mockProcessor).process(isA(String.class), isA(String.class));
  }
}
