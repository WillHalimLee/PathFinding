import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

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
            myPanel.autoAStar();
        }
        // BSF
        if(code == KeyEvent.VK_3){
            myPanel.mBFS();
        }
        if (code == KeyEvent.VK_4){
            myPanel.autoBFS();
        }
        // Dijkstra
        if(code == KeyEvent.VK_5){
            myPanel.mDijkstra();
        }
        if (code == KeyEvent.VK_6){
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
