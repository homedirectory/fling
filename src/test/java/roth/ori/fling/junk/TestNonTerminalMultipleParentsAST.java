package roth.ori.fling.junk;public class TestNonTerminalMultipleParentsAST implements roth.ori.fling.export.AST{public static class S{public roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.A a;public roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.B b;public S(roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.A a,roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.B b){this.a=a;this.b=b;}}public static interface A{}public static interface B{}public static class C implements roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.A,roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.B{public java.lang.String a;public C(java.lang.String a){this.a=a;}}public static class D implements roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.A,roth.ori.fling.junk.TestNonTerminalMultipleParentsAST.B{public java.lang.String b;public D(java.lang.String b){this.b=b;}}}