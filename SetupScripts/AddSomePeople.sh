# Add some people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "d6932481-dbda-4dec-aca8-68d1466b98f2", "name" : "P1" }' http://localhost:9000/people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "361ed5e0-ccbc-4b03-8225-50de38d036db", "name" : "P2" }' http://localhost:9000/people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "657e353d-7274-481a-ab2a-239ef4e8b9f6", "name" : "P3" }' http://localhost:9000/people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "bbac40ef-c852-4a9d-bfa4-836449b41efa", "name" : "P4" }' http://localhost:9000/people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "d0654a6a-d1e0-414f-adee-4beb48f0c165", "name" : "P5" }' http://localhost:9000/people

curl -H 'Content-Type: application/json' -X POST -d '{ "id" : "be40af31-2b40-4a5a-ac09-dca8041c5c47", "name" : "P6" }' http://localhost:9000/people

# Add relations

# P1 - Father - P2
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "361ed5e0-ccbc-4b03-8225-50de38d036db", "relationType" : "Father" }' http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/relatedto

# P1 - Mother - P3
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "657e353d-7274-481a-ab2a-239ef4e8b9f6", "relationType" : "Mother" }' http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/relatedto


# P1 - Sibling - P4
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "bbac40ef-c852-4a9d-bfa4-836449b41efa", "relationType" : "Sibling" }' http://localhost:9000/people/d6932481-dbda-4dec-aca8-68d1466b98f2/relatedto

# P2 - Father - P5
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "d0654a6a-d1e0-414f-adee-4beb48f0c165", "relationType" : "Father" }' http://localhost:9000/people/361ed5e0-ccbc-4b03-8225-50de38d036db/relatedto

# P5 - Father - P6
curl -H 'Content-Type: application/json' -X POST -d '{ "toId" : "be40af31-2b40-4a5a-ac09-dca8041c5c47", "relationType" : "Father" }' http://localhost:9000/people/d0654a6a-d1e0-414f-adee-4beb48f0c165/relatedto
