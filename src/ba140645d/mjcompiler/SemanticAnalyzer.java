package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor{

    private boolean firstIteration = true;
    private Type currentType = null;
    private Obj currentTypeObj = Tab.noObj;



    public SemanticAnalyzer(){
        Tab.init();

        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", Tab.intType));

    }


    private boolean isDefinedInCurrentScope(String symName){
        return Tab.currentScope().findSymbol(symName) != null;
    }

    private boolean isDefined(String symName){
        return Tab.find(symName) != Tab.noObj;
    }


    private void logInfo(String message, SyntaxNode node){
        String lineInfo = "Greska na liniji (" + node.getLine() + "):";
        System.err.println(lineInfo + message);
    }

    @Override
    public void visit(Program Program) {

    }

    @Override
    public void visit(ProgramName programName){
        // ubacujemo Program u tabelu simbola
        Tab.insert(Obj.Prog, programName.getProgramName(), Tab.noType);

        // otvaramo opseg vazenja
        Tab.openScope();
    }


    @Override
    public void visit(ConstDefinition constDefinition){

        // ako tip ne postoji, odnosno nismo ga prepoznali u tabeli simbola
        // to znaci da nema potrebe da proveravamo definiciju konstante
        if (currentTypeObj == Tab.noObj)
            return;

        // naziv konstante
        String constName = constDefinition.getConstName();

        if (isDefined(constName))
            logInfo("Simbol [" + constName +"] je vec definisan!", constDefinition);
        else{
            // dohvatanje vrste tipa podatka
            Struct type = currentTypeObj.getType();

            // produkcija koja je jedna od 3 moguce konstante
            ConstValue constValue = constDefinition.getConstValue();
            // vrednost konstante
            int value = 0;

            // tip sa desne strane dodele
            Struct rightSide = Tab.noType;

            if (constValue instanceof NumConst){
                rightSide = Tab.intType;

                value = ((NumConst) constValue).getNumConst();
            }else if (constValue instanceof CharConst){
                rightSide = Tab.charType;

                value = ((CharConst) constValue).getCharConst();
            }else if (constValue instanceof BoolConst){
                rightSide = Tab.find("bool").getType();

                value = ((BoolConst) constValue).getBoolConst()  ? 1 : 0;
            }

            if (type.assignableTo(rightSide)){
                Obj constObj = Tab.insert(Obj.Con, constName, type);

                constObj.setAdr(value);
            }
        }
    }

    @Override
    public void visit(Type type){


        String typeName = type.getTypeName();

        currentTypeObj = Tab.noObj;

        currentType = type;

        if (!isDefined(typeName)){
            logInfo("Simbol [" + typeName + "] nije definisan!", type);

            return;
        }


        // simbol je vec definisan,sada treba videti da li je u pitanju tip
        Obj typeObj = Tab.find(typeName);

        if (typeObj.getKind() == Obj.Type)
            currentTypeObj = typeObj;
        else
            logInfo("Simbol [" + typeName + "] nije tip!", type);


    }

    @Override
    public void visit(VarDecl varDecl){

    }

    @Override
    public void visit(VarDeclDefinition varDeclDefinition){
        String varName = varDeclDefinition.getVarName();

        // simbol vec definisan
        if (isDefinedInCurrentScope(varName)) {
            logInfo("Simbol[" + varName + "] je vec definisan u trenutnom opsegu!", varDeclDefinition);
            return;
        }

        // vracamo se takodje i ako nismo naisli na validan tip podatka
        if (currentTypeObj == Tab.noObj)
            return;

        boolean isArrayDecl = (varDeclDefinition.getOptArrayDecl() instanceof OptArrayDeclared);

        // u slucaju da je tip podatka niz nekog tipa, potrebna nam je struktura
        // koja predstavlja niz tog tipa
        Struct arrayStruct = new Struct(Struct.Array, currentTypeObj.getType());

        // ako je deklaracija niza, onda postavljamo za tip array strukturu
        // inace postavljamo sam tip koji je naveden
        if (isArrayDecl)
            Tab.insert(Obj.Var, varName, arrayStruct);
        else
            Tab.insert(Obj.Var, varName, currentTypeObj.getType());
    }



}