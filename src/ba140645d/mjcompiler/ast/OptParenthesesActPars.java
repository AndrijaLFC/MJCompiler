// generated with ast extension for cup
// version 0.8
// 22/4/2018 18:10:40


package ba140645d.mjcompiler.ast;

public abstract class OptParenthesesActPars implements SyntaxNode {

    private SyntaxNode parent;

    private int line;

    public ba140645d.mjcompiler.utilities.StructLinkedList structlinkedlist = null;

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

    public abstract void accept(Visitor visitor);
    public abstract void childrenAccept(Visitor visitor);
    public abstract void traverseTopDown(Visitor visitor);
    public abstract void traverseBottomUp(Visitor visitor);

    public String toString() { return toString(""); }
    public abstract String toString(String tab);
}
