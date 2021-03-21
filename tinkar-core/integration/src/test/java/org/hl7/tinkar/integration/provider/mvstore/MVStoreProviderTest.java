package org.hl7.tinkar.integration.provider.mvstore;


import org.hl7.tinkar.common.service.DataServiceController;
import org.hl7.tinkar.common.service.PrimitiveData;
import org.hl7.tinkar.common.service.ServiceProperties;
import org.hl7.tinkar.common.util.time.Stopwatch;
import org.hl7.tinkar.entity.LoadEntitiesFromDTO;
import org.hl7.tinkar.entity.util.EntityCounter;
import org.hl7.tinkar.entity.util.EntityProcessor;
import org.hl7.tinkar.entity.util.EntityRealizer;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MVStoreProviderTest {

    private static Logger LOG = Logger.getLogger(MVStoreProviderTest.class.getName());

    @BeforeSuite
    public void setupSuite() {
        LOG.info("setupSuite: " + this.getClass().getSimpleName());
        LOG.info(ServiceProperties.jvmUuid());
        PrimitiveData.selectProvider((dataServiceController) -> {
            String name = (String) dataServiceController.property(DataServiceController.ControllerProperty.NAME);
            if (name.equals("MVStore")) {
                return 1;
            }
            return -1;
        });
        PrimitiveData.start();
    }


    @AfterSuite
    public void teardownSuite() {
        LOG.info("teardownSuite");
        PrimitiveData.stop();
    }

    @BeforeMethod
    public void setUp() {
        LOG.info("setUp");
    }

    @Test
    public void loadChronologies() throws IOException {
        File file = new File("/Users/kec/Solor/tinkar-export.zip");
        LoadEntitiesFromDTO loadTink = new LoadEntitiesFromDTO(file);
        int count = loadTink.call();
        LOG.info("Loaded. " + loadTink.report());
    }

    @Test
    public void count() {
        if (!PrimitiveData.running()) {
            System.out.println("Reloading MVStoreProvider");
            Stopwatch reloadStopwatch = new Stopwatch();
            PrimitiveData.start();
            System.out.println("Reloading in: " + reloadStopwatch.elapsedTime()+ "\n");
        }
        EntityProcessor processor = new EntityCounter();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential count: \n" + processor.report() + "\n");
        processor = new EntityCounter();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel count: \n" + processor.report()+ "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential realization: \n" + processor.report() + "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel realization: \n" + processor.report()+ "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential realization: \n" + processor.report() + "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel realization: \n" + processor.report()+ "\n");
    }

    @Test
    public void openAndClose() {
        if (PrimitiveData.running()) {
            Stopwatch closingStopwatch = new Stopwatch();
            PrimitiveData.stop();
            closingStopwatch.end();
            System.out.println("MVS Closed in: " + closingStopwatch.elapsedTime()+ "\n");
        }
        System.out.println("Reloading MVStoreProvider");
        Stopwatch reloadStopwatch = new Stopwatch();
        PrimitiveData.start();
        System.out.println("MVS Reloading in: " + reloadStopwatch.elapsedTime()+ "\n");
        EntityProcessor processor = new EntityCounter();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential count: \n" + processor.report() + "\n");
        processor = new EntityCounter();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel count: \n" + processor.report()+ "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential realization: \n" + processor.report() + "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel realization: \n" + processor.report()+ "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEach(processor);
        System.out.println("MVS Sequential realization: \n" + processor.report() + "\n");
        processor = new EntityRealizer();
        PrimitiveData.get().forEachParallel(processor);
        System.out.println("MVS Parallel realization: \n" + processor.report()+ "\n");
     }


}
