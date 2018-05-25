// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:41


package ba140645d.mjcompiler.ast;

public class FactorFuncCallOrVar extends Factor {

    private Designator Designator;
    private OptParenthesesActPars OptParenthesesActPars;

    public FactorFuncCallOrVar (Designator Designator, OptParenthesesActPars OptParenthesesActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.OptParenthesesActPars=OptParenthesesActPars;
        if(OptParenthesesActPars!=null) OptParenthesesActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public OptParenthesesActPars getOptParenthesesActPars() {
        return OptParenthesesActPars;
    }

    public void setOptParenthesesActPars(OptParenthesesActPars OptParenthesesActPars) {
        this.OptParenthesesActPars=OptParenthesesActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(OptParenthesesActPars!=null) OptParenthesesActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(OptParenthesesActPars!=null) OptParenthesesActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(OptParenthesesActPars!=null) OptParenthesesActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorFuncCallOrVar(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptParenthesesActPars!=null)
            buffer.append(OptParenthesesActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorFuncCallOrVar]");
        return buffer.toString();
    }
}
