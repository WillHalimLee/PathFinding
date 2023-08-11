import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Panel extends JPanel {

    /**
     * The length of 2D array.
     */
    private final int maxRow = 75;
    /**
     * The height of the 2D array.
     */
    private final int maxCol = 150;
    /**
     * The size of each node.
     */
    private final int nodeSize = 10;
    /**
     * The panel's width.
     */
    private final int screenWidth = nodeSize * maxCol;
    /**
     * The panel's height.
     */
    private final int screenHeight = nodeSize * maxRow;
    /**
     * A counter for the number of nodes we had to visit.
     */
    private int step = 0;
    /**
     * A 2D array the holds our nodes.
     */
    Node[][] node = new Node[maxRow][maxCol];
    /**
     * Our start node, goal node, and the current node.
     */
    Node startNode, goalNode, currentNode;
    /**
     * A list that contains nodes that we are opening to observe.
     */
    ArrayList<Node> openList = new ArrayList<>();
    /**
     * A list that contains nodes that we have already observed.
     */
    ArrayList<Node> checkedList = new ArrayList<>();

    /**
     * A boolean statement checking if we reach our goal node from the start node.
     */
    boolean goalReached = false;

    /**
     * A default constructor that creates our display panel.
     */
    Panel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol) {
            node[row][col] = new Node(row, col, this);
            add(node[row][col]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
        setTest();
    }

    /**
     * A method that resets the panel.
     */
    public void reset() {
        this.removeAll();
        openList.clear();
        checkedList.clear();
        step = 0;
        goalReached = false;

        int row = 0;
        int col = 0;

        while (row < maxRow && col < maxCol) {
            node[row][col] = new Node(row, col, this);
            add(node[row][col]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
        setTest();
    }

    /**
     * This is what is used to set up the map
     */
    public void setTest() {
        setStartNode(45, 20);
        setGoalNode(20, 130);
        setLAtEnd();
    }
    /**
     * Puts a line through column 75 from row 3 to 71.
     */
    public void setGraphTestLine() {
        for (int i = 3; i < 72; i++) {
            setSolidNode(i, 75);
        }
    }
    /**
     * Puts a L shaped wall surrounding 20,130.
     */
    public void setLAtEnd() {
        for (int i = 0; i < 10; i++) {
            setSolidNode(20+i, 129);
            setSolidNode(19, 129+ i);
        }
    }
    /**
     * Puts a C wall around node in 40,30.
     */
    public void setGraphTestCBig() {
        for (int i = 0; i < 10; i++) {
            setSolidNode(45 - i, 30);
            setSolidNode(45 + i, 30);
        }
        for (int i = 0; i < 20; i++) {
            setSolidNode(35, 30 - i);
            setSolidNode(55, 30 - i);
        }
    }

    public void setGraphTestCSmol() {
        for (int i = 0; i < 5; i++) {
            setSolidNode(7 - i, 10);
            setSolidNode(7 + i, 10);
        }
        for (int i = 0; i < 5; i++) {
            setSolidNode(12, 10 - i);
            setSolidNode(2, 10 - i);
        }
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

    private void openNode(Node node) {
        if (!node.checked && !node.solid) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }

    }

    private void trackThePath() {
        Node current = goalNode;
        int pathL = 0;
        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
                pathL++;
            }
        }
        System.out.println("Path Length: " + pathL);
    }

    private void getCost(Node node) {
        if (node.parent != null) {
            int deltaX = Math.abs(node.col - node.parent.col);
            int deltaY = Math.abs(node.row - node.parent.row);

            if (deltaX == 1 && deltaY == 1) {
                // Diagonal movement
                node.gCost = node.parent.gCost + 14;
            } else {
                // Horizontal or vertical movement
                node.gCost = node.parent.gCost + 10;
            }

        }
        int xDistance = node.col - goalNode.col;
        int yDistance = node.row - goalNode.row;
        node.hCost = (int) (Math.sqrt(xDistance * xDistance + yDistance * yDistance) * 10);

        node.fCost = node.gCost + node.hCost;

//        if(node != startNode && node != goalNode){
//            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<br>H: " + node.hCost + "</html>");
//        }
    }

    public void checkAround(int row, int col) {
        //Top mid
        if (row - 1 > 0) {
            openNode(node[row - 1][col]);
        }
        // Right
        if (col + 1 < maxCol) {
            openNode(node[row][col + 1]);
        }
        // Bottom mid
        if (row + 1 < maxRow) {
            openNode(node[row + 1][col]);
        }
        // Left
        if (col - 1 > 0) {
            openNode(node[row][col - 1]);
        }
        // Top left
        if (row - 1 > 0 && col - 1 > 0) {
            openNode(node[row - 1][col - 1]);
        }
        // Top right
        if (row - 1 > 0 && col + 1 < maxCol) {
            openNode(node[row - 1][col + 1]);
        }
        // bottom right
        if (row + 1 < maxRow && col + 1 < maxCol) {
            openNode(node[row + 1][col + 1]);
        }
        // Bottom left
        if (row + 1 < maxRow && col - 1 > 0) {
            openNode(node[row + 1][col - 1]);
        }

    }

    public void mAStar() {
        if (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            if (currentNode != startNode) {
                currentNode.setAsCurrent();
            }

            checkAround(row, col);
            int bestNodeIndex = 0;
            double bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost) {
                    if (openList.get(i).hCost < openList.get(bestNodeIndex).hCost) {
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
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
    }

    public void autoAStar() {
        long startTime = System.currentTimeMillis();
        while (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            checkAround(row, col);

            int bestNodeIndex = 0;
            double bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if (!openList.get(i).checked) {
                    step++;
                }
                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
//                 This seems to give us a less efficient path
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if (openList.get(i).hCost < openList.get(bestNodeIndex).hCost) {
                        bestNodeIndex = i;
                        bestNodeFCost = openList.get(i).fCost;
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
        System.out.println("Nodes Checked : " + step);
    }

    public void mBFS() {
        if (!goalReached) {
            step++;
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            if (currentNode != startNode) {
                currentNode.setAsCurrent();
            }
            checkAround(row, col);
            int bestNodeIndex = 0;
            double bestNodeHCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if (!openList.get(i).checked) {
                    step++;
                }
                openList.get(i).setAsChecked();
                // Check if this node's F cost is better.
                if (openList.get(i).hCost < bestNodeHCost) {
                    bestNodeIndex = i;
                    bestNodeHCost = openList.get(i).hCost;
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
    }

    public void autoBFS() {
        long startTime = System.currentTimeMillis();
        while (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            checkAround(row, col);

            int bestNodeIndex = 0;
            double bestNodeHCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                getCost(openList.get(i));
                if (!openList.get(i).checked) {
                    step++;
                }

                openList.get(i).setAsChecked();
                if (openList.get(i).hCost < bestNodeHCost) {
                    bestNodeIndex = i;
                    bestNodeHCost = openList.get(i).hCost;
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
        System.out.println("Nodes Checked : " + step);
    }

    public void mDijkstra() {
        if (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);

            if (currentNode != startNode) {
                currentNode.setAsCurrent();
            }

            checkAround(row, col);

            int bestNodeIndex = 0;
            double bestNodeGCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                openList.get(i).setAsChecked();
                // Check if this node's G cost is better.
                if (openList.get(i).gCost < bestNodeGCost) {
                    bestNodeIndex = i;
                    bestNodeGCost = openList.get(i).gCost;
                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
    }

    public void autoDijkstra() {
        long startTime = System.currentTimeMillis();
        while (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            checkedList.add(currentNode);
            openList.remove(currentNode);
            checkAround(row, col);

            int bestNodeIndex = 0;
            double bestNodeGCost = Integer.MAX_VALUE;
            for (int i = 0; i < openList.size(); i++) {
                // Find the cost of traveling to this node.
                getCost(openList.get(i));
                if (!openList.get(i).checked) {
                    step++;
                }
                openList.get(i).setAsChecked();
                // Check if this node's G cost is better.
                if (openList.get(i).gCost < bestNodeGCost) {
                    bestNodeIndex = i;
                    bestNodeGCost = openList.get(i).gCost;

                }
            }
            // Get the next best node for out next step.
            currentNode = openList.get(bestNodeIndex);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
        System.out.println("Nodes Checked : " + step);
    }
}
