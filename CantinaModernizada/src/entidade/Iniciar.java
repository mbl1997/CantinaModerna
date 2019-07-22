package entidade;

import java.util.ArrayList;
import model.Aluno;
import model.Menu;

/**
 *
 * @author Marih Bianchini
 */
public class Iniciar {
   
    public ArrayList<Aluno> alu = new ArrayList<>();
    public ArrayList<Menu> me = new ArrayList<>();
    
    
    public Iniciar(){
        alu.add(new Aluno(130,"mari","lero"));
        alu.add(new Aluno(100,"samuel","123"));
        //alu.add(new Aluno(5,"3","3"));
        me.add(new Menu("Pastel de Forno",4));
        me.add(new Menu("Empada de Frango",3));
        me.add(new Menu("Enroladinho",2));
        me.add(new Menu("Coxinha",6));
        me.add(new Menu("croissant",5));
        me.add(new Menu("Suco de Laranja",10));
        me.add(new Menu("Coca-cola",15));
        me.add(new Menu("Suco de Uva",7));
                
    }
    
    public ArrayList<Aluno> getAluno(){
        return alu;
    }
    
    public ArrayList<Menu> getMenu(){
        return me;
    }
   
}
