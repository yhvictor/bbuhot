FROM java:8
ADD ./bbuhot_server.tar /usr/src/app/  
COPY ./configuration.json /usr/src/app/bbuhot_server/
CMD  /usr/src/app/bbuhot_server/bin/bbuhot_server /usr/src/app/bbuhot_server/configuration.json