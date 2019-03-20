package main.testSmellDetection;

import it.unisa.testSmellDiffusion.beans.ClassBean;
import it.unisa.testSmellDiffusion.beans.InstanceVariableBean;
import it.unisa.testSmellDiffusion.beans.MethodBean;
import it.unisa.testSmellDiffusion.computation.TestSmellDetector;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.EagerTestInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.eagerTest.MethodWithEagerTest;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.GeneralFixtureInfo;
import it.unisa.testSmellDiffusion.testSmellInfo.generalFixture.MethodWithGeneralFixture;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Vector;

public class Detector {
    private TestSmellDetector tsm;

    private String projectFolderPath;

    /**
     * Costruttore usato per instanziare un detector usabile ovunque
     */
    public Detector(){
        tsm = new TestSmellDetector();
    }

    /**
     * Costruttore usato per instanziare un detector usabile solo su un determinato progetto
     * @param pFolderPath path del progetto attivo
     */
    public Detector(String pFolderPath){
        tsm = new TestSmellDetector(pFolderPath);
        projectFolderPath = pFolderPath;
    }



    public Vector<ClassBean> getAllClassesInTheProject(@NotNull String pFolderPath){
        return tsm.getAllClasses(pFolderPath);
    }

    public ArrayList<ClassBean> getAllTestClassesInTheProject(@NotNull String pFolderPath){
        System.out.println("\nDETECTOR: sono il Detector, inizio a lavorare:\nEcco la path del progetto attivo: "+pFolderPath);
        ArrayList<ClassBean> myTestClasses = tsm.getAllTestClass(pFolderPath);
        System.out.println("\nEcco le classi trovate:");
        for (ClassBean cb : myTestClasses){
            System.out.println("\n"+cb.toString());
        }
        return myTestClasses;
    }

    /**
     * Verifica la presenza di classi affette da GeneralFixture all'interno delle classi di test passate
     * @param testClasses le classi di test da analizzare
     * @return un array con le informazioni sulle classi affette da GeneralFixture
     */
    public ArrayList<GeneralFixtureInfo> executeDetectionForGeneralFixture(@NotNull ArrayList<ClassBean> testClasses){
        //Parte relativa a GeneralFixture
        System.out.println("\nDETECTOR: inizio a cercare per GENERAL FIXTURE: ");
        ArrayList<GeneralFixtureInfo> classesWithGeneralFixture = tsm.checkGeneralFixture(testClasses);

        //Parte relativa alla stampa dei dati ottenuti
        for(GeneralFixtureInfo gfi : classesWithGeneralFixture){
            String className = "\nNOME CLASSE: "+gfi.getTestClass().getName();

            for(MethodWithGeneralFixture method : gfi.getMethodsThatCauseGeneralFixture()){
                String methodName = "\nNome Metodo: "+method.getMethod().getName();

                for(InstanceVariableBean instance : method.getListOfInstances()){
                    methodName = new StringBuilder()
                            .append(methodName)
                            .append("\n   "+instance.getName())
                            .toString();
                }
                className = new StringBuilder()
                        .append(className)
                        .append(methodName)
                        .toString();
            }
            System.out.println(className);
        }
        return classesWithGeneralFixture;
    }

    /**
     * Verifica la presenza di classi affette da EagerTest all'interno delle classi di test passate
     * @param testClasses le classi di test da analizzare
     * @param allClassesInTheProject tutte le classi presente nel progetto. Necessarie per trovare la production class
     * @return un array con le informazioni sulle classi affette da EagerTest
     */
    public ArrayList<EagerTestInfo> executeDetectionForEagerTest(@NotNull ArrayList<ClassBean> testClasses, @NotNull Vector<ClassBean> allClassesInTheProject){
        //Parte relativa a EagerTest
        System.out.println("\nDETECTOR: inizio a cercare per EagerTest: ");
        ArrayList<EagerTestInfo> classesWithEagerTest = tsm.checkEagerTest(testClasses, allClassesInTheProject);

        //Parte relativa alla stampa dei dati ottenuti
        for(EagerTestInfo eti : classesWithEagerTest){
            String className = "\nNOME CLASSE: "+eti.getTestClass().getName();
            String productionClassName = "\nNOME PRODUCTION CLASS: "+eti.getProductionClass().getName();

            for(MethodWithEagerTest method : eti.getMethodsThatCauseEagerTest()){
                String methodName = "\nNome Metodo: "+method.getMethod().getName()+"\n   Lista metodi chiamati:";

                for(MethodBean mb : method.getListOfMethodsCalled()){
                    methodName = new StringBuilder()
                            .append(methodName)
                            .append("\n   "+mb.getName())
                            .toString();
                }
                className = new StringBuilder()
                        .append(className)
                        .append(productionClassName)
                        .append(methodName)
                        .toString();
            }
            System.out.println(className);
        }
        return classesWithEagerTest;
    }


}
