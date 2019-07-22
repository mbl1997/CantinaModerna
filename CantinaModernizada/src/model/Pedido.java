package model;

import java.util.ArrayList;

/**
 *
 * @author Marih Bianchini
 */

public class Pedido {
    
    private int porta;
    private ArrayList<Menu> compras = new ArrayList<>();
    private int preco = 0;
    
    public Pedido() {

    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public ArrayList<Menu> getCompras() {
        return compras;
    }

    public void setCompras(ArrayList<Menu> compras) {
        this.compras = compras;
    }

}
