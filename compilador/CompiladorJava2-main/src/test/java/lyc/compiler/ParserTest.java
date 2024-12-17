package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.factories.ParserFactory;
import lyc.compiler.symboltable.SymbolTable;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;
import static lyc.compiler.Constants.EXAMPLES_ROOT_DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ParserTest {

    @Test
    public void assignmentWithExpression() throws Exception {
        System.out.println("---------Corriendo test de ASIGNACION con EXPRESION---------------");
        compilationSuccessful("init{ c,d,e : Int } c := d * (e - 21) / 4");
        System.out.println("---------FIN test de ASIGNACION con EXPRESION---------------\n");

    }

    @Test
    public void syntaxError() {
        System.out.println("---------Corriendo test de \"Error de compilacion\"---------------");
        compilationError("1234");
        System.out.println("---------FIN test de \"Error de compilacion\"---------------\n");
    }

    @Test
    void assignments() throws Exception {
        System.out.println("---------Corriendo test de ASIGNACIONES---------------");
        compilationSuccessful(readFromFile("assignments.txt"));
        System.out.println("---------FIN test de ASIGNACIONES---------------\n");
    }

    @Disabled
    @Test
    void write() throws Exception {
        System.out.println("---------Corriendo test de WRITE---------------");
        compilationSuccessful(readFromFile("write.txt"));
        System.out.println("---------FIN test de WRITE---------------\n");
    }

    @Test
    void read() throws Exception {
        System.out.println("---------Corriendo test de READ---------------");
        compilationSuccessful(readFromFile("read.txt"));
        System.out.println("---------FIN test de READ---------------\n");

    }

    @Test
    void comment() throws Exception {
        System.out.println("---------Corriendo test de COMMENT---------------");
        compilationSuccessful(readFromFile("comment.txt"));
        System.out.println("---------FIN test de COMMENT---------------\n");
    }

    @Test
    void init() throws Exception {
        System.out.println("---------Corriendo test de INIT---------------");
        compilationSuccessful(readFromFile("init.txt"));
        System.out.println("---------FIN test de INIT---------------\n");

    }

    @Test
    void and() throws Exception {
        System.out.println("---------Corriendo test de AND---------------");
        compilationSuccessful(readFromFile("and.txt"));
        System.out.println("---------FIN test de AND---------------\n");

    }

    @Test
    void or() throws Exception {
        System.out.println("---------Corriendo test de OR---------------");
        compilationSuccessful(readFromFile("or.txt"));
        System.out.println("---------FIN test de OR---------------\n");

    }

    @Test
    void not() throws Exception {
        System.out.println("---------Corriendo test de NOT---------------");
        compilationSuccessful(readFromFile("not.txt"));
        System.out.println("---------FIN test de NOT---------------\n");

    }


    @Test
    void ifStatement() throws Exception {
        System.out.println("---------Corriendo test de IF---------------");
        compilationSuccessful(readFromFile("if.txt"));
        System.out.println("---------FIN test de IF---------------\n");

    }

    @Test
    void whileStatement() throws Exception {
        System.out.println("---------Corriendo test de WHILE---------------");
        compilationSuccessful(readFromFile("while.txt"));
        System.out.println("---------FIN test de WHILE---------------\n");

    }


    private void compilationSuccessful(String input) throws Exception {
        assertThat(scan(input).sym).isEqualTo(ParserSym.EOF);
        SymbolTable.getSymbolTable().emptySymbolTableContent();
    }

    private void compilationError(String input){

        assertThrows(Exception.class, () -> scan(input));
        SymbolTable.getSymbolTable().emptySymbolTableContent();
    }

    private Symbol scan(String input) throws Exception {
        return ParserFactory.create(input).parse();
    }

    private String readFromFile(String fileName) throws IOException {
        InputStream resource = getClass().getResourceAsStream("/%s".formatted(fileName));
        if(resource == null){
            System.out.println("No se encontró el archivo: " + fileName);
        }
        assertThat(resource).isNotNull();
        return IOUtils.toString(resource, StandardCharsets.UTF_8);
    }


}
