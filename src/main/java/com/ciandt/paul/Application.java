package com.ciandt.paul;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

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

        Option input = new Option("c", "command", true, "command (predict or upload)");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("d", "debug", false, "[OPTIONAL] turn on debug mode (default = off)");
        output.setRequired(false);
        options.addOption(output);

        Option file = new Option("f", "file", false, "[OPTIONAL] generates the CSV file (only for 'predict' command) (default = no file)");
        output.setRequired(false);
        options.addOption(file);

        Option username = new Option("u", "username", true, "[OPTIONAL] predictor to be used (must be class name - ex. ZeroZeroPredictor, only for 'predict' command) (default = DefaultPredictor)");
        output.setRequired(false);
        options.addOption(username);

        Option predictor = new Option("p", "predictor", true, "username / login (required for 'upload' command)");
        output.setRequired(false);
        options.addOption(predictor);

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

        String command = cmd.getOptionValue("command");
        Boolean debugEnabled = cmd.hasOption("debug");
        if (debugEnabled) {
            config.setDebug("true");
        }
        Boolean generateFile = cmd.hasOption("file");
        String strUsername = cmd.getOptionValue("username");
        String strPredictor = cmd.getOptionValue("predictor");
        if (strPredictor == null) {
            strPredictor = config.getDefaultPredictor();
        }

        //log the arguments
        if (config.isDebugEnabled()) {
            logger.debug("Application started with " + args.length + " arguments");
            for (int i = 0; i < args.length; i++) {
                logger.debug(">> " + args[i]);
            }
            logger.debug("command = " + command);
            logger.debug("debug mode = " + debugEnabled);
            logger.debug("generate file = " + generateFile);
            logger.debug("username = " + strUsername);
            logger.debug("predictor = " + strPredictor);
        }

        //prediction
        if ("predict".equals(command)) {
            try {
                predictionService.predict(generateFile, strPredictor);
            } catch (Exception e) {
                logger.error("Error creating prediction", e);
                System.exit(1);
            }
        }

        //upload
        if ("upload".equals(command)) {
            if (strUsername == null) {
                formatter.printHelp("paul.sh", options);
                System.exit(1);
            } else {
                try {
                    predictionService.uploadPredictions(strUsername);
                } catch (IOException e) {
                    logger.error("Error uploading file to GCS", e);
                    System.exit(1);
                }
            }
        }

        logger.info("Process completed successfully!");
        System.exit(0);
    }

}

