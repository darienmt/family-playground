# Pull docker images.

docker pull couchbase/server

docker pull elasticsearch

# Create containers.

docker run --name local-cb -p 8091:8091 -p 11210:11210 -p 11211:11211 -p 8092:8092 -p 8093:8093 -d couchbase/server

docker run --name local-es -p 9200:9200 -p 9300:9300 -d elasticsearch