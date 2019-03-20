package main.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import main.testSmellDetection.Detector;
import main.toolWindowConstruction.TestSmellWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class TestSmellAction extends AnAction {
    private ToolWindow testWindow;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Detector tsd = new Detector();

        //Mi salvo la lista di classi e di classi di test del progetto attivo
        Vector<ClassBean> myClasses = tsd.getAllClassesInTheProject(Objects.requireNonNull(anActionEvent.getProject().getBasePath()));
        ArrayList<ClassBean> myTestClasses = tsd.getAllTestClassesInTheProject(anActionEvent.getProject().getBasePath());

        //Eseguo l'analisi
        ArrayList<GeneralFixtureInfo> listGFI = tsd.executeDetectionForGeneralFixture(myTestClasses);
        ArrayList<EagerTestInfo> listETI = tsd.executeDetectionForEagerTest(myTestClasses, myClasses);

        //Creo la ToolWindow
        if(myTestClasses.isEmpty()){
            System.out.println("\nNon si e' committata alcuna classe di test");
        } else {
            createToolWindow(anActionEvent.getProject(), listGFI, listETI);
            testWindow.show(null);
        }
    }


    private void createToolWindow(Project project, ArrayList<GeneralFixtureInfo> listGFI, ArrayList<EagerTestInfo> listETI){
        Project activeProject = project;

        System.out.println("\nTOOL WINDOW: Inizio del processo per registrare la ToolWindow: TestWindow\n");
        //Creo la ToolWindow
        ToolWindowManager twm = ToolWindowManager.getInstance(activeProject);
        System.out.println("Ho preso il ToolWindowManager");

        //Questa parte serve a cancellare una eventuale ToolWindow precedentemente presente
        ToolWindow toolWindow = ToolWindowManager.getInstance(activeProject).getToolWindow("TestWindow");
        if(toolWindow != null){
            twm.unregisterToolWindow("TestWindow");
            System.out.println("Ho dovuto disattivare una precedente istanza della ToolWindow");
        }

        testWindow = twm.registerToolWindow("TestWindow", false, ToolWindowAnchor.BOTTOM);
        testWindow.setTitle("TestWindow");
        System.out.println("Ho registrato la ToolWindow");

        //Instanzio la classe che si occupa della formattazione della ToolWindow
        TestSmellWindowFactory testWindowFactory = new TestSmellWindowFactory(listGFI, listETI);
        //Questo metodo si occupa di creare la formattazione interna della ToolWindow e anche di aggiungervela
        testWindowFactory.createToolWindow(testWindow);

        System.out.println("Ho completato le operazioni riguardanti la ToolWindow");
    }


}
