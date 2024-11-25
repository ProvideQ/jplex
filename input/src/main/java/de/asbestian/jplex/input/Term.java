package de.asbestian.jplex.input;

import java.util.Objects;
import java.util.stream.Collectors;
import org.eclipse.collections.api.list.ImmutableList;

public record Term(double coefficient, ImmutableList<Variable> multiplicands) {
  public Term {
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

    Term that = (Term) o;
    return coefficient == that.coefficient
        && Utility.variablesEqual(multiplicands, that.multiplicands);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getUniqueString());
  }

  private String getUniqueString() {
    return coefficient + " * " +
        multiplicands.stream().map(Variable::name).collect(Collectors.joining(" * "));
  }
}
