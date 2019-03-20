package main.toolWindowConstruction;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import main.toolWindowConstruction.testSmellPanel.ClassWithEagerTestPanel;
import main.toolWindowConstruction.testSmellPanel.ClassWithGeneralFixturePanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class TestSmellWindowFactory {
    private JBScrollPane scroll;
    private JPanel generalFixturePanel;
    private JPanel eagerTestPanel;

    private ArrayList<GeneralFixtureInfo> classesWithGeneralFixture;
    private ArrayList<EagerTestInfo> classesWithEagerTest;


    public TestSmellWindowFactory(ArrayList<GeneralFixtureInfo> classesWithGF, ArrayList<EagerTestInfo> classesWithET){
        JPanel principalPanel = new JPanel();
        scroll = new JBScrollPane(principalPanel);

        principalPanel.setLayout(new GridLayout(3,1));

        if(classesWithGF != null){
            classesWithGeneralFixture = classesWithGF;
            TitledBorder border = new TitledBorder("GENERAL FIXTURE");
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitlePosition(TitledBorder.TOP);

            generalFixturePanel = new JPanel(new GridLayout(1+classesWithGF.size(), 1));
            generalFixturePanel.setBorder(border);

            principalPanel.add(generalFixturePanel);
        }
        if (classesWithET != null){
            classesWithEagerTest = classesWithET;

            TitledBorder border = new TitledBorder("EAGER TEST");
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitlePosition(TitledBorder.TOP);

            eagerTestPanel = new JPanel(new GridLayout(1+classesWithET.size(), 1));
            eagerTestPanel.setBorder(border);

            principalPanel.add(eagerTestPanel);
        }
    }

    /**
     * Questo metodo crea il contenuto della ToolWindow
     * @param tw la ToolWindow in cui inserire il contenuto
     */
    public void createToolWindow(ToolWindow tw){
        //In questa parte costruisco i JPanel contenenti le info sui TestSmells
        if(classesWithGeneralFixture != null){
            JPanel topPanel = new JPanel(new GridLayout(1,2));
            topPanel.add(new JLabel("NOME CLASSE"));
            topPanel.add(new JLabel("DETTAGLI METODI"));
            generalFixturePanel.add(topPanel);

            for(GeneralFixtureInfo gfi : classesWithGeneralFixture){
                generalFixturePanel.add(new ClassWithGeneralFixturePanel(gfi));
            }
        }
        if (classesWithEagerTest != null){
            JPanel topPanel = new JPanel(new GridLayout(1,3));
            topPanel.add(new JLabel("NOME CLASSE"));
            topPanel.add(new JLabel("NOME PRODUCTION CLASS"));
            topPanel.add(new JLabel("DETTAGLI METODI"));
            eagerTestPanel.add(topPanel);

            for(EagerTestInfo eti : classesWithEagerTest){
                eagerTestPanel.add(new ClassWithEagerTestPanel(eti));
            }
        }

        //Parte relativa all'aggiunta del Component alla ToolWindow
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(scroll, "", true);
        tw.getContentManager().addContent(content);
    }


}
