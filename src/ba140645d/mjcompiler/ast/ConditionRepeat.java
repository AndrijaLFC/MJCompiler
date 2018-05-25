// generated with ast extension for cup
// version 0.8
// 25/4/2018 20:21:41


package ba140645d.mjcompiler.ast;

public class ConditionRepeat implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private LogicalOr LogicalOr;
    private CondTerm CondTerm;

    public ConditionRepeat (LogicalOr LogicalOr, CondTerm CondTerm) {
        this.LogicalOr=LogicalOr;
        if(LogicalOr!=null) LogicalOr.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public LogicalOr getLogicalOr() {
        return LogicalOr;
    }

    public void setLogicalOr(LogicalOr LogicalOr) {
        this.LogicalOr=LogicalOr;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
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
        if(LogicalOr!=null) LogicalOr.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LogicalOr!=null) LogicalOr.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LogicalOr!=null) LogicalOr.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionRepeat(\n");

        if(LogicalOr!=null)
            buffer.append(LogicalOr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionRepeat]");
        return buffer.toString();
    }
}
