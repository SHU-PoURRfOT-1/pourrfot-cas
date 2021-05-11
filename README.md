# pourrfot-cas

[![wakatime](https://wakatime.com/badge/github/SHU-PoURRfOT-1/pourrfot-cas.svg)](https://wakatime.com/badge/github/SHU-PoURRfOT-1/pourrfot-cas)

[![codecov](https://codecov.io/gh/SHU-PoURRfOT-1/pourrfot-cas/branch/main/graph/badge.svg?token=RBWUIQPC3F)](https://codecov.io/gh/SHU-PoURRfOT-1/pourrfot-cas)

[![Actions Status: Java CI with Gradle and CD with ssh](https://github.com/SHU-PoURRfOT-1/pourrfot-cas/workflows/Java%20CI%20with%20Gradle%20and%20CD%20with%20ssh/badge.svg)](https://github.com/SHU-PoURRfOT-1/pourrfot-cas/actions?query=workflow%3A"Java+CI+with+Gradle+and+CD+with+ssh")

![License](https://img.shields.io/github/license/SHU-PoURRfOT-1/pourrfot-web)


目前pourrfot-cas和pourrfot-server的结合，仅提供半**残废**的 Oauth2.0。

自动生成的 [Swagger 文档](http://47.98.133.186/cas/api/swagger-ui/)

## 用户名-密码 流程

### cURL

```shell
curl -X POST "http://localhost:9001/cas/api/oauth/password-token" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d "{
	\"clientId\": \"pourrfot-web\",
	\"grantType\": \"PASSWORD\",
	\"password\": \"123456\",
	\"scope\": \"student\",
	\"username\": \"spencercjh\"
}"
```

### HTTP Request

```http request
###
POST http://localhost:9001/cas/api/oauth/password-token
Accept: application/json
Content-Type: application/json

{
    "clientId": "pourrfot-web",
    "grantType": "PASSWORD",
    "password": "123456",
    "scope": "student",
    "username": "spencercjh"
}
```

### 结果

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxvY2FsIn0.eyJpZCI6MywiY3JlYXRlVGltZSI6MTYxODc2MjgwOTAwMCwidXBkYXRlVGltZSI6MTYxODc2MjgwOTAwMCwidXNlcm5hbWUiOiJzcGVuY2VyY2poIiwibmlja25hbWUiOiLolKHkvbPmmIoiLCJwcm9maWxlUGhvdG8iOiIiLCJiaXJ0aCI6ODkwNzU1MjAwMDAwLCJzZXgiOiJtYWxlIiwicm9sZSI6InN0dWRlbnQiLCJlbWFpbCI6InNob3VzcGVuY2VyY2poQGZveG1haWwuY29tIiwidGVsZXBob25lIjoiMTUwMDAxMzE5NjUiLCJwYXNzd29yZCI6IioqKioqKiIsImV4cCI6MTYyMDg0NzIwM30.motPt2cOXAUvdQlYWBnGFQkB2o2TYdNo9X7EOpFUL5HnyP2eck_dVcyOamMmVJzLhPX9EMOaRkhDrLOw2o87bA6U1N24SHmr2KfbRPn1iJ-7EsEu_21F0T6erAFKyPWmta1WoR3eO3cMAwLZRKAG_ew2FZx4_-jkUSApZGsi9AxZ_aQxsGRR2G2tgh427tmu0Pl9zkx45bLV6UNjL3Ylc-x3fKug2aVqi7zGQNkpNXOjP4jACKTmNh3vHMKA6RDI-mAi1_XsFH3T6a2mwjTa0QCafA8hg0FlI1pmTqPdnr0KwaJ9t8Co-9Q10VIs2t7rryzTh9zxS_0ISI8e6-_5nA",
    "expireAt": 1620847203,
    "user": {
      "id": 3,
      "createTime": "2021-04-18T16:20:09.000+00:00",
      "updateTime": "2021-04-18T16:20:09.000+00:00",
      "username": "spencercjh",
      "nickname": "蔡佳昊",
      "profilePhoto": "",
      "birth": "1998-03-24T16:00:00.000+00:00",
      "sex": "male",
      "role": "student",
      "email": "shouspencercjh@foxmail.com",
      "telephone": "15000131965",
      "password": "******"
    },
    "message": "Oath2.0 password authorization success"
  },
  "message": "success"
}
```

## 授权码式流程

### cURL
```shell
# 获取授权码
curl -X POST "http://localhost:9001/cas/api/oauth/code" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d "{
	\"clientId\": \"pourrfot-web\",
	\"password\": \"123456\",
	\"redirectUrl\": \"http://pourrfot-server.com/api\",
	\"responseType\": \"code\",
	\"scope\": \"student\",
	\"state\": \"state\",
	\"username\": \"spencercjh\"
}"
# 前端携带授权码请求令牌
curl -X POST "http://localhost:9001/cas/api/oauth/token" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d "{
	\"code\": \"daf9a67f8306439eb4e4cb36debe4e6f\",
	\"state\": \"state\",
	\"clientId\": \"pourrfot-web\",
	\"clientSecret\": \"123456\",
	\"grantType\": \"AUTHORIZATION_CODE\"
}"
```

### HTTP Request
```http request
###
POST http://localhost:9001/cas/api/oauth/code
Content-Type: application/json

{
    "clientId": "pourrfot-web",
    "password": "123456",
    "redirectUrl": "http://pourrfot-server.com/api",
    "responseType": "code",
    "scope": "student",
    "state": "state",
    "username": "spencercjh"
}

###
POST http://localhost:9001/cas/api/oauth/token
Accept: application/json
Content-Type: application/json

{
    "code": "555f7174b22a425bbd631f16a1854bf7",
    "state": "state",
    "clientId": "pourrfot-web",
    "clientSecret": "123456",
    "grantType": "AUTHORIZATION_CODE"
}
```

### 结果

```json
{
    "code": 200,
    "data": {
        "message": "authorize successful, redirecting...",
        "redirectUrl": "//pourrfot-server.com/api?code=555f7174b22a425bbd631f16a1854bf7&state=state"
    },
    "message": "success"
}
```

```json
{
    "code": 200,
    "data": {
        "token": "eyJhbGciOiJSUzI1NiIsImtpZCI6ImxvY2FsIn0.eyJpZCI6MywiY3JlYXRlVGltZSI6MTYxODc2MjgwOTAwMCwidXBkYXRlVGltZSI6MTYxODc2MjgwOTAwMCwidXNlcm5hbWUiOiJzcGVuY2VyY2poIiwibmlja25hbWUiOiLolKHkvbPmmIoiLCJwcm9maWxlUGhvdG8iOiIiLCJiaXJ0aCI6ODkwNzU1MjAwMDAwLCJzZXgiOiJtYWxlIiwicm9sZSI6InN0dWRlbnQiLCJlbWFpbCI6InNob3VzcGVuY2VyY2poQGZveG1haWwuY29tIiwidGVsZXBob25lIjoiMTUwMDAxMzE5NjUiLCJwYXNzd29yZCI6IioqKioqKiIsImV4cCI6MTYyMDg0Mzg3Nn0.SQO1nyAmjnixxBTu18LYQyG7zK65fyFnP4H59K0nyidSx25LTnRQ3JDlojrEUPLGIXu_eO3d0gS3WR3eejSX2WKsGyoRGdBr_mSGveRUdMsxEsmraAVpKuTnNLzgqPzFlUOQUXnlus2NEHFyG0SxjEhlfWqTKtDSr6dqU-in7XD8KR5s1xW1FdQum7UQAGTuCAEgz-Wg0lZgveWLvAVRkqjqQqFg6032qZ_N1BBNmuqiIkj0Ub90Wi8nybCVoR3k0NtatAvUnpJWmrUuwfnRpLggVN2JUl4wAB5xrBxFE4xJl21vUaiGnw-LFZw4kM_CVtiNmCUJ23Riu-XnE57eSg",
        "expireAt": 1620843876,
        "user": {
            "id": 3,
            "createTime": "2021-04-18T16:20:09.000+00:00",
            "updateTime": "2021-04-18T16:20:09.000+00:00",
            "username": "spencercjh",
            "nickname": "蔡佳昊",
            "profilePhoto": "",
            "birth": "1998-03-24T16:00:00.000+00:00",
            "sex": "male",
            "role": "student",
            "email": "shouspencercjh@foxmail.com",
            "telephone": "15000131965",
            "password": "******"
        },
        "message": "Oauth2.0 authorization success"
    },
    "message": "success"
}
```
