/*
 *  Copyright (C) 2013 Marcus Hirt
 *                     www.hirt.se
 *
 * This software is free:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) Marcus Hirt, 2013
 */
package com.adafruit.lcd;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides an example on how you can listen for buttons being
 * pressed on the LCD shield. Notice that this implementation will busy poll the
 * buttons. If you can spare a GPIO port (not one on the MCP27017 on the LCD
 * shield) the buttons can be checked a little bit less often by hooking up INTA
 * (pin 20) to the GPIO port and check for interrupts before reading the state
 * of the input pins. Note that such changes require an update to the LCD class
 * as well.
 * 
 * @author Marcus Hirt
 */
public class ButtonPressedObserver {
	private volatile boolean isRunning = false;
	private final List<ButtonListener> buttonListeners = new LinkedList<ButtonListener>();
	private final LCD lcd;

	private class ButtonChecker implements Runnable {
		@Override
		public void run() {
			Set<Button> trackedButtons = new HashSet<Button>();
			while (isRunning) {
				int bitmask;
				try {
					bitmask = lcd.buttonsPressedBitmask();
					Set<Button> pressedButtons = Button
							.getButtonsPressed(bitmask);

					for (Button trackedButton : trackedButtons) {
						if (!pressedButtons.contains(trackedButton)) {
							fireNotification(trackedButton);
						}
					}
					trackedButtons = pressedButtons;
				} catch (IOException e) {
					Logger.getLogger("se.hirt.pi.com.adafruit").log(Level.SEVERE,
							"Could not get buttons bitmask!", e);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// Don't care
				}
			}

		}

		private void fireNotification(Button trackedButton) {
			ButtonListener[] listeners;
			synchronized (buttonListeners) {
				listeners = buttonListeners
						.toArray(new ButtonListener[buttonListeners.size()]);
			}
			for (ButtonListener l : listeners) {
				l.onButtonPressed(trackedButton);
			}
		}

	}

	public ButtonPressedObserver(LCD lcd) {
		this.lcd = lcd;
	}

	public void removeButtonListener(ButtonListener l) {
		synchronized (buttonListeners) {
			buttonListeners.remove(l);
			if (buttonListeners.isEmpty()) {
				stopButtonMonitor();
			}
		}
	}

	private void stopButtonMonitor() {
		isRunning = false;
	}

	public void addButtonListener(ButtonListener l) {
		synchronized (buttonListeners) {
			if (buttonListeners.isEmpty()) {
				startButtonMonitor();
			}
			buttonListeners.add(l);
		}
	}

	private void startButtonMonitor() {
		isRunning = true;
		Thread t = new Thread(new ButtonChecker(), "Button Checker");
		t.setDaemon(true);
		t.start();
	}
}
