import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Suresh extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;

    ServerSocket serverSocket;
    Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chat;

    Suresh() {
        textField = new TextField(20);
        textArea = new TextArea(10, 40);
        send = new Button("Send");
        send.addActionListener(this);
        try {
            serverSocket = new ServerSocket(12000);
            socket = serverSocket.accept();

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(textField);
        add(textArea);
        add(send);

        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();

        setSize(500, 500);
        setTitle("Suresh");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("Suresh: " + msg + "\n");
        textField.setText("");
        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Suresh();
    }

    public void run() {
        while (socket != null && !socket.isClosed()) {
            try {
                String msg = dataInputStream.readUTF();
                textArea.append("Ajay: " + msg + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
