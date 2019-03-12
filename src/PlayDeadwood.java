import controller.BoardController;
import controller.DeadwoodController;
import view.MainView;
import view.PlayerView;

import javax.swing.*;

public class PlayDeadwood {
    private static JFrame gameFrame;
    private static MainView boardView;
    private static DeadwoodController dwControl;
    private static BoardController boardControl;

    public static void main(String[] args) {
        int players = Integer.parseInt(args[0]);
        dwControl = new DeadwoodController(players);

        initView();
        initController();

        dwControl.startGame();

    }

    public static void initView(){
        gameFrame = new JFrame("DeadwoodController: the Cheap*** Game of Acting Badly");
        gameFrame.setSize(1200, 930);

        boardView = new MainView(dwControl.getBoard(), dwControl.getPlayers());

        initListeners();

        gameFrame.getContentPane().add(boardView);
        gameFrame.setVisible(true);

    }

    public static void initController(){
        boardControl = new BoardController(dwControl,boardView);

    }

    public static void initListeners(){
        for(PlayerView pv : boardView.getpViews()){
            dwControl.addListener(pv);
        }
    }
}
