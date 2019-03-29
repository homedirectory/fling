package fling.grammar;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fling.compiler.Assignment;
import fling.compiler.ast.ASTParserCompiler;
import fling.grammar.sententials.Constants;
import fling.grammar.sententials.Symbol;
import fling.grammar.sententials.Terminal;
import fling.grammar.sententials.Variable;
import fling.grammar.sententials.Verb;
import fling.grammar.types.TypeParameter;
import fling.namers.NaiveNamer;
import fling.util.Collections;

public class LL1JavaASTParserCompiler<Σ extends Enum<Σ> & Terminal> implements ASTParserCompiler {
  @SuppressWarnings("rawtypes") private static final Class<? extends List> inputClass = List.class;
  private final BNF bnf;
  private final Class<Σ> Σ;
  private final String astClassesContainerName;
  private final String packageName;
  private final String className;

  public LL1JavaASTParserCompiler(BNF bnf, Class<Σ> Σ, String astClassesContainerName, String packageName, String className) {
    // TODO support multiple start variables.
    assert bnf.startVariables.size() == 1;
    this.bnf = bnf;
    this.Σ = Σ;
    this.astClassesContainerName = astClassesContainerName;
    this.packageName = packageName;
    this.className = className;
  }
  @Override public String printParserClass() {
    return String.format("package %s;@SuppressWarnings(\"all\")public interface %s{%s}", //
        packageName, //
        className, //
        bnf.V.stream() //
            .filter(v -> !Constants.S.equals(v)) //
            .map(this::printParserVariableCompilerMethod) //
            .collect(joining()));
  }
  @Override public String topClassName() {
    return getClassForVariable(bnf.startVariables.stream().findAny().get());
  }
  @Override public String getTopClassParsingMethodName() {
    return String.format("%s.%s.parse_%s", //
        packageName, //
        className, //
        bnf.startVariables.stream().findAny().get().name());
  }
  private String printParserVariableCompilerMethod(Variable v) {
    return String.format("public static %s parse_%s(%s<%s> w){%s}", //
        getClassForVariable(v), //
        v.name(), //
        inputClass.getCanonicalName(), //
        Assignment.class.getCanonicalName(), //
        Grammar.isSequenceRHS(bnf.rhs(v)) ? //
            printConcreteChildMethodBody(v) : //
            printAbstractParentMethodBody(v));
  }
  private Object printAbstractParentMethodBody(Variable v) {
    List<Variable> children = bnf.rhs(v).stream() //
        .map(sf -> sf.get(0)) //
        .map(Symbol::asVariable) //
        .collect(toList());
    Optional<Variable> optionalNullableChild = children.stream() //
        .filter(bnf::isNullable) //
        .findAny();
    StringBuilder body = new StringBuilder();
    if (bnf.isNullable(v))
      // Nullable child.
      body.append(String.format("if(w.isEmpty())return parse_%s(w);", //
          optionalNullableChild.get().name()));
    // Read input letter:
    body.append(Assignment.class.getCanonicalName() + " a = w.get(0);");
    // Diverge by firsts sets:
    children.stream() //
        .filter(child -> !optionalNullableChild.isPresent() || !child.equals(optionalNullableChild.get())) //
        .map(child -> String.format("if(%s)return parse_%s(w);", //
            printTerminalInclusionCondition(bnf.firsts(child)), //
            child.name())) //
        .forEach(body::append);
    // Default to nullable child or unreachable null value:
    body.append(String.format("return %s;", !optionalNullableChild.isPresent() ? //
        "null" : //
        String.format("parse_%s(w)", optionalNullableChild.get().name())));
    return body.toString();
  }
  private String printConcreteChildMethodBody(Variable v) {
    List<Symbol> children = bnf.rhs(v).get(0);
    StringBuilder body = new StringBuilder();
    body.append(Assignment.class.getCanonicalName() + " a;");
    Map<String, Integer> usedNames = new HashMap<>();
    List<String> argumentNames = new ArrayList<>();
    // Consume input as necessary:
    for (Symbol child : children) {
      // TODO support more complex structures.
      if (child.isVariable()) {
        String variableName = NaiveNamer.getNameFromBase(NaiveNamer.lowerCamelCase(child.name()), usedNames);
        body.append(String.format("%s %s=parse_%s(w);", //
            getClassForVariable(child.asVariable()), //
            variableName, //
            child.name()));
        argumentNames.add(variableName);
      } else if (child.isVerb()) {
        body.append("a=w.remove(0);");
        int index = 0;
        for (TypeParameter parameter : child.asVerb().parameters) {
          String variableName = NaiveNamer.getNameFromBase(parameter.baseParameterName(), usedNames);
          body.append(String.format("%s %s=(%s)a.arguments.get(%s);", //
              parameter.typeName(), //
              variableName, //
              parameter.typeName(), //
              index++));
          argumentNames.add(variableName);
        }
      }
    }
    // Compose abstract syntax node:
    body.append(String.format("return new %s(%s);", //
        getClassForVariable(v), //
        String.join(",", argumentNames)));
    return body.toString();
  }
  private String printTerminalInclusionCondition(Set<Verb> firsts) {
    return String.format("%s.included(a.σ,%s)", //
        Collections.class.getCanonicalName(), //
        firsts.stream() //
            .map(terminal -> String.format("%s.%s", //
                Σ.getCanonicalName(), //
                terminal.name())) //
            .collect(joining(",")));
  }
  private String getClassForVariable(Variable v) {
    return String.format("%s.%s.%s", //
        packageName, //
        astClassesContainerName, //
        v.name());
  }
}
