import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;

import static java.lang.Double.compare;
/** A button that will act as a single node*/
public class Node extends JButton {
    /** The node's parent node.*/
    Node parent;
    /** The node's x coordinate*/
    int col;
    /** The node's y coordinate*/
    int row;
    /** The display panel.*/
    Panel myPanel;

    /**
     * gCost: Distance between current node and the start node
     * hCost: Distance between current node and goal node
     * fCost: The total cost of (G + H).
     */
    double gCost, hCost, fCost;

    /**
     * start: If the node is a start node.
     * goal: If the node is a goal node.
     * solid: If the node is a wall.
     * open: If the node has been opened.
     * checked: If we have visited this node before.
     */
    boolean start, goal, solid, open, checked;

    /**
     * A constructor that builds a node.
     * @param theCol the node's x coordinate.
     * @param theRow the node's y coordinate.
     * @param thePanel the display panel.
     */
    public Node(int theRow, int theCol, Panel thePanel) {
        col = theCol;
        row = theRow;
        myPanel = thePanel;
        setBackground(Color.white);
        setForeground(Color.BLACK);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myPanel.requestFocusInWindow();
            }
        });
    }
    /**
     * A setter that sets the node as the current node that is being looked at.
     */
    public void setAsCurrent() {
        setBackground(Color.lightGray);
        setForeground(Color.white);
        setText("C");
    }
    /**
     * A setter that sets the node as the start node.
     */
    public void setAsStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }
    /**
     * A setter that sets the node as the goal node.
     */
    public void setAsGoal() {
        setBackground(Color.YELLOW);
        setForeground(Color.black);
        setText("Goal");
        goal = true;
    }
    /**
     * A setter that sets the node as a wall.
     */
    public void setAsSolid(){
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }

    /**
     * A setter that opens the node.
     */
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
    /**
     * A setter that paints the path green for display.
     */
    public void setAsPath(){
        setBackground(Color.green);
        setForeground(Color.black);
        setText("P");
    }

}
