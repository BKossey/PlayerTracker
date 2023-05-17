package ui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * Represents the keypad in PlayerTrackerApp
 */
class KeyPad extends JPanel implements KeyListener {
    private static final String CLR_STR = "CLR";
    private JButton[] keys;
    private JLabel label;
    private String code;
    private ClickHandler keyHandler;

    // EFFECTS: creates a keypad
    public KeyPad() {
        code = "0";
        keyHandler = new ClickHandler();
        setLayout(new BorderLayout());
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new GridLayout(4,3));
        addButtons(keyPanel);
        add(keyPanel, BorderLayout.CENTER);
        label = new JLabel(code);
        Box hbox = Box.createHorizontalBox();
        hbox.add(Box.createHorizontalGlue());
        hbox.add(label);
        hbox.add(Box.createHorizontalGlue());
        add(hbox, BorderLayout.SOUTH);
    }

    // EFFECTS: returns code entered
    public String getCode() {
        return code;
    }

    // EFFECTS: clears the code entered in the keypad
    public void clearCode() {
        code = "0";
        label.setText(code);
        label.repaint();
    }

    // EFFECTS: adds buttons to button panel
    private void addButtons(JPanel p) {
        keys = new JButton[12];

        for (int i = 0; i < 9; i++) {
            keys[i] = new JButton(Integer.toString(i + 1));
            keys[i].addActionListener(keyHandler);
            p.add(keys[i]);
        }

        keys[9] = new JButton(CLR_STR);
        keys[9].addActionListener(keyHandler);
        p.add(keys[9]);
        keys[10] = new JButton("0");
        keys[10].addActionListener(keyHandler);
        p.add(keys[10]);

    }



    // A listener for key events.
    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();

            if (src.getText().equals(CLR_STR)) {
                code = "0";
            } else if (code.length() < 4) {
                code = code + src.getText();
            }

            label.setText(code);
            label.repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char key = ke.getKeyChar();

        if (key == '0') {
            keys[10].doClick();
        } else if (key > '0' && key <= '9') {
            keys[ke.getKeyChar() - '1'].doClick();
        }
    }
}

