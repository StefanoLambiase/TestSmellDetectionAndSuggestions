package main.toolWindowConstruction.testSmellPanel;

import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.testSmellInfo.lackOfCohesion.LackOfCohesionInfo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassWithLackOfCohesionPanel extends JPanel {

    public ClassWithLackOfCohesionPanel(LackOfCohesionInfo loci){
        //Setto il contorno del JPanel
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);

        //Costruisco la struttura del JPanel riguardante una specifica classe affetta da GeneralFixture
        JLabel classNameLabel = new JLabel("   " + loci.getTestClass().getBelongingPackage() + "." + loci.getTestClass().getName());

        //Parte riguardante i metodi affetti da GeneralFixture
        JPanel listOfMethodsPanel = new JPanel();
        listOfMethodsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        listOfMethodsPanel.setLayout(new GridLayout(loci.getMethodsThatCauseLackOfCohesion().size(), 1));

        for(MethodBean mb : loci.getMethodsThatCauseLackOfCohesion()){
            JPanel methodPanel = new JPanel(new GridLayout(1, 2));
            JLabel methodName = new JLabel("   "+mb.getName());
            JButton methodButton = new JButton("details");
            methodButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame detailsFrame = new JFrame();
                    Container cp = detailsFrame.getContentPane();
                    cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

                    int windowWidth = detailsFrame.getWidth();
                    int windowHeight = detailsFrame.getHeight();

                    //Parte relativa alla creazione della scritta informativa
                    String methodName = " Method: "+mb.getName()+" isn't cohesive";
                    JLabel methodNameLabel = new JLabel(methodName);
                    methodNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    methodNameLabel.setMinimumSize(new Dimension(500, 125));
                    methodNameLabel.setPreferredSize(new Dimension(500, 125));
                    methodNameLabel.setMaximumSize(new Dimension(500, 125));
                    cp.add(methodNameLabel);

                    //Parte relativa alla refactoring tips
                    JLabel refactoringLabel = new JLabel("The Smell can be removed using one of this refactoring operations:");
                    JLabel firstRefactoringLabel = new JLabel("Extract method: the method can be move in an other class");
                    JLabel secondRefactoringLabel = new JLabel("Extract class: the test class can be split into two separated classes");

                    refactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    firstRefactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    secondRefactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    cp.add(refactoringLabel);
                    cp.add(firstRefactoringLabel);
                    cp.add(secondRefactoringLabel);

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
