# Rest-at-library

To run first buil postgres docker image with required tables:

```shell
docker build -t my_postgres .
```

Then run created image:

```shell
docker run --name myDatabase -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword my_postgres
```
