package main.toolWindowConstruction;

import com.intellij.ui.components.JBScrollPane;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import main.toolWindowConstruction.testSmellPanel.ClassWithGeneralFixturePanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class GeneralFixturePanel extends JPanel{
    private ArrayList<GeneralFixtureInfo> classesWithGeneralFixture;

    public GeneralFixturePanel(ArrayList<GeneralFixtureInfo> classesWithGF){
        if(classesWithGF != null){
            //Parte relativa all'inizializzazione del Panel per GeneralFixture
            classesWithGeneralFixture = classesWithGF;

            TitledBorder border = new TitledBorder("GENERAL FIXTURE");
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitlePosition(TitledBorder.TOP);

            this.setLayout(new GridLayout(1+classesWithGF.size(), 1));
            this.setBorder(border);

            JPanel topPanel = new JPanel(new GridLayout(1,2));
            topPanel.add(new JLabel("NOME CLASSE"));
            topPanel.add(new JLabel("DETTAGLI METODI"));
            this.add(topPanel);

            //Parte relativa alla creazione delle singole info per ogni classe affetta da GeneralFixture
            for(GeneralFixtureInfo gfi : classesWithGeneralFixture){
                this.add(new ClassWithGeneralFixturePanel(gfi));
            }
        }
    }

    public ArrayList<GeneralFixtureInfo> getClassesWithGeneralFixture() {
        return classesWithGeneralFixture;
    }

    public void setClassesWithGeneralFixture(ArrayList<GeneralFixtureInfo> classesWithGeneralFixture) {
        this.classesWithGeneralFixture = classesWithGeneralFixture;
    }


}
