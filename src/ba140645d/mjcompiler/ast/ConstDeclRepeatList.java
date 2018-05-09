// generated with ast extension for cup
// version 0.8
// 9/4/2018 20:42:52


package ba140645d.mjcompiler.ast;

public class ConstDeclRepeatList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclRepeatList ConstDeclRepeatList;
    private ConstDeclRepeat ConstDeclRepeat;

    public ConstDeclRepeatList (ConstDeclRepeatList ConstDeclRepeatList, ConstDeclRepeat ConstDeclRepeat) {
        this.ConstDeclRepeatList=ConstDeclRepeatList;
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.setParent(this);
        this.ConstDeclRepeat=ConstDeclRepeat;
        if(ConstDeclRepeat!=null) ConstDeclRepeat.setParent(this);
    }

    public ConstDeclRepeatList getConstDeclRepeatList() {
        return ConstDeclRepeatList;
    }

    public void setConstDeclRepeatList(ConstDeclRepeatList ConstDeclRepeatList) {
        this.ConstDeclRepeatList=ConstDeclRepeatList;
    }

    public ConstDeclRepeat getConstDeclRepeat() {
        return ConstDeclRepeat;
    }

    public void setConstDeclRepeat(ConstDeclRepeat ConstDeclRepeat) {
        this.ConstDeclRepeat=ConstDeclRepeat;
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
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.accept(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.traverseTopDown(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclRepeatList!=null) ConstDeclRepeatList.traverseBottomUp(visitor);
        if(ConstDeclRepeat!=null) ConstDeclRepeat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclRepeatList(\n");

        if(ConstDeclRepeatList!=null)
            buffer.append(ConstDeclRepeatList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclRepeat!=null)
            buffer.append(ConstDeclRepeat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclRepeatList]");
        return buffer.toString();
    }
}
