#Introduction
ATM Service display services for ATM. It is simple "thin client" for connect to Bank Service. ATM Service contains some important services for communicate with clients

#Requirements
**Java 8+**

**Maven 3.5.4+**

**Docker 1.12+(not required)**

#Dependencies
ATM Service use Bank Services as core app, it is necessary to configure all service url from Bank Service(configuration is described bellow)

#Configure
There is application.properties file in source src/main/resources directory
* It is possible to update properties parameters here(not recommended)
* Replace configuration file location by add command while run app **--spring.config.location=LOCATION**
* Replace individual properties like **--server.port=8081**

Here is configuration for connect Bank Service
**get.card.by.number.service.url=http://localhost:8081/api/cards/getByCardNumber
auth.card.service.url=http://localhost:8081/api/cardAuth/auth
close.session.service.url=http://localhost:8081/api/cardAuth/closeSession
check.balance.service.url=http://localhost:8081/api/cards/operation/checkBalance
deposit.service.url=http://localhost:8081/api/cards/operation/deposit
withdrawal.service.url=http://localhost:8081/api/cards/operation/withdrawal**

#Build and deploy
* It is possible to build and deploy application with docker command **docker compose up**
* With maven: run command **mvn clean install** in root directory, it will create jar file in target directory - **atmservice-VERSION.jar** and it is possible to run it with command **java -jar FILE_NAME**

