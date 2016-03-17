Fiorano Open Source microservice container is built on top of industry standards including OSGi (Apache Karaf) and JMS (Apache ActiveMQ).

![](images/EsbContainerPlatform.png)

Fiorano's Open Source wire level protocol called Component (Microservice) Control Protocol (CCP) allows asynchronous communication between the Open Source ESB server and the microservices. 

**Note:** CCP is used by the microservices to communicate with server for commands like fetch configuration, initiate shutdown, set log level.

| Communication | Runtime Deployment | Monitoring |
| ------------- | ------------- |------------- |
| Deployed Microservices communicate asynchronously using JMS |  - Automatically creates the JMS endpoints (i.e. message queues, Topics) for individual Microservices <br> - Also creates “routes” for communication </br> | - Includes tools and APIs for managing and monitoring Microservices <br>- Ability to debug and modify message flows between Microservices</br> |

![](images/Fiorano.Microservice.Toolkit.png)