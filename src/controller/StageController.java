package controller;

import model.Board.Role.Extra;
import model.Board.Role.Lead;
import model.Board.Role.Role;
import model.Board.Room.Stage;
import model.Deadwood;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gildnej on 3/7/19.
 */
public class StageController{

    private String stageName;
    private JButton moveHere;
    private ArrayList<JButton> extras;
    private ArrayList<JButton> stars;
    private Deadwood deadwood;

    public StageController(Stage thisStage, MainView boardView, Deadwood deadwood){
        extras = new ArrayList<JButton>();
        stars = new ArrayList<>();
        stageName = thisStage.getName();
        this.deadwood = deadwood;

        for(Role extraRole : thisStage.getRoles()){
            if(extraRole instanceof Extra)
                extras.add(createButton(extraRole, extraRole.getArea(), boardView));
        }

        moveHere = createButton(thisStage, boardView);

        updateStars(thisStage, boardView);
    }

    public JButton createButton(Role thisRole, int[] area, MainView boardView){
        JButton thisButton = new JButton();

        thisButton.setBounds(area[0],area[1],area[2],area[3]);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(true);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("work "+thisRole.getName());

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }

    public JButton createButton(Stage thisStage, MainView boardView){
        JButton thisButton = new JButton();
        int[] area = thisStage.getArea();

        thisButton.setBounds(area[0],area[1] +115,area[3]/2,40);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(true);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("move "+thisStage.getName());

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }

    public void updateStars(Stage thisStage, MainView boardView){
        stars.clear();
        int[] sceneArea = thisStage.getArea();

        for(Role starRole : thisStage.getRoles()){
            int[] actualArea = starRole.getArea();

            actualArea[0] += sceneArea[0];
            actualArea[1] += sceneArea[1];

            if(starRole instanceof Lead)
                stars.add(createButton(starRole, actualArea, boardView));
        }

    }
}
