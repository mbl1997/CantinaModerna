package cantina;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import model.Menu;
import model.Pedido;

/**
 *
 * @author Marih Bianchini
 */
public class Atendente {

    static GridLayout relatorio;
    static GridLayout atendimento;
    static String pedidoSt;
    static String servidor;
     static String port = "7004";
     static int porta = 7004;
    static int serverPort = 7000;
    static ArrayList<Pedido>listaPedidos = new ArrayList<>();
    static String portaPedido;
    
    
    public static void main(String[] args) throws UnknownHostException {
        DatagramSocket Socket = null;
        InetAddress endreco = InetAddress.getByName("localhost");
        try{ 
           Socket = new DatagramSocket(porta);
            InetAddress host = InetAddress.getByName("localhost");
            while(true){
                System.out.println("Atendente esperando");
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                Socket.receive(request);
                servidor = retornaPedido(request.getData());
                 if (servidor.trim().equals("pedido")) {
                System.out.println("ENTREI NO PEDIDO"); 
                criarPedido(Socket, request.getData(), request);
                 }
            }
        }catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {
           System.out.println("IO: " + e.getMessage()); 
        }finally {
          if (Socket != null) {
              Socket.close();
          }  
        }   
    }

    private static String retornaPedido(byte[] data) {
       String msg = new String(data);
       String array[] = msg.split(":");
       return array[0];
    }
    
    private static void Envio(int porta){
        pedidoSt = "pedidoPronto:" + porta;
    }
    
    private static void criarPedido(DatagramSocket Socket, byte[] data, DatagramPacket request){
       System.out.println("Tela pedido do aluno");
       String msg = new String(data);
       String array[] = msg.split(":");
       JFrame frame = new JFrame("Pedido:"+array[1]);
       atendimento = new GridLayout(2,1,10,10);
       frame.setLayout(atendimento);
       Pedido pedido = new Pedido();
       pedido.setPorta(Integer.parseInt(array[1].trim()));
       for(int i=2; i<array.length; i++){
           if(i % 2 == 0){
        Menu itemPedido =  new Menu (array[1].trim(), Integer.parseInt(array[i + 1].trim()));
        pedido.getCompras().add(itemPedido);
        pedido.setPreco(pedido.getPreco() + Integer.parseInt(array[i + 1].trim()));
        listaPedidos.add(pedido);
           }
       }
       JButton botao = new JButton("Terminar Pedido");
       botao.addActionListener(new botaoEnviarPedidoPronto(botao, pedido, data, request, Socket, frame));
       JTextArea txt = new JTextArea(msg);
       frame.setSize(400, 100);
       frame.add(txt);
       frame.add(botao);
       frame.setVisible(true); 
    } 
    
    
    
    private static class botaoEnviarPedidoPronto implements ActionListener{
        private JButton botao;
        private Pedido pedido;
        private byte[] data;
        private DatagramPacket request;
        private DatagramSocket Socket;
        private JFrame frame;
        
        private botaoEnviarPedidoPronto(JButton botao, Pedido pedido, byte[] data, DatagramPacket request, DatagramSocket aSocket, JFrame frame) {
            this.botao = botao;
            this.pedido = pedido;
            this.data = data;
            this.request = request;
            this.Socket = aSocket;
            this.frame = frame;
     }

        @Override
        public void actionPerformed(ActionEvent e) {
             try {
                enviarPedidoPronto(pedido.getPorta());
            } catch (UnknownHostException ex) {
                System.out.println("Unknow: " + ex.getMessage());
            } catch (SocketException ex) {
                System.out.println("Socket: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("IO: " + ex.getMessage());
            }
        }

        private void enviarPedidoPronto(int porta) throws IOException {
           System.out.println("enviar pedido pronto");
            InetAddress aHost = InetAddress.getByName("localhost");
            Envio(porta);
            data = pedidoSt.getBytes();
            request = new DatagramPacket(data, data.length, aHost, serverPort);
            Socket.send(request);
            pedidoSt = "";
            frame.setVisible(false);
        }
        
     }
   
}