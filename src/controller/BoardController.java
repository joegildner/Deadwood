package controller;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import model.Room.Office;
import model.Room.Room;
import model.Room.Stage;
import model.Room.Trailer;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class BoardController {
    private ArrayList<StageController> stages;
    private OfficeController office;
    private TrailerController trailer;
    private JButton act;
    private JButton rehearse;
    private JButton end;
    private DeadwoodController deadwood;

    public BoardController(DeadwoodController dwModel, MainView boardView) {
        stages = new ArrayList<>();
        ArrayList<Room> rooms = dwModel.getBoard();
        deadwood = dwModel;

        for(Room thisRoom : rooms){
            if(thisRoom instanceof Stage)
                stages.add(new StageController((Stage)thisRoom, boardView, dwModel));
            if(thisRoom instanceof Office)
                office = new OfficeController((Office)thisRoom, boardView, dwModel);
            if(thisRoom instanceof Trailer)
                trailer = new TrailerController((Trailer)thisRoom, boardView, dwModel);
        }

        end = createButton(new int[]{1120, 905, 75, 40}, "End", boardView);
        act = createButton(new int[]{1040,905,75,40}, "Act", boardView);
        rehearse = createButton(new int[]{885,905,150,40}, "Rehearse", boardView);

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

    public ArrayList<StageController> getStages() {
        return stages;
    }
}
