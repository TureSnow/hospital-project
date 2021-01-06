# hospital 接口说明文档

#### 共用接口：

**（医生，护士）筛选病人：**

方式：POST

格式：JSON

路径：/user/filter

body参数：

```java
/**
 * @param lifeState  1:health;2:treating;3:dead; 4:all
 * @param isMatchWard 1:match;2:dismatch;3:all ok
 * @param IllnessLevel 1:mild; 2:severe; 3:critical; 4:all ok
 * @return 满足筛选条件的病人
 */
//格式：example：
{
    "lifeState":2,
    "isMatch":3,
    "illnessLevel":4
}
//返回结果example：
 {
"code": 200,
"message": "操作成功",
"data": [
{
"id": 16,
"name": "ptt1",
"lifeState": "1",
"areaLevel": "1",
"illnessLevel": "1"
},
{
"id": 17,
"name": "ptt2",
"lifeState": "1",
"areaLevel": "1",
"illnessLevel": "1"
}]
}

```

**通知查看**

方式：GET

路径：/user/readNotify  (已读消息)

​			/user/unreadNotify(未读消息)

#### 医生：

**修改病人病情评级**

方式：POST

路径：/user/doctor/updateLevel

格式：JSON

```json
参数实例:
body:
{
    "patientId":"16",
    "level":"2"
}
返回实例：
return:
{
    "code": 200,
    "message": "操作成功",
    "data": "patient illness level change successful!"
}
```

**进行、添加核酸检测**

方式：POST

路径：/user/doctor/addSheet

格式：JSON

参数实例：

```json
{
	"patientId":16,
    "date":"2021-01-05",
    "result":"0",//0->阴性；1：阳性
	"illnessLevel":"1"//病情评级
}
```

**修改生命状态（是否可出院）**

方式：POST

路径：/user/doctor/updateLife

格式：JSON

参数实例：

```json
{
    "patientId":"16",
    "level":"2"//0:health(可出院);1:treating(治疗中);2:death
}
```

**允许病人出院**

方式：POST

路径：/user/doctor/discharge

格式：JSON



```json
//参数example
{
	"patientId":"16"
}
```



#### 护士长：

**添加护士到本区域**

方式：POST

路径：/head/addNurse

格式：JSON



```json
//参数example
{
	"wardNurseId":16
}
```



**移除指定的本区域护士**

方式：POST

路径：/head/deleteNurse

格式：JSON



```json
//参数example
{
	"wardNurseId":16
}
```



**查看所有病床信息**

方式：GET

路径：/head/allBed

格式：JSON



```json
//返回实例
{
	"patientId":"16"
}
```



**得到指定病床上的病人信息**

方式：POST

路径：/head/bed2patient

格式：JSON

```json
//参数example
{
	"bedId":16
}
```



#### 急诊护士 ：

**添加病人**

方式：POST

路径：/emergency/addPatient

格式：JSON



```json
//参数example
{
	"named":"bob",//病人名字
   	"illnessLevel":"1"//病情评级
}
```



**查看所有病人**

方式：GET

路径：/emergency/allPatient

格式：JSON



```json
//返回实例：
{
	"patientId":"16"
}
```



**筛选病人**

方式：POST

路径：/emergency/filter

格式：JSON



```json
//参数example
{
	"area":"1",//0：isolation;1:mild;2；severe;3:critical 4:all
    "illnessLevel":"1",//1:mild;2severe;3:critical 4:all
    "lifeState":"1"//0:health;1:treating;2:death;3:all
}
```



#### 病房护士：

**登记每日状况**

方式：POST

路径：/ward/addDailyStatus

格式：JSON



```json
//参数example
{
	"patientId":16,//Integer
    "date":"2021-01-06",
    "temperature":36.6,//Float
    "lifeState":1,//Integer
    "na_result":0,//0:negative;1:positive
    "symptom":"fever and cough"//String
}
```



**得到自己照顾的病人**

方式：GET

路径：/ward/allPatient

格式：JSON



```json
//返回实例
{
	"patientId":"16"
}
```



**筛选自己照顾的病人**

方式：POST

路径：/ward/filter

格式：JSON



```json
//参数example
{
	"canDischarge":0,//0:can discharge 1：can't discharge
    "illnessLevel":1//1:mild 2:severe 3:critical 4:all ok
}
```





