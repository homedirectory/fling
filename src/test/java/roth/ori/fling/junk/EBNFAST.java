package roth.ori.fling.junk;public class EBNFAST implements roth.ori.fling.export.AST{public static class EBNF{public roth.ori.fling.junk.EBNFAST.Rule[] ebnf1;public EBNF(roth.ori.fling.junk.EBNFAST.Rule[] ebnf1){this.ebnf1=ebnf1;}}public static class Rule{public java.lang.String derive;public roth.ori.fling.junk.EBNFAST.To to;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> rule1;public Rule(java.lang.String derive,roth.ori.fling.junk.EBNFAST.To to,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> rule1){this.derive=derive;this.to=to;this.rule1=rule1;}}public static class To{}public static class ClauseTail{public roth.ori.fling.junk.EBNFAST.AndOr[] clausetail1;public ClauseTail(roth.ori.fling.junk.EBNFAST.AndOr[] clausetail1){this.clausetail1=clausetail1;}}public static class AndOr{}public static class Literal{}public static class To1 extends roth.ori.fling.junk.EBNFAST.To{public roth.ori.fling.junk.EBNFAST.Literal to;public To1(roth.ori.fling.junk.EBNFAST.Literal to){this.to=to;}}public static class To2 extends roth.ori.fling.junk.EBNFAST.To{public java.lang.String to;public To2(java.lang.String to){this.to=to;}}public static class AndOr3 extends roth.ori.fling.junk.EBNFAST.AndOr{public roth.ori.fling.export.Either andor1;public AndOr3(roth.ori.fling.export.Either andor1){this.andor1=andor1;}}public static class AndOr4 extends roth.ori.fling.junk.EBNFAST.AndOr{public roth.ori.fling.export.Either andor2;public AndOr4(roth.ori.fling.export.Either andor2){this.andor2=andor2;}}public static class Literal12 extends roth.ori.fling.junk.EBNFAST.Literal{public roth.ori.fling.junk.EBNFAST.Literal oneormore;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal1;public Literal12(roth.ori.fling.junk.EBNFAST.Literal oneormore,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal1){this.oneormore=oneormore;this.literal1=literal1;}}public static class Literal13 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String oneormore;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal2;public Literal13(java.lang.String oneormore,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal2){this.oneormore=oneormore;this.literal2=literal2;}}public static class Literal14 extends roth.ori.fling.junk.EBNFAST.Literal{public roth.ori.fling.junk.EBNFAST.Literal noneormore;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal3;public Literal14(roth.ori.fling.junk.EBNFAST.Literal noneormore,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal3){this.noneormore=noneormore;this.literal3=literal3;}}public static class Literal15 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String noneormore;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal4;public Literal15(java.lang.String noneormore,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal4){this.noneormore=noneormore;this.literal4=literal4;}}public static class Literal16 extends roth.ori.fling.junk.EBNFAST.Literal{public roth.ori.fling.junk.EBNFAST.Literal option;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal5;public Literal16(roth.ori.fling.junk.EBNFAST.Literal option,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal5){this.option=option;this.literal5=literal5;}}public static class Literal17 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String option;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal6;public Literal17(java.lang.String option,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal6){this.option=option;this.literal6=literal6;}}public static class Literal18 extends roth.ori.fling.junk.EBNFAST.Literal{public roth.ori.fling.junk.EBNFAST.Literal either1;public roth.ori.fling.junk.EBNFAST.Literal either2;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal7;public Literal18(roth.ori.fling.junk.EBNFAST.Literal either1,roth.ori.fling.junk.EBNFAST.Literal either2,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal7){this.either1=either1;this.either2=either2;this.literal7=literal7;}}public static class Literal19 extends roth.ori.fling.junk.EBNFAST.Literal{public roth.ori.fling.junk.EBNFAST.Literal either1;public java.lang.String either2;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal8;public Literal19(roth.ori.fling.junk.EBNFAST.Literal either1,java.lang.String either2,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal8){this.either1=either1;this.either2=either2;this.literal8=literal8;}}public static class Literal20 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String either1;public roth.ori.fling.junk.EBNFAST.Literal either2;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal9;public Literal20(java.lang.String either1,roth.ori.fling.junk.EBNFAST.Literal either2,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal9){this.either1=either1;this.either2=either2;this.literal9=literal9;}}public static class Literal21 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String either1;public java.lang.String either2;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal10;public Literal21(java.lang.String either1,java.lang.String either2,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal10){this.either1=either1;this.either2=either2;this.literal10=literal10;}}public static class Literal22 extends roth.ori.fling.junk.EBNFAST.Literal{public java.lang.String attribute1;public java.lang.Object[] attribute2;public java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal11;public Literal22(java.lang.String attribute1,java.lang.Object[] attribute2,java.util.Optional<roth.ori.fling.junk.EBNFAST.ClauseTail> literal11){this.attribute1=attribute1;this.attribute2=attribute2;this.literal11=literal11;}}}