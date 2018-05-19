package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import com.sun.istack.internal.Nullable;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticAnalyzer extends VisitorAdaptor{

    // kod za Bool tip
    private static final int Bool = 5;

    private static final Struct boolType = new Struct(Bool);
    // da li je semanticka provera
    private boolean semanticCheck = true;

    // trenutni tip
    private Type currentType = null;

    // objekat trenutnog tipa
    private Obj currentTypeObj = Tab.noObj;

    //struktura trenutnog tipa
    private Struct currentTypeStruct = Tab.noType;

    // objekat trenutnog metoda
    private Obj currentMethod = Tab.noObj;

    // objekat pocetnog designatora
    private Obj designatorObj = Tab.noObj;




    public SemanticAnalyzer(){
        Tab.init();


        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
    }


    private boolean isDefinedInCurrentScope(String symName){
        return Tab.currentScope().findSymbol(symName) != null;
    }

    private boolean isDefined(String symName){
        return Tab.find(symName) != Tab.noObj;
    }


    private boolean areEquivalentTypes(Obj typeObj1, Obj typeObj2){
        if (typeObj1.getKind() != Obj.Type || typeObj2.getKind() != Obj.Type) {
            System.err.println("FATAL ERROR!!! in function areEquivalentTypes! ");
            System.err.println("obj1 type is : " + symbolTypeToString(typeObj1.getKind()));
            System.err.println("obj2 type is : " + symbolTypeToString(typeObj2.getKind()));

            return false;
        }

        boolean areArrays = typeObj1.getType().getKind() == Struct.Array
                && typeObj2.getType().getKind() == Struct.Array;

        return typeObj1.getName().equals(typeObj2.getName()) || typeObj1.getType().equals(typeObj2.getType());
    }




    private void logInfo(String message, SyntaxNode node){
        String lineInfo = "Na liniji (" + node.getLine() + "): ";
        System.out.println(lineInfo + message);
    }

    private void logError(String message, SyntaxNode node){
        String lineInfo = "Greska na liniji (" + node.getLine() + "): ";
        System.err.println(lineInfo + message);
    }

    private String formatAlreadyDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] je vec definisan!";
    }

    private String formatSymbolNotDefinedMessage(String symbolName){
        return "Simbol [" + symbolName + "] nije definisan!";
    }

    private String formatAssignmentMessage(Struct leftSideType, Struct rightSideType, @Nullable Struct expectedType){
        String leftSideTypeName = symbolTypeToString(leftSideType.getKind());
        String rightSideTypeName = symbolTypeToString(rightSideType.getKind());
        String expectedTypeName = (expectedType != null)? symbolTypeToString(expectedType.getKind()) : null;

        String message = "Na levoj strani je tip : {" + leftSideTypeName + "}. Na desnoj strani je tip : {" + rightSideTypeName +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedTypeName + "}";

        return message;
    }


    private String formatWrongTypeMessage(Struct expectedType, Struct actualType){
        String expectedTypeName = symbolTypeToString(expectedType.getKind());
        String actualTypeName = symbolTypeToString(actualType.getKind());

        return "Tip je : {" + actualTypeName +"}. Ocekivan tip je : {" + expectedTypeName + "} ";
    }

    private String formatClassMemberDoesntExistMessage(Obj classObj, String fieldName){
        String className = classObj.getName();

        return "U klasi : {" + className + "} ne postoji polje sa nazivom : {" + fieldName + "} ";
    }

    private String formatWrongTypeMessage(int expectedType, int actualType){
        String expectedTypeName = symbolTypeToString(expectedType);
        String actualTypeName = symbolTypeToString(actualType);

        return "Tip je : {" + actualTypeName +"}. Ocekivan tip je : {" + expectedTypeName + "} ";
    }

    private String formatAssignmentMessage(String leftSideType, String rightSideType, @Nullable String expectedType){

        String message = "Na levoj strani je tip : {" + leftSideType+ "}. Na desnoj strani je tip : {" + rightSideType +"}. ";

        if (expectedType != null)
            message += "Ocekivani tip je : {" + expectedType + "}";

        return message;
    }

    private String formatSymbolName(String symbolName){
        return "Simbol [" + symbolName + "]";
    }

    private String symbolKindToString(int symbolKind){
        String retVal = "Unknown";
        switch(symbolKind){
            case Obj.Con :  retVal =  "Con"; break;
            case Obj.Elem : retVal = "Elem"; break;
            case Obj.Fld : retVal = "Fld"; break;
            case Obj.Meth : retVal = "Meth"; break;
            case Obj.Prog : retVal = "Prog"; break;
            case Obj.Type : retVal = "Type"; break;
            case Obj.Var : retVal = "Var"; break;
        }

        return retVal;
    }

    private String symbolTypeToString(int symbolType){
        String retVal = "Unknown";

        switch(symbolType){
            case Struct.Array : retVal = "Array";       break;
            case Struct.Char :  retVal = "Char";        break;
            case Struct.Int :   retVal = "Int";         break;
            case Struct.None :  retVal = "None";        break;
            case Bool :         retVal = "Bool";        break;
        }

        return retVal;
    }

    private String formatSymbolInfo(Obj symbol){
        String symbolNameFormat = formatSymbolName(symbol.getName());
        String symbolKind = symbolKindToString(symbol.getKind());
        String symbolType = symbolTypeToString(symbol.getType().getKind());

        String format = symbolNameFormat + " kind : " + symbolKind + ", type : " + symbolType;

        if (symbol.getType().getKind() == Struct.Array)
            format += " of " + symbolTypeToString(symbol.getType().getElemType().getKind());


        return format;

    }



    @Override
    public void visit(Program program) {

        // pronalazimo simbol programa u tabeli simbola

        Obj programObj = Tab.find(program.getProgramName().getProgramName());

        logInfo(formatSymbolInfo(programObj), program);

        // ulancamo u njega sve lokalne simbole
        Tab.chainLocalSymbols(programObj);

        // zatvorimo opseg
        Tab.closeScope();
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
            logError(formatAlreadyDefinedMessage(constName), constDefinition);
        else{
            // dohvatanje vrste tipa podatka
            Struct type = currentTypeObj.getType();

            // produkcija koja je jedna od 3 moguce konstante
            ConstValue constValue = constDefinition.getConstValue();
            // vrednost konstante
            int value = 0;

            // tip sa desne strane dodele
            Struct rightSide = Tab.noType;
            Obj rightSideObj = Tab.noObj;

            // da li je sa desne strane bool konstanta
            boolean isConstValBool = false;

            // postavljamo tip podatka sa desne strane i citamo vrednost
            if (constValue instanceof NumConst){
                rightSide = Tab.intType;
                rightSideObj = Tab.find("int");

                value = ((NumConst) constValue).getNumConst();
            }else if (constValue instanceof CharConst){
                rightSide = Tab.charType;
                rightSideObj = Tab.find("char");

                value = ((CharConst) constValue).getCharConst();
            }else if (constValue instanceof BoolConst){
                rightSide = Tab.find("bool").getType();

                rightSideObj = Tab.find("bool");
                isConstValBool = true;

                value = ((BoolConst) constValue).getBoolConst()  ? 1 : 0;
            }

            // da li je bool tip
            boolean isBoolDef = currentTypeObj.getName().equals("bool");

            if (type.assignableTo(rightSide)){
                Obj constObj = Tab.insert(Obj.Con, constName, type);

                logInfo(formatSymbolInfo(constObj), constDefinition);

                constObj.setAdr(value);
            }else{
                String assigmentErrorMsg = formatAssignmentMessage(currentTypeObj.getName(), symbolTypeToString(rightSide.getKind()), currentTypeObj.getName());

                logError(assigmentErrorMsg, constDefinition);
            }


        }
    }

    @Override
    public void visit(Type type){


        String typeName = type.getTypeName();

        currentTypeObj = Tab.noObj;

        currentType = type;

        if (!isDefined(typeName)){
            logError(formatSymbolNotDefinedMessage(typeName), type);

            return;
        }


        // simbol je vec definisan,sada treba videti da li je u pitanju tip
        Obj typeObj = Tab.find(typeName);

        if (typeObj.getKind() == Obj.Type)
            currentTypeObj = typeObj;
        else
            logError(formatSymbolName(typeObj.getName()) + " nije tip!", type);

        currentTypeStruct = currentTypeObj.getType();
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

        Obj varObj = null;
        // ako je deklaracija niza, onda postavljamo za tip array strukturu
        // inace postavljamo sam tip koji je naveden
        if (isArrayDecl)
            varObj = Tab.insert(Obj.Var, varName, arrayStruct);
        else
            varObj = Tab.insert(Obj.Var, varName, currentTypeObj.getType());

        logInfo(formatSymbolInfo(varObj), varDeclDefinition);
    }

    @Override
    public void visit(MethodName methodName){
        String name = methodName.getMethodName();

        // uzimamo da je podrazumevano noObj
        currentMethod = Tab.noObj;

        // ako je vec definisano onda prijavimo gresku
        if (isDefined(name))
            logError(formatAlreadyDefinedMessage(name), methodName);
        else
            currentMethod = Tab.insert(Obj.Meth, name, currentTypeStruct);

        // otvorimo opseg
        Tab.openScope();
    }

    @Override
    public void visit(MethodDecl methodDecl){

        // uvezujemo lokalne simbole samo ukoliko je
        // metoda dodata u tabelu simbola
        if (currentMethod != Tab.noObj) {
            Tab.chainLocalSymbols(currentMethod);

            logInfo(formatSymbolInfo(currentMethod), methodDecl);
            //logInfo(for);
        }

        // zatvorimo doseg
        Tab.closeScope();
    }


    @Override
    public void visit(ReturnTypeVoid voidReturnType){
        currentTypeStruct = Tab.noType;
    }

    @Override
    public void visit(FormParDecl formParDecl){
        // naziv parametra
        String formParName = formParDecl.getFormParName();

        // ako je vec definisan samo napisati gresku i vratiti se
        if (isDefinedInCurrentScope(formParName)){
            logError(formatAlreadyDefinedMessage(formParName), formParDecl);
            return;
        }

        // da li je niz?
        OptArrayDecl isArray = formParDecl.getOptArrayDecl();

        // niz struktura u slucaju da je tip niz nekog tipa podatka (npr int[])
        Struct arrayStruct = new Struct(Struct.Array, currentTypeStruct);

        Obj paramObj = null;

        if (isArray instanceof  OptArrayDeclared)
            paramObj = Tab.insert(Obj.Var, formParName, arrayStruct);
        else
            paramObj = Tab.insert(Obj.Var, formParName, currentTypeStruct);

        // informativno logovanje
        logInfo(formatSymbolInfo(paramObj), formParDecl);
    }

    /****************************************************************************
     * **************************************************************************
    *************     VISIT METODE ZA PRISTUP PROMENLJIVAMA     *****************
     ****************************************************************************
     ****************************************************************************/
    @Override
    public void visit(Designator designator){
        designator.obj = designator.getDesignatorRepeatList().obj;
    }

    @Override
    public void visit(DesignatorInitialName designatorInitialName){
        logInfo(designatorInitialName.getDesignatorName() +" initial", designatorInitialName);

        designatorObj = Tab.find(designatorInitialName.getDesignatorName());

        if (designatorObj == Tab.noObj)
            logError(formatSymbolNotDefinedMessage(designatorInitialName.getDesignatorName()), designatorInitialName);
    }

    @Override
    public void visit(DesignatorRepeatListDeclared designatorRepeatListDeclared){


        designatorRepeatListDeclared.obj = Tab.noObj;


        Obj designatorRepeatListObj = designatorRepeatListDeclared.getDesignatorRepeatList().obj;
        //Obj designatorRepeatObj = designatorRepeatListDeclared.getDesignatorRepeat().obj;

        DesignatorRepeat designatorRepeat = designatorRepeatListDeclared.getDesignatorRepeat();

        // da li je u pitanju pristup polju ili elementu niza
        if (designatorRepeat instanceof DesignatorRepeatField){
            boolean isClass = designatorRepeatListObj.getType().getKind() == Struct.Class;
            String message = "";

            String fieldName = ((DesignatorRepeatField) designatorRepeat).getFieldName();

            // pristup preko tacke je moguc samo za podatke koji su tipa Class
            // ukoliko nisu tipa class, jednostavno treba da prijavimo gresku i vratimo se
            if (!isClass){
                message = formatWrongTypeMessage(Struct.Class, designatorRepeatListObj.getType().getKind());
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // dohvatimo sve clanove klase(funkcije i metode)
            SymbolDataStructure classMembers = designatorRepeatListObj.getType().getMembers();

            // trazimo simbol sa datim nazivom
            Obj fieldObj = classMembers.searchKey(fieldName);

            // ukoliko dato polje ne postoji u klasi
            // javiti gresku
            if (fieldObj == null){
                message = formatClassMemberDoesntExistMessage(designatorRepeatListObj, fieldName);
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // nasli smo dato polje, samo taj objekat dodelimo cvoru AST-a
            designatorRepeatListDeclared.obj = fieldObj;

        }else{ // slucaj kada je DesignatorRepeat zapravo DesignatorRepeatExpr(pristup elementu niza)

        }
    }


    @Override
    public void visit(DesignatorRepeatListEpsilon designatorRepeatListEpsilon){
        designatorRepeatListEpsilon.obj = designatorObj;
    }

    @Override
    public void visit(DesignatorRepeatField designatorRepeatField){
        //logInfo(designatorRepeatField.getFieldName(), designatorRepeatField);
    }




    /**********************************************************************
     **********************************************************************
     ********************       VISIT METODE IZRAZA       *****************
     **********************************************************************
     ********************************************************************/

    @Override
    public void visit(Expr expr){
        // cvor koji predstavlja unarni minus
        OptMinus optMinus = expr.getOptMinus();

        // u pocetku stavljamo da je expr noType
        expr.struct = Tab.noType;

        // tip podatka iz cvora Term
        Struct leftOperandType = expr.getTerm().struct;

        // da li je naveden unarni operator
        // ako jeste onda term mora biti integer
        if (optMinus instanceof OptMinusDeclared){

            if (!leftOperandType.equals(Tab.intType)){
                logError("Operator unarni - ocekuje tip Int", expr);

                return;
            }
        }

        Struct rightOperandType = expr.getAddopTermList().struct;

        if (leftOperandType.equals(Tab.intType) && rightOperandType.equals(Tab.intType))
            expr.struct = Tab.intType;
        else if (!leftOperandType.equals(Tab.noType) && !rightOperandType.equals(Tab.noType))
            logError("Aritmeticki izrazi zahtevaju da operandi budu tipa Int", expr);
    }

    @Override
    public void visit(AddopTerm addopTerm){
        addopTerm.struct = addopTerm.getTerm().struct;
    }


    @Override
    public void visit(AddopTermListEpsilon addopTermListEpsilon){
        // podrazumevamo da kada je u pitanju epsilon smena
        // da tada kao da mnozimo sa * 1
        addopTermListEpsilon.struct = Tab.intType;
    }

    @Override
    public void visit(AddopTermListDeclared addopTermListDeclared){
        // tip levog operanda
        Struct leftOperandType = addopTermListDeclared.getAddopTermList().struct;

        // tip desnog operanda
        Struct rightOperandType = addopTermListDeclared.getAddopTerm().struct;

        addopTermListDeclared.struct = Tab.noType;

        if (leftOperandType.equals(Tab.intType) && rightOperandType.equals(Tab.intType))
            addopTermListDeclared.struct = Tab.intType;
        else if (!leftOperandType.equals(Tab.noType) && !rightOperandType.equals(Tab.noType))
            logError("Aritmeticki izrazi zahtevaju da operandi budu tipa Int", addopTermListDeclared);
    }


    @Override
    public void visit(Term term){
        // tip levog operanda
        Struct leftOperandStruct = term.getFactor().struct;

        // tip desnog operanda
        Struct rightOperandStruct = term.getMulopFactorList().struct;

        term.struct = Tab.noType;

        if (leftOperandStruct.equals(Tab.intType) && rightOperandStruct.equals(Tab.intType))
            term.struct = Tab.intType;
        else if (!leftOperandStruct.equals(Tab.noType) && !rightOperandStruct.equals(Tab.noType))
            logError("Aritmeticke operacije zahtevaju da operandi budu tipa Int", term);

    }

    @Override
    public void visit(MulopFactor mulopFactor){
        mulopFactor.struct = mulopFactor.getFactor().struct;
    }

    @Override
    public void visit(MulopFactorListEpsilon mulopFactorListEpsilon){
        mulopFactorListEpsilon.struct = Tab.intType;
    }

    @Override
    public void visit(MulopFactorListDeclared mulopFactorListDeclared){
        // sve je isto kao i za addopTermListDeclared
        mulopFactorListDeclared.struct = Tab.noType;

        Struct leftOperandType = mulopFactorListDeclared.getMulopFactorList().struct;

        Struct rightOperandType = mulopFactorListDeclared.getMulopFactor().struct;

        if (leftOperandType.equals(Tab.intType) && rightOperandType.equals(Tab.intType))
            mulopFactorListDeclared.struct = Tab.intType;
        else if (!leftOperandType.equals(Tab.noType) && !rightOperandType.equals(Tab.noType))
            logError("Aritmeticke operacije zahtevaju da operandi budu tipa Int", mulopFactorListDeclared);
    }


    @Override
    public void visit(FactorFuncCallOrVar factorFuncCallOrVar){
        factorFuncCallOrVar.struct = factorFuncCallOrVar.getDesignator().obj.getType();
    }

    @Override
    public  void visit(FactorConst factorConst){
        factorConst.struct = Tab.noType;

        ConstValue constValue = factorConst.getConstValue();

        if (constValue instanceof NumConst)
            factorConst.struct = Tab.intType;
        else if (constValue instanceof CharConst)
            factorConst.struct = Tab.charType;
        else
            factorConst.struct = boolType;

    }

    @Override
    public void visit(FactorNew factorNew){
        factorNew.struct = Tab.noType;
    }

    @Override
    public void visit(FactorExpr factorExpr) {
        factorExpr.struct = factorExpr.getExpr().struct;
    }





















}