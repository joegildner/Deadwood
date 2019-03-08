package view;
/**
 * Created by grappom on 3/4/19.
 */

//import java.util.LinkedList;
//import java.util.List;
//import java.awt.event.*;
import javax.swing.*;
//import javax.imageio.*;
//import java.io.*;
//import java.awt.image.*;
//import java.awt.Color;

public class MainView extends JPanel {

    private JLabel board;

    public MainView() {
        super(null);
        setSize(1200, 900);

        board = new JLabel();
        add(board,0);
        board.setBounds(0,0,1200, 900);
        ImageIcon boardIcon = new ImageIcon("../Deadwood/resources/board.jpg");
        board.setIcon(boardIcon);

        setVisible(true);
        setFocusable(true);

    }


}
