package  ba140645d.mjcompiler;


import ba140645d.log4j.Log4JUtil;
import ba140645d.mjcompiler.ast.Program;
import ba140645d.mjcompiler.ast.SyntaxNode;
import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.*;

class MJSemanticTest {
    static {
        DOMConfigurator.configure(Log4JUtil.instance().findLoggerConfigFile());
        Log4JUtil.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(MJLexerTest.class);
        Reader bufferReader = null;

        try{
            File source = new File(args[0]);
            log.info("Compiling source file :" + source.getAbsolutePath());

            bufferReader = new BufferedReader(new FileReader(source));

            Yylex lexer = new Yylex(bufferReader);

            Parser parser = new Parser(lexer);

            Symbol symbol = parser.parse();

            SyntaxNode root = (SyntaxNode)symbol.value;

            if (!(root instanceof Program) || parser.errorDetected) {
                log.error("Sintaksna greska! Prevodjenje se ne moze nastaviti");

                return;
            }

            Program program = (Program)root;

            log.info(program.toString(""));

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

            log.info("====================== Performing Semantic Check ======================");

            root.traverseBottomUp(semanticAnalyzer);

            if (!semanticAnalyzer.isSemanticallyCorrect()){
                log.error("Semanticka greska! Prevodjenje se ne moze nastaviti");

                return;
            }



        } catch (FileNotFoundException e) {

        } finally{
            if (bufferReader != null) try { bufferReader.close(); } catch (IOException exc) { exc.printStackTrace(); }
        }
    }
}