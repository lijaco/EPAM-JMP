package com.epam.integration.tests;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.epam.processor.FileExecutor;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Category(IntegrationTest.class)
public class FileExecutorTest {
    private static FileExecutor processor;

    private static final String DATA_DIR = "src/main/resources";

    @BeforeClass
    public static void init() {
        int sizeOfIncomingQueue = 2;
        int sizeOfOutgoingQueue = 2;
        int sizeOfThreadPool = 20;
        int sizeOfReaderThreads = 3;
        int sizeOfEnricherThreads = 5;
        int sizeOfBatch = 40000;
        processor = new FileExecutor(
                sizeOfIncomingQueue,
                sizeOfOutgoingQueue,
                sizeOfThreadPool,
                sizeOfReaderThreads,
                sizeOfEnricherThreads,
                sizeOfBatch,
                DATA_DIR);
    }

    @Test
    public void testProcess() throws Exception {
        File daytimeFile = new File(DATA_DIR+"/DaytimeAccidents.csv");
        File nighttimeFile = new File(DATA_DIR+"/NighttimeAccidents.csv");
        if(daytimeFile.exists())daytimeFile.delete();
        if(nighttimeFile.exists())nighttimeFile.delete();

        processor.lanuchThread();
        Thread.sleep(20000);

        assertThat(daytimeFile.exists()).isTrue();
        assertThat(daytimeFile.isDirectory()).isFalse();
        assertThat(nighttimeFile.exists()).isTrue();
        assertThat(nighttimeFile.isDirectory()).isFalse();
    }
}
