### How to build docker image
```
bash gradlew :bbuhot_server:jibDockerBuild
```
    
### How to run the image
Edit the configuation.json to suit your need,
remember the jspb will look differently if from docker.
```
docker run \
      --name bbuhot_server \
      -p 8080:8080 \
      -v ~/configuation.json:/app/cfg.json \
      -d bbuhot_server /app/cfg.json
```