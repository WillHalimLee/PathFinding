import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Panel extends JPanel {

    final int maxCol =150;
    final int maxRow = 75;
    final int nodeSize = 10;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    int step = 0;
    Node[][] node = new Node[maxRow][maxCol];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();
    boolean goalReached = false;

    Panel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow,maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol){
            node[row][col] = new Node(row,col);
            add(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
        setStartNode(65,2);
        setGoalNode(2,145);

       for (int i = 1; i < 74; i++){
           setSolidNode(i, 100);
       }

        setCostOnNodes();

    }

    private void setStartNode(final int row, final int col) {
        node[row][col].setAsStart();
        startNode = node[row][col];
        currentNode = startNode;
    }
    private void setGoalNode(final int row, final int col) {
        node[row][col].setAsGoal();
        goalNode = node[row][col];
    }
    private void setSolidNode(final int row, final int col) {
        node[row][col].setAsSolid();
    }
    private void setCostOnNodes() {
        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol){
         getCost(node[row][col]);
            col++;
            if(col == maxCol) {
                col = 0;
                row++;
            }
        }
    }
    private void getCost(Node node){
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;

        if(node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }

    public void aStarSearch(){
        long startTime = System.currentTimeMillis();
        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;
            step++;
            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if(row - 1 >= 0){
                openNode(node[row-1][col]);
            }
            if(row + 1 <= maxRow){
                openNode(node[row+1][col]);
            }
            if(col - 1 >= 0){
                openNode(node[row][col-1]);
            }
            if(col + 1 <= maxCol){
                openNode(node[row][col+1]);
            }
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                // Check if this node's G cost is better.
                else if (openList.get(i).fCost == bestNodeFCost){
                   if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                       bestNodeIndex = i;
                   }
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid){
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(){
        Node current = goalNode;
        while(current != startNode){
            current = current.parent;
            if(current != startNode) {
                current.setAsPath();
            }
        }
    }

    public void ManualSearch(){
        if (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            if(row - 1 >= 0){
                openNode(node[row-1][col]);
            }
            if(row + 1 <= maxRow){
                openNode(node[row+1][col]);
            }
            if(col - 1 >= 0){
                openNode(node[row][col-1]);
            }
            if(col + 1 <= maxCol){
                openNode(node[row][col+1]);
            }
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;


            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                // Check if this node's G cost is better.
                else if (openList.get(i).fCost == bestNodeFCost){
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
            }
        }

    }
}
