NASA PROXY SERVICE

Step 1:
Pull and run this image
docker pull tboychuk/lmps

docker run -d -p 8080:8080 tboychuk/lmps

Step 2:
Pull and run redis:
docker pull redis

docker run -d --name bobo-redis -p 6379:6379 redis

Step 3:
Run this application and send some requests:

use this url to download the largest picture from nasa service:
http://localhost:9090/mars/pictures/largest?sol=15&camera=NAVCAM

!!! Don't forget to check the response and grab the value of header 'x-nasa-largest-picture-id' !!!
This is should be used in the next step.

use this url to make sure the picture is stored in redis:
http://localhost:9090/mars/pictures/redis/largest?id=15e01c28-0c69-4092-a681-5001cce28af6

Note: id parameter is automatically generated, please find it attached to the response of previous request or check logs
