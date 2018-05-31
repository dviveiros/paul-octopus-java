package com.ciandt.paul;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * This is the basic Application class. When you run the command line, this is the first class that will be called
 * and that will be responsible for triggering the prediction process.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Application.class.getName());

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private Config config;

    @Autowired
    private ApplicationContext context;

    private static final String[] validCommands = {"predict", "upload"};

    /**
     * Init method called by the runtime execution engine
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

        Options options = new Options();

        Option file = new Option("f", "file", true, "File prefix. Example: actual to actual_2006.csv, actual_2010.csv etc");
        file.setRequired(true);
        options.addOption(file);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("paul.sh", options);
            System.exit(1);
            return;
        }

        String command = "predict";
        Boolean debugEnabled = true;
        String filename = cmd.getOptionValue("file");

        //log the arguments
        if (config.isDebugEnabled()) {
            logger.debug("Application started with " + args.length + " arguments");
            for (int i = 0; i < args.length; i++) {
                logger.debug(">> " + args[i]);
            }
            logger.debug("command = " + command);
            logger.debug("debug mode = " + debugEnabled);
            logger.debug("file prefix = " + filename);
        }

        //prediction
        if ("predict".equals(command)) {
            try {
                predictionService.predict(filename, "CSVPredictor");
            } catch (Exception e) {
                logger.error("Error creating prediction", e);
                System.exit(1);
            }
        }

        logger.info("Process completed successfully!");
        System.exit(0);
    }

}

