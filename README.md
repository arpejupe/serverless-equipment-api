Serverless Equipment API
=================================
Demo application for IoT projects. Provisions and scales automatically. Can be easily exported into CI/CD pipeline.


### Features
* Fetch Equipment by EquipmentNumber
* Fetch List of Equipment
* Create Equipment Item


##Instructions
### Pre-Requisites
* [AWS CLI configured](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html) for local development
* [Node](https://nodejs.org/en/download/)  for running serverless
* [Serverless framework](https://serverless.com/). Powerful toolkit for developing serverless web applications in "infrastructure as code" manner.
* JDK version 8.x or newer
* [Maven](https://maven.apache.org/) for building the Java app
* [Docker]() for local development

### Installed Plugins for serverless
* [serverless-plugin-stage-variables]() For enabling Cloudwatch logging automatically
* [serverless-sam]() for generating SAM templates from serverless template
* [serverless-finch]() for deploying client app to s3

### How to deploy to cloud
Since serverless framework generates SAM based on the implementations of Handler and Response interfaces we can provision
needed resources automatically:
```
mvn clean install && sls deploy
```

Additional needed resources such as dynamodb, s3 buckets and IAM roles are defined in serverless.yml

Serverless Framework allows you to create stages for your project to deploy to. Stages are useful for creating environments for testing and development.
You can specify stages in yml template or by deploying with `--stage` flag

### How to develop locally
Instead of deploying to the cloud, you can deploy application locally in docker containers and test lambdas in your local 
environment.

1) Set up docker. In order to make containers link to each other, create dynamodb container using specified network and attach it to SAM with --docker

    ```
    docker run --network serverless-equipment-api-local \
    -d -p 8000:8000 --name dynamodb amazon/dynamodb-local
    ```

2) Create table to dynamodb running in the container:
    ```
    aws dynamodb create-table \
      --table-name Equipment  \
      --attribute-definitions \
        AttributeName=EquipmentNumber,AttributeType=S --key-schema \
        AttributeName=EquipmentNumber,KeyType=HASH \
      --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
      --endpoint-url http://localhost:8000
    ```

3) Populate database with mock data
    ```
    aws dynamodb --region=eu-west-1 --endpoint-url http://localhost:8000 \
    put-item --table-name Equipment --item \
    '{"EquipmentNumber": {"S": "D435"}, "Address": {"S": "Test"},"ContractStartDate":{"S": "2010-05-30 22:15:51"},"ContractEndDate": {"S": "2010-05-30 22:15:52"},"Status":{"S":"STOPPED"}}'
    ```

4) You can invoke single function with serverless invoke:
    ```
    serverless invoke local --function getEquipment --data '{"pathParameters": {"id": "1"}}'`
    ```
5) You can change environment variables such dynamodb endpoint by using `-env` flag

6) Whole application can be emulated locally by using SAM:
    ```
    serverless sam export --output ./template.yml
    ```
    
    ```
    sam local start-api --docker-network serverless-equipment-api-local
    ```

## License
This project is licensed under the MIT license, Copyright (c) 2019 Arttu Pekkarinen. For more information see `LICENSE.md`