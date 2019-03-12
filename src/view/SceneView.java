package view;

import model.Room.Stage;

import javax.swing.*;

/**
 * Created by gildnej on 3/7/19.
 */
public class SceneView extends JLayeredPane {

    public SceneView(Stage thisRoom, MainView boardView){
        int[] area = thisRoom.getArea();
        String imgFile = thisRoom.getScene().getImgFile();

        JLabel thisScene = new JLabel();
        ImageIcon sceneIcon = new ImageIcon(imgFile);
        thisScene.setBounds(area[0],area[1],area[3],area[2]);
        thisScene.setIcon(sceneIcon);
        thisScene.setVisible(true);
        boardView.add(thisScene,new Integer(1));
    }
}
