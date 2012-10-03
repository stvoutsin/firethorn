#!/bin/bash
#
#

hostname=localhost
hostport=8080

name()
    {
    date '+%Y%m%d %H%M%S%N'
    }

for i in {1..1000}
do
    curl -v \
        -H 'Accept: application/json' \
        --data "jdbc.resource.catalogs.create.name=jdbc-catalog-$(name)" \
        http://${hostname}:${hostport}/firethorn/jdbc/resource/1/catalogs/create
done


