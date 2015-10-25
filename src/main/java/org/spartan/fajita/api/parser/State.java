package org.spartan.fajita.api.parser;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.spartan.fajita.api.bnf.BNF;
import org.spartan.fajita.api.bnf.rules.DerivationRule;
import org.spartan.fajita.api.bnf.symbols.Symbol;
import org.spartan.fajita.api.bnf.symbols.Terminal;

public class State {
  private final Set<Item> items;
  private final BNF bnf;
  private final Map<Symbol, State> transitions;
  public final int index;
  public final String name;

  State(final Set<Item> items, final BNF bnf, final int stateIndex) {
    this.items = items;
    index = stateIndex;
    this.bnf = bnf;
    transitions = new HashMap<>();
    name = "Q" + stateIndex;
  }
  void addGotoTransition(final Symbol symbol, final State newState) {
    transitions.put(symbol, newState);
  }
  public boolean isLegalLookahead(final Symbol lookahead) {
    if (lookahead == Terminal.$)
      return getItems().stream().anyMatch(i -> i.readyToReduce() && bnf.getAugmentedStartSymbol().equals(i.rule.lhs));
    return transitions.containsKey(lookahead) || getItems().stream().anyMatch(item -> item.isLegalLookahead(lookahead));
  }
  public Set<Symbol> allLegalLookaheads() {
    return transitions.keySet();
  }
  @Override public String toString() {
    return name + ":" + System.lineSeparator() + extentedToString();
    // return name;
  }
  @SuppressWarnings("boxing") public String extentedToString() {
    String $ = "";
    Set<SimpleEntry<DerivationRule, Integer>> noLookaheads = getItems().stream()
        .map(item -> new SimpleEntry<>(item.rule, item.dotIndex)).distinct().collect(Collectors.toSet());
    for (SimpleEntry<DerivationRule, Integer> item : noLookaheads) {
      List<Item> matching = getItems().stream()
          .filter(i -> i.rule.equals(item.getKey()) && i.dotIndex == item.getValue().intValue()).collect(Collectors.toList());
      for (int i = 0; i < matching.size(); i++) {
        Item match = matching.get(i);
        if (i == 0)
          $ += "[" + match.toString();
        else
          $ += "/" + match.lookahead.methodSignatureString();
      }
      $ += "]" + System.lineSeparator();
    }
    return $;
  }
  public String compactToString() {
    String $ = "{";
    for (Item item : getItems().stream().filter(item -> (item.dotIndex != 0 || bnf.getAugmentedStartSymbol() == item.rule.lhs))
        .collect(Collectors.toList()))
      $ += item.toString() + ",";
    return $ + "}";
  }
  @Override public boolean equals(final Object obj) {
    if (obj.getClass() != State.class)
      return false;
    State s = (State) obj;
    return bnf.equals(s.bnf) && s.getItems().equals(getItems());
  }
  /**
   * @param lookahead
   *          - the transition symbol
   * @return the index of the next state or -1 if the transition does not exist.
   * 
   */
  public State goTo(final Symbol lookahead) {
    return transitions.getOrDefault(lookahead, null);
  }
  @Override public int hashCode() {
    return getItems().hashCode() + bnf.hashCode();
  }
  public Set<Item> getItems() {
    return items;
  }
}
