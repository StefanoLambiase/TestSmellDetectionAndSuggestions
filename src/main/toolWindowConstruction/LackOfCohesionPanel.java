package main.toolWindowConstruction;

import it.unisa.testSmellDiffusion.testSmellInfo.lackOfCohesion.LackOfCohesionInfo;
import main.toolWindowConstruction.testSmellPanel.ClassWithLackOfCohesionPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class LackOfCohesionPanel extends JPanel {
    private ArrayList<LackOfCohesionInfo> classesWithLackOfCohesion;

    public LackOfCohesionPanel(ArrayList<LackOfCohesionInfo> classesWithLOC){
        //Parte relativa al titolo ed al bordo del panel
        TitledBorder border = new TitledBorder("LACK OF COHESION");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(border);

        if (!classesWithLOC.isEmpty()){
            //Parte relativa all'inizializzazione del Panel per EagerTest
            classesWithLackOfCohesion = classesWithLOC;

            //Creazione della parte alta del JPanel
            JPanel topPanel = new JPanel(new GridLayout(1,2));
            topPanel.add(new JLabel("CLASS NAME"));
            JLabel dettagli = new JLabel("DETAILS");
            dettagli.setHorizontalAlignment(SwingConstants.CENTER);
            topPanel.add(dettagli);

            //Mi prendo le dimensioni dello schermo
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();

            topPanel.setMinimumSize(new Dimension(width-70,40));
            topPanel.setPreferredSize(new Dimension(width-70, 40));
            topPanel.setMaximumSize(new Dimension(width-70, 40));
            this.add(topPanel);

            //Parte relativa alla creazione delle singole info per ogni classe affetta da EagerTest
            for(LackOfCohesionInfo loci : classesWithLackOfCohesion){
                this.add(new ClassWithLackOfCohesionPanel(loci));
            }
        } else {
            JPanel emptyPanel = new JPanel(new GridLayout(1,1));
            JLabel emptyLabel = new JLabel("Nessuno Smell Trovato!");
            emptyPanel.add(emptyLabel);
            this.add(emptyPanel);
        }
    }
}
