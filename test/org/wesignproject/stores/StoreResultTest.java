package org.wesignproject.stores;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;


public class StoreResultTest {
  private static final String CONSTANT = "CONSTANT";

  @Test (expectedExceptions = NullPointerException.class)
  public void testStoreResultCannotBeEmptyOnSuccess() throws Exception {
    new StoreResult<>(null, StoreResult.Status.SUCCESS);
  }

  @Test (expectedExceptions = IllegalStateException.class)
  public void testCanOnlyGetDataOnSuccess() throws Exception {
    StoreResult<Object> resultNotFound = new StoreResult<>(null, StoreResult.Status.NOT_FOUND);
    resultNotFound.getDataOnSuccess();
  }

  @Test
  public void testNotFoundStaticFactory() throws Exception {
    StoreResult resultNotFound = StoreResult.notFound();
    assertEquals(resultNotFound.getStatus(), StoreResult.Status.NOT_FOUND);
  }

  @Test
  public void testGetDataOnSuccess() {
    final String data = "data";
    StoreResult<String> storeResult = new StoreResult<>(data, StoreResult.Status.SUCCESS);
    assertEquals(storeResult.getDataOnSuccess(), data);
  }

  @Test
  public void testEqualsAndHashcode() {
    StoreResult<String> lhs = StoreResult.success(CONSTANT);
    StoreResult<String> rhs = StoreResult.success(CONSTANT);
    assertEquals(lhs, rhs);
    assertEquals(lhs.hashCode(), rhs.hashCode());
    assertNotEquals(lhs, StoreResult.success("Other"));
    assertNotEquals(lhs, StoreResult.notFound());
  }
}