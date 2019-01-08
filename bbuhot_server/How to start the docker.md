### How to build docker image
    bash gradlew docker 
    
### How to run the image
    docker run \
      --name bbuhot_server \
      --network host \
      -v ${THE_CONFIG_PATH}:/usr/src/app/bbuhot_server/configuration.json \
      -d bbuhot_server 