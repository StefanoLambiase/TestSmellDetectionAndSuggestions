package main.extension;

import it.unisa.testSmellDiffusion.beans.ClassBean;

import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import main.testSmellDetection.Detector;
import main.toolWindowConstruction.TestSmellWindowFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;


public class CommitFactory  extends CheckinHandlerFactory{

    //Oggetto usato per ottenere i file salvati durante la fase di commit
    private CheckinProjectPanel myPanel;
    //ToolWindow da visualizzare
    private ToolWindow testWindow;


    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
        myPanel = panel;

        final CheckinHandler checkinHandler = new CheckinHandler() {
            @Override
            public ReturnResult beforeCheckin() {
                //Stampa di inizio
                System.out.println("\n\nHello! Inizio la fase pre-commit");

                /*
                //In questa parte mi prendo le classi di test per lavorarci in seguito
                ArrayList<VirtualFile> testClasses = getTestClasses();

                //Creazione programmatica della ToolWindow
                if(testClasses.isEmpty()){
                    System.out.println("\nNon si e' committata alcuna classe di test");
                } else {
                    createToolWindowWithFiles(testClasses);
                    testWindow.show(null);
                }
                */

                //Questa parte riguarda l'analisi degli Smells
                Detector tsd = new Detector();

                //Mi salvo la lista di classi e di classi di test del progetto attivo
                Vector<ClassBean> myClasses = tsd.getAllClassesInTheProject(Objects.requireNonNull(myPanel.getProject().getBasePath()));
                ArrayList<ClassBean> myTestClasses = tsd.getAllTestClassesInTheProject(myPanel.getProject().getBasePath());

                //Eseguo l'analisi
                ArrayList<GeneralFixtureInfo> listGFI = tsd.executeDetectionForGeneralFixture(myTestClasses);
                ArrayList<EagerTestInfo> listETI = tsd.executeDetectionForEagerTest(myTestClasses, myClasses);

                //Creo la ToolWindow
                if(myTestClasses.isEmpty()){
                    System.out.println("\nNon si e' committata alcuna classe di test");
                } else {
                    createToolWindow(listGFI, listETI);
                    testWindow.show(null);
                }

                //Chiamata finale per completare il commit
                return super.beforeCheckin();
            }
        };
        return checkinHandler;
    }

    /**
     * Metodo usato per la creazione della ToolWindow che mostrerà i dati delle classi di test
     * @param listGFI lista di info su GeneralFixture
     * @param listETI lista di info su EagerTest
     */
    private void createToolWindow(ArrayList<GeneralFixtureInfo> listGFI, ArrayList<EagerTestInfo> listETI){
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
        TestSmellWindowFactory testWindowFactory = new TestSmellWindowFactory(listGFI, listETI);
        //Questo metodo si occupa di creare la formattazione interna della ToolWindow e anche di aggiungervela
        testWindowFactory.createToolWindow(testWindow);

        System.out.println("Ho completato le operazioni riguardanti la ToolWindow");
    }



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
