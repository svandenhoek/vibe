#!/usr/bin/env bash

# DisGeNET database license can be found on: http://www.disgenet.org/ds/DisGeNET/html/legal.html
wget -v -nc http://rdf.disgenet.org/download/v5.0.0/gda-batch/gda_SIO_001347.ttl.tar.gz -P src/test/resources &&
tar -zxvf src/test/resources/gda_SIO_001347.ttl.tar.gz -C src/test/resources/

wget -v -nc http://rdf.disgenet.org/download/v5.0.0/gene.ttl.tar.gz -P src/test/resources &&
tar -zxvf src/test/resources/gene.ttl.tar.gz -C src/test/resources/

wget -v -nc http://rdf.disgenet.org/download/v5.0.0/disease.ttl.tar.gz -P src/test/resources &&
tar -zxvf src/test/resources/disease.ttl.tar.gz -C src/test/resources/