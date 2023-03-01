### MEAL系统

#### 通用约定说明

1. 关于通用返回格式

   本服务的返回值除特别说明外，均遵守相同的格式约定。格式如下:

    ```json
    {
      "code": 0,
      "description": "...",
      "records": 0,
      "result": {}
    }
    ```

  - `code`: number。处理结果标识。标识本次请求的状态。成功或失败
  - `description`: string。处理结果说明。通常在处理失败的情况下，给予人类可读的失败原因
  - `records`: number。列表型结果的元素数量。通常在`result`为列表的情况下，标识匹配条件的记录数，非列表型结果集时，该处的数据没有意义
  - `result`: array or object or other。结果业务数据。列表型为`[]`型，否则`{}`型。处理失败的情况下，此处大概率没有数据

2. 关于身份验证

   本服务依赖于身份验证系统，排除登录相关的接口外，其余接口均要求提交用户身份标识。身份标识遵守通用http约定，以下方的格式从`http header`中获取。

    ```
    Authorization: xxxxxx
    ```

   如果客户端提交的身份标识无法通过验证，服务器通常返回`403 Forbidden`响应码。客户端应当结合自身的逻辑，决定自己的跳转或提示处理。

3. 关于全局失败响应码

**响应状态码说明**:


| 状态码         | 说明                             |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200 | OK  |响应参数«用户注册成功响应»|
| 500 | 服务器内部错误  |Result|
| 703 | 短信验证码不匹配  ||
| 705 | 手机号已经注册  ||
| 707 | 无效手机号  ||
| 708 | 小程序openId失效  ||
| 709 | 小程序openId 无效绑定  ||
| 2040 | 当前帐户同时被登录了两次  ||
| 2050 | 当前帐户需要重新设置密码  ||
| 3100 | 无效店铺  ||
| 3101 | 无效商品  ||
| 4010 | 访问频率过快  ||
| 4020 | 帐户被禁用  ||
| 4030 | 用户名或密码错误  ||
| 4040 | 无效的身份标识  ||
| 4090 | 超时  ||
| 5030 | 金额限制  ||
| 5060 | 两次密码不一致  ||
| 5080 | 会员信息异常  ||
| 7001 | 参数错误  ||
| 7031 | 短信渠道获取失败  ||
| 9999 | 未知错误  ||


#### 业务API

##### 用户相关

1. 用户注册

    ```http request
    POST /wx/auth/register
    Content-Type: application/json

    ```
   request:

    ```json
   {
    "code": "123456",
    "username":"13566410997",
    "nickname":"test1",
    "password": "123456783",
    "mobile": "13566410997"
    }
   ```

   - `code`: string。 //手机验证码 目前可以随机(必须)
   - `username`: string。//与手机号相同 
   - `nickname`: string。// 网名 (必须)
   - `password`: string。-- 密码 (必须)
   - `mobile`: string。 -- 手机号 (必须)

   response:

    ```json
    {
    "code": 200,
    "description": null,
    "result": {
    "userInfo": {
    "nickName": "test21",
    "avatarUrl": "https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64",
    "country": null,
    "province": null,
    "city": null,
    "language": null,
    "gender": 0
    },
    "token": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2Nzc2NjMxNTUsImNyZWF0ZWQiOjE2Nzc2NTk1NTUxMTcsInVzZXJuYW1lIjoiMTM4NzY0MTA5NDUifQ.fVNFC8gS1wCVU_MlCDbKX6RTFUmDjqMG0yz2esLh9wh3Uo1P4VSOsAz6Bz-s0hBNZ1-0BZIJ0FOFlwf57XwCVA",
    "refresh": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2Nzc2OTU1NTUsImNyZWF0ZWQiOjE2Nzc2NTk1NTUxMTcsInVzZXJuYW1lIjoiMTM4NzY0MTA5NDUifQ.7ddNuJtlxPtNhBcZ34wYZgKryXkWTI9bKDqFA3_N2rUAB-Y8v9swSSACNVhpDsA0FTIes1Ppz7Xs8xBchVVt-A",
    "expire": 3600
    }
    },
    "records": null
    }
    ```
  - `userInfo`: string。 //用户信息
  - `userInfo.nickName`: string。//网名
  - `userInfo.avatarUrl`: string。// 头像url
  - `userInfo.country`: string。-- 国家(可忽略)
  - `userInfo.province`: string。 -- 省份 (可忽略)
  - `userInfo.city`: string。--城市 (可忽略)
  - `userInfo.gender`: int。--0是未知 1男 2女 (可忽略)
  - `token.token`: string。--token 
  - `token.refresh`: string。--刷新token
  - `token.expire`: int。--过期时间 秒

2. 用户登录

    ```http request
    POST http://localhost:9001/wx/auth/login
    Content-Type: application/json
    ```
   request:

    ```json
    {
    "phoneNumber": "13876410945",
    "password": "123456783",
    "code": "",
    "type": "1"
    }
   ```
    - `phoneNumber`: string。 //手机号*(必须)
    - `code`: string。 //手机验证码 目前可以随机(必须)
    - `password`: string。//密码 (选填)
    - `type`: int。//" 1、账号密码登录，2、手机验证码登录" (必须)
    ```
  response:

   ```json
    {
    "code": 200,
    "description": null,
    "result": {
    "token": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2Nzc2NjMxNTUsImNyZWF0ZWQiOjE2Nzc2NTk1NTUxMTcsInVzZXJuYW1lIjoiMTM4NzY0MTA5NDUifQ.fVNFC8gS1wCVU_MlCDbKX6RTFUmDjqMG0yz2esLh9wh3Uo1P4VSOsAz6Bz-s0hBNZ1-0BZIJ0FOFlwf57XwCVA",
    "refresh": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2Nzc2OTU1NTUsImNyZWF0ZWQiOjE2Nzc2NTk1NTUxMTcsInVzZXJuYW1lIjoiMTM4NzY0MTA5NDUifQ.7ddNuJtlxPtNhBcZ34wYZgKryXkWTI9bKDqFA3_N2rUAB-Y8v9swSSACNVhpDsA0FTIes1Ppz7Xs8xBchVVt-A",
    "expire": 3600
    }
    },
    "records": null
    }

  ```
  - `token.token`: string。--token
  - `token.refresh`: string。--刷新token
  - `token.expire`: int。--过期时间 秒
3. 刷新

    ```http request
    POST http://localhost:9001/wx/auth/refresh
    Authorization:  token
    ```
    - `token`: string。--此时token是refresh token
4. 店铺一览
   
    ```http request
    POST http://localhost:9001/wx/shop/list
    Authorization:  {{token}}
    Content-Type: application/json
    ```
    request:
   ```json
    {
      "shopId": "",
      "shopName": "",
      "page": 1,
      "limit": 10
    }
    ```
   - `shopId`: integer。--店铺id 单独查询时可用(选填)
   - `shopName`: string。--店铺名单独查询时可用(选填)
   - `page`: int。--页数
   - `limit`:int --分割
     response:

   ```json
    {
    "code": 200,
    "description": null,
    "result": [
    {
    "id": 1046003,
    "name": "汇佳",
    "desc": "鼎丰大厦便利店",
    "picUrl": "",
    "sortOrder": 1,
    "addTime": "2023-02-16 10:56:55"
    },
    {
    "id": 1046004,
    "name": "湘味小厨",
    "desc": "向西路",
    "picUrl": "",
    "sortOrder": 2,
    "addTime": "2023-02-16 10:59:16"
    }
    ],
    "records": 2
    }    
    ```
    - `id`: integer。--店铺id 
    - `name`: string。--店铺名
    - `desc`: string。--描述
    - `picUrl`:string --店铺url
    - `addTime`: date。--店铺创建时间
