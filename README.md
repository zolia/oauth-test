=========

## Spring JdbcTokenStore example using 'password' authorization grant

### mysql db setup

```mysqladmin -u root -p  create oauth```

```mysql -u root -p -e "grant all privileges on oauth.* to oauth@localhost identified by 'verysecret'"```

### running 

```./gradlew build```

```java -jar  build/libs/oauth-test-0.1.0.jar```

### possible pitfalls

Do not forget that Your 'User' model needs to 'implement Serializable', otherwise your 'User' model will not be stored in Authorization field of oauth_access_token.

### example usage
 
```
$ curl -X POST -vu clientapp:123456 http://localhost:8090/oauth/token -H "Accept: application/json" -d "password=12341232412341232412342&username=38068887730&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"
```

and use access token like this:

```
$ curl -X POST http://localhost:8090/greeting  -H "Authorization: Bearer 5fd3b58d-5d99-4a4e-bde4-2ac7cc7c8337"
```

expected result:

```
{"id":1,"content":"Hello, 38068887730!"}
```

###  InMemoryTokenStore usage:

for InMemoryTokenStore usage, change to this:

``` java
		@Bean
		public TokenStore tokenStore() {
			return new InMemoryTokenStore();
			// return new JdbcTokenStore(dataSource);
		}
```