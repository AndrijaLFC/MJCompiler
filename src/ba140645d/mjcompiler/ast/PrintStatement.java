// generated with ast extension for cup
// version 0.8
// 22/4/2018 18:10:38


package ba140645d.mjcompiler.ast;

public class PrintStatement extends Statement {

    private Expr Expr;
    private OptPrintNumConst OptPrintNumConst;

    public PrintStatement (Expr Expr, OptPrintNumConst OptPrintNumConst) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.OptPrintNumConst=OptPrintNumConst;
        if(OptPrintNumConst!=null) OptPrintNumConst.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public OptPrintNumConst getOptPrintNumConst() {
        return OptPrintNumConst;
    }

    public void setOptPrintNumConst(OptPrintNumConst OptPrintNumConst) {
        this.OptPrintNumConst=OptPrintNumConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(OptPrintNumConst!=null) OptPrintNumConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(OptPrintNumConst!=null) OptPrintNumConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(OptPrintNumConst!=null) OptPrintNumConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptPrintNumConst!=null)
            buffer.append(OptPrintNumConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
