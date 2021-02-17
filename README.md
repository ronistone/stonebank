# Stonebank

It's just a repo for testing technologies, it implements basic operations of a bank.

### Technologies

 - Kafka
 - Zookeeper
 - Camunda
 - Consul
 - Git2Consul
 - Postgres
 - K8s
 - Helm
 - Docker
 - Docker-compose
 - Maven
 - Spring


### Requirements

 - Local K8s
    - Helm 3.x
    - Minikube
 - Local docker
    - Docker
    - Docker-compose
    - Maven
    

### How To Run

 - Local docker
    - Run `docker-compose up` to run kafka, zookeeper, postgres and consul
    - Run `mvn clean install -T4` to build project
    - Run in folders `application`, `consumer` and `processor`, the command `mvn spring-boot:run`
    
 - Local K8s
    - Run minikube see [Minikube Install](https://minikube.sigs.k8s.io/docs/start/)
    - Install helm 3.x see [Helm Installation](https://helm.sh/docs/intro/quickstart/)
    - After installing helm, run in `deploy` folder `./init-helm-repos.sh` to add all necessary helm repos
    - With minikube and helm up and running, run in `deploy` folder `./deploy.sh apply all`. This command will deploy all stonebank stack
    

### Structure

 - `application` is the microservice with all APIs
 - `consumer` is the microservice that processes all transfer operations
 - `processor` is the microservice that runs all BPMN Camunda instances (the account creation operation)

