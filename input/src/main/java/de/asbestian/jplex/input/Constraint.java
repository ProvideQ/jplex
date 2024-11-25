package de.asbestian.jplex.input;

import de.asbestian.jplex.input.Term.TermBuilder;
import java.util.Arrays;
import java.util.Objects;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

/**
 * @author Sebastian Schenker
 */
public record Constraint(String name, int lineNumber, ImmutableList<Term> terms, ConstraintSense sense, double rhs) {

  public enum ConstraintSense {
    LE("<="),
    EQ("="),
    GE(">=");

    public static ConstraintSense from(final String rep) {
      return Arrays.stream(values())
          .filter(sense -> sense.representation.equals(rep)).findFirst().orElseThrow();
    }

    ConstraintSense(final String representation) {
      this.representation = representation;
    }

    final String representation;
  }

  public Constraint {
    Objects.requireNonNull(name);
    if (lineNumber <= 0) {
      throw new InputException("Expected positive line number.");
    }
    Objects.requireNonNull(sense);
    if (terms.isEmpty()) {
      throw new InputException("Expected non-empty coefficient map.");
    }
  }

  public static final class ConstraintBuilder {

    private String name = null;
    private final MutableList<Term.TermBuilder> terms = Lists.mutable.empty();
    private Integer lineNumber = null;
    private ConstraintSense sense = null;
    private Double rhs = null;

    public ConstraintBuilder setName(final String value) {
      name = value;
      return this;
    }

    public ConstraintBuilder setLineNumber(final int value) {
      lineNumber = value;
      return this;
    }

    public ConstraintBuilder addTerms(final ImmutableList<TermBuilder> newTerms) {
      terms.addAll(newTerms.castToList());

      return this;
    }

    public ConstraintBuilder setSense(final ConstraintSense value) {
      sense = value;
      return this;
    }

    public ConstraintBuilder setRhs(final double value) {
      rhs = value;
      return this;
    }

    public Constraint build() {
      var builtTerms = TermBuilder.buildTerms(terms.toImmutable());
      return new Constraint(name, lineNumber, builtTerms, sense, rhs);
    }
  }

}
