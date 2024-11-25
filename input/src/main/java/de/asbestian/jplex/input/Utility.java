package de.asbestian.jplex.input;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class Utility {
  private Utility() {}

  public static void mergeTerm(MutableList<Term> terms, Term newTerm) {
    terms.stream()
        .filter(term -> term.equals(newTerm))
        .findFirst()
        .ifPresentOrElse(
            term -> {
              final double sum = term.coefficient() + newTerm.coefficient();
              terms.set(terms.indexOf(term), new Term(sum, term.multiplicands()));
            },
            () -> terms.add(newTerm));
  }

  public static boolean variablesEqual(ImmutableList<Variable> v1, ImmutableList<Variable> v2) {
    return v1.size() == v2.size()
        && v1.containsAll(v2.castToCollection())
        && v2.containsAll(v1.castToCollection());
  }
}
