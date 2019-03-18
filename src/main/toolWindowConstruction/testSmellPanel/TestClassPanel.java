package main.toolWindowConstruction.testSmellPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Questa classe rappresenta il JPanel contenente i dati su una singola classe di test
 */
public class TestClassPanel extends JPanel{
    private JLabel testClassName;
    private JLabel testClassPath;
    private JButton details;

    public TestClassPanel(String testName, String testPath){
        testClassName = new JLabel();
        testClassName.setText(testName);
        testClassPath = new JLabel();
        testClassPath.setText(testPath);
        details = new JButton();
        details.setText("details");
        details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame detailsFrame = new JFrame();
                detailsFrame.getContentPane().add(new JLabel(testPath));
                detailsFrame.setSize(300,100);
                detailsFrame.setVisible(true);
            }
        });

        this.setLayout(new GridLayout(1,3));
        this.add(testClassName);
        this.add(testClassPath);
        this.add(details);
    }

}
