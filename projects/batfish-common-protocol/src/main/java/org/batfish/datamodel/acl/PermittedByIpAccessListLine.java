package org.batfish.datamodel.acl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.batfish.datamodel.FilterResult;
import org.batfish.datamodel.LineAction;

public final class PermittedByIpAccessListLine implements TerminalTraceEvent {

  private static final String PROP_INDEX = "index";

  private static final String PROP_LINE_DESCRIPTION = "lineDescription";

  private static final String PROP_NAME = "name";

  private static final long serialVersionUID = 1L;

  private final int _index;

  private final String _lineDescription;

  private final String _name;

  @JsonCreator
  public PermittedByIpAccessListLine(
      @JsonProperty(PROP_NAME) @Nonnull String name,
      @JsonProperty(PROP_INDEX) int index,
      @JsonProperty(PROP_LINE_DESCRIPTION) @Nonnull String lineDescription) {
    _name = name;
    _index = index;
    _lineDescription = lineDescription;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PermittedByIpAccessListLine)) {
      return false;
    }
    PermittedByIpAccessListLine rhs = (PermittedByIpAccessListLine) obj;
    return _index == rhs._index
        && _lineDescription.equals(rhs._lineDescription)
        && _name.equals(rhs._name);
  }

  @JsonProperty(PROP_INDEX)
  public int getIndex() {
    return _index;
  }

  @JsonProperty(PROP_LINE_DESCRIPTION)
  public @Nonnull String getLineDescription() {
    return _lineDescription;
  }

  @JsonProperty(PROP_NAME)
  public @Nonnull String getName() {
    return _name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_index, _lineDescription, _name);
  }

  @Override
  public FilterResult toFilterResult() {
    return new FilterResult(_index, LineAction.ACCEPT);
  }

  @Override
  public String toString() {
    return String.format("Permitted by ACL '%s', index %d: %s", _name, _index, _lineDescription);
  }
}
