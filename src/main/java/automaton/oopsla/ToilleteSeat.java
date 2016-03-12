package automaton.oopsla;

public abstract class ToilleteSeat {
  public static Q0 downSeat(){return null;}
  
  public static interface Q0{
    Q1 female();
    Q6 male();
    void $();
  }
  
  public static interface Q1{
    Q0 urinate();
    Q0 defecate();
  }
  public static interface Q2{
    Q1 lower();
  }
  public static interface Q3{
    Q2 female();
    Q4 male();
    void $();
  }
  public static interface Q4{
    Q6 lower();
    Q3 urinate();
  }
  public static interface Q5{
    Q3 urinate();
  }
  public static interface Q6{
    Q5 raise();
    Q0 defecate();
  }
  public static void main(String[] args) {
    downSeat() //
        .female().urinate()//
        .male().defecate() //
        .male().raise().urinate() // seat is up
        .female().lower().defecate() // seat is down
        .$();
  }
}
