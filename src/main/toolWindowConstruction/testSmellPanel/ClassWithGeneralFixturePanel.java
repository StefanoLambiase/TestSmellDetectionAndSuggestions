package main.toolWindowConstruction.testSmellPanel;

import it.unisa.testSmellDiffusion.beans.InstanceVariableBean;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.MethodWithGeneralFixture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassWithGeneralFixturePanel extends JPanel {
    private JLabel classNameLabel;
    private JPanel listOfMethodsPanel;

    public ClassWithGeneralFixturePanel(GeneralFixtureInfo gfi){
        classNameLabel = new JLabel(gfi.getTestClass().getName());

        listOfMethodsPanel = new JPanel();
        listOfMethodsPanel.setLayout(new GridLayout(gfi.getMethodsThatCauseGeneralFixture().size(), 1));
        for(MethodWithGeneralFixture mb : gfi.getMethodsThatCauseGeneralFixture()){
            JPanel methodPanel = new JPanel(new GridLayout(1, 2));
            JLabel methodName = new JLabel(mb.getMethod().getName());
            JButton methodButton = new JButton("details");
            methodButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame detailsFrame = new JFrame();

                    //Parte relativa alla creazione della scritta informativa
                    String methodName = mb.getMethod().getName()+" non usa le seguenti variabili: ";

                    for(InstanceVariableBean instance : mb.getListOfInstances()){
                        methodName = new StringBuilder()
                                .append(methodName)
                                .append("\n   "+instance.getName())
                                .toString();
                    }

                    //Parte relativa alla creazione del frame
                    detailsFrame.getContentPane().add(new JLabel(methodName));
                    detailsFrame.setSize(500,300);
                    detailsFrame.setVisible(true);
                }
            });
            methodPanel.add(methodName);
            methodPanel.add(methodButton);

            listOfMethodsPanel.add(methodPanel);
        }
        this.setLayout(new GridLayout(1,2));
        this.add(classNameLabel);
        this.add(listOfMethodsPanel);
    }


}
