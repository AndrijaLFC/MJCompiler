// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:57


package ba140645d.mjcompiler.ast;

public class ActPars implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Expr Expr;
    private ActParsRepeatList ActParsRepeatList;

    public ActPars (Expr Expr, ActParsRepeatList ActParsRepeatList) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.ActParsRepeatList=ActParsRepeatList;
        if(ActParsRepeatList!=null) ActParsRepeatList.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public ActParsRepeatList getActParsRepeatList() {
        return ActParsRepeatList;
    }

    public void setActParsRepeatList(ActParsRepeatList ActParsRepeatList) {
        this.ActParsRepeatList=ActParsRepeatList;
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
        if(Expr!=null) Expr.accept(visitor);
        if(ActParsRepeatList!=null) ActParsRepeatList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(ActParsRepeatList!=null) ActParsRepeatList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(ActParsRepeatList!=null) ActParsRepeatList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActPars(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParsRepeatList!=null)
            buffer.append(ActParsRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActPars]");
        return buffer.toString();
    }
}
