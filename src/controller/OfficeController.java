package controller;

import model.Room.Office;
import model.Room.Upgrade;
import view.MainView;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by gildnej on 3/7/19.
 */
public class OfficeController {

    private ArrayList<JButton> upgradeButtons;
    private DeadwoodController deadwood;

    public OfficeController(Office office, MainView boardView, DeadwoodController deadwood){
        this.deadwood = deadwood;
        upgradeButtons = new ArrayList<>();

        for(Upgrade thisUpgrade : office.getUpgrades()){
            upgradeButtons.add(createButton(thisUpgrade,boardView));
        }

        upgradeButtons.add(createButton(office.getArea(), boardView));


    }

    public JButton createButton(Upgrade upgrade, MainView boardView){
        JButton thisButton = new JButton();
        int[] area = upgrade.getArea();

        thisButton.setBounds(area[0],area[1],area[2],area[3]);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(true);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("upgrade "+upgrade.getCurrency()+" "+upgrade.getAmt());

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }

    public JButton createButton(int[] area, MainView boardView){
        JButton thisButton = new JButton();

        thisButton.setBounds(area[0],area[1],area[2],60);
        thisButton.setOpaque(false);
        thisButton.setContentAreaFilled(false);
        thisButton.setBorderPainted(false);
        thisButton.setVisible(true);

        thisButton.addActionListener(deadwood);
        thisButton.setActionCommand("move office");

        boardView.add(thisButton, new Integer(2));

        return thisButton;
    }
}
