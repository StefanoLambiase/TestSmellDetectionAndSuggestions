package main.toolWindowConstruction.testSmellPanel;

import it.unisa.testSmellDiffusion.beans.InstanceVariableBean;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.MethodWithGeneralFixture;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Questa classe rappresenta la GUI nella quale vengono mostrate le informazioni riguardanti una classe affetta da GeneralFixture
 */
public class ClassWithGeneralFixturePanel extends JPanel {

    public ClassWithGeneralFixturePanel(GeneralFixtureInfo gfi){
        //Setto il contorno del JPanel
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);

        //Costruisco la struttura del JPanel riguardante una specifica classe affetta da GeneralFixture
        JLabel classNameLabel = new JLabel("   " + gfi.getTestClass().getBelongingPackage() + "." + gfi.getTestClass().getName());

        //Parte riguardante i metodi affetti da GeneralFixture
        JPanel listOfMethodsPanel = new JPanel();
        listOfMethodsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        listOfMethodsPanel.setLayout(new GridLayout(gfi.getMethodsThatCauseGeneralFixture().size(), 1));

        for(MethodWithGeneralFixture mb : gfi.getMethodsThatCauseGeneralFixture()){
            JPanel methodPanel = new JPanel(new GridLayout(1, 2));
            JLabel methodName = new JLabel("   "+mb.getMethod().getName());
            JButton methodButton = new JButton("details");
            methodButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame detailsFrame = new JFrame();
                    Container cp = detailsFrame.getContentPane();
                    cp.setLayout(new GridLayout(1+mb.getListOfInstances().size(), 1));

                    //Parte relativa alla creazione della scritta informativa
                    String methodName = " Il metodo: "+mb.getMethod().getName()+" non usa le seguenti variabili: ";
                    cp.add(new JLabel(methodName));

                    for(InstanceVariableBean instance : mb.getListOfInstances()){
                        JLabel instanceCalledName = new JLabel("   "+instance.getName());
                        cp.add(instanceCalledName);
                    }

                    //Parte relativa alla creazione del frame
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
