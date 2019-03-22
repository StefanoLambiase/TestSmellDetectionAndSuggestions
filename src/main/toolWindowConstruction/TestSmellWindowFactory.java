package main.toolWindowConstruction;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;

import javax.swing.*;
import java.util.ArrayList;

public class TestSmellWindowFactory {
    private JPanel generalFixturePanel;
    private JPanel eagerTestPanel;

    private ArrayList<GeneralFixtureInfo> classesWithGeneralFixture;
    private ArrayList<EagerTestInfo> classesWithEagerTest;


    public TestSmellWindowFactory(){

    }


    /**
     * Questo metodo si occupa di registrare e mostrare la ToolWindow
     * @param project il progetto attivo
     * @param listGFI lista di info su GeneralFixture
     * @param listETI lista di info su EagerTest
     */
    public void registerToolWindow(Project project, ArrayList<GeneralFixtureInfo> listGFI, ArrayList<EagerTestInfo> listETI) {
        Project activeProject = project;

        System.out.println("\nTOOL WINDOW: Inizio del processo per registrare la ToolWindow: TestWindow\n");
        //Creo la ToolWindow
        ToolWindowManager twm = ToolWindowManager.getInstance(activeProject);
        System.out.println("Ho preso il ToolWindowManager");

        //Questa parte serve a cancellare una eventuale ToolWindow precedentemente presente
        ToolWindow toolWindow = ToolWindowManager.getInstance(activeProject).getToolWindow("TestWindow");
        if (toolWindow != null) {
            twm.unregisterToolWindow("TestWindow");
            System.out.println("Ho dovuto disattivare una precedente istanza della ToolWindow");
        }

        ToolWindow testWindow = twm.registerToolWindow("TestWindow", false, ToolWindowAnchor.BOTTOM);
        testWindow.setTitle("TestWindow");
        System.out.println("Ho registrato la ToolWindow");

        //Inizio ad occuparmi della formattazione della ToolWindow
        classesWithGeneralFixture = listGFI;
        classesWithEagerTest = listETI;

        if (listGFI != null) {
            generalFixturePanel = new GeneralFixturePanel(listGFI);
        }
        if (listETI != null) {
            eagerTestPanel = new EagerTestPanel(listETI);

            //Questo metodo si occupa di creare la formattazione interna della ToolWindow e anche di aggiungervela
            createToolWindow(testWindow);
            testWindow.show(null);

            System.out.println("Ho completato le operazioni riguardanti la ToolWindow");
        }
    }

    /**
     * Questo metodo crea il contenuto della ToolWindow
     * @param tw la ToolWindow in cui inserire il contenuto
     */
    public void createToolWindow(ToolWindow tw){
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        //In questa parte costruisco i Content da mettere nella ToolWindow
        if(classesWithGeneralFixture != null){
            JBScrollPane scroll = new JBScrollPane(generalFixturePanel);
            Content contentGeneralFixture = contentFactory.createContent(scroll, "GeneralFixture", true);
            tw.getContentManager().addContent(contentGeneralFixture);
        }
        if (classesWithEagerTest != null){
            JBScrollPane scroll = new JBScrollPane(eagerTestPanel);
            Content contentEagerTest = contentFactory.createContent(scroll, "EagerTest", true);
            tw.getContentManager().addContent(contentEagerTest);
        }
    }



}
