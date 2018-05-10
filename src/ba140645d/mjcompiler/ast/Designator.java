// generated with ast extension for cup
// version 0.8
// 10/4/2018 19:54:57


package ba140645d.mjcompiler.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String designatorName;
    private DesignatorRepeatList DesignatorRepeatList;

    public Designator (String designatorName, DesignatorRepeatList DesignatorRepeatList) {
        this.designatorName=designatorName;
        this.DesignatorRepeatList=DesignatorRepeatList;
        if(DesignatorRepeatList!=null) DesignatorRepeatList.setParent(this);
    }

    public String getDesignatorName() {
        return designatorName;
    }

    public void setDesignatorName(String designatorName) {
        this.designatorName=designatorName;
    }

    public DesignatorRepeatList getDesignatorRepeatList() {
        return DesignatorRepeatList;
    }

    public void setDesignatorRepeatList(DesignatorRepeatList DesignatorRepeatList) {
        this.DesignatorRepeatList=DesignatorRepeatList;
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
        if(DesignatorRepeatList!=null) DesignatorRepeatList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorRepeatList!=null) DesignatorRepeatList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorRepeatList!=null) DesignatorRepeatList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        buffer.append(" "+tab+designatorName);
        buffer.append("\n");

        if(DesignatorRepeatList!=null)
            buffer.append(DesignatorRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
