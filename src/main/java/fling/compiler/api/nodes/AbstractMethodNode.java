package fling.compiler.api.nodes;

public interface AbstractMethodNode<T, D> {
  public static class Start<T, D> implements AbstractMethodNode<T, D> {
    // TODO add declaration.
    public final PolymorphicTypeNode<T> returnType;

    public Start(PolymorphicTypeNode<T> returnType) {
      this.returnType = returnType;
    }
  }

  public static class Termination<T, D> implements AbstractMethodNode<T, D> {
  }

  public static class Intermediate<T, D> implements AbstractMethodNode<T, D> {
    public final D declaration;
    public final PolymorphicTypeNode<T> returnType;

    public Intermediate(D declaration, PolymorphicTypeNode<T> returnType) {
      this.declaration = declaration;
      this.returnType = returnType;
    }
    
    public D declaration() {
      return declaration;
    }
  }

  public static class Chained<T, D> implements AbstractMethodNode<T, D> {
    public final D declaration;

    public Chained(D declaration) {
      this.declaration = declaration;
    }
    
    public D declaration() {
      return declaration;
    }
  }

  default boolean isStartMethod() {
    return this instanceof Start;
  }
  default boolean isTerminationMethod() {
    return this instanceof Termination;
  }
  default boolean isIntermediateMethod() {
    return this instanceof Intermediate;
  }
  default boolean isChainedMethod() {
    return this instanceof Chained;
  }
  default Start<T, D> asStartMethod() {
    return (Start<T, D>) this;
  }
  default Intermediate<T, D> asIntermediateMethod() {
    return (Intermediate<T, D>) this;
  }
  default Chained<?, D> asChainedMethod() {
    return (Chained<?, D>) this;
  }
}
