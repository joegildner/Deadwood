package view;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import controller.DeadwoodController;
import model.Player;
import model.Room.Room;
import model.Room.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class MainView extends JLayeredPane implements DeadwoodController.DayListener{

    private JLabel board;
    private ArrayList<SceneView> sceneViews;
    private ArrayList<PlayerView> pViews;
    private ArrayList<Room> rooms;
    private DeadwoodController dwController;


    public MainView(ArrayList<Room> rooms, Queue<Player> players, DeadwoodController dwController) {
        super();
        setSize(1200,950);

        this.dwController = dwController;
        this.rooms=rooms;

        board = new JLabel();
        add(board,new Integer(0));
        board.setBounds(0,0,1200, 900);

        String filename = "";
        Class cls = MainView.class;
        try {
            URL u = cls.getResource("board.jpg");
            filename = u.getFile();
        } catch (Exception e) {
            System.err.println("board.jpg");
            e.printStackTrace();
            System.exit(1);
        }

        //ImageIcon boardIcon = new ImageIcon("../Deadwood/src/resources/board.jpg");
        ImageIcon boardIcon = new ImageIcon(filename);
        board.setIcon(boardIcon);

        sceneViews = new ArrayList<>();
        pViews = new ArrayList<>();

        initScenes();

        int n = 1;
        for(Player p : players){
                pViews.add(new PlayerView(p, n, this));
                n++;
        }

        setVisible(true);
        setFocusable(true);
    }

    public void initScenes(){
        sceneViews.clear();
        for(Room thisRoom : rooms){
            if(thisRoom instanceof Stage)
                sceneViews.add(new SceneView((Stage)thisRoom, this));
        }
    }

    public ArrayList<PlayerView> getPViews() {
        return pViews;
    }

    public ArrayList<SceneView> getSViews() {
        return sceneViews;
    }

    public void changed(){
        initScenes();

        for (SceneView sv: sceneViews)
            dwController.addListener(sv);
    }
}
