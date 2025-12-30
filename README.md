# simple-ship-managerment
A simple ship managerment for Software engineering course training in two weeks.

Build with **SpringBoot** + **mysql** + **redis**
with **tencent cos cloud file storage**.

Help with **[trae.cn](https://www.trae.cn/) solo mode**


## Preview
<img width="1916" height="864" alt="image" src="https://github.com/user-attachments/assets/5041f154-22a3-4ca8-a91f-20790176efdf" />
<img width="1888" height="862" alt="image" src="https://github.com/user-attachments/assets/6d4e3d99-b3d0-4f35-b928-0e0afd67bc0d" />
<img width="1857" height="875" alt="image" src="https://github.com/user-attachments/assets/66e2ea2c-791f-4e2d-a405-1875ea4b0b5a" />
<img width="1857" height="905" alt="image" src="https://github.com/user-attachments/assets/4a6f8fce-3a41-4908-905a-ed8acb997cf2" />


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