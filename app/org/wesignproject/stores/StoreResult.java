package org.wesignproject.stores;

import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Helper class handling result from data store
 */
public class StoreResult<T> {
  private final T _data;
  private final Status _status;

  public StoreResult(T data, @Nonnull Status status) {
    Preconditions.checkNotNull(status);
    if (status == Status.SUCCESS) {
      Preconditions.checkNotNull(data, "data must not be null if status=" + Status.SUCCESS);
    }
    _data = data;
    _status = status;
  }

  public T getDataOnSuccess() {
    Preconditions.checkState(_status == Status.SUCCESS);
    return _data;
  }

  public Status getStatus() {
    return _status;
  }

  public enum Status {
    SUCCESS, NOT_FOUND
  }

  public static <T> StoreResult<T> success(T data) {
    return new StoreResult<>(data, Status.SUCCESS);
  }

  public static <T> StoreResult<T> notFound() {
    return new StoreResult<>(null, Status.NOT_FOUND);
  }

  public boolean isSuccess() {
    return _status == Status.SUCCESS;
  }

  public boolean isNotFound() {
    return _status == Status.NOT_FOUND;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StoreResult<?> that = (StoreResult<?>) o;

    return new EqualsBuilder()
        .append(_data, that._data)
        .append(_status, that._status)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(37, 11)
        .append(_data)
        .append(_status)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("data", _data)
        .append("status", _status)
        .toString();
  }
}
