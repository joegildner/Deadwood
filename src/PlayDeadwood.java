import controller.MainController;
import model.Deadwood;
import view.MainView;
import view.SceneView;

import javax.swing.*;
import java.util.ArrayList;

public class PlayDeadwood {
    private static JFrame gameFrame;
    private static MainView boardView;
    private static Deadwood dwModel;
    private static MainController dwControl;

    public static void main(String[] args) {
        int players = Integer.parseInt(args[0]);
        dwModel = new Deadwood(players);

        initView();
        initController();

    }

    public static void initView(){
        gameFrame = new JFrame("Deadwood: the Cheap*** Game of Acting Badly");
        gameFrame.setSize(1200, 900);

        boardView = new MainView(dwModel.getBoard());

        gameFrame.getContentPane().add(boardView);
        gameFrame.setVisible(true);


    }

    public static void initController(){
        dwControl = new MainController(dwModel.getBoard(),boardView);

    }
}
