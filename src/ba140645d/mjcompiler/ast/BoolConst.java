// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:43


package ba140645d.mjcompiler.ast;

public class BoolConst extends ConstValue {

    private Boolean boolConst;

    public BoolConst (Boolean boolConst) {
        this.boolConst=boolConst;
    }

    public Boolean getBoolConst() {
        return boolConst;
    }

    public void setBoolConst(Boolean boolConst) {
        this.boolConst=boolConst;
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
        buffer.append("BoolConst(\n");


        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolConst]");
        return buffer.toString();
    }
}
