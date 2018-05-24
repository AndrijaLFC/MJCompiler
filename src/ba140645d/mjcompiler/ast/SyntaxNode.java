// generated with ast extension for cup
// version 0.8
// 24/4/2018 15:22:3


package ba140645d.mjcompiler.ast;

public interface SyntaxNode { 

    public void accept(Visitor visitor); 

    public void childrenAccept(Visitor visitor); 
    public void traverseBottomUp(Visitor visitor); 
    public void traverseTopDown(Visitor visitor); 

    public SyntaxNode getParent(); 
    public void setParent(SyntaxNode parent); 
    public int getLine(); 
    public void setLine(int line); 
}
