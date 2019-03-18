package main.toolWindowConstruction;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import main.toolWindowConstruction.testSmellPanel.ClassWithGeneralFixturePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TestSmellWindowFactory {
    private JPanel principalPanel;
    private JPanel generalFixturePanel;
    private JPanel eagerTestPanel;

    private ArrayList<GeneralFixtureInfo> classesWithGeneralFixture;
    private ArrayList<EagerTestInfo> classesWithEagerTest;


    public TestSmellWindowFactory(ArrayList<GeneralFixtureInfo> classesWithGF, ArrayList<EagerTestInfo> classesWithET){
        principalPanel = new JPanel();
        principalPanel.setLayout(new GridLayout(3,1));

        if(classesWithGF != null){
            classesWithGeneralFixture = classesWithGF;
            generalFixturePanel = new JPanel(new GridLayout(classesWithGF.size(), 1));
            principalPanel.add(generalFixturePanel);
        }
        if (classesWithET != null){
            classesWithEagerTest = classesWithET;
            eagerTestPanel = new JPanel(new GridLayout(classesWithET.size(), 1));
            principalPanel.add(eagerTestPanel);
        }
    }

    /**
     * Questo metodo crea il contenuto della ToolWindow
     * @param tw la ToolWindow in cui inserire il contenuto
     */
    public void createToolWindow(ToolWindow tw){
        if(classesWithGeneralFixture != null){
            for(GeneralFixtureInfo gfi : classesWithGeneralFixture){
                generalFixturePanel.add(new ClassWithGeneralFixturePanel(gfi));
            }
        }
        if (classesWithEagerTest != null){
            for(EagerTestInfo eti : classesWithEagerTest){

            }
        }

        //Parte relativa all'aggiunta del Component alla ToolWindow
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(principalPanel, "", true);
        tw.getContentManager().addContent(content);
    }


}
