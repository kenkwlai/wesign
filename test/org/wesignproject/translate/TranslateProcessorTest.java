package org.wesignproject.translate;

import java.util.Collections;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wesignproject.models.IrrelevantCharacter;
import org.wesignproject.models.Synonym;
import org.wesignproject.models.Vocabulary;
import org.wesignproject.stores.cached.IrrelevantCharacterCachedDatabaseStore;
import org.wesignproject.stores.cached.SynonymCachedDatabaseStore;
import org.wesignproject.stores.cached.VocabularyCachedDatabaseStore;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class TranslateProcessorTest {
  private static final String SAMPLE_SENTENCE = "我的你";
  private static final String WORD = "我";
  private static final String IRR_WORD = "的";
  private static final String TYPE = "type";
  private static final String PATH = "path";
  private static final Vocabulary VOCABULARY;
  private static final Synonym SYNONYM;
  private static final IrrelevantCharacter IRRELEVANT_CHARACTER;

  static {
    VOCABULARY = new Vocabulary();
    VOCABULARY.setWord(WORD);
    VOCABULARY.setMainType(TYPE);
    VOCABULARY.setSubType(TYPE);
    VOCABULARY.setPath(PATH);

    SYNONYM = new Synonym();
    SYNONYM.setWord(WORD);
    SYNONYM.setSynonym(WORD);

    IRRELEVANT_CHARACTER = new IrrelevantCharacter();
    IRRELEVANT_CHARACTER.setWord(IRR_WORD);
  }

  @Mock
  private VocabularyCachedDatabaseStore _mockVocabularyStore;
  @Mock
  private SynonymCachedDatabaseStore _mockSynonymStore;
  @Mock
  private IrrelevantCharacterCachedDatabaseStore _mockIrrelevantCharacterStore;

  private TranslateProcessor _processorUnderTest;

  private Object[] _mocksCollection;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _processorUnderTest = new TranslateProcessor(_mockVocabularyStore, _mockIrrelevantCharacterStore, _mockSynonymStore);
    _mocksCollection = new Object[] {_mockVocabularyStore, _mockIrrelevantCharacterStore, _mockSynonymStore};
  }

  @AfterMethod
  public void tearDown() {
    verifyNoMoreInteractions(_mocksCollection);
  }

  @Test
  public void testProcess() {
    when(_mockVocabularyStore.getAll()).thenReturn(Collections.singletonList(VOCABULARY));
    when(_mockSynonymStore.getAll()).thenReturn(Collections.singletonList(SYNONYM));
    when(_mockIrrelevantCharacterStore.getAll()).thenReturn(Collections.singletonList(IRRELEVANT_CHARACTER));
    List<Vocabulary> resultList = _processorUnderTest.process(SAMPLE_SENTENCE, "deaf");
    assertNotNull(resultList);
    assertEquals(resultList, Collections.singletonList(VOCABULARY));

    verify(_mockVocabularyStore).getAll();
    verify(_mockSynonymStore).getAll();
    verify(_mockIrrelevantCharacterStore).getAll();
  }
}
