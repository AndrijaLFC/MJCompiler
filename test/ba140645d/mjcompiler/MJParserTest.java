package ba140645d.mjcompiler;

import java.io.*;

import ba140645d.log4j.Log4JUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;


public class MJParserTest {

    static {
        DOMConfigurator.configure(Log4JUtil.instance().findLoggerConfigFile());
        Log4JUtil.instance().prepareLogFile(Logger.getRootLogger());
    }

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(MJLexerTest.class);
        Reader bufferReader = null;

        try{
            File source = new File("test/testProgram.mj");
            log.info("Compiling source file :" + source.getAbsolutePath());

            bufferReader = new BufferedReader(new FileReader(source));

            Yylex lexer = new Yylex(bufferReader);

            Parser parser = new Parser(lexer);

            Symbol symbol = parser.parse();



        } catch (FileNotFoundException e) {

        } finally{
            if (bufferReader != null) try { bufferReader.close(); } catch (IOException exc) { exc.printStackTrace(); }
        }
    }
}
