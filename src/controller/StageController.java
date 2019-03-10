package controller;

import model.Board.Role.Extra;
import model.Board.Role.Role;
import model.Board.Room.Stage;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by gildnej on 3/7/19.
 */
public class StageController {

    private ArrayList<JButton> extras;

    public StageController(Stage thisStage, MainView boardView){
        extras = new ArrayList<JButton>();
        for(Role extraRole : thisStage.getRoles()){
            if(extraRole instanceof Extra)
                extras.add(createButton((Extra)extraRole, boardView));
        }
    }

    public JButton createButton(Extra extra, MainView boardView){
        int[] area = extra.getArea();
        JButton thisButton = new JButton();

        thisButton.setBounds(area[0],area[1],area[2],area[3]);
        thisButton.setOpaque(true);
        thisButton.setContentAreaFilled(true);
        thisButton.setBorderPainted(true);
        thisButton.setVisible(true);

        boardView.add(thisButton, new Integer(1));

        return thisButton;
    }

}
