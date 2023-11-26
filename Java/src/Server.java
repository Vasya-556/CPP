import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server {
    static JPanel jPanel = new JPanel();
    static JButton button = new JButton("Click me!");

    public static void main(String[] args) throws IOException {
        InitComponent();
        while (true) {
            HandleMessage();
        }
    }

    public static void InitComponent() {
        JFrame jFrame = getFrame();
        jPanel.setBackground(Color.white);
        jFrame.add(jPanel);
        jPanel.add(button);
    }

    private static JFrame getFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("Server");
        jFrame.setVisible(true);
        jFrame.setSize(500, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        return jFrame;
    }

    public static void HandleMessage() throws IOException {
        try (ServerSocket ss = new ServerSocket(4000);
             Socket s = ss.accept();
             ObjectInputStream inputStream = new ObjectInputStream(s.getInputStream())) {

            String object = (String) inputStream.readObject();
            String state = (String) inputStream.readObject();

            ChangeObjectState(object, state);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ChangeObjectState(String object, String state) {
        if ("Button".equals(object)) {
            if ("disabled".equals(state)) {
                button.setEnabled(true);
            } else if ("enabled".equals(state)) {
                button.setEnabled(false);
            }
        } else if ("Panel".equals(object)) {
            if ("visible".equals(state)) {
                jPanel.setVisible(true);
            } else if ("invisible".equals(state)) {
                jPanel.setVisible(false);
            } else if ("white".equals(state)) {
                jPanel.setBackground(Color.white);
            } else if ("red".equals(state)) {
                jPanel.setBackground(Color.red);
            } else if ("yellow".equals(state)) {
                jPanel.setBackground(Color.yellow);
            }
        }
    }
}