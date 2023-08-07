import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Node extends JButton {

    Node parent;
    int col;
    int row;
    Panel myPanel;

    /**
     * For A*.
     * gCost: Distance between current node and the start node
     * hCost: Distance between current node and goal node
     * fCost: The total cost of (G + H).
     */
    int gCost, hCost, fCost;


    boolean start, goal, solid, open, checked, clicked;

    public Node(int theRow, int theCol, Panel thePanel) {
        col = theCol;
        row = theRow;
        myPanel = thePanel;
        setBackground(Color.white);
        setForeground(Color.BLACK);

    }
    public void setAsStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }
    public void setAsGoal() {
        setBackground(Color.YELLOW);
        setForeground(Color.black);
        setText("Goal");
        goal = true;
    }
    public void setAsSolid(){
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }
    public void setNotSolid(){
        setBackground(Color.WHITE);
        setForeground(Color.black);
        solid = false;
    }
    public void setAsOpen() {
        open =true;
    }
    public void setAsChecked(){
        if(!start && !goal){
            setBackground(Color.orange);
            setForeground(Color.black);
        }
        checked = true;
    }
    public void setAsPath(){
        setBackground(Color.green);
        setForeground(Color.black);
    }
}
