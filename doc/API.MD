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

   本服务依赖于身份验证系统，排除登录相关的接口外，其余接口均要求提交用户身份标识。身份标识遵守通用http约定，以下方的格式从`http header`
   中获取。

    ```
    Authorization: xxxxxx
    ```
  

3. 关于全局失败响应码

**响应状态码说明**:

| 状态码  | 说明             | schema         |
|------|----------------|----------------|
| 200  | OK             | 响应参数«用户注册成功响应» |
| 500  | 服务器内部错误        | Result         |
| 703  | 短信验证码不匹配       ||
| 705  | 手机号已经注册        ||
| 707  | 无效手机号          ||
| 708  | 小程序openId失效    ||
| 709  | 小程序openId 无效绑定 ||
| 2040 | 当前帐户同时被登录了两次   ||
| 2050 | 当前帐户需要重新设置密码   ||
| 3100 | 无效店铺           ||
| 3101 | 无效商品           ||
| 4010 | 访问频率过快         ||
| 4020 | 帐户被禁用          ||
| 4030 | 用户名或密码错误       ||
| 4040 | 无效的身份标识        ||
| 4090 | 超时             ||
| 5030 | 金额限制           ||
| 5060 | 两次密码不一致        ||
| 5080 | 会员信息异常         ||
| 7001 | 参数错误           ||
| 7031 | 短信渠道获取失败       ||
| 9999 | 未知错误           ||

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
4. 店铺一览 支持店铺id 单独查询 支持店铺名模糊查询

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

5. 商品一览 分页(目前商品一览 只是单纯商品一览 没有覆盖某个商品的小料信息 后续可增加) 支持 商品id查询 商品名模糊查询 支持分类id单独查询
   有效商品查询 贩卖时间段查询

    ```http request
    POST http://localhost:9001/wx/goods
    Authorization:  {{token}}
    Content-Type: application/json
    ```
   request:
   ```json
   {
   "shopId":1046003,
   "isOnSale": true,
   "categoryId": 1036008,
   "goodsSn": "1181004",
   "goodsName": "蛋肠粉",
   "goodsId": "1",
   "isTimeOnSale": 1,
   "page": 1,
   "limit": 10
   }
    ```
    - `shopId`: integer。--店铺id (必填)
    - `isOnSale`: boolean。 --是否贩卖 单独查询时可用(选填)
    - `categoryId`: integer。 --分类id 单独查询时可用(选填)
    - `goodsName`: string。--商品名 单独查询时可用(选填) 支持模糊查询
    - `goodsId`: int。--商品id 单独查询时可用(选填)
    - `goodsSn`: string。--商品编号 单独查询时可用(选填)
    - `isTimeOnSale`: int。--时间段贩卖标识位. 1,早餐2,午餐 3,晚餐 0,全时段供应
    - `page`: int。--页数 (选填)
    - `limit`:int --分割 (选填)
      response:

   ```json
   {
   "code": 200,
   "description": null,
   "result": [
   {
   "id": 1181004,
   "goodsSn": "1181004",
   "name": "蛋肠粉",
   "isOnSale": true,
   "isTimeOnSale": 1,
   "picUrl": null,
   "sortOrder": 10,
   "unit": "份",
   "retailPrice": 7.00,
   "addTime": "2023-02-16 13:05:06",
   "detail": "蛋肠粉"
   },
   {
   "id": 1181005,
   "goodsSn": "1181005",
   "name": "肉肠粉",
   "isOnSale": true,
   "isTimeOnSale": 1,
   "picUrl": null,
   "sortOrder": 20,
   "unit": "份",
   "retailPrice": 7.00,
   "addTime": "2023-02-16 13:07:06",
   "detail": "肉肠粉"
   }
   ],
   "records": 2
   }
   ```
    - `id`: integer。-- 商品id
    - `isOnSale`: boolean。 --是否贩卖 是
    - `name`: string。--商品名
    - `goodsSn`: string。--商品编号
    - `picUrl`: string。--商品图片url
    - `sortOrder`: integer。--排序
    - `unit`: string。--单位
    - `retailPrice`: integer。--卖价
    - `isTimeOnSale`: int。--时间段贩卖标识位. 1,早餐2,午餐 3,晚餐 0,全时段供应
6. 小料一览 (分页 支持小料id查询 某个商品id的特定小料查询 , 是否贩卖查询 ,时间段查询)

    ```http request
    POST http://localhost:9001/wx/goods/little
    Authorization:  {{token}}
    Content-Type: application/json
    ```
   request:
   ```json
   {
   "shopId":1046003,
   "littleCalamityId": 1,
   "isOnSale": true,
   "goodsName": "蛋肠粉",
   "goodsId": "1",
   "isTimeOnSale": 1,
   "page": 1,
   "limit": 10
   }
    ```
    - `shopId`: integer。--店铺id (必填)
    - `littleCalamityId`: integer。--小料id 单独查询时可用(选填)
    - `isOnSale`: boolean。 --是否贩卖 单独查询时可用(选填)
    - `goodsName`: string。--小料名 单独查询时可用(选填) 支持模糊查询
    - `goodsId`: int。--商品id 单独查询时可用(选填)
    - `isTimeOnSale`: int。--时间段贩卖标识位. 1,早餐2,午餐 3,晚餐 0,全时段供应
    - `page`: int。--页数
    - `limit`:int --分割
      response:

   ```json
   {
   "code": 200,
   "description": null,
   "result": [
   {
   "id": 253,
   "name": "萝卜干",
   "goodsId": 0,
   "picUrl": null,
   "shopId": 1046003,
   "brief": "萝卜干",
   "unit": "’份‘",
   "retailPrice": 0.00,
   "isTimeOnSale": 1,
   "isOnSale": true
   },
   {
   "id": 254,
   "name": "xo酱",
   "goodsId": 0,
   "picUrl": null,
   "shopId": 1046003,
   "brief": "xo酱",
   "unit": "’份‘",
   "retailPrice": 0.00,
   "isTimeOnSale": 1,
   "isOnSale": true
   }
   ],
   "records": 4
   }
   ```
    - `id`: integer。-- 小料id
    - `name`: string。--商品名
    - `brief`: string。--详细
    - `isOnSale`: boolean。 --是否贩卖
    - `goodsId`: string。--商品编号
    - `picUrl`: string。--商品url
    - `unit`: string。--单位
    - `retailPrice`: integer。--卖价
    - `isTimeOnSale`: int。--时间段贩卖标识位. 1,早餐2,午餐 3,晚餐 0,全时段供应

7. 购物车一览

    ```http request
   GET  http://localhost:9001/wx/cart?shopId=1046003
   Authorization: {{token}}
    ```
   request:
    - `shopId`: integer。--店铺id (必填)

   response:

   ```json
   {
   "code": 200,
   "description": null,
   "result": [
   {
   "goodsId": 1181006,
   "goodsSn": "1181006",
   "goodsIsTime": true,
   "goodsName": "炒粉",
   "number": 1,
   "url": null,
   "unit": null,
   "price": null,
   "checked": true,
   "errStatus": true
   },
   {
   "goodsId": 1181005,
   "goodsSn": "1181005",
   "goodsIsTime": true,
   "goodsName": "肉肠粉",
   "number": 1,
   "url": null,
   "unit": "份",
   "price": 7.00,
   "checked": true,
   "errStatus": false
   },
   {
   "goodsId": 1181004,
   "goodsSn": "1181004",
   "goodsIsTime": true,
   "goodsName": "蛋肠粉",
   "number": 1,
   "url": null,
   "unit": "份",
   "price": 7.00,
   "checked": true,
   "calamityVo": {
   "calamityId": 251,
   "calamityName": "蒜蓉酱油",
   "calamityNumber": 2,
   "calamityUnit": "’份‘",
   "calamityUrl": null,
   "calamityPrice": 0.00,
   "errStatus": false
   },
   "errStatus": false
   }
   ],
   "records": 3
   }
   ```
    - `id`: integer。-- 小料id
    - `name`: string。--商品名
    - `brief`: string。--详细
    - `isOnSale`: boolean。 --是否贩卖
    - `goodsId`: string。--商品id
    - `goodsIsTime`: boolean。-- 商品到期提示flag 可忽略
    - `goodsSn`: string。--商品编号
    - `url`: string。--商品url
    - `number`: int。--商品数量
    - `price`: decimal。--商品价格
    - `unit`: string。--单位
    - `checked`: boolean。-- 商品选择flag
    - `retailPrice`: decimal。--卖价 (单价)
    - `calamityVo.calamityId`: int。--小料ID
    - `calamityVo.calamityName`: string。--小料名
    - `calamityVo.calamityNumber`: int。--小料数量
    - `calamityVo.calamityUnit`: string。--单位
    - `calamityVo.calamityPrice`: decimal。--小料价格
    - `calamityVo.calamityUrl`: string。--小料图片url
    - `calamityVo.retailPrice`: decimal。--小料价格(单价)
    - `calamityVo.errStatus`: boolean。--小料失效flag

8. 购物车 单子数量
    ```http request
   GET  http://localhost:9001/wx/cart?shopId=1046003
   Authorization: {{token}}
    ```
   request:
    - `shopId`: integer。--店铺id (必填)
      response:

   ```json
   
   {
   "code": 200,
   "description": null,
   "result": 3,
   "records": null
   }
   ```
    - `result`: integer。-- 单数
9. 增加购物车和更新购物车(每次把购物车的所有条数都带过来)//目前逻辑每次先清空购物车 然后再全部新增进去
   ```http request
   POST http://localhost:9001/wx/cart/add
   Authorization:  {{token}}
   Content-Type: application/json
    ```
   request:
   ```json
   {
   "shopId": 1046003,
   "goods": [
   {
   "goodsId": "1181004",
   "goodsSn": "1181004",
   "checked": "true",
   "number":"1"
   },
   {
   "goodsId": "1181004",
   "goodsSn": "1181004",
   "checked": "true",
   "number":"0",
   "calamityId": 251,
   "calamityName": "蒜蓉酱油",
   "calamityNumber": 2
   },
   {
   "goodsId": "1181005",
   "goodsSn": "1181005",
   "checked": "true",
   "number":"1"
   },
   {
   "goodsId": "1181006",
   "goodsSn": "1181006",
   "checked": "true",
   "number":"1"
   }
   ]
   }
      ```
    - `shopId`: integer。--店铺id (必填)
    - `goods.goodsId`: integer。--商品id
    - `goods.goodsSn`: string。--商品编号
    - `goods.checked`: boolean。--选择flag
    - `goods.number`: integer。--数量
    - `goods.calamityId`: integer。--小料ID
    - `goods.calamityName`: string。--小料name
    - `goods.calamityNumber`: integer。--小料数量
      response:

   ```json
   {
   "code": 200,
   "description": null,
   "result": null,
   "records": null
   }
   ```
10. 清空购物车
   ```http request
   POST  http://localhost:9001/wx/cart/delete?shopId=1046003
   Authorization: {{token}}
   ```

request:

- `shopId`: integer。--店铺id (必填)
  response:

 ```json
{
"code": 200,
"description": null,
"result": 3,
"records": null
}
```

11. 分类一览(支持多级分类)

   ```http request
   GET http://localhost:9001/wx/category?shopId=1046003
   Authorization: {{token}}
   ```
request:

- `shopId`: integer。--店铺id (必填)
response:

 ```json
{
  "code": 200,
  "description": null,
  "result": [
    {
      "id": 1036007,
      "name": "面食",
      "parentId": 0
    },
    {
      "id": 1036008,
      "name": "饭食",
      "parentId": 0,
      "categoryVos": [
        {
          "id": 1036010,
          "name": "炒饭",
          "parentId": 1036008
        }
      ]
    },
    {
      "id": 1036009,
      "name": "粉",
      "parentId": 0
    }
  ],
  "records": null
}
```
- `id`: integer。--分类Id (可以使用此id 去 商品一览结口获取商品)
- `name`: string。--分类名 
- `parentId`: integer。--父类id 
- `categoryVos`: 子类

11. banner一览

   ```http request
   GET http://localhost:9001/wx/banner?shopId=1046003
   Authorization: {{token}}
   ```
request:

- `shopId`: integer。--店铺id (选填,单独店铺banner查询时使用)默认是广告位banner 
  response:

 ```json
{
   "code": 200,
   "description": null,
   "result": [
      {
         "description": "百度",
         "bannerUrl": "https://www.baidu.com/img/pcdoodle_2a77789e1a67227122be09c5be16fe46.png",
         "orderNum": 1,
         "name": "百度"
      }
   ],
   "records": 1
}
```
- `bannerUrl`: string。--banner图片url
- `description`: string。--banner描述
- `orderNum`: integer。--排序
- `name`: string banner名

12. 绑定店铺(新增绑定店铺,更新绑定店铺)

   ```http request
   GET  http://localhost:9001/wx/auth/bind/{shopId}
   Authorization: {{token}}
   ```

response:

 ```json
{
   "code": 200,
   "description": null,
   "result": 1046003,
   "records": null
}
```
- `result`: int。店铺id
