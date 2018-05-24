// generated with ast extension for cup
// version 0.8
// 24/4/2018 15:22:5


package ba140645d.mjcompiler.ast;

public class OptParenthesesActParsDeclared extends OptParenthesesActPars {

    private OptActPars OptActPars;

    public OptParenthesesActParsDeclared (OptActPars OptActPars) {
        this.OptActPars=OptActPars;
        if(OptActPars!=null) OptActPars.setParent(this);
    }

    public OptActPars getOptActPars() {
        return OptActPars;
    }

    public void setOptActPars(OptActPars OptActPars) {
        this.OptActPars=OptActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptActPars!=null) OptActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptActPars!=null) OptActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptActPars!=null) OptActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptParenthesesActParsDeclared(\n");

        if(OptActPars!=null)
            buffer.append(OptActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptParenthesesActParsDeclared]");
        return buffer.toString();
    }
}
