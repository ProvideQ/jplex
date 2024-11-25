package de.asbestian.jplex.input;

import java.util.Objects;
import java.util.stream.Collectors;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

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

  public static final class TermBuilder {
    private Double coefficient = 1.0;
    private final MutableList<Variable.VariableBuilder> multiplicands = Lists.mutable.empty();

    public TermBuilder setCoefficient(final double value) {
      coefficient = value;
      return this;
    }

    public TermBuilder addMultiplicands(
        final ImmutableList<Variable.VariableBuilder> newMultiplicands) {
      multiplicands.addAll(newMultiplicands.castToList());
      return this;
    }

    public TermBuilder addMultiplicand(final Variable.VariableBuilder multiplicand) {
      multiplicands.add(multiplicand);
      return this;
    }

    public Term build() {
      var terms = multiplicands.stream().map(Variable.VariableBuilder::build);
      return new Term(coefficient, Lists.immutable.fromStream(terms));
    }

    public static ImmutableList<Term> buildTerms(final ImmutableList<TermBuilder> termBuilders) {
      // Merge terms with same multiplicands
      MutableList<TermBuilder> mergedTerms = Lists.mutable.empty();
      for (var term : termBuilders) {
        term.mergeInto(mergedTerms);
      }

      return Lists.immutable.fromStream(mergedTerms.stream().map(TermBuilder::build));
    }

    private void mergeInto(MutableList<TermBuilder> terms) {
      terms.stream()
          .filter(term -> term.equals(this))
          .findFirst()
          .ifPresentOrElse(
              term -> term.setCoefficient(term.coefficient + coefficient),
              () -> terms.add(this));
    }
  }
}
