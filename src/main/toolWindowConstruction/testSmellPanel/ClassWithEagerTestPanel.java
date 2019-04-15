package main.toolWindowConstruction.testSmellPanel;

import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.MethodWithEagerTest;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Questa classe rappresenta la GUI nella quale vengono mostrate le informazioni riguardanti una classe affetta da EagerTest
 */
public class ClassWithEagerTestPanel extends JPanel {

    public ClassWithEagerTestPanel(EagerTestInfo eti){
        //Setto il contorno del JPanel
        Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);

        //Costruisco la struttura del JPanel riguardante una specifica classe affetta da EagerTest
        JLabel classNameLabel = new JLabel("   "+eti.getTestClass().getBelongingPackage()+"."+eti.getTestClass().getName());
        JLabel pClassNameLabel = new JLabel(eti.getProductionClass().getBelongingPackage()+"."+eti.getProductionClass().getName());

        //parte riguardante i metodi
        JPanel listOfMethodsPanel = new JPanel();
        listOfMethodsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        listOfMethodsPanel.setLayout(new GridLayout(eti.getMethodsThatCauseEagerTest().size(), 1));

        for(MethodWithEagerTest mb : eti.getMethodsThatCauseEagerTest()){
            JPanel methodPanel = new JPanel(new GridLayout(1, 2));
            JLabel methodName = new JLabel("   "+mb.getMethod().getName());
            JButton methodButton = new JButton("details");
            methodButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame detailsFrame = new JFrame();
                    Container cp = detailsFrame.getContentPane();
                    cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

                    //Parte relativa alla creazione della scritta informativa
                    String methodName = " Method: "+mb.getMethod().getName()+" calls the following methods: ";
                    JLabel methodNameLabel = new JLabel(methodName);
                    methodNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    cp.add(methodNameLabel);

                    for(MethodBean mbCalled : mb.getListOfMethodsCalled()){
                        JLabel methodCalledName = new JLabel("   "+mbCalled.getName());
                        methodCalledName.setAlignmentX(Component.LEFT_ALIGNMENT);
                        cp.add(methodCalledName);
                    }

                    //Parte relativa alla refactoring tips
                    JLabel refactoringLabel = new JLabel("It can be removed using this refactoring operations:");
                    JLabel firstRefactoringLabel = new JLabel("Extract method: affected method can be splitted into smaller methods, each one testing a specific behavior of the tested object.");

                    refactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    firstRefactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    cp.add(refactoringLabel);
                    cp.add(firstRefactoringLabel);

                    //Parte relativa alla creazione del frame
                    detailsFrame.setSize(500,300);
                    detailsFrame.setVisible(true);
                }
            });
            methodPanel.add(methodName);
            methodPanel.add(methodButton);

            listOfMethodsPanel.add(methodPanel);
        }
        this.setLayout(new GridLayout(1,3));
        this.add(classNameLabel);
        this.add(pClassNameLabel);
        this.add(listOfMethodsPanel);
    }
}
