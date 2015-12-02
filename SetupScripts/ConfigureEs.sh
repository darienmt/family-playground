# Getting docker-machine address
DOCKER_MACHINE_IP=$(docker-machine ip default)

# Create index 
curl -X PUT http://$DOCKER_MACHINE_IP:9200/family-tree

# People mapping
curl -H 'Content-Type: application/json' -X PUT -d '{ "properties" : { "id" : { "type" : "string", "index": "not_analyzed" }, "name" : { "type" : "string" } } }' http://$DOCKER_MACHINE_IP:9200/family-tree/_mapping/people

# Relation mapping
curl -H 'Content-Type: application/json' -X PUT -d '{ "properties" : { "fromId" : { "type" : "string", "index": "not_analyzed" }, "toId" : { "type" : "string", "index": "not_analyzed" }, "relationType" : { "type" : "string" } } }' http://$DOCKER_MACHINE_IP:9200/family-tree/_mapping/relations
