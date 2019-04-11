package fling.grammars;

import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fling.grammars.api.BNFAST;
import fling.grammars.api.BNFAST.ConcreteDerivation;
import fling.grammars.api.BNFAST.Rule;
import fling.grammars.api.BNFAST.SpecializationRule;
import fling.grammars.api.BNFAST.Specification;
import fling.internal.grammar.sententials.Constants;
import fling.internal.grammar.sententials.DerivationRule;
import fling.internal.grammar.sententials.Notation;
import fling.internal.grammar.sententials.SententialForm;
import fling.internal.grammar.sententials.Symbol;
import fling.internal.grammar.sententials.Variable;
import fling.internal.grammar.sententials.Verb;
import fling.internal.grammar.types.TypeParameter;

public class BNF {
  public final Set<DerivationRule> R;
  public final Set<Symbol> nullables;
  public final Map<Symbol, Set<Verb>> firsts;
  public final Map<Variable, Set<Verb>> follows;
  public final Set<Verb> Σ;
  public final Set<Variable> V;
  public final Variable startVariable;
  public final Set<Variable> headVariables;
  public final Map<Variable, Notation> extensionHeadsMapping;
  public final Set<Variable> extensionProducts;

  public BNF(Set<Verb> Σ, Set<? extends Variable> V, Set<DerivationRule> R, Variable startVariable, Set<Variable> headVariables,
      Map<Variable, Notation> extensionHeadsMapping, Set<Variable> extensionProducts, boolean addStartSymbolDerivationRules) {
    this.Σ = Σ;
    Σ.add(Constants.$$);
    this.V = new LinkedHashSet<>(V);
    this.R = R;
    if (addStartSymbolDerivationRules) {
      this.V.add(Constants.S);
      R.add(new DerivationRule(Constants.S, new ArrayList<>()));
      rhs(Constants.S).add(new SententialForm(startVariable));
    }
    this.headVariables = headVariables;
    this.startVariable = startVariable;
    this.extensionHeadsMapping = extensionHeadsMapping == null ? Collections.emptyMap() : extensionHeadsMapping;
    this.extensionProducts = extensionProducts == null ? Collections.emptySet() : extensionProducts;
    this.nullables = getNullables();
    this.firsts = getFirsts();
    this.follows = getFollows();
  }
  public Set<Symbol> symbols() {
    Set<Symbol> $ = new LinkedHashSet<>();
    $.addAll(Σ);
    $.addAll(V);
    return unmodifiableSet($);
  }
  public List<SententialForm> rhs(Variable v) {
    return R.stream().filter(r -> r.lhs.equals(v)).findFirst().map(DerivationRule::rhs).orElse(null);
  }
  public boolean isNullable(Symbol... symbols) {
    return isNullable(Arrays.asList(symbols));
  }
  public boolean isNullable(List<Symbol> symbols) {
    return symbols.stream().allMatch(symbol -> nullables.contains(symbol) || //
        symbol.isNotation() && symbol.asNotation().isNullable(this::isNullable));
  }
  public Set<Verb> firsts(Symbol... symbols) {
    return firsts(Arrays.asList(symbols));
  }
  public Set<Verb> firsts(Collection<Symbol> symbols) {
    Set<Verb> $ = new LinkedHashSet<>();
    for (Symbol s : symbols) {
      $.addAll(firsts.get(s));
      if (!isNullable(s))
        break;
    }
    return unmodifiableSet($);
  }
  public BNF reachableSubBNF() {
    Set<DerivationRule> subR = new LinkedHashSet<>();
    Set<Verb> subΣ = new LinkedHashSet<>();
    Set<Variable> subV = new LinkedHashSet<>();
    Set<Variable> newSubV = new LinkedHashSet<>();
    newSubV.add(startVariable);
    int previousCount = -1;
    while (previousCount < subV.size()) {
      previousCount = subV.size();
      Set<Variable> newestSubV = new LinkedHashSet<>();
      for (DerivationRule rule : R) {
        if (!newSubV.contains(rule.lhs))
          continue;
        subR.add(rule);
        for (SententialForm sf : rule.rhs)
          for (Symbol symbol : sf)
            if (symbol.isVerb())
              subΣ.add(symbol.asVerb());
            else if (symbol.isVariable())
              newestSubV.add(symbol.asVariable());
            else
              throw new RuntimeException("problem while analyzing BNF");
      }
      subV.addAll(newSubV);
      newSubV = newestSubV;
    }
    return new BNF(subΣ, subV, subR, startVariable, null, null, null, true);
  }
  public boolean isOriginalVariable(Symbol symbol) {
    return symbol.isVariable() && !extensionProducts.contains(symbol);
  }
  private Set<Symbol> getNullables() {
    Set<Symbol> $ = new LinkedHashSet<>();
    for (; $.addAll(V.stream() //
        .filter(v -> rhs(v).stream() //
            .anyMatch(sf -> sf.stream().allMatch(symbol -> isNullable(symbol, $)))) //
        .collect(toSet()));)
      ;
    return $;
  }
  private boolean isNullable(Symbol symbol, Set<Symbol> knownNullables) {
    if (symbol.isVerb())
      return false;
    if (symbol.isVariable())
      return knownNullables.contains(symbol);
    if (symbol.isNotation())
      return symbol.asNotation().isNullable(s -> isNullable(s, knownNullables));
    throw new RuntimeException("problem while analyzing BNF");
  }
  private Map<Symbol, Set<Verb>> getFirsts() {
    Map<Symbol, Set<Verb>> $ = new LinkedHashMap<>();
    Σ.forEach(σ -> $.put(σ, singleton(σ)));
    V.forEach(v -> $.put(v, new LinkedHashSet<>()));
    for (boolean changed = true; changed;) {
      changed = false;
      for (Variable v : V)
        for (SententialForm sf : rhs(v))
          for (Symbol symbol : sf) {
            changed |= $.get(v).addAll(!symbol.isNotation() ? $.get(symbol) : //
                symbol.asNotation().getFirsts($::get));
            if (!isNullable(symbol))
              break;
          }
    }
    V.forEach(v -> $.put(v, unmodifiableSet($.get(v))));
    return unmodifiableMap($);
  }
  private Map<Variable, Set<Verb>> getFollows() {
    Map<Variable, Set<Verb>> $ = new LinkedHashMap<>();
    V.forEach(v -> $.put(v, new LinkedHashSet<>()));
    $.get(Constants.S).add(Constants.$$);
    for (boolean changed = true; changed;) {
      changed = false;
      for (Variable v : V)
        for (SententialForm sf : rhs(v))
          for (int i = 0; i < sf.size(); ++i) {
            if (!sf.get(i).isVariable())
              continue;
            Variable current = sf.get(i).asVariable();
            List<Symbol> rest = sf.subList(i, sf.size());
            changed |= $.get(current).addAll(firsts(rest));
            if (isNullable(rest))
              changed |= $.get(v).addAll($.get(current));
          }
    }
    V.forEach(s -> $.put(s, unmodifiableSet($.get(s))));
    return unmodifiableMap($);
  }
  public static BNF toBNF(Specification specification) {
    Builder $ = new Builder();
    $.start(specification.start);
    for (Rule rule : specification.rule)
      if (rule instanceof BNFAST.DerivationRule) {
        // Derivation rule.
        BNFAST.DerivationRule derivationRule = (BNFAST.DerivationRule) rule;
        if (derivationRule.derivationTarget instanceof ConcreteDerivation)
          // Concrete derivation rule.
          $.derive(derivationRule.derive).to(((ConcreteDerivation) derivationRule.derivationTarget).to);
        else
          // Epsilon derivation rule.
          $.derive(derivationRule.derive).toEpsilon();
      } else {
        // Specialization rule.
        SpecializationRule specializationRule = (SpecializationRule) rule;
        $.specialize(specializationRule.specialize).into(specializationRule.into);
      }
    return $.build();
  }

  @Deprecated private static class Builder {
    private final Set<Verb> Σ;
    private final Set<Variable> V;
    private final Set<DerivationRule> R;
    private Variable start;
    private final Set<Variable> heads;

    public Builder() {
      this.Σ = new LinkedHashSet<>();
      this.V = new LinkedHashSet<>();
      this.R = new LinkedHashSet<>();
      this.heads = new LinkedHashSet<>();
    }
    public Derive derive(Variable lhs) {
      processSymbol(lhs);
      return new Derive(lhs);
    }
    public Specialize specialize(Variable lhs) {
      return new Specialize(lhs);
    }
    void processSymbol(Symbol symbol) {
      assert !symbol.isTerminal();
      if (symbol.isVerb()) {
        Σ.add(symbol.asVerb());
        symbol.asVerb().parameters.stream() //
            .map(TypeParameter::declaredHeadVariables) //
            .forEach(heads::addAll);
      } else if (symbol.isNotation())
        symbol.asNotation().abbreviatedSymbols().forEach(this::processSymbol);
      else if (symbol.isVariable()) {
        Variable variable = symbol.asVariable();
        if (!V.contains(variable)) {
          V.add(variable);
          R.add(new DerivationRule(variable, new ArrayList<>()));
        }
      }
    }
    public final Builder start(Variable startVariable) {
      start = startVariable;
      return this;
    }
    public BNF build() {
      assert start != null : "declare a start variable";
      return new BNF(Σ, V, R, start, heads, null, null, true);
    }
    List<SententialForm> rhs(Variable v) {
      return R.stream().filter(r -> r.lhs.equals(v)).findFirst().map(DerivationRule::rhs).orElse(null);
    }

    public class Derive {
      private final Variable lhs;

      public Derive(Variable lhs) {
        this.lhs = lhs;
      }
      public Builder to(Symbol... sententialForm) {
        SententialForm processedSententialForm = new SententialForm(Arrays.stream(sententialForm) //
            .map(symbol -> {
              return !symbol.isTerminal() ? symbol : new Verb(symbol.asTerminal());
            }) //
            .collect(Collectors.toList()));
        processedSententialForm.forEach(Builder.this::processSymbol);
        rhs(lhs).add(processedSententialForm);
        return Builder.this;
      }
      public Builder toEpsilon() {
        SententialForm processedSententialForm = new SententialForm();
        rhs(lhs).add(processedSententialForm);
        return Builder.this;
      }
    }

    public class Specialize {
      private final Variable lhs;

      public Specialize(Variable lhs) {
        this.lhs = lhs;
      }
      public Builder into(Variable... variables) {
        for (Variable variable : variables) {
          SententialForm processedSententialForm = new SententialForm(variable);
          processedSententialForm.forEach(Builder.this::processSymbol);
          rhs(lhs).add(processedSententialForm);
        }
        return Builder.this;
      }
    }
  }
}
