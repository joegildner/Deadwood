package view;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import model.Player;
import model.Room.Room;
import model.Room.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Queue;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class MainView extends JLayeredPane {

    private JLabel board;
    private ArrayList<SceneView> sceneViews;
    private ArrayList<PlayerView> pViews;


    public MainView(ArrayList<Room> rooms, Queue<Player> players) {
        super();
        setSize(1200,930);

        board = new JLabel();
        add(board,new Integer(0));
        board.setBounds(0,0,1200, 900);
        ImageIcon boardIcon = new ImageIcon("../Deadwood/src/resources/board.jpg");
        board.setIcon(boardIcon);

        sceneViews = new ArrayList<>();
        pViews = new ArrayList<>();

        for(Room thisRoom : rooms){
            if(thisRoom instanceof Stage)
                sceneViews.add(new SceneView((Stage)thisRoom, this));
        }

        int n = 1;
        for(Player p : players){
                pViews.add(new PlayerView(p, n, this));
                n++;
        }

        setVisible(true);
        setFocusable(true);
    }

    public void initPViews(){

    }

    public ArrayList<PlayerView> getpViews() {
        return pViews;
    }
}
