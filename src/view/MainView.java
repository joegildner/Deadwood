package view;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import model.Board.Room.Room;
import model.Board.Room.Stage;

import javax.swing.*;
import java.util.ArrayList;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class MainView extends JLayeredPane {

    private JLabel board;

    public MainView() {
        //super(null);

        board = new JLabel();
        add(board,new Integer(0));
        board.setBounds(0,0,1200, 900);
        ImageIcon boardIcon = new ImageIcon("../Deadwood/src/resources/board.jpg");
        board.setIcon(boardIcon);

        setVisible(true);
        setFocusable(true);


    }


}
