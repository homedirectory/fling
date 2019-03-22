package fling.adapters;

import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import fling.compiler.api.APICompiler;
import fling.compiler.api.PolymorphicLanguageAPIAdapter;
import fling.compiler.api.nodes.APICompilationUnitNode;
import fling.compiler.api.nodes.AbstractMethodNode;
import fling.compiler.api.nodes.InterfaceNode;
import fling.compiler.api.nodes.PolymorphicTypeNode;
import fling.grammar.sententials.Named;
import fling.grammar.sententials.Terminal;
import fling.grammar.sententials.Word;

public class JavaAPIAdapter<Q extends Named, Σ extends Terminal, Γ extends Named>
    implements PolymorphicLanguageAPIAdapter<Q, Σ, Γ> {
  private final Collection<Σ> terminals;
  private final String packageName;
  private final String className;
  private final String startMethodName;
  private final String terminationMethodName;

  public JavaAPIAdapter(Collection<Σ> terminals, String packageName, String className, String startMethodName,
      String terminationMethodName) {
    this.terminals = terminals;
    this.startMethodName = startMethodName;
    this.terminationMethodName = terminationMethodName;
    this.packageName = packageName;
    this.className = className;
  }
  @Override public String printTopType() {
    return "$";
  }
  @Override public String printBotType() {
    return "ø";
  }
  @Override public String printIntermediateType(APICompiler<Q, Σ, Γ>.TypeName name) {
    return printTypeName(name);
  }
  @Override public String printIntermediateType(APICompiler<Q, Σ, Γ>.TypeName name,
      List<PolymorphicTypeNode<APICompiler<Q, Σ, Γ>.TypeName>> typeArguments) {
    return String.format("%s<%s>", //
        printTypeName(name), //
        typeArguments.stream().map(this::printType).collect(joining(",")));
  }
  @Override public String printStartMethod(PolymorphicTypeNode<APICompiler<Q, Σ, Γ>.TypeName> returnType) {
    return String.format("public static %s %s() {return new α();}", printType(returnType), startMethodName);
  }
  @Override public String printTerminationMethod() {
    return String.format("void %s();", terminationMethodName);
  }
  @Override public String printIntermediateMethod(APICompiler<Q, Σ, Γ>.MethodDeclaration declaration,
      PolymorphicTypeNode<APICompiler<Q, Σ, Γ>.TypeName> returnType) {
    return String.format("%s %s(%s);", //
        printType(returnType), //
        declaration.name.name(), //
        declaration.name.parameters().stream() //
            .map(parameter -> String.format("%s %s", parameter.typeName(), parameter.parameterName())) //
            .collect(joining(",")));
  }
  @Override public String printTopInterface() {
    return String.format("interface ${void %s();}", terminationMethodName);
  }
  @Override public String printBotInterface() {
    return "interface ø {}";
  }
  @Override public String printInterface(APICompiler<Q, Σ, Γ>.InterfaceDeclaration declaration,
      List<AbstractMethodNode<APICompiler<Q, Σ, Γ>.TypeName, APICompiler<Q, Σ, Γ>.MethodDeclaration>> methods) {
    return String.format("interface %s{%s}", //
        printInterfaceDeclaration(declaration), //
        methods.stream().map(this::printMethod).collect(joining()));
  }
  @Override public String printFluentAPI(
      APICompilationUnitNode<APICompiler<Q, Σ, Γ>.TypeName, APICompiler<Q, Σ, Γ>.MethodDeclaration, APICompiler<Q, Σ, Γ>.InterfaceDeclaration> fluentAPI) {
    return String.format("package %s;@SuppressWarnings(\"all\")public interface %s{%s%s%s}", //
        packageName, //
        className, //
        fluentAPI.startMethods.stream().map(this::printMethod).collect(joining()),
        fluentAPI.interfaces.stream().map(this::printInterface).collect(joining()),
        printConcreteImplementation(fluentAPI.interfaces));
  }
  public String printTypeName(APICompiler<Q, Σ, Γ>.TypeName name) {
    return printTypeName(name.q, name.α);
  }
  public String printTypeName(APICompiler<Q, Σ, Γ>.InterfaceDeclaration declaration) {
    return printTypeName(declaration.q, declaration.α);
  }
  public String printTypeName(Q q, Word<Γ> α) {
    return α == null ? q.name() : String.format("%s_%s", q.name(), α.stream().map(Named::name).collect(Collectors.joining()));
  }
  public String printInterfaceDeclaration(APICompiler<Q, Σ, Γ>.InterfaceDeclaration declaration) {
    return String.format("%s<%s>", printTypeName(declaration), //
        declaration.typeVariables.stream().map(Named::name).collect(Collectors.joining(",")));
  }
  public String printTypeName(
      InterfaceNode<APICompiler<Q, Σ, Γ>.TypeName, APICompiler<Q, Σ, Γ>.MethodDeclaration, APICompiler<Q, Σ, Γ>.InterfaceDeclaration> interfaze) {
    return interfaze.isTop() ? "$" : interfaze.isBot() ? "ø" : printTypeName(interfaze.declaration);
  }
  public String printConcreteImplementation(
      List<InterfaceNode<APICompiler<Q, Σ, Γ>.TypeName, APICompiler<Q, Σ, Γ>.MethodDeclaration, APICompiler<Q, Σ, Γ>.InterfaceDeclaration>> interfaces) {
    return String
        .format("static class α implements %s{%s%s}", interfaces.stream().map(this::printTypeName).collect(joining(",")), terminals
            .stream().map(t -> String.format("public α %s(%s){return this;}", //
                t.name(), //
                t.parameters().stream().map(parameter -> String.format("%s %s", parameter.typeName(), parameter.parameterName()))
                    .collect(joining(",")))) //
            .collect(joining()), "public void $(){}");
  }
}
