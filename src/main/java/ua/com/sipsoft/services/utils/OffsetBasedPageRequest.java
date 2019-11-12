package ua.com.sipsoft.services.utils;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * The Class OffsetBasedPageRequest.
 */
public class OffsetBasedPageRequest implements Pageable, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -25822477129613575L;

    /** The limit. */
    private int limit;

    /** The offset. */
    private int offset;

    /** The sort. */
    private final Sort sort;

    /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     * @param sort   can be {@literal null}.
     */
    public OffsetBasedPageRequest(int offset, int limit, Sort sort) {
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
     * @param direction  the direction of the {@link Sort} to be specified, can be
     *                   {@literal null}.
     * @param properties the properties to sort by, must not be {@literal null} or
     *                   empty.
     */
    public OffsetBasedPageRequest(int offset, int limit, Sort.Direction direction, String[] properties) {
	this(offset, limit, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
     *
     * @param offset zero-based offset.
     * @param limit  the size of the elements to be returned.
     */
    public OffsetBasedPageRequest(int offset, int limit) {
	this(offset, limit, new Sort(Sort.Direction.ASC, "id"));
    }

    /**
     * Gets the page number.
     *
     * @return the page number
     */
    @Override
    public int getPageNumber() {
	return offset / limit;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    @Override
    public int getPageSize() {
	return limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    @Override
    public long getOffset() {
	return offset;
    }

    /**
     * Gets the sort.
     *
     * @return the sort
     */
    @Override
    public Sort getSort() {
	return sort;
    }

    /**
     * Next.
     *
     * @return the pageable
     */
    @Override
    public Pageable next() {
	// Typecast possible because number of entries cannot be bigger than integer
	// (primary key is integer)
	return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()));
    }

    /**
     * Previous.
     *
     * @return the pageable
     */
    public Pageable previous() {
	// The integers are positive. Subtracting does not let them become bigger than
	// integer.
	return hasPrevious() ? new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize())) : this;
    }

    /**
     * Previous or first.
     *
     * @return the pageable
     */
    @Override
    public Pageable previousOrFirst() {
	return hasPrevious() ? previous() : first();
    }

    /**
     * First.
     *
     * @return the pageable
     */
    @Override
    public Pageable first() {
	return new OffsetBasedPageRequest(0, getPageSize(), getSort());
    }

    /**
     * Checks for previous.
     *
     * @return true, if successful
     */
    @Override
    public boolean hasPrevious() {
	return offset > limit;
    }

    /**
     * Equals.
     *
     * @param o the o
     * @return true, if successful
     */
    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;

	if (!(o instanceof OffsetBasedPageRequest))
	    return false;

	OffsetBasedPageRequest that = (OffsetBasedPageRequest) o;

	return new EqualsBuilder()
		.append(limit, that.limit)
		.append(offset, that.offset)
		.append(sort, that.sort)
		.isEquals();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
	return new HashCodeBuilder(17, 37)
		.append(limit)
		.append(offset)
		.append(sort)
		.toHashCode();
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return new ToStringBuilder(this)
		.append("limit", limit)
		.append("offset", offset)
		.append("sort", sort)
		.toString();
    }
}
