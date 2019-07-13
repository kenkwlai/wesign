package org.wesignproject.models;

import java.util.function.BiConsumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class VocabularyTest extends ModelBaseTest<Vocabulary> {
  private long _id;
  private String _word;
  private String _path;
  private String _mainType;
  private String _subType;
  private String _prefix;
  private Double _duration;

  @Test
  public void testGetter() {
    final Vocabulary vocabulary = createModelWithFixedPopulatedFields();

    assertEquals(vocabulary.getId().longValue(), _id);
    assertEquals(vocabulary.getWord(), _word);
    assertEquals(vocabulary.getPath(), _path);
    assertEquals(vocabulary.getMainType(), _mainType);
    assertEquals(vocabulary.getSubType(), _subType);
    assertEquals(vocabulary.getPrefix(), _prefix);
    assertEquals(vocabulary.getDuration(), _duration);
  }

  @DataProvider
  public Object[][] notBlankSetters() {
    return new Object[][] {
        {(BiConsumer<Vocabulary, String>) Vocabulary::setWord, null},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setWord, " "},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setWord, ""},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setPath, null},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setPath, " "},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setPath, ""},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setMainType, null},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setMainType, " "},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setMainType, ""},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setSubType, null},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setSubType, " "},
        {(BiConsumer<Vocabulary, String>) Vocabulary::setSubType, ""}
    };
  }

  @Test(dataProvider = "notBlankSetters", expectedExceptions = IllegalArgumentException.class)
  public void settersShouldNotAcceptBlank(BiConsumer<Vocabulary, String> setter, String value) {
    setter.accept(new Vocabulary(), value);
  }

  @Override
  public void setupTest() {
    _id = randomInt();
    _word = randomString();
    _path = randomString();
    _mainType = randomString();
    _subType = randomString();
    _prefix = randomString();
    _duration = (double) randomInt();
  }

  @Override
  public Vocabulary createModelWithRandomPopulatedFields() {
    Vocabulary vocabulary = new Vocabulary();
    vocabulary.setId((long) randomInt());
    vocabulary.setWord(randomString());
    vocabulary.setPath(randomString());
    vocabulary.setMainType(randomString());
    vocabulary.setSubType(randomString());
    vocabulary.setPrefix(randomString());
    vocabulary.setDuration((double) randomInt());

    return vocabulary;
  }

  @Override
  public Vocabulary createModelWithFixedPopulatedFields() {
    Vocabulary vocabulary = new Vocabulary();
    vocabulary.setId(_id);
    vocabulary.setWord(_word);
    vocabulary.setPath(_path);
    vocabulary.setMainType(_mainType);
    vocabulary.setSubType(_subType);
    vocabulary.setPrefix(_prefix);
    vocabulary.setDuration(_duration);

    return vocabulary;
  }
}
