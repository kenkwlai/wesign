package org.wesignproject.test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import play.mvc.Http;


public class TestUtils {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static Http.RequestBuilder loadJsonRequest(String filename) {
    try {
      InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(filename);
      String jsonString = IOUtils.toString(inputStream, Charset.defaultCharset());
      JsonNode jsonNode = MAPPER.readTree(jsonString);
      return new Http.RequestBuilder().bodyJson(jsonNode);
    } catch (Exception e) {
      throw new AssertionError("cannot load json request", e);
    }
  }

  public static <T> List<T> deserializeBodyToListOfClass(String responseBody, TypeReference<?> type) {
    List<T> listOfObject;
    try {
      listOfObject = MAPPER.readValue(responseBody, type);
    } catch (Exception e) {
      listOfObject = Collections.emptyList();
    }
    return listOfObject;
  }

  public static Optional<?> deserializeBodyToClass(String responseBody, Class<?> clazz) {
    Optional<?> object;
    try {
      object = Optional.of(MAPPER.readValue(responseBody, clazz));
    } catch (Exception e) {
      object = Optional.empty();
    }
    return object;
  }

  private TestUtils() {}
}
