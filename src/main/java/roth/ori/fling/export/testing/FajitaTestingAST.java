package roth.ori.fling.export.testing;

public class FajitaTestingAST implements roth.ori.fling.export.AST {
  public static class ExampleKind {
  }

  public static class ExampleBody {
    public java.util.Optional<roth.ori.fling.symbols.NonTerminal> examplebody1;
    public roth.ori.fling.symbols.Terminal call;
    public java.lang.Object[] with;
    public roth.ori.fling.export.testing.FajitaTestingAST.ExampleBodyNext[] examplebody2;

    public ExampleBody(java.util.Optional<roth.ori.fling.symbols.NonTerminal> examplebody1,
        roth.ori.fling.symbols.Terminal call, java.lang.Object[] with,
        roth.ori.fling.export.testing.FajitaTestingAST.ExampleBodyNext[] examplebody2) {
      this.examplebody1 = examplebody1;
      this.call = call;
      this.with = with;
      this.examplebody2 = examplebody2;
    }
  }

  public static class ExampleBodyNext {
    public roth.ori.fling.symbols.Terminal then;
    public java.lang.Object[] with;

    public ExampleBodyNext(roth.ori.fling.symbols.Terminal then, java.lang.Object[] with) {
      this.then = then;
      this.with = with;
    }
  }

  public static class Test {
    public roth.ori.fling.export.testing.FajitaTestingAST.ExampleKind[] test1;

    public Test(roth.ori.fling.export.testing.FajitaTestingAST.ExampleKind[] test1) {
      this.test1 = test1;
    }
  }

  public static class Example extends roth.ori.fling.export.testing.FajitaTestingAST.ExampleKind {
    public roth.ori.fling.export.testing.FajitaTestingAST.ExampleBody example;

    public Example(roth.ori.fling.export.testing.FajitaTestingAST.ExampleBody example) {
      this.example = example;
    }
  }

  public static class MalExample extends roth.ori.fling.export.testing.FajitaTestingAST.ExampleKind {
    public roth.ori.fling.export.testing.FajitaTestingAST.ExampleBody malexample;

    public MalExample(roth.ori.fling.export.testing.FajitaTestingAST.ExampleBody malexample) {
      this.malexample = malexample;
    }
  }
}