package fling.languages;

import static java.util.stream.Collectors.joining;
import static fling.generated.Datalog.fact;
import static fling.generated.Datalog.Term.l;
import static fling.generated.Datalog.Term.v;
import static fling.grammar.BNF.bnf;
import static fling.grammar.sententials.Notation.noneOrMore;
import static fling.grammar.sententials.Notation.oneOrMore;
import static fling.languages.Datalog.V.AdditionalClause;
import static fling.languages.Datalog.V.Bodyless;
import static fling.languages.Datalog.V.Fact;
import static fling.languages.Datalog.V.FirstClause;
import static fling.languages.Datalog.V.Program;
import static fling.languages.Datalog.V.Query;
import static fling.languages.Datalog.V.Rule;
import static fling.languages.Datalog.V.RuleBody;
import static fling.languages.Datalog.V.RuleHead;
import static fling.languages.Datalog.V.Statement;
import static fling.languages.Datalog.V.Term;
import static fling.languages.Datalog.V.WithBody;
import static fling.languages.Datalog.Σ.always;
import static fling.languages.Datalog.Σ.and;
import static fling.languages.Datalog.Σ.fact;
import static fling.languages.Datalog.Σ.infer;
import static fling.languages.Datalog.Σ.l;
import static fling.languages.Datalog.Σ.of;
import static fling.languages.Datalog.Σ.query;
import static fling.languages.Datalog.Σ.v;
import static fling.languages.Datalog.Σ.when;
import fling.generated.DatalogAST;

import fling.adapters.JavaMediator;
import fling.generated.DatalogAST.Fact;
import fling.generated.DatalogAST.Program;
import fling.grammar.BNF;
import fling.grammar.sententials.Terminal;
import fling.grammar.sententials.Variable;
import static java.lang.String.format;

import java.util.Arrays;

public class Datalog {
  public enum Σ implements Terminal {
    infer, fact, query, of, and, when, always, v, l
  }

  public enum V implements Variable {
    Program, Statement, Rule, Query, Fact, Bodyless, WithBody, //
    RuleHead, RuleBody, FirstClause, AdditionalClause, Term
  }

  private static final Class<String> String = String.class;
  public static final BNF bnf = bnf(V.class). //
      start(Program). //
      derive(Program, oneOrMore(Statement)). //
      derive(Statement, Fact). //
      derive(Statement, Rule). //
      derive(Statement, Query). //
      derive(Fact, fact.with(String), of.many(String)). //
      derive(Query, query.with(String), of.many(Term)). //
      derive(Rule, Bodyless). //
      derive(Rule, WithBody). //
      derive(Bodyless, always.with(String), of.many(Term)). //
      derive(WithBody, RuleHead, RuleBody). //
      derive(RuleHead, infer.with(String), of.many(Term)). //
      derive(RuleBody, FirstClause, noneOrMore(AdditionalClause)). //
      derive(FirstClause, when.with(String), of.many(Term)). //
      derive(AdditionalClause, and.with(String), of.many(Term)). //
      derive(Term, l.with(String)). //
      derive(Term, v.with(String)). //
      build();
  public static final JavaMediator jm = new JavaMediator(bnf, //
      "fling.generated", "Datalog", Σ.class);

  public static void main(String[] args) {
    Program program = fact("parent").of("john", "bob"). //
        fact("parent").of("bob", "donald"). //
        always("ancestor").of(l("adam"), v("X")). //
        infer("ancestor").of(v("A"), v("B")). //
        when("parent").of(v("A"), v("B")). //
        infer("ancestor").of(v("A"), v("B")). //
        when("parent").of(v("A"), v("C")). //
        and("ancestor").of(v("C"), v("B")). //
        query("ancestor").of(l("john"), v("X")). //
        $();
    new DatalogPrinter().visit(program);
  }

  public static class DatalogPrinter extends DatalogAST.Visitor {
    @Override public void whileVisiting(Fact fact) {
      System.out.println(format("%s(%s).", fact.s, Arrays.stream(fact.j).collect(joining(","))));
    }
  }
}
