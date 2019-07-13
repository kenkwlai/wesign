package org.wesignproject.models;

import java.util.function.BiConsumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class IrrelevantCharacterTest extends ModelBaseTest<IrrelevantCharacter> {
  private long _id;
  private String _word;

  @Test
  public void testGetter() {
    final IrrelevantCharacter irrelevantCharacter = createModelWithFixedPopulatedFields();

    assertEquals(irrelevantCharacter.getId().longValue(), _id);
    assertEquals(irrelevantCharacter.getWord(), _word);
  }

  @DataProvider
  public Object[][] notBlankSetters() {
    return new Object[][] {
        {(BiConsumer<IrrelevantCharacter, String>) IrrelevantCharacter::setWord, null},
        {(BiConsumer<IrrelevantCharacter, String>) IrrelevantCharacter::setWord, " "},
        {(BiConsumer<IrrelevantCharacter, String>) IrrelevantCharacter::setWord, ""},
    };
  }

  @Test(dataProvider = "notBlankSetters", expectedExceptions = IllegalArgumentException.class)
  public void settersShouldNotAcceptBlank(BiConsumer<IrrelevantCharacter, String> setter, String value) {
    setter.accept(new IrrelevantCharacter(), value);
  }

  @Override
  public void setupTest() {
    _id = randomInt();
    _word = randomString();
  }

  @Override
  public IrrelevantCharacter createModelWithRandomPopulatedFields() {
    IrrelevantCharacter irrelevantCharacter = new IrrelevantCharacter();
    irrelevantCharacter.setId((long) randomInt());
    irrelevantCharacter.setWord(randomString());

    return irrelevantCharacter;
  }

  @Override
  public IrrelevantCharacter createModelWithFixedPopulatedFields() {
    IrrelevantCharacter irrelevantCharacter = new IrrelevantCharacter();
    irrelevantCharacter.setId(_id);
    irrelevantCharacter.setWord(_word);

    return irrelevantCharacter;
  }
}
