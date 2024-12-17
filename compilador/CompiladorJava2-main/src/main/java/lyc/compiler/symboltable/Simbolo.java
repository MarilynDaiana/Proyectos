package lyc.compiler.symboltable;

public class Simbolo {
    String nombre;
    String tipo;
    String valor;
    int longitud;
    boolean utilizado;

    public Simbolo(String nombre, String tipo,String valor,int longitud, boolean utilizado){
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.longitud = longitud;
        this.utilizado = utilizado;
    }
    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public int getLongitud() {
        return longitud;
    }
    public String getTipo() {
        return tipo;
    }
    public String getValor() {
        return valor;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString(){
        return  "Nombre: " + nombre + "\t\t" + "Tipo: "+ tipo + "\t\t" + " Valor: " + valor  + "\t\t" + " Longitud: "+ longitud +  "\t\t" + " Utilizado: " + utilizado;
    }
}