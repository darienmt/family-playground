Family tree rest service
-----------------------

This service allow to maintain a family tree as a set of people related to each other.
It use [Couchbase](http://www.couchbase.com/) as data repository and [Elasticseach](www.elastic.co) as full-text-search service.
The application is written is Scala and Play.
Both Couchbase and Elasticsearch are cluster oriented applications and their client libraries most of the time have client-side load balancing capabilies.
This features combined provide the playground to create a service example that could be replicated as well and conform a cluster that other clients with client-side load balancing could use in order to provide resiliency to their applications.

It is hard to create so many servers to host all this pieces together, but there is [Docker](https://www.docker.com/).
With Docker container approach, every development station can host all this servers as containers.

# Service documentation

## Return a person by its identifier

### Request
- URI: /people/:id
- HTTP Verb: GET
- Request body should be empty

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
{
  "id": "d6932481-dbda-4dec-aca8-68d1466b98f2",
  "name": "P1"
}
```

## Register a new person

### Request
- URI: /people
- HTTP Verb: POST
- HTTP Headers:
    - Content-Type : application/json

Sample request body:
```
{
    "id" : "86efdab4-8ccc-43b6-8fb7-9fc3592158b4",
    "name" : "AnotherYou"
}
```

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
{
  "Location": "/people/86efdab4-8ccc-43b6-8fb7-9fc3592158b4"
}
```

# Search for a person by name

### Request
- URI: /people/search/:part_of_name
- HTTP Verb: GET
- Request body should be empty

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
[
  {
    "id": "d6932481-dbda-4dec-aca8-68d1466b98f2",
    "name": "P1"
  }
]
```

# Find out if a person is related to another person

### Request
- URI: /people/:fromId/relatedto/:toId
- HTTP Verb: GET
- Request body should be empty

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
{
  "relation": "Sibling"
}
```

# Create/update a relation between two people

### Request
- URI: /people/:fromId/relatedto
- HTTP Verb: POST
- HTTP Headers:
    - Content-Type : application/json

Sample request body:
```
{
    "toId" : "86dfdab4-8ccc-4eb6-8fb7-9fc1592158b4",
    "relationType" : "Friends"
}
```

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
{
  "Location": "/people/86dfdab4-8ccc-43b6-8fb7-9fc3592158b4/relatedto/86dfdab4-8ccc-4eb6-8fb7-9fc1592158b4"
}
```

# Get a family/relation tree

### Request
- URI: /people/:id/familytree/:maxDepth
- HTTP Verb: GET
- Request body should be empty

Note: maxDepth is the maximum amount of jumps between the initial person and the person who is farther from him.
If no maxDepth is pass, 1 is assumed.
### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
{
  "treeStartId": "d6932481-dbda-4dec-aca8-68d1466b98f2",
  "maxDepth": 3,
  "people": [
    {
      "id": "d6932481-dbda-4dec-aca8-68d1466b98f2",
      "name": "P1"
    },
    {
      "id": "361ed5e0-ccbc-4b03-8225-50de38d036db",
      "name": "P2"
    },
    {
      "id": "657e353d-7274-481a-ab2a-239ef4e8b9f6",
      "name": "P3"
    },
    {
      "id": "bbac40ef-c852-4a9d-bfa4-836449b41efa",
      "name": "P4"
    },
    {
      "id": "d0654a6a-d1e0-414f-adee-4beb48f0c165",
      "name": "P5"
    },
    {
      "id": "be40af31-2b40-4a5a-ac09-dca8041c5c47",
      "name": "P6"
    }
  ],
  "relations": [
    {
      "fromId": "d6932481-dbda-4dec-aca8-68d1466b98f2",
      "relationType": "Father",
      "toId": "361ed5e0-ccbc-4b03-8225-50de38d036db"
    },
    {
      "fromId": "d6932481-dbda-4dec-aca8-68d1466b98f2",
      "relationType": "Mother",
      "toId": "657e353d-7274-481a-ab2a-239ef4e8b9f6"
    },
    {
      "fromId": "d6932481-dbda-4dec-aca8-68d1466b98f2",
      "relationType": "Sibling",
      "toId": "bbac40ef-c852-4a9d-bfa4-836449b41efa"
    },
    {
      "fromId": "361ed5e0-ccbc-4b03-8225-50de38d036db",
      "relationType": "Father",
      "toId": "d0654a6a-d1e0-414f-adee-4beb48f0c165"
    },
    {
      "fromId": "d0654a6a-d1e0-414f-adee-4beb48f0c165",
      "relationType": "Father",
      "toId": "be40af31-2b40-4a5a-ac09-dca8041c5c47"
    }
  ]
}
```

# Get people related to a person with a relation type at certain depth

### Request
- URI: /people/:id/inrelationto/:relationType/:depth
- HTTP Verb: GET
- Request body should be empty

Sample request
```
curl http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/inrelationto/Father/3
```

### Response
- HTTP Status: 200 OK
- HTTP Headers:
    - Content-Type : application/json

Sample response body:
```
[
  {
    "id": "be40af31-2b40-4a5a-ac09-dca8041c5c47",
    "name": "P6"
  }
]
```