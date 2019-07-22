package cantina;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import entidade.Iniciar;

/**
 *
 * @author Marih Bianchini
 */
public class Servidor {
    
    static String usuario;
    static String pedido;
    static String senha;
    static String portaCliente;
    static int saldoParaEnviar;
    static int portaUsuario;
    static int portaAtendente = 7004;
    static InetAddress adressAtendente;
    public static Iniciar iniciar;
    
    
    public static void main(String[] args) {
         DatagramSocket aSocket = null;
        String[] respostas;
        iniciar = new Iniciar();
        try {
            aSocket = new DatagramSocket(7000);
            while (true) {
                byte[] buffer = new byte[1000];
                System.out.println("entrei no servdor");
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                
                String frase = new String(request.getData());
                String array[] = frase.split(":");
                System.out.println("==>" + frase);
                
                pedido = retornaPedido(request.getData());
                if (pedido.trim().equals("login")) {
                    loginRequest(aSocket, request.getData(), request);
                } else if (pedido.trim().equals("pedido")) {
                    System.out.println("PEDIDO RECEBIDO " + request.getData());
                    boolean temSaldo = false;
                    temSaldo = avaliaSaldoUsuario(array);
                    if (temSaldo) {
                        pedidoRequest(aSocket, request.getData(), request);
                        
                    } else {
                        faltaSaldoRequest(aSocket, request.getData(), request);
                    }
                } else if (pedido.trim().equals("pedidoPronto")) {
                    enviarPedidoProntoParaCliente(aSocket, array[1], request);
                }

            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }

    private static void loginRequest(DatagramSocket aSocket, byte[] data, DatagramPacket request) throws IOException {
        boolean testaLogin = false;
        System.out.println("ENTREI NO LOGIN REQUEST");
        String frase = new String(data);
        String array[] = frase.split(":");
        usuario = array[1];
        senha = array[2];
        portaCliente = array[3].trim();
        testaLogin = validaLogin(usuario, senha, portaCliente);
        if (testaLogin) {
            System.out.println("LOGIN VALIDO!");
            String sucessoLogin = "Menu:"+iniciar.getMenu();
            byte[] m = sucessoLogin.getBytes();
            DatagramPacket reply = new DatagramPacket(m, m.length, request.getAddress(), request.getPort());
            aSocket.send(reply);
        } else {
            System.out.println("LOGIN FALHOU!");
            String falhaLogin = "falhou";
            byte[] m = falhaLogin.getBytes();
            DatagramPacket reply = new DatagramPacket(m, m.length, request.getAddress(), request.getPort());
            aSocket.send(reply);
        }

    }

    private static String retornaPedido(byte[] data) {
        String frase = new String(data);
        String array[] = frase.split(":");

        return array[0];
    }

    private static boolean validaLogin(String usuario, String senha, String porta) {
        System.out.println("ENTREI NO VALIDA LOGIN" + iniciar);
        
        for (int i = 0; i < iniciar.getAluno().size(); i++) {
                
            System.out.println("--->" + iniciar.getAluno().get(i).getMatricula());
            
            if (iniciar.getAluno().get(i).getMatricula().trim().equals(usuario) && iniciar.getAluno().get(i).getSenha().trim().equals(senha)) {
                iniciar.getAluno().get(i).setPorta(porta.trim());
                saldoParaEnviar = iniciar.getAluno().get(i).getCreditos();
                return true;
            }
        }
        return false;
    }

    private static void pedidoRequest(DatagramSocket aSocket, byte[] data, DatagramPacket request) throws IOException {
        System.out.println("ENTREI NO PEDIDO REQUEST");
        request = new DatagramPacket(data, data.length, request.getAddress(), portaAtendente);
        System.out.println("SERVIDOR MANDOU: ");
        aSocket.send(request);
    }

    private static void enviarPedidoProntoParaCliente(DatagramSocket aSocket, String texto, DatagramPacket request) throws IOException {
        System.out.println("Entrei no envia pedido pronto aluno");
        byte[] data = new byte[1000];
        String pedidoPronto = "pedidopronto";
        data = pedidoPronto.getBytes();
        int portaDoClientePedidoPronto = Integer.parseInt(texto.trim());
        request = new DatagramPacket(data, data.length, request.getAddress(), portaDoClientePedidoPronto);
        aSocket.send(request);
        for (int i = 0; i < iniciar.getAluno().size(); i++) {
            if (iniciar.getAluno().get(i).getPorta().trim().equals(texto)) {
                iniciar.getAluno().get(i).setPorta("");
            }
        }
    }

    private static boolean avaliaSaldoUsuario(String[] array) {
        System.out.println("ENTREI NO VALIDA SALDO");
        int valorTotal = 0;
        for (int i = 0; i < iniciar.getAluno().size(); i++) {
            if (iniciar.getAluno().get(i).getPorta().trim().equals(array[1])) {
                for (int a = 3; a < array.length; a++) {
                    if (a % 2 == 1) {
                        valorTotal += Integer.parseInt(array[a].trim());
                    }
                }
                System.out.println("Valor total do Pedido: " + valorTotal);
                if (valorTotal <= iniciar.getAluno().get(i).getCreditos()) {
                    System.out.println(iniciar.getAluno().get(i).getMatricula() + " SALDO " + iniciar.getAluno().get(i).getCreditos());
                    iniciar.getAluno().get(i).setCreditos(iniciar.getAluno().get(i).getCreditos() - valorTotal);
                    System.out.println(iniciar.getAluno().get(i).getMatricula() + " SALDO " + iniciar.getAluno().get(i).getCreditos());
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void faltaSaldoRequest(DatagramSocket aSocket, byte[] data, DatagramPacket request) throws IOException {
        String semSaldo = "semsaldo";
        data = semSaldo.getBytes();
        request = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
        aSocket.send(request);
    }
 }