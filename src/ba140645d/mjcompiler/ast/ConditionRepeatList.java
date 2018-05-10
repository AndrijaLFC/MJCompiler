// generated with ast extension for cup
// version 0.8
// 9/4/2018 23:56:12


package ba140645d.mjcompiler.ast;

public class ConditionRepeatList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConditionRepeatList ConditionRepeatList;
    private ConditionRepeat ConditionRepeat;

    public ConditionRepeatList (ConditionRepeatList ConditionRepeatList, ConditionRepeat ConditionRepeat) {
        this.ConditionRepeatList=ConditionRepeatList;
        if(ConditionRepeatList!=null) ConditionRepeatList.setParent(this);
        this.ConditionRepeat=ConditionRepeat;
        if(ConditionRepeat!=null) ConditionRepeat.setParent(this);
    }

    public ConditionRepeatList getConditionRepeatList() {
        return ConditionRepeatList;
    }

    public void setConditionRepeatList(ConditionRepeatList ConditionRepeatList) {
        this.ConditionRepeatList=ConditionRepeatList;
    }

    public ConditionRepeat getConditionRepeat() {
        return ConditionRepeat;
    }

    public void setConditionRepeat(ConditionRepeat ConditionRepeat) {
        this.ConditionRepeat=ConditionRepeat;
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
        if(ConditionRepeatList!=null) ConditionRepeatList.accept(visitor);
        if(ConditionRepeat!=null) ConditionRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionRepeatList!=null) ConditionRepeatList.traverseTopDown(visitor);
        if(ConditionRepeat!=null) ConditionRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionRepeatList!=null) ConditionRepeatList.traverseBottomUp(visitor);
        if(ConditionRepeat!=null) ConditionRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionRepeatList(\n");

        if(ConditionRepeatList!=null)
            buffer.append(ConditionRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionRepeat!=null)
            buffer.append(ConditionRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionRepeatList]");
        return buffer.toString();
    }
}
