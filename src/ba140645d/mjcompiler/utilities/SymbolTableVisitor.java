package ba140645d.mjcompiler.utilities;

import ba140645d.mjcompiler.SemanticAnalyzer;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SymbolTableVisitor extends DumpSymbolTableVisitor {

    @Override
    public void visitStructNode(Struct structToVisit) {
        switch (structToVisit.getKind()) {
            case Struct.None:
                output.append("notype");
                break;
            case Struct.Int:
                output.append("int");
                break;
            case Struct.Char:
                output.append("char");
                break;
            case SemanticAnalyzer.Bool :
                output.append("bool");
                break;
            case Struct.Array:
                output.append("Arr of ");

                switch (structToVisit.getElemType().getKind()) {
                    case Struct.None:
                        output.append("notype");
                        break;
                    case Struct.Int:
                        output.append("int");
                        break;
                    case Struct.Char:
                        output.append("char");
                        break;
                    case Struct.Class:
                        output.append("Class");
                        break;
                    case SemanticAnalyzer.Bool :
                        output.append("bool");
                        break;
                }
                break;
            case Struct.Class:
                output.append("Class [");
                for (Obj obj : structToVisit.getMembers().symbols()) {
                    obj.accept(this);
                }
                output.append("]");
                break;
        }

    }


}