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
| 713  | 商品失效           ||
| 714  | 小料失效           ||
| 708  | 小程序openId失效    ||
| 722  | 订单价格check失败    ||
| 723  | 订单状态错误,无法支付    ||
| 724  | 订单不能支付         ||
| 623  | 订单状态无法取消       ||
| 624  | 找不到此笔订单        ||
| 621  | 订单退款失败         ||
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
   "goodsId": 1181004,
   "goodsSn": "1181004",
   "goodsName": "蛋肠粉",
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
   "goodsId": 1181005,
   "goodsSn": "1181005",
   "goodsName": "肉肠粉",
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
    - `goodsId`: integer。-- 商品id
    - `isOnSale`: boolean。 --是否贩卖 是
    - `goodsName`: string。--商品名
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
   "calamityId": 1,
   "isOnSale": true,
   "calamityName": "蛋肠粉",
   "goodsId": "1",
   "isTimeOnSale": 1,
   "page": 1,
   "limit": 10
   }
    ```
    - `shopId`: integer。--店铺id (必填)
    - `calamityId`: integer。--小料id 单独查询时可用(选填)
    - `isOnSale`: boolean。 --是否贩卖 单独查询时可用(选填)
    - `calamityName`: string。--小料名 单独查询时可用(选填) 支持模糊查询
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
   "calamityId": 253,
   "calamityName": "萝卜干",
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
   "calamityId": 254,
   "calamityName": "xo酱",
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
    - `calamityId`: integer。-- 小料id
    - `calamityName`: string。--商品名
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
   "checked": true,
   "price": null,
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
   "checked": true,
   "price": 7.00,
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
   "checked": true,
   "price": 7.00,
   "calamityVos": [
   {
   "calamityId": 251,
   "calamityName": "蒜蓉酱油",
   "calamityNumber": null,
   "calamityUnit": "’份‘",
   "calamityUrl": null,
   "errStatus": false,
   "calamityPrice": 0.00
   }
   ],
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
    - `calamityVos.calamityId`: int。--小料ID
    - `calamityVos.calamityName`: string。--小料名
    - `calamityVos.calamityNumber`: int。--小料数量
    - `calamityVos.calamityUnit`: string。--单位
    - `calamityVos.calamityPrice`: decimal。--小料价格
    - `calamityVos.calamityUrl`: string。--小料图片url
    - `calamityVos.retailPrice`: decimal。--小料价格(单价)
    - `calamityVos.errStatus`: boolean。--小料失效flag

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
   "goodsName": "蛋肠粉",
   "checked": "true",
   "number": "1",
   "cartCalamityVos": [
   {
   "calamityId": 251,
   "calamityName": "蒜蓉酱油",
   "calamityNumber": 2
   }
   ]
   },
   {
   "goodsId": "1181005",
   "goodsSn": "1181005",
   "goodsName": "肉肠粉",
   "checked": "true",
   "number": "1"
   },
   {
   "goodsId": "1181006",
   "goodsSn": "1181006",
   "goodsName": "炒粉",
   "checked": "true",
   "number": "1"
   }
   ]
   }
      ```
    - `shopId`: integer。--店铺id (必填)
    - `goods.goodsId`: integer。--商品id
    - `goods.goodsSn`: string。--商品编号
    - `goods.checked`: boolean。--选择flag
    - `goods.number`: integer。--数量
    - `goods.cartCalamityVos`: list。--小料集合
    - `goods.cartCalamityVos.calamityId`: integer。--小料ID
    - `goods.cartCalamityVos.calamityName`: string。--小料name
    - `goods.cartCalamityVos.calamityNumber`: integer。--小料数量
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
   GET http://localhost:9001/wx/category?shopId=6&isTimeOnSale=2
   Authorization: {{token}}
   ```
request:

- `shopId`: integer。--店铺id (必填)
- `isTimeOnSale`: integer。--时间段贩卖 1,早餐2,午餐 3,晚餐 0,全时段供应
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


13. 微信登录(包含注册)

   ```http request
   POST http://localhost:9001/wx/auth/loginByWx
   Content-Type: application/json
   Authorization: {{token}}
   ```
request:
   ```json
{
   "code":"081tg80w33Fcf03BdE0w3RMpmP3tg80j",
   "encryptedData":"g+GpXedQ0bQG/mg1pzs1bE544DNrC0fWg2M9pDq/dWpqbKk9Yi67h0Kl7B3KEdZhvy9ASdsSkAvARrGOzAFZ81IQz39RzYDm/bnfPx9YNHNRFxoymq8xpMWY8pJsp6px6NDDBYpuM3h4dGSAMx0KcvO1hOWZuGo3c8G1iPFIyNXAQDsPc4ugEGhj90DyCtqRz4Wp5BhFxC5xk3R1gnDP9BEarGkm7VioQqRCk3DavfY2d4/VfY5eN52+Lbn5wvyRwbvEEO4KVdA6eGOvZe2E0tauCutGYSLnCo5d0CUvLAYJZsgnkc1/SpK2zbfDFTnTzl4cCGsR2NlNFGaIbVFFLh6xKyIKuKXz54PK2oETAbZhkdxxNR33c7VcujFTO75w9xL9FPEoWdTs4DKDlUwQ2TJXZd7F8ClX6uxx5/yhFA/MYN7p4iWfaVzcI+kjoAq6ntvGR0ekljK+8FlT65duZA==",
   "iv":"dpRIcbq8B1H/HJoKkTPbXg==",
   "codePhone":"d9ac9bb37a8d9c1e7e3191eafe7df8c11e6d2c2c8adaa1d582077abe062404e6"
} 
```
- `code`:string。 微信code (必填)
- `encryptedData`:string。 加密数据 (必填)
- `iv`:string。  (必填)
- `codePhone`:string。 微信获取手机code(必填)
response: 

 ```json
{
   "code": 200,
   "description": null,
   "result": {
      "userInfo": {
         "nickName": "微信用户",
         "avatarUrl": "https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132"
      },
      "token": {
         "token": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODE4NjIzODMsImNyZWF0ZWQiOjE2NzgyNjIzODMxMzYsInVzZXJuYW1lIjoibzAzUEs0a0xtdkt0bGVpUEFJcEZrbHMtMzBwWSJ9.PJLPNTDcC9yDgOjSzK5nSF4KzdUKe9lSGcg9-dLYZ3evCFUMrKwcePl69nmWy6b5swkGsFv6jV4IyRtAxMo27g",
         "refresh": "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTQyNjIzODMsImNyZWF0ZWQiOjE2NzgyNjIzODMxMzYsInVzZXJuYW1lIjoibzAzUEs0a0xtdkt0bGVpUEFJcEZrbHMtMzBwWSJ9.rF7CfO4iHic7W2cZY5qE4vSGKyI0leAAAmIbeBFPUZf8e5J_ZrAJpfvwOPkw_AmDCXmcRYN4ZQ991ltb6AXsKQ",
         "expire": 3600
      }
   },
   "records": null
}
```
- `userInfo.nickName`: string。网名
- `userInfo.avatarUrl`: string。头像链接
- `token.token`: string。--token
- `token.refresh`: string。--刷新token
- `token.expire`: int。--过期时间 秒

14. 提交订单

   ```http request
   POST {{host}}/wx/order/submit
   Content-Type: application/json
   Authorization: {{token}}
   ```
request:
   ```json
{
   "shopId": 1046003,
   "message": "请尽快送达",
   "shoppingCartVos": [
      {
         "goodsId": 1181004,
         "number": 2,
         "price": 7.00,
         "orderCartCalamityVos": [
            {
               "calamityId": 251,
               "calamityNumber": 1,
               "calamityPrice": 10.00
            }
         ]
      },
      {
         "goodsId": 1181005,
         "number": 1,
         "price": 7.00,
         "orderCartCalamityVos": [
            {
               "calamityId": 253,
               "calamityNumber": 2,
               "calamityPrice": 10.00
            },
            {
               "calamityId": 254,
               "calamityNumber": 1,
               "calamityPrice": 10.00
            }
         ]
      }
   ],
   "orderPrice": 61.00,
   "actualPrice": 61.00
}
```
- `shopId`:long。 店铺id (必填)
- `message`:string。 备注 (非必填)
- `shoppingCartVos`:list。 订单商品list
- `shoppingCartVos.orderCartCalamityVos`:list。 订单小料list
- `calamityPrice`:decimal。 小料费用(必填)  单价
- `price`:decimal。 商品单独费用 不包含小料的单价

response:

 ```json
{
"code": 200,
"description": null,
"result": {
"appId": "wxff1a8500e8238674",
"timeStamp": "1680074902",
"nonceStr": "EmaauQ5VeSCTp3Bt",
"packageValue": "prepay_id=wx29152822575330db204a9bc9cc57d60000",
"signType": "MD5",
"paySign": "69C2CAB69CDFA94815240B050DDACF0E"
},
"records": null
}
```

15. 提交再支付订单

   ```http request
   ###
   POST  {{host}}/wx/order/prepay/6
   Authorization: {{token}}
   ```
- `orderId`:long。 商品Id (必填)
- 返回值和14.提交订单相同


16. 商品详情(带orderId 是某个订单详细,不带orderId  是全部订单记录)

   ```http request
   GET {{host}}/wx/order/detail?orderId=6
   Content-Type: application/json
   Authorization: {{token}}
   ```
- `orderId`:long。 商品Id (必填)

某个订单的response:

 ```json
{
   "code": 200,
   "description": null,
   "result": {
      "nickName": "test21",
      "orderSn": "mealq00Udif",
      "orderDate": "2023-03-28 12:46:32",
      "orderDetailGoodsVos": [
         {
            "goodsName": "蛋肠粉",
            "goodsNumber": 2,
            "unit": "’份‘",
            "goodsMoney": 7.00,
            "orderDetailCalamityVos": [
               {
                  "calamityName": "蒜蓉酱油",
                  "calamityNumber": 1,
                  "unit": "’份‘",
                  "calamityMoney": 10.00
               }
            ]
         },
         {
            "goodsName": "肉肠粉",
            "goodsNumber": 1,
            "unit": "’份‘",
            "goodsMoney": 7.00,
            "orderDetailCalamityVos": [
               {
                  "calamityName": "萝卜干",
                  "calamityNumber": 2,
                  "unit": "’份‘",
                  "calamityMoney": 10.00
               },
               {
                  "calamityName": "xo酱",
                  "calamityNumber": 1,
                  "unit": "’份‘",
                  "calamityMoney": 10.00
               }
            ]
         }
      ],
      "count": 1,
      "money": 61.00,
      "shopName": "汇佳",
      "shopPhone": ""
   },
   "records": null
}
 ```
- `nickName`:string。 用户名 (必填)
- `orderSn`:string。 订单编号 (非必填)
- `orderDate`:date。 订单日期
- `orderDetailGoodsVos`:list。 订单商品list
- `orderDetailGoodsVos.orderDetailCalamityVos`:list。 订单小料list
- `count`:订单笔数。 1
- `money`:decimal。 订单费用
- `shopName`:string。 店铺名
- `shopPhone`:string。 店铺电话

某个订单的response:

 ```json
{
   "code": 200,
   "description": null,
   "result": [
      {
         "orderId": 10,
         "shopName": "汇佳",
         "shopAvatar": "",
         "orderDate": "2023-03-29 16:28:19",
         "orderStatus": 0,
         "orderStatusMessage": "未支付",
         "money": 61.00
      },
      {
         "orderId": 9,
         "shopName": "汇佳",
         "shopAvatar": "",
         "orderDate": "2023-03-29 16:25:49",
         "orderStatus": 0,
         "orderStatusMessage": "未支付",
         "money": 61.00
      },
      {
         "orderId": 8,
         "shopName": "汇佳",
         "shopAvatar": "",
         "orderDate": "2023-03-29 16:25:27",
         "orderStatus": 0,
         "orderStatusMessage": "未支付",
         "money": 61.00
      }
   ],
   "records": 8
}
 ```
- `orderId`:string。订单id
- `shopAvatar`:string。 店铺头像url 
- `orderDate`:date。 订单日期
- `orderStatus`:int。 0, "未支付" 1, "已支付" 3, "订单完成" 4, "订单退款"
- `orderStatusMessage`:string。 状态描述 可用可不用
- `money`:decimal。 店铺名
- `records`:long。 订单数

