import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by grappom on 3/4/19.
 */
public class Tester {

    public static void main(String[] args){
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Deadwood");
        mainFrame.setSize(1200, 900);

        MainView mainView = new MainView();
        mainFrame.getContentPane().add(mainView);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }});

        mainFrame.setVisible(true);
        mainView.requestFocus();
    }
}
