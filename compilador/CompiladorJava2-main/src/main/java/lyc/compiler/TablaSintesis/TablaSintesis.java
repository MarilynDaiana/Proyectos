package lyc.compiler.TablaSintesis;

import lyc.compiler.model.InvalidAritmeticOperationException;
import lyc.compiler.model.InvalidAsignmentException;
import lyc.compiler.model.InvalidComparisonException;

public class TablaSintesis {
    private static final String int_type = "INT";
    private static final String float_type = "FLOAT";
    private static final String string_type = "STRING";

    public static String resolverTipoDatoAsignacion(String tipoDatoLeft, String tipoDatoRight) throws InvalidAsignmentException {
        String returnValue = "";
        switch (tipoDatoLeft){
            case int_type:
                switch (tipoDatoRight){
                    case int_type:
                        returnValue =  int_type;
                        break;
                    case float_type:
                        throw new InvalidAsignmentException(getAsignacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case string_type:
                        throw new InvalidAsignmentException(getAsignacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidAsignmentException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case float_type:
                switch (tipoDatoRight){
                    case int_type:
                        returnValue =  float_type;
                        break;
                    case float_type:
                        returnValue =  float_type;
                        break;
                    case string_type:
                        throw new InvalidAsignmentException(getAsignacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidAsignmentException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case string_type:
                switch (tipoDatoRight){
                    case int_type:
                        throw new InvalidAsignmentException(getAsignacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case float_type:
                        throw new InvalidAsignmentException(getAsignacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case string_type:
                        returnValue =  string_type;
                        break;
                    default:
                        throw new InvalidAsignmentException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            default:
                throw new InvalidAsignmentException("Tipo de dato desconocido: " + tipoDatoRight);
        }

        return returnValue;
    }

    public static String resolverTipoDatoAritmetica(String tipoDatoLeft, String tipoDatoRight) throws InvalidAritmeticOperationException {
        String returnValue = "";
        switch (tipoDatoLeft){
            case int_type:
                switch (tipoDatoRight){
                    case int_type:
                        returnValue =  int_type;
                        break;
                    case float_type:
                        returnValue =  float_type;
                        break;
                    case string_type:
                        throw new InvalidAritmeticOperationException(getOpAritmeticaExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidAritmeticOperationException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case float_type:
                switch (tipoDatoRight){
                    case int_type:
                        returnValue =  float_type;
                        break;
                    case float_type:
                        returnValue =  float_type;
                        break;
                    case string_type:
                        throw new InvalidAritmeticOperationException(getOpAritmeticaExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidAritmeticOperationException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case string_type:
                switch (tipoDatoRight){
                    case int_type:
                        throw new InvalidAritmeticOperationException(getOpAritmeticaExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case float_type:
                        throw new InvalidAritmeticOperationException(getOpAritmeticaExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case string_type:
                        returnValue =  string_type;
                        break;
                    default:
                        throw new InvalidAritmeticOperationException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            default:
                throw new InvalidAritmeticOperationException("Tipo de dato desconocido: " + tipoDatoRight);
        }

        return returnValue;
    }
    public static String resolverTipoDatoComparacion(String tipoDatoLeft, String tipoDatoRight) throws InvalidComparisonException {
        String returnValue = "";
        switch (tipoDatoLeft){
            case int_type:
                switch (tipoDatoRight){
                    case int_type:
                        returnValue =  int_type;
                        break;
                    case float_type, string_type:
                        throw new InvalidComparisonException(getComparacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidComparisonException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case float_type:
                switch (tipoDatoRight){
                    case int_type:
                        throw new InvalidComparisonException(getComparacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case float_type:
                        returnValue =  float_type;
                        break;
                    case string_type:
                        throw new InvalidComparisonException(getComparacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    default:
                        throw new InvalidComparisonException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            case string_type:
                switch (tipoDatoRight){
                    case int_type, float_type:
                        throw new InvalidComparisonException(getComparacionExceptionMessage(tipoDatoLeft, tipoDatoRight));
                    case string_type:
                        returnValue =  string_type;
                        break;
                    default:
                        throw new InvalidComparisonException("Tipo de dato desconocido: " + tipoDatoRight);
                }
                break;
            default:
                throw new InvalidComparisonException("Tipo de dato desconocido: " + tipoDatoRight);
        }

        return returnValue;
    }

    private static String getAsignacionExceptionMessage(String tipoDatoLeft, String tipoDatoRight){
        return "No se puede asignar un " + tipoDatoRight + " a un " + tipoDatoLeft;
    }

    private static String getOpAritmeticaExceptionMessage(String tipoDatoLeft, String tipoDatoRight){
        return "No se puede operar un " + tipoDatoRight + " con un " + tipoDatoLeft;
    }

    private static String getComparacionExceptionMessage(String tipoDatoLeft, String tipoDatoRight){
        return "No se puede comparar un " + tipoDatoRight + " con un " + tipoDatoLeft;
    }
}
