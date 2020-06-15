package il.ac.technion.cs.fling.adapters;

import java.util.Date;
import java.util.List;
import java.util.Set;

import il.ac.technion.cs.fling.internal.compiler.Namer;
import il.ac.technion.cs.fling.internal.compiler.api.dom.Model;
import il.ac.technion.cs.fling.internal.compiler.api.dom.Type;
import il.ac.technion.cs.fling.internal.compiler.api.dom.TypeSignature;
import il.ac.technion.cs.fling.internal.compiler.api.dom.Method;
import il.ac.technion.cs.fling.internal.compiler.api.dom.MethodSignature;
import il.ac.technion.cs.fling.internal.compiler.api.dom.SkeletonType;
import il.ac.technion.cs.fling.internal.compiler.api.dom.TypeName;
import il.ac.technion.cs.fling.internal.grammar.rules.Named;
import il.ac.technion.cs.fling.internal.grammar.rules.Word;

public abstract class APIGenerator {
  public final String bottomName;
  final String endName;
  final Namer namer;
  public final String topName;

  protected APIGenerator(final Namer namer) {
    this(namer, "$");
  }

  APIGenerator(final Namer namer, final String endName) {
    this(namer, endName, "BOTTOM", "TOP");
  }

  protected APIGenerator(final Namer namer, final String endName, final String bottomName, final String topName) {
    this.namer = namer;
    this.endName = endName;
    this.bottomName = bottomName;
    this.topName = topName;
  }

  protected abstract String comment(String text);

  public abstract String render(Model m);

  abstract String render(TypeSignature declaration);

  public abstract String render(TypeSignature declaration, List<Method> methods);

  final String render(final Method m) {
    return m.render(this);
  }

  public abstract String render(MethodSignature s, SkeletonType t);

  abstract String render(Named q, Word<Named> α, Set<Named> legalJumps);

  public final String render(final SkeletonType t) {
    return t.render(this);
  }

  public abstract String render(TypeName name);

  public abstract String render(TypeName name, List<SkeletonType> typeArguments);

  final String render(final Type i) {
    return i.render(this);
  }

  public abstract String renderInterfaceBottom();

  public abstract String renderInterfaceTop();

  public abstract String renderMethod(MethodSignature s, SkeletonType t);

  public abstract String renderTerminationMethod();

  final String startComment() {
    return comment(initialComment());
  }

  private static String initialComment() {
    return String.format("This file was automatically generated by Fling (c) on %s ", new Date());
  }
}