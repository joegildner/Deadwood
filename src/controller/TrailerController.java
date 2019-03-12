package controller;

import model.Room.Office;
import model.Room.Trailer;
import view.MainView;

import javax.swing.*;

/**
 * Created by gildnej on 3/11/19.
 */
public class TrailerController {

    private JButton moveHere;
    private DeadwoodController deadwood;

    public TrailerController(Trailer trailer, MainView boardView, DeadwoodController deadwood){
        this.deadwood = deadwood;
        moveHere = createButton(trailer.getArea(), boardView);


    }

    public JButton createButton(int[] area, MainView boardView){
        JButton thisButton = new JButton();

        thisButton.setBounds(area[0],area[1]+30,area[2],40);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(false);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("move trailer");

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }
}
