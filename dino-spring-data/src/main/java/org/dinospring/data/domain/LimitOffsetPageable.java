package org.dinospring.data.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class LimitOffsetPageable implements Pageable, Serializable {
  private static final long serialVersionUID = -25822477129613575L;

  private int limit;
  private long offset;
  private final Sort sort;

  /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     * @param sort   can be {@literal null}.
     */
  public LimitOffsetPageable(long offset, int limit, Sort sort) {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset index must not be less than zero!");
    }

    if (limit < 1) {
      throw new IllegalArgumentException("Limit must not be less than one!");
    }
    this.limit = limit;
    this.offset = offset;
    this.sort = sort;
  }

  /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset     zero-based offset.
     * @param limit      the size of the elements to be returned.
     * @param direction  the direction of the {@link Sort} to be specified, can be {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or empty.
     */
  public LimitOffsetPageable(long offset, int limit, Sort.Direction direction, String... properties) {
    this(offset, limit, Sort.by(direction, properties));
  }

  /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     */
  public LimitOffsetPageable(long offset, int limit) {
    this(offset, limit, Sort.unsorted());
  }

  @Override
  public int getPageNumber() {
    return (int) (offset / limit);
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new LimitOffsetPageable(getOffset() + getPageSize(), getPageSize(), getSort());
  }

  public LimitOffsetPageable previous() {
    return hasPrevious() ? new LimitOffsetPageable(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  @Override
  public Pageable first() {
    return new LimitOffsetPageable(0, getPageSize(), getSort());
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LimitOffsetPageable)) {
      return false;
    }

    LimitOffsetPageable that = (LimitOffsetPageable) o;

    return new EqualsBuilder().append(limit, that.limit).append(offset, that.offset).append(sort, that.sort).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(limit).append(offset).append(sort).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("limit", limit).append("offset", offset).append("sort", sort).toString();
  }

  @Override
  public Pageable withPage(int pageNumber) {
    return new LimitOffsetPageable(1L * pageNumber * getPageSize(), getPageSize(), getSort());
  }
}
