package com.ciandt.paul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

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

        String command = null;
        Boolean debugEnabled = null;

        if (args.length == 0) {
            showUsage();
            System.exit(-1);
        }

        // define the command to be executed
        command = readCommand(args);
        if (command == null) {
            showUsage();
            System.exit(-1);
        }

        // define the debug
        debugEnabled = readDebugStatus(args);
        if ((debugEnabled != null) && (debugEnabled)) {
            config.setDebug("true");
        }

        //log the arguments
        if (config.isDebugEnabled()) {
            logger.debug("Application started with " + args.length + " arguments");
            for (int i = 0; i < args.length; i++) {
                logger.debug(">> " + args[i]);
            }
            logger.debug("command = " + command);
            logger.debug("debugEnabled = " + debugEnabled);
        }

        //prediction
        if ("predict".equals(command)) {
            predictionService.predict();
        }


        System.exit(0);
    }

    /**
     * Read the command to be executed
     */
    private String readCommand(String[] args) {
        String command = args[0];
        if (!Arrays.asList(validCommands).contains(command)) {
            return null;
        } else {
            return command;
        }
    }

    /**
     * Read the debug status
     */
    private Boolean readDebugStatus(String[] args) {
        return "-debug".equals(args[args.length - 1]);
    }

    /**
     * Invalid command line. Show usage.
     */
    private void showUsage() {

        System.out.println("Syntax:");
        System.out.println(">> ./paul.sh <command> [<-debug>]");
        System.out.println();
        System.out.println("Command: 'predict' or 'upload'");
    }
}

