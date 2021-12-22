# campsite-reservation

## Exercise

An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.
The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.
To streamline the reservations a few constraints need to be in place -
- The campsite will be free for all.
- The campsite can be reserved for max 3 days.
- The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
- Reservations can be cancelled anytime.
- For sake of simplicity assume the check-in & check-out time is 12:00 AM

### System Requirements
- The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
availability of the campsite for a given date range with the default being 1 month.
- Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
- The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
modification/cancellation of an existing reservation
- Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
- Provide appropriate error messages to the caller to indicate the error cases.
- In general, the system should be able to handle large volume of requests for getting the campsite availability.
- There are no restrictions on how reservations are stored as as long as system constraints are not violated.

## Tech Stack
- Sprint Boot
- Maven
- Postgres DB
- Swagger UI

## Setup
Instructions below are for installing on Mac.

* JDK >= 11 [Amazon Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/macos-install.html)
* Maven (https://maven.apache.org/download.cgi)
  ```
  $ brew install maven
  ```
* IntelliJ IDEA Ultimate (https://www.jetbrains.com/idea/download/) 

* Install Lombok Plugin in IntelliJ (In Intellij, Under "Preferences->Plugins", search for "Lombok" and install)
* Postgres (https://www.postgresql.org/download/)
  ```
  $ brew install postgresql
  ```

 ### Maven Settings
 Edit ~/.m2/settings.xml
 ```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                            https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <profiles>
        <profile>
            <id>securecentral</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
             <!-- Override the repository (and pluginRepository) "central" from the
               Maven Super POM -->  
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles> 
</settings>
 ```
## Build
From the campsite-reservation directory, run
```
mvn clean install -U
```
## Run
Run the CampgroundApplication.java class which contains the main method
## Swagger UI
http://localhost:8443/campsite/swagger-ui/#/
