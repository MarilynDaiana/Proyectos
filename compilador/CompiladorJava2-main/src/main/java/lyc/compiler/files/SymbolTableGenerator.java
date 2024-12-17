package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.symboltable.SymbolTable;

public class SymbolTableGenerator implements FileGenerator{

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("Tabla de Símbolos: \n\n");
        fileWriter.write(SymbolTable.getSymbolTable().toString());
    }
}
