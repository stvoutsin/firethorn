FROM firethorn/python:2.1.36
MAINTAINER Stelios Voutsinas <stv@roe.ac.uk>

# Install Python Libs
RUN apt-get update
RUN apt-get install -y python-numpy python-scipy
RUN apt-get install -y pwgen
RUN apt-get install -y python-pycurl
RUN apt-get install unixodbc-dev
RUN pip install --allow-external pyodbc --allow-unverified pyodbc==4.0.7 pyodbc==4.0.7
RUN apt-get -y install python-matplotlib
RUN pip install --no-deps astropy

