// generated with ast extension for cup
// version 0.8
// 18/4/2018 3:41:45


package ba140645d.mjcompiler.ast;

public class FactorNew extends Factor {

    private Type Type;
    private OptArrExpr OptArrExpr;

    public FactorNew (Type Type, OptArrExpr OptArrExpr) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.OptArrExpr=OptArrExpr;
        if(OptArrExpr!=null) OptArrExpr.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public OptArrExpr getOptArrExpr() {
        return OptArrExpr;
    }

    public void setOptArrExpr(OptArrExpr OptArrExpr) {
        this.OptArrExpr=OptArrExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptArrExpr!=null) OptArrExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptArrExpr!=null) OptArrExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptArrExpr!=null) OptArrExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNew(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptArrExpr!=null)
            buffer.append(OptArrExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNew]");
        return buffer.toString();
    }
}
