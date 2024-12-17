package lyc.compiler.files;

import lyc.compiler.polacaInversa.PolacaInversa;

import java.io.FileWriter;
import java.io.IOException;

public class IntermediateCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("Notación intermedia Polaca Inversa\n\n");
        fileWriter.write(PolacaInversa.getPolacaInversa().toString());
    }
}
