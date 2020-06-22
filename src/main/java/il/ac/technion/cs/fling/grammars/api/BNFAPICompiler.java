package il.ac.technion.cs.fling.grammars.api;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.derive;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.or;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.orNone;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.specialize;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.to;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.toEpsilon;
import java.util.List;
import il.ac.technion.cs.fling.FancyEBNF;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.ConcreteDerivation;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.ConcreteDerivationTail;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.Derivation;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.EpsilonDerivation;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.EpsilonDerivationTail;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.PlainBNF;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.Rule;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.RuleBody;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.RuleTail;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.Specialization;
import il.ac.technion.cs.fling.internal.compiler.Invocation;
import il.ac.technion.cs.fling.internal.grammar.rules.TempComponent;
import il.ac.technion.cs.fling.internal.grammar.rules.Variable;
/** {@link FancyEBNF} AST run-time compiler. Initially generated by Fling.
 *
 * @author Ori Roth */
@SuppressWarnings("all") public interface BNFAPICompiler {
  static PlainBNF parse_PlainBNF(final List<Invocation> w) {
    Invocation _a;
    List<?> _b;
    _a = w.remove(0);
    _a = w.remove(0);
    final Variable variable = (Variable) _a.arguments.get(0);
    _b = il.ac.technion.cs.fling.internal.grammar.rules.NoneOrMore.abbreviate(parse__Rule2(w), 1);
    final List<Rule> rule = (List<Rule>) _b.get(0);
    return new PlainBNF(variable, rule);
  }
  static Rule parse_Rule(final List<Invocation> w) {
    final Invocation _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, derive))
      return parse_Rule1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, specialize))
      return parse_Rule2(w);
    return null;
  }
  static RuleBody parse_RuleBody(final List<Invocation> w) {
    final Invocation _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, to))
      return parse_RuleBody1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, toEpsilon))
      return parse_RuleBody2(w);
    return null;
  }
  static RuleTail parse_RuleTail(final List<Invocation> w) {
    final Invocation _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, or))
      return parse_RuleTail1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, orNone))
      return parse_RuleTail2(w);
    return null;
  }
  static Derivation parse_Rule1(final List<Invocation> w) {
    Invocation _a;
    _a = w.remove(0);
    final Variable variable = (Variable) _a.arguments.get(0);
    final RuleBody ruleBody = parse_RuleBody(w);
    return new Derivation(variable, ruleBody);
  }
  static Specialization parse_Rule2(final List<Invocation> w) {
    Invocation _a;
    _a = w.remove(0);
    final Variable variable = (Variable) _a.arguments.get(0);
    _a = w.remove(0);
    final Variable[] variables = (Variable[]) _a.arguments.get(0);
    return new Specialization(variable, variables);
  }
  static ConcreteDerivation parse_RuleBody1(final List<Invocation> w) {
    Invocation _a;
    List<?> _b;
    _a = w.remove(0);
    final Object object = _a.arguments.get(0);
    assert object instanceof TempComponent[] : object;
    final TempComponent[] symbols = (TempComponent[]) object;
    _b = il.ac.technion.cs.fling.internal.grammar.rules.NoneOrMore.abbreviate(parse__RuleTail2(w), 1);
    final List<RuleTail> ruleTail = (List<RuleTail>) _b.get(0);
    return new ConcreteDerivation(symbols, ruleTail);
  }
  static EpsilonDerivation parse_RuleBody2(final List<Invocation> w) {
    w.remove(0);
    return new EpsilonDerivation();
  }
  static ConcreteDerivationTail parse_RuleTail1(final List<Invocation> w) {
    Invocation _a;
    _a = w.remove(0);
    final TempComponent[] cs = (TempComponent[]) _a.arguments.get(0);
    return new ConcreteDerivationTail(cs);
  }
  static EpsilonDerivationTail parse_RuleTail2(final List<Invocation> w) {
    w.remove(0);
    return new EpsilonDerivationTail();
  }
  static List<Object> parse__Rule2(final List<Invocation> w) {
    Invocation _a;
    if (w.isEmpty())
      return java.util.Collections.emptyList();
    _a = w.get(0);
    if (!il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, derive, specialize))
      return java.util.Collections.emptyList();
    final Rule rule = parse_Rule(w);
    final List<Object> _c = parse__Rule2(w);
    return java.util.Arrays.asList(rule, _c);
  }
  static List<Object> parse__RuleTail2(final List<Invocation> w) {
    Invocation _a;
    if (w.isEmpty())
      return java.util.Collections.emptyList();
    _a = w.get(0);
    if (!il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, or, orNone))
      return java.util.Collections.emptyList();
    final RuleTail ruleTail = parse_RuleTail(w);
    final List<Object> _c = parse__RuleTail2(w);
    return java.util.Arrays.asList(ruleTail, _c);
  }
}
