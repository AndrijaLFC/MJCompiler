// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:36:15


package ba140645d.mjcompiler.ast;

public class DesignatorStmtErrorRecovery extends DesignatorStatement {

    private Designator Designator;
    private DesignatorErr DesignatorErr;

    public DesignatorStmtErrorRecovery (Designator Designator, DesignatorErr DesignatorErr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorErr=DesignatorErr;
        if(DesignatorErr!=null) DesignatorErr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorErr getDesignatorErr() {
        return DesignatorErr;
    }

    public void setDesignatorErr(DesignatorErr DesignatorErr) {
        this.DesignatorErr=DesignatorErr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorErr!=null) DesignatorErr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorErr!=null) DesignatorErr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorErr!=null) DesignatorErr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStmtErrorRecovery(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorErr!=null)
            buffer.append(DesignatorErr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStmtErrorRecovery]");
        return buffer.toString();
    }
}
