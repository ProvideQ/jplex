package de.asbestian.jplex.input;

import org.eclipse.collections.api.list.ImmutableList;

public class Utility {
  private Utility() {}

  public static boolean variablesEqual(ImmutableList<Variable> v1, ImmutableList<Variable> v2) {
    return v1.size() == v2.size()
        && v1.containsAll(v2.castToCollection())
        && v2.containsAll(v1.castToCollection());
  }
}
