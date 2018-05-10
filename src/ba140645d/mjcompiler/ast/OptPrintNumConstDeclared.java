// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:57


package ba140645d.mjcompiler.ast;

public class OptPrintNumConstDeclared extends OptPrintNumConst {

    private Integer N1;

    public OptPrintNumConstDeclared (Integer N1) {
        this.N1=N1;
    }

    public Integer getN1() {
        return N1;
    }

    public void setN1(Integer N1) {
        this.N1=N1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptPrintNumConstDeclared(\n");

        buffer.append(" "+tab+N1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptPrintNumConstDeclared]");
        return buffer.toString();
    }
}
