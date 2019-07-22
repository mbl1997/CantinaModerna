package model;

/**
 *
 * @author Marih Bianchini
 */
public class Aluno {
    
    private int creditos;
    private String matricula;
    private String senha;
    private String porta = "";
    
    public Aluno(){
        
    }
    
    public Aluno(int creditos, String matricula, String senha){
        this.matricula = matricula;
        this.senha = senha;
        this.creditos = creditos;
    }
    
    public Aluno(int creditos, String matricula, String senha, String porta) {
        this.creditos = creditos;
        this.matricula = matricula;
        this.senha = senha;
        this.porta = porta;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }
    
    public void setPorta(int portaUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
}
