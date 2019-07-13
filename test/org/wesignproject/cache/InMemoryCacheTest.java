package org.wesignproject.cache;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class InMemoryCacheTest {
  private static final String CACHE_NAME = "test";
  private static final String ID_1 = "id1";
  private static final String ID_2 = "id2";
  private static final String ID_NOT_PRESENT = "not present";
  private static final String VALUE_1 = "value 1";
  private static final String VALUE_2 = "value 2";

  private InMemoryCache<String> _cacheUnderTest;

  @BeforeMethod
  public void setup() {
    _cacheUnderTest = new InMemoryCache<>(CACHE_NAME, 0);
    _cacheUnderTest.invalidate(_cacheUnderTest.getAllPresent().keySet());
    assertEquals(_cacheUnderTest.size(), 0);
  }

  @DataProvider
  public Object[][] getResults() {
    return new Object[][] {
        {ID_1, Optional.of(VALUE_1)},
        {ID_NOT_PRESENT, Optional.empty()}
    };
  }

  @Test (dataProvider = "getResults")
  public void testPutAndGetIfPresent(String id, Optional<String> maybeExpectedValue) {
    maybeExpectedValue.ifPresent(value -> _cacheUnderTest.put(id, value));
    assertEquals(_cacheUnderTest.getIfPresent(id), maybeExpectedValue);
  }

  @Test
  public void testGetAllPresentAndSize() {
    _cacheUnderTest.put(ID_1, VALUE_1);
    _cacheUnderTest.put(ID_2, VALUE_2);

    Map<String, String> expectedMap = ImmutableMap.of(ID_1, VALUE_1, ID_2, VALUE_2);
    assertEquals(_cacheUnderTest.getAllPresent(), expectedMap);
    assertEquals(_cacheUnderTest.size(), 2);
  }

  @Test
  public void testInvalidate() {
    _cacheUnderTest.put(ID_1, VALUE_1);
    assertEquals(_cacheUnderTest.size(), 1);

    _cacheUnderTest.invalidate(Collections.singleton(ID_1));
    assertEquals(_cacheUnderTest.size(), 0);
  }

  @Test
  public void testTtl() throws InterruptedException {
    InMemoryCache<String> cacheWithTtl = new InMemoryCache<>("timeout", 1);
    cacheWithTtl.put(ID_1, VALUE_1);
    assertEquals(cacheWithTtl.size(), 1);
    Thread.sleep(2000);
    assertEquals(cacheWithTtl.getIfPresent(ID_1), Optional.empty());
  }
}