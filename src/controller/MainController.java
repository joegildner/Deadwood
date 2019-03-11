package controller;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import model.Board.Room.Office;
import model.Board.Room.Room;
import model.Board.Room.Stage;
import model.Deadwood;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class MainController{
    private ArrayList<StageController> stages;
    private OfficeController office;
    private JButton act;
    private JButton rehearse;
    private Deadwood deadwood;

    public MainController(Deadwood dwModel, MainView boardView) {
        stages = new ArrayList<>();
        ArrayList<Room> rooms = dwModel.getBoard();
        deadwood = dwModel;

        for(Room thisRoom : rooms){
            if(thisRoom instanceof Stage)
                stages.add(new StageController((Stage)thisRoom, boardView, dwModel));
            if(thisRoom instanceof Office)
                office = new OfficeController((Office)thisRoom, boardView, dwModel);
        }

        act = createButton(new int[]{0,900,75,30}, "Act", boardView);
        rehearse = createButton(new int[]{80,900,150,30}, "Rehearse", boardView);
    }

    public JButton createButton(int[] area, String command, MainView boardView){
        JButton thisButton = new JButton(command);

        thisButton.setBounds(area[0],area[1],area[2],area[3]);

        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand(command);

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }
}
