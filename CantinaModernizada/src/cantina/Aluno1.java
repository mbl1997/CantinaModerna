package cantina;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import entidade.Iniciar;

/**
 *
 * @author Marih Bianchini
 */
public class Aluno1 {

    static String usuario;
    static String senha;
    static GridLayout MenuLayout;
    static String mensagem;
    static String pedido;
    static JButton[] arrayBtn;
    static int saldo;
    static String port = "7003";
    static int porta = 7003;
    static int serverPort = 7000;
    static int contadorPedido = 0;
    static Iniciar iniciar = new Iniciar();
    static String recebidoServer;
    
    public static void main(String[] args) {
        DatagramSocket Socket = null;
        try {
            Socket = new DatagramSocket(porta);
            InetAddress aHost = InetAddress.getByName("localhost");
            usuario = JOptionPane.showInputDialog("Usuário:");
            senha = JOptionPane.showInputDialog("Senha:");
            montaEnvio(usuario, senha);
            enviaLogin(Socket, aHost);
            while (true) {
                System.out.println("CLIENTE " + port + " ESPERANDO SERVIDOR");
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                Socket.receive(request);
                String frase = new String(request.getData());
                
                String array[] = frase.split(":");
                recebidoServer = array[0];
                System.out.println("RESPOSTA: " + recebidoServer);
                
                
                if (recebidoServer.trim().equals("falhou")) {
                    System.out.println("ENTREI NO FALHOU");
                    telaLoginFalhou();
                } else if (recebidoServer.trim().equals("Menu")) {
                    System.out.println("ENTREI NO CARDAPIO");
                    montaTelaMenu(Socket, request.getData(), request, array);
                    
                } else if (recebidoServer.trim().equals("semsaldo")) {
                    System.out.println("ENTREI NO SEM SALDO");
                    telaSaldoFalhou();
                } else if (recebidoServer.trim().equals("pedidopronto")) {
                    telaPedidoPronto();
                }
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (Socket != null) {
                Socket.close();
            }
        }
    }

    private static void montaEnvio(String usuario, String senha) {
        mensagem = "login:" + usuario + ":" + senha + ":" + port;
        System.out.println(mensagem);

    }

    private static void enviaLogin(DatagramSocket aSocket, InetAddress aHost) throws IOException {
        System.out.println("ENVIA LOGIN");
        byte[] m = mensagem.getBytes();
        DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
        aSocket.send(request);
    }

    private static void montaTelaMenu(DatagramSocket Socket, byte[] data, DatagramPacket request, String array[]) {
        System.out.println("Monta Menu");
        JFrame frame = new JFrame("Meunu para " + port);
        frame.setSize(350, 600);
        MenuLayout = new GridLayout((iniciar.getMenu().size() + 2), iniciar.getMenu().size(), 10, 10);
        frame.setLayout(MenuLayout);
        JTextArea saldoNoMenu = new JTextArea(array[1].trim());
        JButton botao = new JButton("Enviar");
        botao.addActionListener(new buttonEnviarPedido(Socket, data, request, frame));
        arrayBtn = new JButton[iniciar.getMenu().size()];
        frame.add(saldoNoMenu);
        for (int i = 0; i < iniciar.getMenu().size(); i++) {
            arrayBtn[i] = new JButton(iniciar.getMenu().get(i).getNomeProduto() + ":"
                    + iniciar.getMenu().get(i).getPrecoProduto());
            arrayBtn[i].setText(iniciar.getMenu().get(i).getNomeProduto() + ":"
                    + iniciar.getMenu().get(i).getPrecoProduto());
            arrayBtn[i].addActionListener(new buttonMontaPedido(arrayBtn[i]));
            frame.add(arrayBtn[i]);
        }

        frame.add(botao);
        frame.setVisible(true);

    }

    private static void telaLoginFalhou() {
        System.out.println("TELA DE FALHA DE LOGIN");
        JFrame frame = new JFrame("Login Inválido");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea texto = new JTextArea("Usuário ou senha inválido!");
        Font fonte = new Font("Serif", Font.PLAIN, 35);
        texto.setFont(fonte);
        frame.add(texto);
        frame.setVisible(true);
    }

    private static void telaSaldoFalhou() {
        System.out.println("TELA SEM SALDO");
        JFrame frame = new JFrame("Saldo insuficiente");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea texto = new JTextArea("Sem saldo suficiente");
        Font fonte = new Font("Serif", Font.PLAIN, 35);
        texto.setFont(fonte);
        frame.add(texto);
        frame.setVisible(true);

    }

    private static void telaPedidoPronto() {
        System.out.println("TELA PEDIDO PRONTO");
        JFrame frame = new JFrame("Pedido Pronto");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea texto = new JTextArea("Seu Pedido está no balcão!");
        Font fonte = new Font("Serif", Font.PLAIN, 35);
        texto.setFont(fonte);
        frame.add(texto);
        frame.setVisible(true);
    }

    private static class buttonEnviarPedido implements ActionListener {

        DatagramSocket aSocket;
        byte[] data;
        DatagramPacket request;
        JFrame frame;

        private buttonEnviarPedido(DatagramSocket aSocket, byte[] data, DatagramPacket request, JFrame frame) {
            this.aSocket = aSocket;
            this.data = data;
            this.request = request;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                enviarPedido(aSocket, data, request);
                frame.setVisible(false);
            } catch (SocketException ex) {
                System.out.println("Socket: " + ex.getMessage());
            } catch (UnknownHostException ex) {
                System.out.println("Unknow: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("IO: " + ex.getMessage());
            }
        }

        private void enviarPedido(DatagramSocket aSocket, byte[] data, DatagramPacket request) throws SocketException, UnknownHostException, IOException {
            try {
                System.out.println("ENVIAR PEDIDO BUTTON");
                int valorTotal = 0;
                System.out.println("ENTREI NO ENVIAR PEDIDO");
                InetAddress aHost = InetAddress.getByName("localhost");
                data = pedido.getBytes();
                String pedidoFeito = new String(pedido.getBytes());
                String array[] = pedidoFeito.split(":");
                request = new DatagramPacket(data, data.length, aHost, serverPort);
                aSocket.send(request);
                pedido = "";
                contadorPedido = 0;
            } catch (NullPointerException ex) {
                System.out.println("Pedido não realizado");
            }

        }

    }

    private static class buttonMontaPedido implements ActionListener {

        private JButton botao;

        private buttonMontaPedido(JButton jButton) {
            this.botao = jButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            criaPedido(botao.getText());
        }

        private void criaPedido(String texto) {
            if (contadorPedido == 0) {
                pedido = "pedido:" + port;
                pedido += ":";
                pedido += texto;
                contadorPedido++;
            } else {
                pedido += ":";
                pedido += texto;
            }
            System.out.println(pedido);
        }
    }
    
}
