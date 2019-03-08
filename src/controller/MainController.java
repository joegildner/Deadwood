package controller;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import model.Board.Room.Room;
import model.Board.Room.Stage;
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

    public MainController(ArrayList<Room> rooms, MainView boardView) {
        stages = new ArrayList<>();
        for(Room thisRoom : rooms){
            if(thisRoom instanceof Stage)
                stages.add(new StageController((Stage)thisRoom, boardView));
        }
    }
}
