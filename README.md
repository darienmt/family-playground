# Family playground
Proof of concept for a REST-full service using Scala/Play, Elasticseach and Couchbase.

## Container setup

This PoC use Docker to create instances of Elasticsearch and Couchbase for the family-rest service to access.
The setup scripts on ./Setupscripts will help to create and configure an Elasticseach instance and Couchbase instance.
The scripts assume docker, and docker-machine is installed locally, but they could be modified to run on another environments easily.

- CreateContainer.sh : Creates an Elasticsearch and a Couchbase container on the docker instance the local docker client is pointing to.
- ConfigureCb.sh : Configure the newly created Couchbase instance creating a bucket to connect to.
- ConfigureEs.sh : Configure the newly created Elasticseach instance with an index and mappings.
- AddSomePeople.sh : Once the family-rest service is running, this script will create some people on it, and also serve as an example of the service usage.


