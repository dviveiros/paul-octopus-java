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
    >> ./paul.sh <command> [<params>] [<-debug>]
    
    Command: 'predict' or 'upload'
    Params:
    	- predict: <year> (default = 2018)
    	- upload: no params

    ``` 

## Working on your prediction