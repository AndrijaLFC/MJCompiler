// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:29


package ba140645d.mjcompiler.ast;

public class ConstDefinition implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String constName;
    private ConstValue ConstValue;

    public ConstDefinition (String constName, ConstValue ConstValue) {
        this.constName=constName;
        this.ConstValue=ConstValue;
        if(ConstValue!=null) ConstValue.setParent(this);
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public ConstValue getConstValue() {
        return ConstValue;
    }

    public void setConstValue(ConstValue ConstValue) {
        this.ConstValue=ConstValue;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstValue!=null) ConstValue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstValue!=null) ConstValue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstValue!=null) ConstValue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDefinition(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        if(ConstValue!=null)
            buffer.append(ConstValue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDefinition]");
        return buffer.toString();
    }
}
