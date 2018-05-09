# Paul the Octopus (Java Quick Starter)

A basic project to use for the Paul the Octopus challenge (World Cup 2018) written in Java.

## Getting started

Steps to install and configure the project:

1. Clone the repository

    ```
    > git clone https://github.com/dviveiros/paul-octopus-java
    ```

2. Install [Gradle 4.7](https://gradle.org/releases/) or later. After that, edit the file *paul.sh* to configure the proper path for the tool.

    ```
    > export GRADLE_HOME=*<my_path>*/gradle
    ```
   
3. Install [JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Check if the installation was successful running:
    ```
    > java -version
    java version 1.8.0_65
    Java(TM) SE Runtime Environment (build 1.8.0_65-b17)
    Java HotSpot(TM) 64-Bit Server VM (build 25.65-b01, mixed mode)
    ```
       
4. Install the lastest version of the [gcloud tool](https://cloud.google.com/sdk/downloads). Initialize the tool using:
    ```
    > gcloud init
    > gcloud auth application-default login //use your @ciandt.com account
    > gcloud config set project project-paul-the-octopus  
    ```

5. Test your installation
    ```
    > ./paul.sh
        ____              __   __  __            ____       __
       / __ \____ ___  __/ /  / /_/ /_  ___     / __ \_____/ /_____  ____  __  _______
      / /_/ / __ `/ / / / /  / __/ __ \/ _ \   / / / / ___/ __/ __ \/ __ \/ / / / ___/
     / ____/ /_/ / /_/ / /  / /_/ / / /  __/  / /_/ / /__/ /_/ /_/ / /_/ / /_/ (__  )
    /_/    \__,_/\__,_/_/   \__/_/ /_/\___/   \____/\___/\__/\____/ .___/\__,_/____/
                                                                 /_/
    2018-05-07 18:49:30 - Starting Application on MacBookDaniel15 with PID 71719 (/Users/dviveiros/git/paul-octopus-java/build/libs/paul-the-octopus-0.1.jar started by dviveiros in /Users/dviveiros/git/paul-octopus-java)
    2018-05-07 18:49:30 - Running with Spring Boot v2.0.1.RELEASE, Spring v5.0.5.RELEASE
    2018-05-07 18:49:30 - No active profile set, falling back to default profiles: default
    2018-05-07 18:49:31 - Started Application in 1.169 seconds (JVM running for 1.694)
    Syntax:
    >> ./paul.sh
    
    usage: paul.sh
     -c,--command <arg>     command (predict or upload)
     -d,--debug             [OPTIONAL] turn on debug mode (default = off)
     -f,--file              [OPTIONAL] generates the CSV file (only for 'predict' command) (default = no file)
     -p,--predictor <arg>   [OPTIONAL] predictor to be used (must be class name - ex. ZeroZeroPredictor, only for 'predict' command) (default = DefaultPredictor)
     -u,--username <arg>    username / login (required for 'upload' command)
    ``` 

## Working on your prediction

First, I strongly recommend you to run some basic commands just to try the foundation out.

```
>> ./paul.sh -c predict
    ____              __   __  __            ____       __
   / __ \____ ___  __/ /  / /_/ /_  ___     / __ \_____/ /_____  ____  __  _______
  / /_/ / __ `/ / / / /  / __/ __ \/ _ \   / / / / ___/ __/ __ \/ __ \/ / / / ___/
 / ____/ /_/ / /_/ / /  / /_/ / / /  __/  / /_/ / /__/ /_/ /_/ / /_/ / /_/ (__  )
/_/    \__,_/\__,_/_/   \__/_/ /_/\___/   \____/\___/\__/\____/ .___/\__,_/____/
                                                             /_/
2018-05-08 20:12:34 - Starting Application on MacBookDaniel15 with PID 83310 (/Users/dviveiros/git/paul-octopus-java/build/libs/paul-the-octopus-0.1.jar started by dviveiros in /Users/dviveiros/git/paul-octopus-java)
2018-05-08 20:12:34 - Running with Spring Boot v2.0.1.RELEASE, Spring v5.0.5.RELEASE
2018-05-08 20:12:34 - No active profile set, falling back to default profiles: default
2018-05-08 20:12:35 - Started Application in 1.083 seconds (JVM running for 1.551)
2018-05-08 20:12:35 - Predicting results for year: 2018
2018-05-08 20:12:44 - Predicting results for year: 2006
2018-05-08 20:12:48 - Predicting results for year: 2010
2018-05-08 20:12:50 - Predicting results for year: 2014
2018-05-08 20:12:53 - **********************************************
2018-05-08 20:12:53 - * Algorithm performance
2018-05-08 20:12:53 - * 2006: Score = 363, Performance = 30.2500 %
2018-05-08 20:12:53 - * 2010: Score = 265, Performance = 22.0833 %
2018-05-08 20:12:53 - * 2014: Score = 298, Performance = 24.8333 %
2018-05-08 20:12:53 - * 
2018-05-08 20:12:53 - * Overall performance = 25.7222 %
2018-05-08 20:12:53 - **********************************************
2018-05-08 20:12:53 - Process completed successfully!
```

The default predictor is the OneZeroPredictor. Which means that all predictions will be 1x0. Note that the performance of this algorithm is 25.7222%.

Now, let's try a different one:
```
>> ./paul.sh -c predict -p ZeroZeroPredictor
    ____              __   __  __            ____       __
   / __ \____ ___  __/ /  / /_/ /_  ___     / __ \_____/ /_____  ____  __  _______
  / /_/ / __ `/ / / / /  / __/ __ \/ _ \   / / / / ___/ __/ __ \/ __ \/ / / / ___/
 / ____/ /_/ / /_/ / /  / /_/ / / /  __/  / /_/ / /__/ /_/ /_/ / /_/ / /_/ (__  )
/_/    \__,_/\__,_/_/   \__/_/ /_/\___/   \____/\___/\__/\____/ .___/\__,_/____/
                                                             /_/
2018-05-08 20:14:45 - Starting Application on MacBookDaniel15 with PID 83341 (/Users/dviveiros/git/paul-octopus-java/build/libs/paul-the-octopus-0.1.jar started by dviveiros in /Users/dviveiros/git/paul-octopus-java)
2018-05-08 20:14:45 - Running with Spring Boot v2.0.1.RELEASE, Spring v5.0.5.RELEASE
2018-05-08 20:14:45 - No active profile set, falling back to default profiles: default
2018-05-08 20:14:46 - Started Application in 1.254 seconds (JVM running for 1.785)
2018-05-08 20:14:46 - Predicting results for year: 2018
2018-05-08 20:14:56 - Predicting results for year: 2006
2018-05-08 20:14:59 - Predicting results for year: 2010
2018-05-08 20:15:02 - Predicting results for year: 2014
2018-05-08 20:15:05 - **********************************************
2018-05-08 20:15:05 - * Algorithm performance
2018-05-08 20:15:05 - * 2006: Score = 363, Performance = 30.2500 %
2018-05-08 20:15:05 - * 2010: Score = 406, Performance = 33.8333 %
2018-05-08 20:15:05 - * 2014: Score = 341, Performance = 28.4167 %
2018-05-08 20:15:05 - * 
2018-05-08 20:15:05 - * Overall performance = 30.8333 %
2018-05-08 20:15:05 - **********************************************
2018-05-08 20:15:05 - Process completed successfully!
```

Wow! If you change your prediction from 1x0 to 0x0, you should expect a better performance. This is consistent for previous editions of the World Cup.

*Your new benchmark is 30.8333%*

Are you ready to improve it? Just change the implementation of the class `DefaultPredictor` or create a new one and let's see how accurate you can be. Good luck!