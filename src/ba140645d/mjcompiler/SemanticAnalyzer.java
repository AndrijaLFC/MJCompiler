package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import ba140645d.mjcompiler.utilities.StructLinkedList;
import com.sun.istack.internal.Nullable;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

import java.util.Collection;
import java.util.Iterator;

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

    // broj formalnih parametara metode
    private int currentMethodFormParNum = 0;

    // objekat pocetnog designatora
    private Obj designatorObj = Tab.noObj;

    private static final String ARR_INDEX_TYPE_ERR = "Indeks niza mora biti tipa Int!";

    private static final String NOT_ARR_TYPE_ERR_MSG = "Tip mora biti niz!";




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


    private String formatWrongSymbolKindMessage(int actualSymbolKind, int expectedSymbolKind){
        return "Stvarna vrsta simbola je : {" + symbolKindToString(actualSymbolKind) + "}. Ocekivana vrsta simbola je : {" + symbolKindToString(expectedSymbolKind) + "}";
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

    /**
     * Konvertuje kod vrste simbola u string
     * @param symbolKind kod vrste simbola
     * @return vrsta simbola kao string
     */
    private String symbolKindToString(int symbolKind){
        String retVal = "Unknown";
        switch(symbolKind){
            case Obj.Con :  retVal =  "Con";    break;
            case Obj.Elem : retVal = "Elem";    break;
            case Obj.Fld : retVal = "Fld";      break;
            case Obj.Meth : retVal = "Meth";    break;
            case Obj.Prog : retVal = "Prog";    break;
            case Obj.Type : retVal = "Type";    break;
            case Obj.Var : retVal = "Var";      break;
        }

        return retVal;
    }

    /**
     * Konvertuje kod tipa simbola u string
     * @param symbolType tip simbola predstavljen celobrojnom vrednoscu
     * @return vraca zeljeni tip simbola u vidu stringa
     */
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

    /**
     * Formatira opste informacije simbola za ispis ( naziv, vrsta, tip)
     * @param symbol zeljeni simbol za ispis
     * @return formatirana poruka sa opstim informacijama simbola
     */
    private String formatSymbolInfo(Obj symbol){
        String symbolNameFormat = formatSymbolName(symbol.getName());
        String symbolKind = symbolKindToString(symbol.getKind());
        String symbolType = symbolTypeToString(symbol.getType().getKind());

        String format = symbolNameFormat + " kind : " + symbolKind + ", type : " + symbolType;

        if (symbol.getType().getKind() == Struct.Array)
            format += " of " + symbolTypeToString(symbol.getType().getElemType().getKind());


        return format;

    }


    /**
     * Formatira ispis koji naglasava koliko parametara ima data metoda
     * @param funcObj simbol metode
     * @return formatirana poruka za ispis
     */
    private String formatFuncParNumIncorrectMessage(Obj funcObj){

        if (funcObj.getLevel() == 0)
            return "Funkcija : {" + funcObj.getName() + "} ne prima parametre";
        else if (funcObj.getLevel() == 1)
            return "Funkcija : {" + funcObj.getName() + "} ima " + funcObj.getLevel() + "parametar";
        else
            return "Funkcija : {" + funcObj.getName() + "} ima " + funcObj.getLevel() + "parametar";
    }


    /**
     * Formatira ispis greske kada tip formalnog i stvarnog parametra metode nije kompatabilan pri dodeli
     * @param actualType tip stvarnog parametra metode
     * @param formalType tip formalnog parametra metode
     * @param paramNum redni broj parametra u metodi
     * @return formatirana poruka za ispis
     */
    private String formatFuncWrongParTypeMessage(Struct actualType, Struct formalType, int paramNum){
        String wrongTypeMsg = formatWrongTypeMessage(formalType.getKind(), actualType.getKind());

        return "Parametar broj : " + paramNum + ". " + wrongTypeMsg;
    }


    /**
     * Funkcija koja obmotava ponasanje za visit metode vezane za izraze
     * Moze biti maksimalno jedan od tipova operanda izostavljen, ali ne i oba ( smatra se greskom )
     * @param leftOperandType tip levog operanda, mooze biti izostavljen
     * @param rightOperandType tip desnog operanda, moze biti izostavljen
     * @return vraca tip koji je rezultat izraza
     */
    private Struct checkIfValidExpr(@Nullable  Struct leftOperandType, @Nullable Struct rightOperandType, SyntaxNode visitedNode){
        Struct returnType = Tab.noType;

        if (leftOperandType == null && rightOperandType == null){
            System.err.println("FATAL ERROR!!! Function checkIfValidExpr has null left and right parameter");

            return returnType;
        }


        // postoji samo jedan operand i samo se vratimo
        if (rightOperandType == null)
            return leftOperandType;
        else if (leftOperandType == null)
            return rightOperandType;


        if (leftOperandType.equals(Tab.intType) && rightOperandType.equals(Tab.intType))
            return Tab.intType;
        else if (!leftOperandType.equals(Tab.noType) || !rightOperandType.equals(Tab.noType))
            logError("Aritmeticki izrazi zahtevaju da operandi budu tipa Int", visitedNode);


        return returnType;
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


            // postavljamo tip podatka sa desne strane i citamo vrednost
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

            // ispitujemo da li je dodela moguca
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

    /****************************************************************************
     * **************************************************************************
     ***************     VISIT METODE ZA DEFINISANJE METODA     *****************
     ***************************    I PARAMETARA   ******************************
     ****************************************************************************/

    @Override
    public void visit(MethodName methodName){
        String name = methodName.getMethodName();

        // uzimamo da je podrazumevano noObj
        currentMethod = Tab.noObj;

        // inicijalizujemo broj formalnih parametara na 0
        currentMethodFormParNum = 0;

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

            // postavljamo broj formalnih parametara
            currentMethod.setLevel(currentMethodFormParNum);

            logInfo(formatSymbolInfo(currentMethod), methodDecl);
           /// logInfo(Integer.toString(currentMethodFormParNum) + " je broj formalnih parametara", methodDecl);
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

        // uvecavamo broj formalnih parametara
        currentMethodFormParNum++;

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


        Obj designatorObj = designatorRepeatListDeclared.getDesignatorRepeatList().obj;
        //Obj designatorRepeatObj = designatorRepeatListDeclared.getDesignatorRepeat().obj;

        DesignatorRepeat designatorRepeat = designatorRepeatListDeclared.getDesignatorRepeat();

        // da li je u pitanju pristup polju ili elementu niza
        if (designatorRepeat instanceof DesignatorRepeatField){
            boolean isClass = designatorObj.getType().getKind() == Struct.Class;
            String message = "";

            String fieldName = ((DesignatorRepeatField) designatorRepeat).getFieldName();

            // pristup preko tacke je moguc samo za podatke koji su tipa Class
            // ukoliko nisu tipa class, jednostavno treba da prijavimo gresku i vratimo se
            if (!isClass){
                message = formatWrongTypeMessage(Struct.Class, designatorObj.getType().getKind());
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // dohvatimo sve clanove klase(funkcije i metode)
            SymbolDataStructure classMembers = designatorObj.getType().getMembers();

            // trazimo simbol sa datim nazivom
            Obj fieldObj = classMembers.searchKey(fieldName);

            // ukoliko dato polje ne postoji u klasi
            // javiti gresku
            if (fieldObj == null){
                message = formatClassMemberDoesntExistMessage(designatorObj, fieldName);
                logError(message, designatorRepeatListDeclared);

                return;
            }

            // nasli smo dato polje, samo taj objekat dodelimo cvoru AST-a
            designatorRepeatListDeclared.obj = fieldObj;

        }else{ // slucaj kada je DesignatorRepeat zapravo DesignatorRepeatExpr(pristup elementu niza)
            Expr arraySize = ((DesignatorRepeatExpr)designatorRepeatListDeclared.getDesignatorRepeat()).getExpr();

            if (!arraySize.struct.equals(Tab.intType)){
                logError(ARR_INDEX_TYPE_ERR, designatorRepeatListDeclared);

                return;
            }

            if (designatorObj.getType().getKind() != Struct.Array){
                logError(NOT_ARR_TYPE_ERR_MSG, designatorRepeatListDeclared.getDesignatorRepeatList());
            }

            // naziv elementa
            String elementName = designatorObj.getName();

            // sam tip elementa
            // posto je tip designatorObj niz nekog tipa, treba da dohvatimo taj tip
            Struct elementType = designatorObj.getType().getElemType();

            //objekat koji je tipa element niza
            designatorRepeatListDeclared.obj = new Obj(Obj.Elem, elementName, elementType);
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


        expr.struct = checkIfValidExpr(leftOperandType, rightOperandType, expr.getTerm());
    }

    @Override
    public void visit(AddopTerm addopTerm){
        addopTerm.struct = addopTerm.getTerm().struct;
    }


    @Override
    public void visit(AddopTermListEpsilon addopTermListEpsilon){
        // podrazumevamo da kada je u pitanju epsilon smena
        // da tada kao da mnozimo sa * 1 odnosno sabiramo sa 0
        addopTermListEpsilon.struct = null;
    }

    @Override
    public void visit(AddopTermListDeclared addopTermListDeclared){
        // tip levog operanda
        Struct leftOperandType = addopTermListDeclared.getAddopTermList().struct;

        // tip desnog operanda
        Struct rightOperandType = addopTermListDeclared.getAddopTerm().struct;

       addopTermListDeclared.struct =  checkIfValidExpr(leftOperandType, rightOperandType, addopTermListDeclared.getAddopTermList());
    }


    @Override
    public void visit(Term term){
        // tip levog operanda
        Struct leftOperandType = term.getFactor().struct;

        // tip desnog operanda
        Struct rightOperandType = term.getMulopFactorList().struct;

        term.struct = checkIfValidExpr(leftOperandType, rightOperandType, term);

    }

    @Override
    public void visit(MulopFactor mulopFactor){
        mulopFactor.struct = mulopFactor.getFactor().struct;
    }

    @Override
    public void visit(MulopFactorListEpsilon mulopFactorListEpsilon){
        mulopFactorListEpsilon.struct = null;
    }

    @Override
    public void visit(MulopFactorListDeclared mulopFactorListDeclared){
        // sve je isto kao i za addopTermListDeclared
        mulopFactorListDeclared.struct = Tab.noType;

        Struct leftOperandType = mulopFactorListDeclared.getMulopFactorList().struct;

        Struct rightOperandType = mulopFactorListDeclared.getMulopFactor().struct;

        mulopFactorListDeclared.struct = checkIfValidExpr(leftOperandType, rightOperandType, mulopFactorListDeclared.getMulopFactorList());
    }


    @Override
    public void visit(FactorFuncCallOrVar factorFuncCallOrVar){
        factorFuncCallOrVar.struct = factorFuncCallOrVar.getDesignator().obj.getType();

        // cvor koji sadrzi parametre fje
        OptParenthesesActPars funcPars = factorFuncCallOrVar.getOptParenthesesActPars();

        // ako nije poziv f-je onda samo se vratimo
        if (!(funcPars instanceof OptParenthesesActParsDeclared))
            return;

        // dohvatamo objekat
        Obj funcObj = factorFuncCallOrVar.getDesignator().obj;

        // u pitanju je poziv fje a objekat nije vrsta fja, onda samo ispisemo gresku i zavrsimo obradu
        if (funcObj.getKind() != Obj.Meth){
            logError(formatWrongSymbolKindMessage(funcObj.getKind(), Obj.Meth),  factorFuncCallOrVar.getDesignator());
            return;
        }

        //dohvatimo listu stvarnih parametara
        StructLinkedList actualParsList = factorFuncCallOrVar.getOptParenthesesActPars().structlinkedlist;

        // proverimo da li je broj elemenata liste jednak broju formalnih parametara fje
        // ukoliko nije jednostavno se vratimo nazad
        if (actualParsList.size() != funcObj.getLevel()){
            logError(formatFuncParNumIncorrectMessage(funcObj), factorFuncCallOrVar);

            return;
        }

        // kolekcija svih lokalnih simbola fje
        // prvih N simbola su formalni parametri funkcije
        Iterator<Obj> formalParsIt = funcObj.getLocalSymbols().iterator();

        // broj formalnih parametara
        int parNum = funcObj.getLevel();


        for(int i = 0; i <  parNum; ++i){
            // formalni tip parametra
            Struct formParType = formalParsIt.next().getType();

            // stvarni tip parametra
            Struct actualParType = actualParsList.get(i);

            // ako nije moguce dodeliti onda treba ispisati gresku
            if (!actualParType.assignableTo(formParType))
                logError(formatFuncWrongParTypeMessage(actualParType, formParType, i + 1), factorFuncCallOrVar);
        }

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





    /**********************************************************************
     **********************************************************************
     ****************   VISIT METODE Stvarnih Parametara  *****************
     **********************************************************************
     ********************************************************************/

    @Override
    public void visit(OptParenthesesActParsEpsilon noParams){

        noParams.structlinkedlist = new StructLinkedList();

       // logInfo("Visited OptParanthesesActParsEpsilon", noParams);
    }

    @Override
    public void visit(OptParenthesesActParsDeclared params){
        params.structlinkedlist = params.getOptActPars().structlinkedlist;

       // logInfo("Visited OptParanthesesActParsDeclared", params);
    }

    @Override
    public void visit(OptActParsDeclared actualPars){
        actualPars.structlinkedlist = actualPars.getActPars().structlinkedlist;

       // logInfo("Visited OptActParsDeclared ", actualPars);
    }

    @Override
    public void visit(OptActParsEpsilon noPars){
        // epsilon cvor, ovde samo kreiramo listu
        noPars.structlinkedlist = new StructLinkedList();

       // logInfo("Visited OptActParsEpsilon", noPars);
    }

    @Override
    public void visit(ActPars actPars){
        // dohvatimo parametra iz izraza
        Struct paramType = actPars.getExpr().struct;

        // dodamo tip stvarnog parametra u listu
        (actPars.structlinkedlist = actPars.getActParsRepeatList().structlinkedlist).add(paramType);

       // logInfo("Visited ActPars", actPars);
    }

    @Override
    public void visit(ActParsRepeatListDeclared actParsRepeatListDeclared){
        actParsRepeatListDeclared.structlinkedlist = actParsRepeatListDeclared.getActParsRepeatList().structlinkedlist;

        actParsRepeatListDeclared.structlinkedlist.add(actParsRepeatListDeclared.getActParsRepeat().struct);
    }


    @Override
    public void visit(ActParsRepeatListEpsilon actParsRepeatListEpsilon){
        actParsRepeatListEpsilon.structlinkedlist = new StructLinkedList();
    }

    @Override
    public void visit(ActParsRepeat actualPars){
        actualPars.struct = actualPars.getExpr().struct;
    }



}