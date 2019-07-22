package model;


import java.util.Objects;

/**
 *
 * @author Marih Bianchini
 */

public class Menu {
  
    private String nomeProduto;
    private int precoProduto;
     
    public Menu(String nomeProduto,int precoProduto){
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;  
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(int precoProduto) {
        this.precoProduto = precoProduto;
    }
    
    public int hashCode(){
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.nomeProduto);
        return hash;
    }
    
    public boolean aquals(Object obj){
    if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Menu other = (Menu) obj;
        if (!Objects.equals(this.nomeProduto, other.nomeProduto)) {
            return false;
        }
        return true;
    }
  
}
