import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A class the allows keyboard interactions.
 * */
public class KeyHandler implements KeyListener {

    /** The panel the displays the algorithms*/
    Panel myPanel;

    public KeyHandler(Panel thePanel){
        myPanel = thePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // A*
        if(code == KeyEvent.VK_1){
            myPanel.mAStar();
        }
        if(code == KeyEvent.VK_2){
            System.out.println("A Star");
            myPanel.autoAStar();
        }
        // BSF
        if(code == KeyEvent.VK_3){
            myPanel.mBFS();
        }
        if (code == KeyEvent.VK_4){
            System.out.println("BFS");
            myPanel.autoBFS();
        }
        // Dijkstra
        if(code == KeyEvent.VK_5){
            myPanel.mDijkstra();
        }
        if (code == KeyEvent.VK_6){
            System.out.println("Dijkstra");
            myPanel.autoDijkstra();
        }
        if (code == KeyEvent.VK_R){
            myPanel.reset();
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
