package fling.examples.languages;

import org.antlr.v4.Tool;
import org.antlr.v4.tool.Grammar;

import fling.BNF;
import fling.adapters.JavaANTLRAPIAdapter;
import fling.compilers.api.ReliableAPICompiler;
import fling.grammars.LL1;
import fling.internal.compiler.Namer;
import fling.namers.NaiveNamer;

public class TableMaker {
  public static final String apiClass;
  static {
    String grammarFilePath = TableMaker.class.getClassLoader().getResource("grammars/TableMaker.g").getPath();
    Tool tool = new Tool();
    Grammar grammar = tool.loadGrammar(grammarFilePath);
    BNF bnf = BNF.fromANTLR(grammar);
    String packageName = "fling.examples.generated";
    String apiName = "TableMaker";
    Namer namer = new NaiveNamer(packageName, apiName);
    LL1 ll1 = new LL1(bnf, namer);
    JavaANTLRAPIAdapter adapter = new JavaANTLRAPIAdapter(grammarFilePath, packageName, apiName, "$", namer);
    apiClass = adapter.printFluentAPI(new ReliableAPICompiler(ll1.buildAutomaton(ll1.bnf.reachableSubBNF())).compileFluentAPI());
  }
}
