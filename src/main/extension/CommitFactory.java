package main.extension;

import it.unisa.testSmellDiffusion.beans.ClassBean;

import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.lackOfCohesion.LackOfCohesionInfo;
import it.unisa.testSmellDiffusion.utility.TestSmellUtilities;
import main.testSmellDetection.IDetector;
import main.testSmellDetection.detector.StructuralDetector;
import main.testSmellDetection.detector.TextualDetector;
import main.toolWindowConstruction.TestSmellWindowFactory;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CommitFactory  extends CheckinHandlerFactory{

    //Oggetto usato per ottenere i file salvati durante la fase di commit
    private CheckinProjectPanel myPanel;


    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
        myPanel = panel;

        final CheckinHandler checkinHandler = new CheckinHandler() {
            @Override
            public ReturnResult beforeCheckin() {
                //Stampa di inizio
                System.out.println("\n\nHello! Inizio la fase pre-commit");

                //Questa parte riguarda l'analisi degli Smells
                IDetector detector;

                //Mi salvo la lista di classi di test del progetto attivo
                ArrayList<ClassBean> myTestClasses = TestSmellUtilities.getAllTestClasses(myPanel.getProject().getBasePath());

                //Eseguo l'analisi Testuale
                detector = new TextualDetector();

                ArrayList<GeneralFixtureInfo> listGFI = detector.executeDetectionForGeneralFixture(myPanel.getProject().getBasePath());
                ArrayList<EagerTestInfo> listETI = detector.executeDetectionForEagerTest(myPanel.getProject().getBasePath());
                ArrayList<LackOfCohesionInfo> listLOCI = detector.executeDetectionForLackOfCohesion(myPanel.getProject().getBasePath());

                //Creo la ToolWindow
                if(myTestClasses.isEmpty()){
                    System.out.println("\nNon si e' committata alcuna classe di test");
                } else {
                    new TestSmellWindowFactory().registerToolWindow(true, false, myPanel.getProject(), listGFI, listETI, listLOCI);
                }

                //Eseguo l'analisi Strutturale
                detector = new StructuralDetector();

                listGFI = detector.executeDetectionForGeneralFixture(myPanel.getProject().getBasePath());
                listETI = detector.executeDetectionForEagerTest(myPanel.getProject().getBasePath());
                listLOCI = detector.executeDetectionForLackOfCohesion(myPanel.getProject().getBasePath());

                //Creo la ToolWindow
                if(myTestClasses.isEmpty()){
                    System.out.println("\nNon si e' committata alcuna classe di test");
                } else {
                    new TestSmellWindowFactory().registerToolWindow(false, true, myPanel.getProject(), listGFI, listETI, listLOCI);
                }
                //Chiamata finale per completare il commit
                return super.beforeCheckin();
            }
        };
        return checkinHandler;
    }



    //METODI NON PIU' USATI
    /**
     * Questo metodo si occupa di trovare all'interno di tutte le classi nel commit quelle di test
     * @return un array contenente tutte le classe di test nel commit
     */
    /*
    private ArrayList<VirtualFile> getTestClasses(){
        ArrayList<VirtualFile> testClassesFiles = new ArrayList<>();
        ArrayList<VirtualFile> allFiles = (ArrayList<VirtualFile>) myPanel.getVirtualFiles();
        //Eseguo una doppia stampa a fini di test
        //Stampe per la verifica di TUTTI file ottenuti
        System.out.println("\nFile inseriti nel Commit:");
        for (VirtualFile fileX: allFiles) {
            System.out.println("\nNOME FILE: " + fileX.getName());
            System.out.println(fileX.getPath());
            //Inserisco solo i file di "test" all'interno del nuovo array
            if(fileX.getName().contains("Test")){
                testClassesFiles.add(fileX);
            }
        }
        //Stampe per i file di test aggiunti al commit
        if(!testClassesFiles.isEmpty()){
            System.out.println("\nFile di test:");
            for (VirtualFile fileX: testClassesFiles) {
                System.out.println("\nNOME FILE: " + fileX.getName());
                System.out.println(fileX.getPath());
            }
        }
        return testClassesFiles;
    }
    */

    /**
     * Questo metodo si occupa di creare la ToolWindow che mostrerà i file di test individuati
     * @param testFiles la lista di file di test individuati
     */
    /*
    private void createToolWindowWithFiles(ArrayList<VirtualFile> testFiles){
        Project activeProject = myPanel.getProject();

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
        TestSmellWindowFactory testWindowFactory = new TestSmellWindowFactory(testFiles);
        //Questo metodo si occupa di creare la formattazione interna della ToolWindow e anche di aggiungervela
        testWindowFactory.createToolWindowWithTestFile(testWindow);

        System.out.println("Ho completato le operazioni riguardanti la ToolWindow");
    }
    */

}
