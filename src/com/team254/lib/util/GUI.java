package com.team254.lib.util;


import com.team254.lib.trajectory.WaypointSequence;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * @author Unnas Hussain
 */
public class GUI extends JPanel implements ActionListener, FocusListener {

    static JFrame frame;
    JTextField xPositionField, yPositionField, thetaField, fileName;
    boolean waypointSet = false;
    Font regularFont, italicFont;

    final static int GAP = 10;
    int counter;
    private static WaypointSequence wp= new WaypointSequence(10);

    private JLabel waypointDisplay;

    public GUI() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel leftHalf = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };
        leftHalf.setLayout(new BoxLayout(leftHalf,
                BoxLayout.PAGE_AXIS));
        leftHalf.add(createEntryFields());
        leftHalf.add(createButtons());

        add(leftHalf);
        add(createWaypointDisplay());
    }

    public GUI(boolean b) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel full = new JPanel() {
                //Don't allow us to stretch vertically.
//                public Dimension getMaximumSize() {
//                Dimension pref = getPreferredSize();
//                return new Dimension(Integer.MAX_VALUE,
//                        pref.height);
//            }
        };
        full.setLayout(new BoxLayout(full,
                BoxLayout.PAGE_AXIS));
        full.add(createEntryFields());
        full.add(createButtons());

        add(full);
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Create File");
        button.addActionListener(this);
        button.setActionCommand("createFileAndExit");
        panel.add(button);

        button = new JButton("New Waypoint");
        button.addActionListener(this);
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP-5, GAP-5));
        return panel;
    }

    protected JComponent createWaypointDisplay() {
        JPanel panel = new JPanel(new BorderLayout());
        waypointDisplay = new JLabel();
        waypointDisplay.setHorizontalAlignment(JLabel.CENTER);
        regularFont = waypointDisplay.getFont().deriveFont(Font.PLAIN,
                16.0f);
        italicFont = regularFont.deriveFont(Font.ITALIC);
        updateDisplays();

        //Lay out the panel.
        panel.setBorder(BorderFactory.createEmptyBorder(
                GAP/2, //top
                0,     //left
                GAP/2, //bottom
                0));   //right
        panel.add(new JSeparator(JSeparator.VERTICAL),
                BorderLayout.LINE_START);
        panel.add(waypointDisplay,
                BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 150));

        return panel;
    }

    protected void updateDisplays() {
        waypointDisplay.setText("Waypoints created:"+counter);
        counter++;
    }
    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("createFileAndExit".equals(e.getActionCommand())) {
            frame.setVisible(false);
            frame.dispose();
            createAndShowGUI("Create File");
        } else {
            addWaypoint();
        }
        updateDisplays();
    }

    protected void addWaypoint(){
        double x = Double.parseDouble(xPositionField.getText());
        double y = Double.parseDouble(yPositionField.getText());
        double theta = Double.parseDouble(thetaField.getText());
        wp.addWaypoint(new WaypointSequence.Waypoint(x, y, theta));
    }



    /**
     * Called when one of the fields gets the focus so that
     * we can select the focused field.
     */
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    //Workaround for formatted text field focus side effects.
    protected void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    //Needed for FocusListener interface.
    public void focusLost(FocusEvent e) { } //ignore


    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "X Position: ",
                "Y Position: ",
                "Angle: "};

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        xPositionField  = new JTextField();
        xPositionField.setColumns(20);
        fields[fieldNum++] = xPositionField;

        yPositionField = new JTextField();
        yPositionField.setColumns(20);
        fields[fieldNum++] = yPositionField;

        thetaField = new JTextField();
       thetaField.setColumns(20);
        fields[fieldNum++] = thetaField;

        //Associate label/field pairs, add everything,
        //and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                    JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = null;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner)fields[i]);
            } else {
                tf = (JTextField)fields[i];
            }
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }
        SpringUtilities.makeCompactGrid(panel,
                labelStrings.length, 2,
                GAP, GAP, //init x,y
                GAP, GAP/2);//xpad, ypad
        return panel;
    }


    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                    + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI(String name) {
        //Create and set up the window.
        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        if(name.equals("Create File")){
            frame.add(new FileGUI());
        }
        else {
            frame.add(new GUI());
        }
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static WaypointSequence getWaypoints(){
        return wp;
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI("Trajectory Generator");
            }
        });
    }
}

