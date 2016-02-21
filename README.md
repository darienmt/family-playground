# Family playground
Proof of concept for a REST-full service using Scala/Play, Elasticseach and Couchbase.

# family-rest service

This service maintains people's registration and the registered relations between people.
People can be registered by posting data to the /people end-point. For example:

```
curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "d6932481-dbda-4dec-aca8-68d1466b98f2", "name" : "P1" }' http://localhost:9000/people
```

People can be retrieved also using:
```
curl http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2
```

In order to register a relation between two people:

```
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "86dfdab4-8ccc-4eb6-8fb7-9fc1592158b4", "relationType" : "Friends" }' http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/relatedto
```

And a family/relation graph can be accessed as:
```
curl http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/familytree
```

For more API documentation please take a look at ./family-rest/README.md

## Technology decisions

Here are some reason why the current technology stack was chosen:

- [Scala](http://www.scala-lang.org/): Its OOP and functional programming provide the tools to express the code in a very consist way while applying the technique you prefer.
- [Play framework](https://www.playframework.com/): It is a very popular non-blocking web framework not just in Scala but in Java too. Again, less lines of code and maturity.
- [Elasticsearch](https://www.playframework.com/): Very popular full-text-search product. Very easy to install, very fast and HTTP interface.
- [elastic4s](https://github.com/sksamuel/elastic4s): Mature Elasticsearch client for Scala. It does not use the HTTP interface. This is good because you can run out of ports on high-traffic applications if you are not careful enough. This client does not have that problem. Very powerful DSL.
- [Couchbase](http://www.couchbase.com/): Strong document database. Cross-data-center replication is available out-of-the-box. Very fast upserts and retrieval by primary key. Version 4 comes with N1QL for SQL-like query on documents(similar to what MongoDB has).
- [Reactive Couchbase](http://reactivecouchbase.org/): It is the official Couchbase's Scala client. It is based on Akka. Very easy to use(some features like N1QL support are still experimental).

## Data storage configuration

The Couchbase instances and Elasticsearch instances the family-rest service use should be configured on the file ./family-rest/conf/application.conf
on the following sections:

```
# Couchbase
couchbase {
  akka {
    timeout=1000
    execution-context {
      fork-join-executor {
        parallelism-factor = 4.0
        parallelism-max = 40
      }
    }
  }
  buckets = [{
    host="192.168.99.100"
    port="8091"
    base="pools"
    bucket="default"
    user=""
    pass=""
    timeout="0"
  }]
}

# Elasticseach
elasticseach.urls="elasticsearch://192.168.99.100:9300"
```

In above example the service is configured to talk to one instance of each server only, but both libraries has the ability to configure more than one instance for resiliency.
The IP address in use there is the IP address returned from:

```
docker-machine ip default
```


## Containers setup

This PoC use Docker to create instances of Elasticsearch and Couchbase for the family-rest service to access.
The setup scripts on ./Setupscripts will help to create and configure an Elasticseach instance and Couchbase instance.
The scripts assume docker, and docker-machine is installed locally, but they could be modified to run on another environments easily.

- CreateContainer.sh : Creates an Elasticsearch and a Couchbase container on the docker instance the local docker client is pointing to.
- ConfigureCb.sh : Configure the newly created Couchbase instance creating a bucket to connect to.
- ConfigureEs.sh : Configure the newly created Elasticseach instance with an index and mappings.
- AddSomePeople.sh : Once the family-rest service is running, this script will create some people on it, and also serve as an example of the service usage.




[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/darienmt/family-playground/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

