# Getting docker-machine address
DOCKER_MACHINE_IP=$(docker-machine ip default)

# Setup memory
curl -v -X POST http://$DOCKER_MACHINE_IP:8091/pools/default -d memoryQuota=300 -d indexMemoryQuota=300

# Configure services
curl -v http://$DOCKER_MACHINE_IP:8091/node/controller/setupServices -d 'services=kv%2Cn1ql%2Cindex'

# Setup admin credentials
curl -v -X POST http://$DOCKER_MACHINE_IP:8091/settings/web -d port=8091 -d username=Administrator -d password=password

# Create bucket
curl -u Administrator:password -v -X POST http://$DOCKER_MACHINE_IP:8091/pools/default/buckets -d 'flushEnabled=1&threadsNumber=3&replicaIndex=0&replicaNumber=0&evictionPolicy=valueOnly&ramQuotaMB=100&bucketType=couchbase&name=default&authType=sasl&saslPassword='