package org.wesignproject.models;

import java.util.function.BiConsumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class SynonymTest extends ModelBaseTest<Synonym> {
  private long _id;
  private String _word;
  private String _synonym;

  @Test
  public void testGetter() {
    final Synonym synonym = createModelWithFixedPopulatedFields();

    assertEquals(synonym.getId().longValue(), _id);
    assertEquals(synonym.getWord(), _word);
    assertEquals(synonym.getSynonym(), _synonym);
  }

  @DataProvider
  public Object[][] notBlankSetters() {
    return new Object[][] {
        {(BiConsumer<Synonym, String>) Synonym::setWord, null},
        {(BiConsumer<Synonym, String>) Synonym::setWord, " "},
        {(BiConsumer<Synonym, String>) Synonym::setWord, ""},
        {(BiConsumer<Synonym, String>) Synonym::setSynonym, null},
        {(BiConsumer<Synonym, String>) Synonym::setSynonym, " "},
        {(BiConsumer<Synonym, String>) Synonym::setSynonym, ""}
    };
  }

  @Test(dataProvider = "notBlankSetters", expectedExceptions = IllegalArgumentException.class)
  public void settersShouldNotAcceptBlank(BiConsumer<Synonym, String> setter, String value) {
    setter.accept(new Synonym(), value);
  }

  @Override
  public void setupTest() {
    _id = randomInt();
    _word = randomString();
    _synonym = randomString();
  }

  @Override
  public Synonym createModelWithRandomPopulatedFields() {
    Synonym synonym = new Synonym();
    synonym.setId((long) randomInt());
    synonym.setWord(randomString());
    synonym.setSynonym(randomString());

    return synonym;
  }

  @Override
  public Synonym createModelWithFixedPopulatedFields() {
    Synonym synonym = new Synonym();
    synonym.setId(_id);
    synonym.setWord(_word);
    synonym.setSynonym(_synonym);

    return synonym;
  }
}
