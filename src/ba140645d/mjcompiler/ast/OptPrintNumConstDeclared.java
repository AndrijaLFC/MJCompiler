// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:30


package ba140645d.mjcompiler.ast;

public class OptPrintNumConstDeclared extends OptPrintNumConst {

    private Integer numConst;

    public OptPrintNumConstDeclared (Integer numConst) {
        this.numConst=numConst;
    }

    public Integer getNumConst() {
        return numConst;
    }

    public void setNumConst(Integer numConst) {
        this.numConst=numConst;
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

        buffer.append(" "+tab+numConst);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptPrintNumConstDeclared]");
        return buffer.toString();
    }
}
