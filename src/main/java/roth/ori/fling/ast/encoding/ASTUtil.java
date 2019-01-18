package roth.ori.fling.ast.encoding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import roth.ori.fling.bnf.EBNF;
import roth.ori.fling.symbols.NonTerminal;
import roth.ori.fling.symbols.SpecialSymbols;
import roth.ori.fling.symbols.GrammarElement;
import roth.ori.fling.symbols.Verb;
import roth.ori.fling.util.DAG;

public class ASTUtil {
  public static Map<NonTerminal, Set<List<GrammarElement>>> normalize(EBNF ebnf, DAG<NonTerminal> inheritance,
      Function<NonTerminal, NonTerminal> producer) {
    return sortRules(ebnf.normalizedForm(producer), inheritance);
  }
  private static Map<NonTerminal, Set<List<GrammarElement>>> sortRules(Map<NonTerminal, Set<List<GrammarElement>>> orig,
      DAG<NonTerminal> inheritance) {
    clearAugSRules(orig);
    inheritance.clear();
    for (Entry<NonTerminal, Set<List<GrammarElement>>> e : orig.entrySet())
      if (isInheritanceRule(e.getValue()))
        for (List<GrammarElement> rhs : e.getValue())
          for (GrammarElement s : rhs)
            if (s.isNonTerminal()) {
              inheritance.initialize((NonTerminal) s);
              inheritance.add((NonTerminal) s, e.getKey());
            }
    Map<NonTerminal, Set<List<GrammarElement>>> $ = new LinkedHashMap<>(), remain = new LinkedHashMap<>(orig);
    orig.keySet().stream().filter(x -> !inheritance.containsKey(x)).forEach(x -> {
      $.put(x, orig.get(x));
      remain.remove(x);
    });
    while (!remain.isEmpty()) {
      remain.entrySet().stream()
          .filter(
              e -> e.getValue().stream().allMatch(c -> c.stream().allMatch(s -> (!(s instanceof NonTerminal) || $.containsKey(s)))))
          .forEach(e -> $.put(e.getKey(), e.getValue()));
      $.keySet().forEach(x -> remain.remove(x));
    }
    return $;
  }
  @SuppressWarnings("unused") private static void clearEmptyRules(Map<NonTerminal, Set<List<GrammarElement>>> rs) {
    List<GrammarElement> tbr = new ArrayList<>();
    rs.keySet().stream().forEach(k -> //
    rs.get(k).stream().forEach(c -> //
    c.stream().filter(l -> //
    l.isVerb() && ((Verb) l).type.length == 0).forEach(e -> tbr.add(e))));
    rs.values().stream().forEach(r -> r.stream().forEach(c -> c.removeAll(tbr)));
  }
  private static void clearAugSRules(Map<NonTerminal, Set<List<GrammarElement>>> rs) {
    rs.remove(SpecialSymbols.augmentedStartSymbol);
  }
  public static boolean isInheritanceRule(Set<List<GrammarElement>> rhs) {
    return rhs.size() > 1;
  }
  public static String capital(String s) {
    if (s == null)
      throw new IllegalArgumentException("Should not capitalize null String");
    if (s.length() == 0)
      return s;
    return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
  }
}
