package de.asbestian.jplex.input;

import java.util.Objects;
import org.eclipse.collections.api.list.ImmutableList;

public record VariableIdentifier(
    ImmutableList<String> multiplicands) {
  public VariableIdentifier {
    if (multiplicands.isEmpty()) {
      throw new InputException("Expected non-empty list of multiplicands.");
    }
  }

  @Override
  public String toString() {
    return getUniqueString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VariableIdentifier that = (VariableIdentifier) o;
    return multiplicands.size() == that.multiplicands.size()
        && multiplicands.containsAll(that.multiplicands.castToCollection())
        && that.multiplicands.containsAll(multiplicands.castToCollection());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getUniqueString());
  }

  private String getUniqueString() {
    return multiplicands.makeString(" * ");
  }
}
