import model.Deadwood;
import view.MainView;
import javax.swing.*;

public class PlayDeadwood {
    private static JFrame gameFrame;
    private static MainView boardView;
    private Deadwood deadwoodModel;

    public static void main(String[] args) {
        initFrame();

    }

    public static void initFrame(){
        gameFrame = new JFrame("Deadwood: the Cheap*** Game of Acting Badly");
        gameFrame.setSize(1200, 900);

        boardView = new MainView();
        gameFrame.getContentPane().add(boardView);

        gameFrame.setVisible(true);
        boardView.requestFocus();
    }
}
