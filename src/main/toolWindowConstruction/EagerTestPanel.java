package main.toolWindowConstruction;

import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import main.toolWindowConstruction.testSmellPanel.ClassWithEagerTestPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class EagerTestPanel extends JPanel {
    private ArrayList<EagerTestInfo> classesWithEagerTest;

    public EagerTestPanel(ArrayList<EagerTestInfo> classesWithET){
        if (classesWithET != null){
            //Parte relativa all'inizializzazione del Panel per EagerTest
            classesWithEagerTest = classesWithET;

            TitledBorder border = new TitledBorder("EAGER TEST");
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitlePosition(TitledBorder.TOP);

            this.setLayout(new GridLayout(1+classesWithET.size(), 1));
            this.setBorder(border);

            //Creazione della parte alta del JPanel
            JPanel topPanel = new JPanel(new GridLayout(1,3));
            topPanel.add(new JLabel("NOME CLASSE"));
            topPanel.add(new JLabel("NOME PRODUCTION CLASS"));
            JLabel dettagli = new JLabel("DETTAGLI METODI");
            dettagli.setHorizontalAlignment(SwingConstants.CENTER);
            topPanel.add(dettagli);

            this.add(topPanel);

            //Parte relativa alla creazione delle singole info per ogni classe affetta da EagerTest
            for(EagerTestInfo eti : classesWithEagerTest){
                this.add(new ClassWithEagerTestPanel(eti));
            }
        }
    }


    public ArrayList<EagerTestInfo> getClassesWithEagerTest() {
        return classesWithEagerTest;
    }

    public void setClassesWithEagerTest(ArrayList<EagerTestInfo> classesWithEagerTest) {
        this.classesWithEagerTest = classesWithEagerTest;
    }


}
