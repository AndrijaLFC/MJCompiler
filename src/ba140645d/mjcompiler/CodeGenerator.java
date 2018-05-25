package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.*;

public class CodeGenerator extends VisitorAdaptor {


    private static final String ORD_METH_NAME = "ord";

    private static final String CHR_METH_NAME = "chr";

    private static final String LEN_METH_NAME = "len";

    private static final String MAIN_METH_NAME = "main";

    private static final int ALLOC_BYTES = 0;

    private static final int ALLOC_WORDS = 1;

    private Obj designatorObj = Tab.noObj;

    private static final Obj minus1constObj = new Obj(Obj.Con, "", Tab.intType, -1, 0);

    private static final Obj num1constObj = new Obj(Obj.Con, "", Tab.intType, 1, 0);


    // stek koji pamti mesta za prepravljanje adresa skokova
    // gde treba da skocimo ako je uslov true
    private Stack<List<Integer>> truePatchStack = new Stack<>();

    // stek koji pamti mesta za prepravljanje adresa skokova
    // gde treba da skocimo ako je uslov false
    private Stack<List<Integer>> falsePatchStack = new Stack<>();

    // svi simboli programa
    private HashMap<String, Obj> programSymbols = new HashMap<>();

    /**
     * Generise kod za predefinisane metode
     * Predefinisane metode su : ord, chr, len
     */
    private void generateCodeForPredefinedMethods(){
        // simboli iz scope-a univerzuma
        Collection<Obj> universeSymbols = Tab.currentScope.getLocals().symbols();

        // kod universe scope-a postoje predefinisani tipovi podataka, metode
        // i naziv programa, od interesa su nam samo predefinisane metode
        // i za njih zelimo da generisemo kod
        universeSymbols.forEach((universeSymbol)->{
            // ako nije metoda, ne obradjujemo
            if (universeSymbol.getKind() != Obj.Meth)
                return;

            // postavljamo adresu funkcije na trenutni pokazivac PC-a
            universeSymbol.setAdr(Code.pc);

            // instrukcija koja frsi pamcenje stack pointera, function pointera i
            // inicijalizuje prostor za parametre i lokalne promenljive
            Code.put(Code.enter);

            // format enter instrukcije je enter paramSize, Lsize gde je Lsize ukupan prostor
            // koji zauzimaju

            // prvo ide broj parametara
            Code.put(universeSymbol.getLevel());

            // pa ukupan broj lokalnih simbola(parametri + lokalne promenljive)
            Code.put(universeSymbol.getLocalSymbols().size());

            // ucitamo vrednost parametra u expr stek
            Code.put(Code.load_n);

            // ukoliko je metoda len u pitanju, onda treba pored gore navedenih akcija
            // takodje i dodati instrukciju arraylength, koja ce nam dati duzinu samog niza
            if (universeSymbol.getName().equals("len"))
                Code.put(Code.arraylength);

            // kao i kod x86 jednostavno uklonimo sve lokalne promenljive sa steka
            // i bp(ovde je to fp) vratimo na prethodnu vrednost
            Code.put(Code.exit);

            // narebom return napustamo funkciju  pc = pop();
            Code.put(Code.return_);
        });
    }


    private void storeDesignator(Obj designatorObj){
        if (designatorObj.getKind() != Obj.Elem)
            Code.store(designatorObj);
        else if (designatorObj.getType() == Tab.intType)
            Code.put(Code.astore);
        else
            Code.put(Code.bastore);
    }

    private void loadDesignator(Obj designatorObj){
        if (designatorObj.getKind() != Obj.Elem)
            Code.load(designatorObj);
        else if (designatorObj.getType() == Tab.intType)
            Code.put(Code.aload);
        else
            Code.put(Code.baload);
    }


    private int getRelopCode(Relop  relop){
        if (relop instanceof  RelopEquals)
            return Code.eq;
        else if (relop instanceof  RelopNotEquals)
            return Code.ne;
        else if (relop instanceof  RelopGreater)
            return Code.gt;
        else if (relop instanceof RelopGreaterEqual)
            return Code.ge;
        else if (relop instanceof  RelopLesser)
            return Code.lt;
        else if (relop instanceof RelopLesserEqual)
            return Code.le;


        // nikad nece doci do ovoga
        return Code.eq;
    }

    private void adrPatching(CondFact condFact){
        // adresa na koju treba da skoce svi uslovi koji su ispunili true
        int fixupAdr  = Code.pc;

    }
    @Override
    public void visit(Program program){
        Tab.closeScope();
    }

    @Override
    public void visit(ProgramName programName){

        // generisanje koda za predefinisane metode
        generateCodeForPredefinedMethods();

        Obj programObj = Tab.find(programName.getProgramName());

        // simboli iz scope-a programa
        Collection<Obj> programSymbols = programObj.getLocalSymbols();

        // otvaranje opsega zarad lakseg nalazenja simbola
        Tab.openScope();

        // dodavanje simbola programa u trenutni opseg
        // moramo da navedemo koliko prostora treba da zauzimaju globalne promenljive, velicina
        // se izrazava u recima(jedna rec je 4 bajta)
        programSymbols.forEach((symbol)->{
            Tab.currentScope().addToLocals(symbol);
            if (symbol.getKind() == Obj.Var)
                Code.dataSize++;
        });
    }


    @Override
    public void visit(MethodName method){
        String methodName = method.getMethodName();

        Obj methodObj = Tab.find(methodName);

        // ukoliko je u pitanju main metoda, moramo navesti koja je njena adresa
        if (methodName.equals(MAIN_METH_NAME)){
            Code.mainPc = Code.pc;
        }

        // namestamo adresu metode na trenutnu vrednost pc
        methodObj.setAdr(Code.pc);

        // broj formalnih parametara
        int paramNum = methodObj.getLevel();

        // ukupan broj lokalnih simbola u metodi( parametri + promenljive)
        int localSymbolNum = methodObj.getLocalSymbols().size();

        // kod za inicijalizaciju metode pri ulasku, instrukcija ima 2 parametra
        // broj parametara i ukupan broj lokalnih simbola
        Code.put(Code.enter);

        // prvo ide broj parametara
        Code.put(paramNum);

        // pa zatim i ukupan broj simbola
        Code.put(localSymbolNum);


        //  otvorimo scope zarad lakse pretrage simbola
        Tab.openScope();

        // dohvatimo sve simbole metode
        Collection<Obj> methodSymbols = methodObj.getLocalSymbols();

        // dodamo ih u scope
        for (Obj methodSymbol : methodSymbols) {
            Tab.currentScope.addToLocals(methodSymbol);
        }

    }

    @Override
    public void visit(MethodDecl methodDecl){
        // dohvatimo ime metode
        String methodName = methodDecl.getMethodName().getMethodName();

        // dohvatimo objekat(simbol) metode
        Obj methodObj = Tab.find(methodName);

        // ciscenje steka od lokalnih simbola
        Code.put(Code.exit);

        // vracanje nazad
        Code.put(Code.return_);
    }

    @Override
    public void visit(Expr expr){
        expr.struct = expr.getTerm().struct;
    }

    @Override
    public void visit(OptMinusDeclared minusOp){
        //Code.load(minus1constObj);
    }


    @Override
    public void visit(Term term){
        if (term.getParent() instanceof Expr && ((Expr)term.getParent()).getOptMinus() instanceof OptMinusDeclared)
            Code.put(Code.neg);

        term.struct = term.getFactor().struct;
    }

    @Override
    public void visit(AddopTermListDeclared addopTermList){
        Addop addOp = addopTermList.getAddopTerm().getAddop();

        if (addOp instanceof AddopAddition)
                Code.put(Code.add);
        else if (addOp instanceof  AddopSubtraction)
            Code.put(Code.sub);

        addopTermList.struct = addopTermList.getAddopTerm().struct;
    }

    @Override
    public void visit(AddopTerm addopTerm){
        addopTerm.struct = addopTerm.getTerm().struct;
    }


    @Override
    public void visit(MulopFactorListDeclared mulopFactorList){
        Mulop mulop = mulopFactorList.getMulopFactor().getMulop();

        if (mulop instanceof  MulopMultiplication)
            Code.put(Code.mul);
        else if (mulop instanceof  MulopDivision)
            Code.put(Code.div);
        else if (mulop instanceof  MulopModuo)
            Code.put(Code.rem);

        mulopFactorList.struct = mulopFactorList.getMulopFactor().struct;
    }

    @Override
    public void visit(MulopFactor mulopFactor){
        mulopFactor.struct = mulopFactor.getFactor().struct;
    }

    @Override
    public void visit(FactorFuncCallOrVar factor){
        Obj designatorObj = factor.getDesignator().obj;


        if (designatorObj.getKind() == Obj.Meth) {
            Code.put(Code.call);
            // treba nam relativan pomeraj u odnosu na instrukciju call
            Code.put2(designatorObj.getAdr() - Code.pc + 1);
        }else
            loadDesignator(designatorObj);

        factor.struct = factor.getDesignator().obj.getType();
    }


    @Override
    public void visit(FactorConst factorConst){
        ConstValue constValue = factorConst.getConstValue();

        // objekat konstante
        Obj constObj = null;

        // proverimo kog je tipa konstanta i kreiramo odgovarajuci simbol
        if (constValue instanceof NumConst)
            constObj = new Obj(Obj.Con, "",Tab.intType, ((NumConst) constValue).getNumConst(), 0);
        else if (constValue instanceof  CharConst)
            constObj = new Obj(Obj.Con, "", Tab.charType, ((CharConst) constValue).getCharConst(), 0);
        else if (constValue instanceof  BoolConst)
            constObj = new Obj(Obj.Con, "", SemanticAnalyzer.boolType, ((BoolConst) constValue).getBoolConst() ? 1 : 0, 0 );

        Code.load(constObj);

        factorConst.struct = constObj.getType();
    }


    @Override
    public void visit(FactorNew factorNew){
        // ispitujemo da li je alokacija niza
        boolean isArrayAlloc = factorNew.getOptArrExpr() instanceof  OptArrExprDeclared;

        Struct newType = Tab.find(factorNew.getType().getTypeName()).getType();
        // za sad ne podrzavamo dinamicku alokaciju tipova, vec samo nizova, semanticka analiza je proverila
        // vec uslov za to, stoga mi ne moramo

        Code.put(Code.newarray);

        if (newType == Tab.intType)
            Code.put(ALLOC_WORDS);
        else
            Code.put(ALLOC_BYTES);

        factorNew.struct = newType;
    }


    @Override
    public void visit(FactorExpr factorExpr){
        factorExpr.struct = factorExpr.getExpr().struct;
    }


    /****************************************************************************
     ****************************************************************************
     ******************     VISIT METODE ZA LOGICKE IZRAZE    *******************
     ****************************************************************************
     ****************************************************************************/


    @Override
    public void visit(Condition condition){

    }

    @Override
    public void visit(ConditionRepeatList conditionRepeatList){

    }

    @Override
    public void visit(CondTerm condTerm){

    }

    @Override
    public void visit(CondFactRepeatList condFactRepeatList){

    }

    @Override
    public void visit(CondFactRepeat condFactRepeat){

    }

    @Override
    public void visit(CondFact condFact){

        // podrazumevano je poredjenje eq, za slucaj kada je promenljiva samo boolean
        int relopCode = Code.eq;

        if (condFact.getOptRelopExpr() instanceof  OptRelopExprDeclared)
            relopCode = getRelopCode(((OptRelopExprDeclared) condFact.getOptRelopExpr()).getRelop());


        Code.putFalseJump(relopCode, 0);

        falsePatchStack.peek().add(Code.pc - 2);
    }



    @Override
    public void visit(OptRelopExprEpsilon exprEpsilon){
        loadDesignator(num1constObj);
    }


    /****************************************************************************
     ****************************************************************************
     *************     VISIT METODE ZA PRISTUP PROMENLJIVAMA     ****************
     *****************************    I FUNKCIJAMA    ***************************
     ****************************************************************************/

    @Override
    public void visit(Designator designator){
        // u pitanju je B nivo, tu imamo samo promenljive i nizove
        // nema klasa, stoga ne moramo da imamo neki poseban kod za pristup poljima objekata
        designator.obj = Tab.find(designator.getDesignatorInitialName().getDesignatorName());


        // ako nije niz vratimo se
        if (designator.obj.getType().getKind() != Struct.Array)
            return;


        boolean isElemAccess = designator.getDesignatorRepeatList() instanceof  DesignatorRepeatListDeclared;

        if (isElemAccess)
            designator.obj = new Obj(Obj.Elem, "", designator.obj.getType().getElemType());
    }

    @Override
    public void visit(DesignatorInitialName designatorInitialName){
        String designatorName = designatorInitialName.getDesignatorName();

        designatorObj = Tab.find(designatorName);

        // fleg koji oznacava da li pristupamo elementu niza
        boolean isElemAccess = ((Designator)designatorInitialName.getParent()).getDesignatorRepeatList() instanceof  DesignatorRepeatListDeclared;


        // treba da izgenerismo dodatnu instrukciju za ucitavanje adrese niza
        // ukoliko pristupamo elementu niza
        if (isElemAccess)
            loadDesignator(designatorObj);
    }

    /****************************************************************************
     ****************************************************************************
     *********************     VISIT METODE ZA NAREDBE     **********************
     ****************************************************************************
     ****************************************************************************/


    @Override
    public void visit(PrintStatement printStatement){

        // ako je izraz charType onda radimo ispis karaktera, inace ispisujemo broj
        if (printStatement.getExpr().struct == Tab.charType)
            Code.put(Code.bprint);
        else
            Code.put(Code.print);

    }

    @Override
    public void visit(OptPrintNumConstDeclared width){
        Code.load(new Obj(Obj.Con, "", Tab.intType, width.getNumConst(), 0));
    }

    @Override
    public void visit(OptPrintNumConstEpsilon width){
        Code.load(new Obj(Obj.Con, "", Tab.intType, 1, 0));
    }


    @Override
    public void visit(DesignatorStatementAssign assignmentStmt){
        Obj designatorObj = assignmentStmt.getDesignator().obj;


        storeDesignator(designatorObj);

    }

    @Override
    public void visit(DesignatorStatementIncrement incrementStmt){

        // ukoliko se na steku nalaze array_addr, indeks
        // treba da dupliramo poslednje dve vrednosti na steku
        // jer cemo isti taj element koristiti za skladistenje rezultata
        if (incrementStmt.getDesignator().obj.getKind() == Obj.Elem)
            Code.put(Code.dup2);

        // ucitamo prvo staru vrednost elementa
        loadDesignator(incrementStmt.getDesignator().obj);

        // ucitamo konstantu 1
        Code.load(num1constObj);

        // izvrsimo operaciju sabiranja
        Code.put(Code.add);

        // sacuvamo u element niza
        storeDesignator(incrementStmt.getDesignator().obj);

    }

    @Override
    public void visit(IfStart ifStart){
        // stavimo na stek listu za obradu adresa skoka za trenutni if/else/while
        truePatchStack.add(new LinkedList<>());

        // isto kao gore, ali za skok ako je uslov netacan if/else/while
        falsePatchStack.add(new LinkedList<>());
    }
}