package view;

import controller.DeadwoodController;
import model.Player;
import model.Room.Stage;
import model.Room.Take;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by gildnej on 3/7/19.
 */
public class SceneView extends JLayeredPane implements DeadwoodController.Listener{

    private JLabel sceneCard;
    private MainView boardView;
    private Stage stage;
    private Stack<JLabel> takes;

    public SceneView(Stage thisRoom, MainView boardView){
        int[] area = thisRoom.getArea();
        String imgFile = thisRoom.getScene().getImgFile();

        JLabel thisScene = new JLabel();
        ImageIcon sceneIcon = new ImageIcon(imgFile);
        thisScene.setBounds(area[0],area[1],area[3],area[2]);
        thisScene.setIcon(sceneIcon);
        thisScene.setVisible(true);
        boardView.add(thisScene,new Integer(1));

        sceneCard = thisScene;
        this.boardView = boardView;
        stage = thisRoom;
        takes = new Stack<>();

        initTakes();

        for(JLabel take : takes){
            boardView.add(take, new Integer(1));
        }
    }

    public void initTakes(){
        for(Take thisTake : stage.getTakeArea()){
            int[] takeArea = thisTake.getArea();
            JLabel takeLabel = new JLabel();
            takeLabel.setIcon(new ImageIcon("/home/gildnej/Code/CSCI345/Deadwood/src/resources/shot.png"));
            takeLabel.setBounds(takeArea[0],takeArea[1],takeArea[2],takeArea[3]);
            takeLabel.setVisible(true);
            takes.push(takeLabel);
        }
    }

    public void changed(Player p){
        while(takes.size() > stage.getTakes()) boardView.remove(takes.pop());
        if(stage.getTakes() == 0) boardView.remove(sceneCard);
        boardView.repaint();
    }
}
