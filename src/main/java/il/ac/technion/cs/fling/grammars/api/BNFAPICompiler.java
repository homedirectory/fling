package il.ac.technion.cs.fling.grammars.api;
import static il.ac.technion.cs.fling.grammars.api.BNFAPI.Σ.*;

import java.util.List;

import il.ac.technion.cs.fling.*;
import il.ac.technion.cs.fling.grammars.api.BNFAPIAST.*;

/**
 * {@link FancyEBNF} AST run-time compiler. Initially generated by Fling.
 * 
 * @author Ori Roth
 */
@SuppressWarnings("all") public interface BNFAPICompiler {
  public static PlainBNF parse_PlainBNF(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    _a = w.remove(0);
    Variable variable = (Variable) _a.arguments.get(0);
    _b = il.ac.technion.cs.fling.internal.grammar.sententials.notations.NoneOrMore.abbreviate(parse__Rule2(w), 1);
    List<Rule> rule = (List<Rule>) _b.get(0);
    return new PlainBNF(variable, rule);
  }
  public static Rule parse_Rule(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, derive))
      return parse_Rule1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, specialize))
      return parse_Rule2(w);
    return null;
  }
  public static RuleBody parse_RuleBody(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, to))
      return parse_RuleBody1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, toEpsilon))
      return parse_RuleBody2(w);
    return null;
  }
  public static RuleTail parse_RuleTail(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a = w.get(0);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, or))
      return parse_RuleTail1(w);
    if (il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, orNone))
      return parse_RuleTail2(w);
    return null;
  }
  public static Derivation parse_Rule1(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    Variable variable = (Variable) _a.arguments.get(0);
    RuleBody ruleBody = parse_RuleBody(w);
    return new Derivation(variable, ruleBody);
  }
  public static Specialization parse_Rule2(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    Variable variable = (Variable) _a.arguments.get(0);
    _a = w.remove(0);
    Variable[] variables = (Variable[]) _a.arguments.get(0);
    return new Specialization(variable, variables);
  }
  public static ConcreteDerivation parse_RuleBody1(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    Symbol[] symbols = (Symbol[]) _a.arguments.get(0);
    _b = il.ac.technion.cs.fling.internal.grammar.sententials.notations.NoneOrMore.abbreviate(parse__RuleTail2(w), 1);
    List<RuleTail> ruleTail = (List<RuleTail>) _b.get(0);
    return new ConcreteDerivation(symbols, ruleTail);
  }
  public static EpsilonDerivation parse_RuleBody2(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    return new EpsilonDerivation();
  }
  public static ConcreteDerivationTail parse_RuleTail1(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    Symbol[] symbols = (Symbol[]) _a.arguments.get(0);
    return new ConcreteDerivationTail(symbols);
  }
  public static EpsilonDerivationTail parse_RuleTail2(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<?> _b;
    _a = w.remove(0);
    return new EpsilonDerivationTail();
  }
  public static List<Object> parse__Rule2(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<Object> _b;
    if (w.isEmpty())
      return java.util.Collections.emptyList();
    _a = w.get(0);
    if (!(il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, derive, specialize)))
      return java.util.Collections.emptyList();
    Rule rule = parse_Rule(w);
    List<Object> _c = parse__Rule2(w);
    return java.util.Arrays.asList(rule, _c);
  }
  public static List<Object> parse__RuleTail2(List<il.ac.technion.cs.fling.internal.compiler.Assignment> w) {
    il.ac.technion.cs.fling.internal.compiler.Assignment _a;
    List<Object> _b;
    if (w.isEmpty())
      return java.util.Collections.emptyList();
    _a = w.get(0);
    if (!(il.ac.technion.cs.fling.internal.util.Is.included(_a.σ, or, orNone)))
      return java.util.Collections.emptyList();
    RuleTail ruleTail = parse_RuleTail(w);
    List<Object> _c = parse__RuleTail2(w);
    return java.util.Arrays.asList(ruleTail, _c);
  }
}
