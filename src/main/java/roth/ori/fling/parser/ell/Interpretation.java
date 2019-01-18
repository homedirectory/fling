package roth.ori.fling.parser.ell;

import java.util.LinkedList;
import java.util.List;

import roth.ori.fling.export.FluentAPIRecorder;
import roth.ori.fling.symbols.GrammarElement;

@SuppressWarnings("rawtypes") public class Interpretation {
  public GrammarElement symbol;
  public List value;
  private static final String PP_IDENT = "--";

  public Interpretation(GrammarElement symbol, List<?> value) {
    this.symbol = symbol;
    this.value = value;
  }
  @Override public String toString() {
    return toString(0);
  }
  @SuppressWarnings("unchecked") public void foldExtendibles() {
    for (Object o : value)
      if (o instanceof Interpretation)
        ((Interpretation) o).foldExtendibles();
    // else if (o instanceof FluentAPIRecorder)
    // ((FluentAPIRecorder) o).fold();
    if (symbol.isExtendible()) {
      List t = new LinkedList(value);
      value.clear();
      value.addAll(symbol.asExtendible().fold(t));
    } else if (symbol.isVerb()) {
      List t = new LinkedList(value);
      value.clear();
      for (Object o : t) {
        if (o instanceof FluentAPIRecorder)
          value.add(((FluentAPIRecorder) o).ell.ast());
        else
          value.add(o);
      }
    }
  }
  public String toString(int ident) {
    StringBuilder $ = new StringBuilder();
    for (int i = 0; i < ident; ++i)
      $.append(PP_IDENT);
    $.append(symbol).append("\n");
    for (Object o : value) {
      if (o instanceof Interpretation)
        $.append(((Interpretation) o).toString(ident + 1));
      else if (o instanceof FluentAPIRecorder)
        $.append(((FluentAPIRecorder) o).toString(ident + 1));
      else {
        for (int i = 0; i < ident + 1; ++i)
          $.append(PP_IDENT);
        $.append(o);
        $.append("\n");
      }
    }
    return $.toString();
  }
  public static Interpretation of(GrammarElement symbol, List<?> value) {
    return new Interpretation(symbol, value);
  }
}
