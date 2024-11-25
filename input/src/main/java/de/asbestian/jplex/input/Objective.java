package de.asbestian.jplex.input;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

/**
 * @author Sebastian Schenker
 */
public record Objective(String name, ObjectiveSense sense, ImmutableList<Term> terms) {

  public enum ObjectiveSense {
    MAX(Lists.immutable.of("max", "maximise", "maximize", "maximum")),
    MIN(Lists.immutable.of("min", "minimise", "minimize", "minimum")),
    UNDEF(Lists.immutable.empty());

    Stream<String> rep() {
      return representation.stream();
    }

    ObjectiveSense(final ImmutableList<String> values) {
      representation = values;
    }

    private final ImmutableList<String> representation;
  }

  public Objective {
    Objects.requireNonNull(name);
    Objects.requireNonNull(sense);
    // no check on coefficient map in order to allow zero objective
  }

  public Optional<Double> getTermCoefficient(final ImmutableList<Variable> variables) {
    return terms.stream()
        .filter(t -> Utility.variablesEqual(t.multiplicands(), variables))
        .findFirst()
        .map(Term::coefficient);
  }

  public Optional<Double> getTermCoefficient(final Variable variable) {
    return terms.stream()
        .filter(t -> Utility.variablesEqual(t.multiplicands(), Lists.immutable.of(variable)))
        .findFirst()
        .map(Term::coefficient);
  }

  public static final class ObjectiveBuilder {
    private String name = null;
    private ObjectiveSense sense = null;
    private final MutableList<Term> terms = Lists.mutable.empty();

    public ObjectiveBuilder setName(final String value) {
      name = value;
      return this;
    }

    public ObjectiveBuilder setSense(final ObjectiveSense value) {
      sense = value;
      return this;
    }

    public ObjectiveBuilder addTerms(final ImmutableList<Term> newTerms) {
      for (Term newTerm : newTerms) {
        Utility.mergeTerm(terms, newTerm);
      }

      return this;
    }

    public Objective build() {
      return new Objective(name, sense, terms.toImmutable());
    }

  }

}
