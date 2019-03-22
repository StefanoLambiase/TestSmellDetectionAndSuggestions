package main.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.ToolWindow;
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
            new TestSmellWindowFactory().registerToolWindow(anActionEvent.getProject(), listGFI, listETI);
        }
    }


}
