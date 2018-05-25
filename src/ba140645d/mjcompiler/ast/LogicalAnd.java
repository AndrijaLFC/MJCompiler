// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:41


package ba140645d.mjcompiler.ast;

public class LogicalAnd implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public LogicalAnd () {
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
        buffer.append("LogicalAnd(\n");

        buffer.append(tab);
        buffer.append(") [LogicalAnd]");
        return buffer.toString();
    }
}