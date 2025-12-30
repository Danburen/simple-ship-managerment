# simple-ship-managerment
A simple ship managerment for Software engineering course training in two weeks.

Build with **SpringBoot** + **mysql** + **redis**
with **tencent cos cloud file storage**.

Help with **[trae.cn](https://www.trae.cn/) solo mode**


## How to build
* require **Java JDK 22** or newer
* test frontend run in **nodejs 22+**
* *optional* generate your own RSA public key and private for rsa jwt token sign and verify algorithm with [OpenSSL](https://www.openssl.org/)
```bash
cd .\src\main\resources\keys
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

```bash
git clone https://github.com/Danburen/simple-ship-managerment
gradlew clean build
cd .\web-client\
npm install
```

## How to run in dev

* preparation
> [!IMPORTANT]
> make sure you have already installed [`nodejs 22`](https://nodejs.org/en/)
> make sure you have already installed [`redis`](https://redis.io/) and [`mysql8.0+`](https://www.mysql.com/)
> config your system environment variable for [`tencent cos`](https://console.cloud.tencent.com/cos).

* backend:

```bash
cd .\
```
**run with springboot in your idea**.
or run with **gradlew** use: 
```bash
gradlew bootRun
 ```

* frontend:
```bash
cd .\web-client\
npm run dev 
```

## Hot to use
>[!NOTE]
> 1. register your account.
> 2. login with your account.
> 3. click whatever you want to do.

*some resource are form web, If there is any infringement, please contact me to delete.*