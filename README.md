=========

## Spring JdbcTokenStore issue using 'password' authorization grant

### mysql db setup

```mysqladmin -u root -p  create oauth```

```mysql -u root -p -e "grant all privileges on oauth.* to oauth@localhost identified by 'verysecret'"```

### running 

```./gradlew build```

```java -jar  build/libs/oauth-test-0.1.0.jar```

### problem statement

Was not able to use JdbcTokenStore when user `password` authorization is used. To reproduce, run the service and execute 
the following: 
```
$ curl -X POST -vu clientapp:123456 http://localhost:8090/oauth/token -H "Accept: application/json" -d "password=12341232412341232412342&username=38068887730&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"
```

and use access token like this:

```
$ curl -X POST http://localhost:8090/greeting  -H "Authorization: Bearer 5fd3b58d-5d99-4a4e-bde4-2ac7cc7c8337"
```

error received should be like this:

```
{"timestamp":1487924536102,"status":500,"error":"Internal Server Error","exception":"java.lang.IllegalArgumentException","message":"Principal must not be null","path":"/greeting"}
```

as soon as you change JdbcTokenStore to InMemoryTokenStore() like this:

``` java
		@Bean
		public TokenStore tokenStore() {
			return new InMemoryTokenStore();
			// return new JdbcTokenStore(dataSource);
		}
```

everything works like a charm, producing:

```
{"id":1,"content":"Hello, 38068887730!"}
```

I was able to reproduce the same effect on Mysql and sqlite3.