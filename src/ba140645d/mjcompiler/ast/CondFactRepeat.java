// generated with ast extension for cup
// version 0.8
// 27/4/2018 17:38:30


package ba140645d.mjcompiler.ast;

public class CondFactRepeat implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private LogicalAnd LogicalAnd;
    private CondFact CondFact;

    public CondFactRepeat (LogicalAnd LogicalAnd, CondFact CondFact) {
        this.LogicalAnd=LogicalAnd;
        if(LogicalAnd!=null) LogicalAnd.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public LogicalAnd getLogicalAnd() {
        return LogicalAnd;
    }

    public void setLogicalAnd(LogicalAnd LogicalAnd) {
        this.LogicalAnd=LogicalAnd;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
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
        if(LogicalAnd!=null) LogicalAnd.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LogicalAnd!=null) LogicalAnd.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LogicalAnd!=null) LogicalAnd.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactRepeat(\n");

        if(LogicalAnd!=null)
            buffer.append(LogicalAnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactRepeat]");
        return buffer.toString();
    }
}
