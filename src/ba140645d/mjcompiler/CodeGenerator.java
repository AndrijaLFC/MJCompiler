package ba140645d.mjcompiler;


import ba140645d.mjcompiler.ast.ProgramName;
import ba140645d.mjcompiler.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

import java.util.Collection;

public class CodeGenerator extends VisitorAdaptor {


    private static final String ORD_METH_NAME = "ord";

    private static final String CHR_METH_NAME = "chr";

    private static final String LEN_METH_NAME = "len";

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

            // narebom return napustamo funkciju
            Code.put(Code.return_);

        });

    }

    @Override
    public void visit(ProgramName programName){

        generateCodeForPredefinedMethods();

        Obj programObj = Tab.find(programName.getProgramName());

        // simboli iz scope-a programa
        Collection<Obj> programSymbols = programObj.getLocalSymbols();

        for(Obj symbol : programSymbols){

        }
    }
}