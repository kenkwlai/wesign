package org.wesignproject.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public abstract class ModelBaseTest<Model> {
  private static ModelValidator MODEL_VALIDATOR = new ModelValidator();
  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final int RANDOM_INTEGER_BOUND = 100;
  private static final String NO_MORE_RANDOM_INT_ERROR_MESSAGE =
      "No more available random integers. Reduce the usage or increase the bound if necessary";

  private Queue<Integer> _nextRandomInt = new LinkedList<>();
  private final Random _random = new Random();

  @BeforeClass
  public void setup() {
    List<Integer> integers = IntStream.range(0, RANDOM_INTEGER_BOUND).boxed().collect(Collectors.toList());
    Collections.shuffle(integers, _random);
    _nextRandomInt.addAll(integers);

    setupTest();
  }

  protected abstract void setupTest();

  protected abstract Model createModelWithRandomPopulatedFields();

  protected abstract Model createModelWithFixedPopulatedFields();

  @Test
  public void printSampleJson() throws JsonProcessingException {
    JsonNode jsonNode = OBJECT_MAPPER.valueToTree(createModelWithRandomPopulatedFields());
    String jsonString = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    System.out.println(jsonString);
  }

  @Test
  public void testAnnotationBasedModelValidation() {
    Model model = createModelWithRandomPopulatedFields();
    List<String> constraintViolations = MODEL_VALIDATOR.validateAndGetViolations(model);
    assertTrue(constraintViolations.isEmpty(), constraintViolations.toString());
  }

  @SuppressWarnings({"EqualsWithItself", "ObjectEqualsNull"})
  @Test
  public void testEquality() {
    Model model = createModelWithFixedPopulatedFields();
    Model sameModel = createModelWithFixedPopulatedFields();
    Model differentModel = createModelWithRandomPopulatedFields();

    assertNotNull(model, "Implement createModelWithFixedPopulatedFields()");
    assertNotNull(differentModel, "Implement createModelWithRandomPopulatedFields()");

    assertTrue(model.equals(model));
    assertTrue(model.equals(sameModel) && sameModel.equals(model));
    assertTrue(model.hashCode() == sameModel.hashCode());

    assertFalse(model.equals(null));
    assertFalse(model.equals(differentModel) || differentModel.equals(model));
    assertFalse(model.hashCode() == differentModel.hashCode());
  }

  protected int randomInt() {
    return Optional.ofNullable(_nextRandomInt.poll())
        .orElseThrow(() -> new IllegalStateException(NO_MORE_RANDOM_INT_ERROR_MESSAGE));
  }

  protected String randomString() {
    return randomString(10);
  }

  protected String randomString(int length) {
    return RandomStringUtils.randomAlphabetic(length);
  }

  protected List<Integer> randomList(){
    return randomList(10);
  }

  protected List<Integer> randomList(int length) {
    List<Integer> list = IntStream.range(0, length)
        .boxed()
        .map(x -> randomInt())
        .collect(Collectors.toList());
    Collections.shuffle(list);
    return list;
  }
}
