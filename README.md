# Paul the Octopus (Java Quick Starter)

A basic project to use for the Paul the Octopus challenge (World Cup 2018) written in Java.

## Getting started

Steps to install and configure the project:

1. Install [JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Check if the installation was successful running:
    ```
    > java -version
    java version 1.8.0_65
    Java(TM) SE Runtime Environment (build 1.8.0_65-b17)
    Java HotSpot(TM) 64-Bit Server VM (build 25.65-b01, mixed mode)
    ```

2. Clone the repository

    ```
    > git clone https://github.com/dviveiros/paul-octopus-java
    ```

3. Install [Gradle 4.7](https://gradle.org/releases/) or later. After that, make sure that the GRADLE_HOME environment variable is properly set and exported.

    ```
    > export GRADLE_HOME=<my_path>/gradle
    > echo $GRADLE_HOME
    <my_path>/gradle
    ```
   
   Test your gradle installation:
   
    ```
    > gradle -version
    ------------------------------------------------------------
    Gradle 4.7
    ------------------------------------------------------------
    
    Build time:   2018-04-18 09:09:12 UTC
    Revision:     b9a962bf70638332300e7f810689cb2febbd4a6c
    
    Groovy:       2.4.12
    Ant:          Apache Ant(TM) version 1.9.9 compiled on February 2 2017
    JVM:          1.8.0_65 (Oracle Corporation 25.65-b01)
    OS:           Mac OS X 10.13.4 x86_64

    ```
          
4. Install the lastest version of the [gcloud tool](https://cloud.google.com/sdk/downloads). Initialize the tool using:
    ```
    > gcloud init
    > gcloud auth application-default login
    (use your @ciandt.com login for the step above)
    > gcloud config set project project-paul-the-octopus  
    ```

    *Important*: gcloud is part of any GCE VM. If you are using it instead of your own environment, you can skip this step. 

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

Let start! I strongly recommend you to run some basic commands just to try it out.

```
> ./paul.sh -c predict
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
> ./paul.sh -c predict -p ZeroZeroPredictor
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

## Uploading the results

First, you must execute a prediction using the `-f` flag.

```
> ./paul.sh -c predict -p ZeroZeroPredictor -f
    ____              __   __  __            ____       __
   / __ \____ ___  __/ /  / /_/ /_  ___     / __ \_____/ /_____  ____  __  _______
  / /_/ / __ `/ / / / /  / __/ __ \/ _ \   / / / / ___/ __/ __ \/ __ \/ / / / ___/
 / ____/ /_/ / /_/ / /  / /_/ / / /  __/  / /_/ / /__/ /_/ /_/ / /_/ / /_/ (__  )
/_/    \__,_/\__,_/_/   \__/_/ /_/\___/   \____/\___/\__/\____/ .___/\__,_/____/
                                                             /_/
2018-05-08 20:29:46 - Starting Application on MacBookDaniel15 with PID 83585 (/Users/dviveiros/git/paul-octopus-java/build/libs/paul-the-octopus-0.1.jar started by dviveiros in /Users/dviveiros/git/paul-octopus-java)
2018-05-08 20:29:46 - Running with Spring Boot v2.0.1.RELEASE, Spring v5.0.5.RELEASE
2018-05-08 20:29:46 - No active profile set, falling back to default profiles: default
2018-05-08 20:29:47 - Started Application in 1.086 seconds (JVM running for 1.553)
2018-05-08 20:29:47 - Predicting results for year: 2018
2018-05-08 20:29:56 - Predicting results for year: 2006
2018-05-08 20:29:59 - Predicting results for year: 2010
2018-05-08 20:30:03 - Predicting results for year: 2014
2018-05-08 20:30:05 - **********************************************
2018-05-08 20:30:05 - * Algorithm performance
2018-05-08 20:30:05 - * 2006: Score = 363, Performance = 30.2500 %
2018-05-08 20:30:05 - * 2010: Score = 406, Performance = 33.8333 %
2018-05-08 20:30:05 - * 2014: Score = 341, Performance = 28.4167 %
2018-05-08 20:30:05 - * 
2018-05-08 20:30:05 - * Overall performance = 30.8333 %
2018-05-08 20:30:05 - **********************************************
2018-05-08 20:30:05 - Generating file predictions.csv
2018-05-08 20:30:05 - File created successfully. Run './paul.sh -c upload -u <username>' to upload it.
2018-05-08 20:30:05 - Process completed successfully!

```

Double check if the file was correctly created.

```
> cat predictions.csv
home,home_score,away_score,away
Russia,0,0,Saudi Arabia
Egypt,0,0,Uruguay
Morocco,0,0,IR Iran
Portugal,0,0,Spain
France,0,0,Australia
Argentina,0,0,Iceland
Peru,0,0,Denmark
Croatia,0,0,Nigeria
...
```

Upload the file to your bucket using the following command:

```
> ./paul.sh -c upload -u viveiros
    ____              __   __  __            ____       __
   / __ \____ ___  __/ /  / /_/ /_  ___     / __ \_____/ /_____  ____  __  _______
  / /_/ / __ `/ / / / /  / __/ __ \/ _ \   / / / / ___/ __/ __ \/ __ \/ / / / ___/
 / ____/ /_/ / /_/ / /  / /_/ / / /  __/  / /_/ / /__/ /_/ /_/ / /_/ / /_/ (__  )
/_/    \__,_/\__,_/_/   \__/_/ /_/\___/   \____/\___/\__/\____/ .___/\__,_/____/
                                                             /_/
2018-05-08 20:32:51 - Starting Application on MacBookDaniel15 with PID 83629 (/Users/dviveiros/git/paul-octopus-java/build/libs/paul-the-octopus-0.1.jar started by dviveiros in /Users/dviveiros/git/paul-octopus-java)
2018-05-08 20:32:51 - Running with Spring Boot v2.0.1.RELEASE, Spring v5.0.5.RELEASE
2018-05-08 20:32:51 - No active profile set, falling back to default profiles: default
2018-05-08 20:32:52 - Started Application in 1.103 seconds (JVM running for 1.57)
2018-05-08 20:32:52 - Uploading file predictions.csv to bucket ciandt_projectoctopus_2018_viveiros
2018-05-08 20:32:54 - Upload completed!
2018-05-08 20:32:54 - Process completed successfully!
```