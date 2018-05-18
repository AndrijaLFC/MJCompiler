// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:44


package ba140645d.mjcompiler.ast;

public class CondFact implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Expr Expr;
    private OptRelopExpr OptRelopExpr;

    public CondFact (Expr Expr, OptRelopExpr OptRelopExpr) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptRelopExpr=OptRelopExpr;
        if(OptRelopExpr!=null) OptRelopExpr.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptRelopExpr getOptRelopExpr() {
        return OptRelopExpr;
    }

    public void setOptRelopExpr(OptRelopExpr OptRelopExpr) {
        this.OptRelopExpr=OptRelopExpr;
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
        if(OptRelopExpr!=null) OptRelopExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptRelopExpr!=null) OptRelopExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptRelopExpr!=null) OptRelopExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFact(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptRelopExpr!=null)
            buffer.append(OptRelopExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFact]");
        return buffer.toString();
    }
}
