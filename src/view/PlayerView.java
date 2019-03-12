package view;

import controller.DeadwoodController;
import model.Player;
import model.Role.Lead;
import model.Role.Role;
import model.Room.Room;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created by gildnej on 3/11/19.
 */
public class PlayerView extends JLayeredPane implements DeadwoodController.Listener{

    private Player p;
    private JLabel pView;
    private JLabel infoText;
    private JLabel infoIcon;
    private int eminence;
    private MainView boardView;

    public PlayerView(Player p, int n, MainView boardView){
        this.p = p;
        pView = new JLabel();
        infoIcon = new JLabel();
        infoText = new JLabel();
        this.boardView = boardView;

        boardView.add(pView,new Integer(6));
        boardView.add(infoIcon,new Integer(6));
        boardView.add(infoText,new Integer(6));

        eminence = n;
        changed(p);
    }

    public void changed(Player p){
        boardView.remove(infoIcon);
        boardView.remove(infoText);
        if(this.p.getName().equals(p.getName())) {
            if (p.getCurRole() != null) {
                paintRole();
            } else {
                paintLocation();
            }
            paintInfoPanel();
        }
    }

    public void paintRole(){
        Role curRole = p.getCurRole();
        pView.setIcon(new ImageIcon(p.getImgFile()));

        int[] roleArea = curRole.getArea();
        if(curRole instanceof Lead){
            int[] roomArea = p.getCurRoom().getArea();
            pView.setBounds(roleArea[0]+roomArea[0], roleArea[1]+roomArea[1], 46, 46);
        }else {
            pView.setBounds(roleArea[0], roleArea[1], 46, 46);
        }
        pView.setVisible(true);
    }

    public void paintLocation(){
        int area[] = Arrays.copyOf(p.getCurRoom().getPlayerPos(),p.getCurRoom().getPlayerPos().length);
        pView.setIcon(new ImageIcon(p.getImgFile()));

        area[0] += eminence*10;
        pView.setBounds(area[0],area[1],46,46);

        pView.setVisible(true);

    }

    public void paintInfoPanel(){
        String pInfo = p.toString();

        infoText.setText(pInfo);
        infoText.setBounds(70,900,500,50);
        infoText.setVisible(true);

        infoIcon.setIcon(new ImageIcon(p.getImgFile()));
        infoIcon.setBounds(10,900,46,46);
        infoIcon.setVisible(true);
        boardView.add(infoIcon);
        boardView.add(infoText);

    }
}
