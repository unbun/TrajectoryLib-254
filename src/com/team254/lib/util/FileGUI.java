package com.team254.lib.util;


import com.team254.lib.trajectory.*;
import com.team254.lib.trajectory.io.TextFileSerializer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
/**
 * @author Unnas Hussain
 */
public class FileGUI extends GUI implements ActionListener, FocusListener {

    private static JTextField pathNameField;

    private JLabel waypointDisplay;
    private static final double kWheelbaseWidth = 25.5/12;
    private static String directory = "../paths";


    public FileGUI() {
        super(false);
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("createFileAndExit".equals(e.getActionCommand())) {
            psuedoMain();
        }
    }

    /*****************OVERRIDDEN GUI METHODS:*******************/
    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Finish File");
        button.addActionListener(this);
        button.setActionCommand("createFileAndExit");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP-5, GAP-5));
        return panel;
    }

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "File/Path Name: "};

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        pathNameField  = new JTextField();
        pathNameField.setColumns(20);
        fields[fieldNum++] = pathNameField;

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


    /*********************File Writing Methods********************/

    private static void psuedoMain(){
        TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
        config.dt = .01;
        config.max_acc = 8.0;
        config.max_jerk = 50.0;
        config.max_vel = 10.0;
        // Path name must be a valid Java class name.
        final String path_name = pathNameField.getText();

        // Description of this auto mode path.
        // Remember that this is for the GO LEFT CASE!
        Path path = PathGenerator.makePath(getWaypoints(), config,
                kWheelbaseWidth, path_name);

        // Outputs to the directory supplied as the first argument.
        TextFileSerializer js = new TextFileSerializer();
        String serialized = js.serialize(path);
        //System.out.print(serialized);
        String fullpath = joinPath(directory, path_name + ".txt");
        if (!writeFile(fullpath, serialized)) {
            System.err.println(fullpath + " could not be written!!!!1");
            System.exit(1);
        } else {
            System.out.println("Wrote " + fullpath);
        }
    }

    public static String joinPath(String path1, String path2)
    {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    private static boolean writeFile(String path, String data) {
        try {
            File file = new File(path);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}

