package lyc.compiler.files;

import lyc.compiler.polacaInversa.PolacaInversa;
import lyc.compiler.symboltable.Simbolo;
import lyc.compiler.symboltable.SymbolTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AsmCodeGenerator implements FileGenerator {
    HashMap<String, String[]> mapaOperadoresConCodigo = new HashMap<String, String[]>();
    ArrayList<String> polacaInversa =  PolacaInversa.getPolacaInversa().getListaElementos();
    HashMap<String, Simbolo> tablaSimbolos = SymbolTable.getSymbolTable().getTabla();
    Stack<String> pilaOperandos = new Stack<>();

    public AsmCodeGenerator(){
//        String[] codigoSuma = {"FADD"};
//        this.mapaOperadoresConCodigo.put("+", codigoSuma);
//
//        String[] codigoResta = {"FSUB"};
//        this.mapaOperadoresConCodigo.put("-", codigoResta);
//
//        String[] codigoMulti = {"FMUL"};
//        this.mapaOperadoresConCodigo.put("*", codigoMulti);
//
//        String[] codigoDiv = {"FDIV"};
//        this.mapaOperadoresConCodigo.put("/", codigoDiv);
//
//        String[] codigoAsig = {"FSTP"};
//        this.mapaOperadoresConCodigo.put("/", codigoDiv);
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        int indiceSalto = 1;
        Stack<String> pilaEtiqSalto = new Stack<String>();
        Stack<Integer> pilaCeldaParaSaltar = new Stack<Integer>();
        String etiqSalto = "";

        final String ETIQUETA_SALTO = "etiq_salto_";
        int contadorCeldaPolaca = 1;

        //Cabecera de ASM
        fileWriter.write(".MODEL  LARGE\n");
        fileWriter.write(".386\n");
        fileWriter.write(".STACK 200h\n");

        //Tabla de simbolos
        fileWriter.write("\n.DATA\n\n");
        cargarTablaSimbolosAssembler(fileWriter);

        //Codigo
        fileWriter.write("\n.CODE\n\n");

        //Inicio de codigo (genérico)
        fileWriter.write("START:\n");
        fileWriter.write("mov AX,@DATA\n");
        fileWriter.write("mov DS,AX\n");
        fileWriter.write("mov es,ax\n\n");


        //Codigo del programador
        for(String celda: polacaInversa){
            System.out.println(celda);

            if(celdaEsOperador(celda)){
//                System.out.println("pila");
//                System.out.println(pilaOperandos.size());
                String op2Key = pilaOperandos.pop();
                String op2 = op2Key != "@aux" ? SymbolTable.getSymbolTable().getSymbol(op2Key).getNombre() : "@aux";

                String op1Key = pilaOperandos.pop();
                String op1 = op1Key != "@aux" ? SymbolTable.getSymbolTable().getSymbol(op1Key).getNombre() : "@aux";
                switch (celda){
                    case "+":
                        fileWriter.write("FLD " + op1 + "\n");
                        if(op2 != "@aux"){
                            fileWriter.write("FLD " + op2 + "\n");
                        }

                        fileWriter.write("FADD"+ "\n\n");
                        pilaOperandos.add("@aux");
                        break;
                    case "-":
                        fileWriter.write("FLD " + op1 + "\n");
                        if(op2 != "@aux"){
                            fileWriter.write("FLD " + op2 + "\n");
                        }


                        fileWriter.write("FSUB"+ "\n\n");
                        pilaOperandos.add("@aux");
                        break;
                    case "*":
                        fileWriter.write("FLD " + op1 + "\n");
                        if(op2 != "@aux"){
                            fileWriter.write("FLD " + op2 + "\n");
                        }

                        fileWriter.write("FMUL"+ "\n\n");
                        pilaOperandos.add("@aux");
                        break;
                    case "/":
                        fileWriter.write("FLD " + op1 + "\n");
                        if(op2 != "@aux"){
                            fileWriter.write("FLD " + op2 + "\n");
                        }

                        fileWriter.write("FDIV"+ "\n\n");
                        pilaOperandos.add("@aux");
                        break;
                    case "=":
                        if(op1 != "@aux"){
                            fileWriter.write("FLD " + op1 + "\n");
                        }
                        fileWriter.write("FSTP " + op2 + "\n\n");
                        break;
                    case "BLT":
                        if(op1 != "@aux"){
                            fileWriter.write("FLD " + op1 + "\n");
                        }
                        fileWriter.write("FLD " + op2 + "\n\n");

                        fileWriter.write("FXCH" + "\n");
                        fileWriter.write("FCOM" + "\n");
                        fileWriter.write("FSTSW AX" + "\n");
                        fileWriter.write("SAHF" + "\n");


                        etiqSalto = ETIQUETA_SALTO + indiceSalto;
                        fileWriter.write("JB " + etiqSalto + "\n\n");

                        pilaEtiqSalto.add(etiqSalto);
                        indiceSalto++;

                        break;
                    default:
                        System.out.println("OPERADOR NO RECONOCIDO: " + celda);
                }

            }
            else{
                //si estoy en el else es que tengo una celda que no es operador, y aca evaluo que tipo de celda es.
                //añado operando a la pila de operandos
                if(!Objects.equals(celda, "CMP")){
                    pilaOperandos.add(celda);
                }

                //La celda es un numero de salto.
                if(celda.startsWith("#")){
                    pilaCeldaParaSaltar.add(Integer.valueOf(celda.substring(1)));
                }
            }

            /// Luego de cada celda evaluo si tengo apilada una etiqueta de salto y si ya tengo que insertar la etiquta apilada o no
            if(!pilaCeldaParaSaltar.isEmpty()  && pilaCeldaParaSaltar.peek() == contadorCeldaPolaca){
                fileWriter.write(pilaEtiqSalto.pop() + ":" + "\n\n");
            }
            contadorCeldaPolaca++;

        }

        ///Se vuelve a hacer el if al final de recorrer la polaca por si la ultima sentencia fue un if
        if(!pilaCeldaParaSaltar.isEmpty()  && pilaCeldaParaSaltar.peek() == contadorCeldaPolaca){
            fileWriter.write(pilaEtiqSalto.pop() + ":" + "\n\n");
        }



        //Fin de codigo (genérico)
        fileWriter.write("\n\nmov ax, 4C00h\n");
        fileWriter.write("int 21h\n");
        fileWriter.write("END START\n");


    }

    private boolean celdaEsOperador(String celda){
        List<String> listaOperadores = Arrays.asList("+", "-", "*", "/", "=", "BLT");
        return listaOperadores.contains(celda);
    }

    private void cargarTablaSimbolosAssembler(FileWriter fileWriter) throws IOException{
        for (Simbolo simbolo: tablaSimbolos.values()) {
            String tipo = Objects.equals(simbolo.getTipo(), "STRING") ? "db" : "dd";
            String valor = simbolo.getValor() != null ? simbolo.getValor() : "?";

            fileWriter.write(simbolo.getNombre() + "\t\t\t\t\t" + tipo + "\t\t" + valor + "\n");
        }
    }
}
