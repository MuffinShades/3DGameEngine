package game;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class KeyboardHead {
	public Keyboard k;
	
	KeyboardHead(Keyboard k) {
		this.k = k;
	}
	
	public void Start() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					k.keyPressed(e);
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					k.keyReleased(e);
				}
				return false;
			}
		});
	}
}
