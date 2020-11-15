## ES SearchAPI

### 指定查询的索引

| 语法                   | 范围              |
| ---------------------- | ----------------- |
| /_search               | 集群上所有的索引  |
| /index1/_search        | index1            |
| /index1,index2/_search | index1,index2     |
| /index*/_search        | 以index开头的索引 |



### URL 查询

- 使用 ”q“，指定查询字符串
- ”name:zhangsan“,KV 健值对

```
curl -XGET

"http://localhost:9200/index/_search?q=name:zhangsan"
```

q:表示查询内容



```
GET /index/_search?q=2020&df=title&sort=year:desc&from=0&size=10&timeout=1s
{
	"profile":true
}
```

- q:指定查询语句，使用 Query String Syntax 
- df:默认查询字段，不指定，会查询所有字段
- sort:排序 from 和 size 用于分页
- profile:查询执行计划

#### Query String Syntax 

##### 第一部分

- 指定字段 v.s 泛查询

  q=title:2020  / q=2020

- Term v.s Phrase

  （Beautiful Mind ）等于 Beautiful OR Mind

  "Beautiful Mind"      等于 Beautiful AND Mind 。Phrase查询，还要求顺序一致

  ```
  // Term 查询
  GET /index/_search?q=title:Beautiful Mind
  {
  	"profile":true
  }
  
  // Phrase 查询
  GET /index/_search?q=title:"Beautiful Mind"
  {
  	"profil":true
  }
  
  ```

##### 第二部分

- 布尔查询

  AND / OR / NOT / 或者 && / || / !

  - 必须大写

  - title:(魔戒 NOT 霍比特)

    ```
    // bool 查询 结果必须包含Beautiful，Mind
    GET /index/_search?q=title:(Beautiful AND Mind)
    {
    	"profile":true
    }
    ```

    

- 分组

  + +表示must
  + -表示must_not
  + title:(+魔戒  -霍比特)

##### 第三部分

- 范围查询
  - 区间表示：[] 闭区间，{} 开区间
    - year:{2019 TO 2020]
    - year:[* TO 2019]
- 算数符号
  - year:>2010
  - year:(>2010 && <=2020)
  - year:(+>2010 +<=2020)

```
GET /index/_search?q=year:>2010
{
	"profile":true
}
```

##### 第四部分

- 通配符查询（效率低，占用内存大）
  - ？表示一个字符，*表示0或者多个字符
    - title:he?lo
    - title:w*

- 正则
  - title:[bt]oy

- 模糊匹配与近似查询
  - title:beautifl~1
  - title:"lord rings"~2

```
title:beautifl~1         可以查询出  beautiful  
title:"lord rings"~2     可以查询出  lord of the rings
```

#### Request Body Search

- 将查询语句通过http request body 发给elasticsearch
- Query DSL

```
POST /index/_search
{
	"profile":true,
	"query":{
		"match_all":{}
	}
}
```

##### 排序

```
POST /index/_search
{
	"sort":[{"create_date":"desc"}],
	"from":1,
	"size":10,
	"query":{
		"match_all":{}
	}
}
```

##### 过滤返回结果

```
POST /index/_search
{
	"_source":["order_date","categroy.keyword"],
	"from":1,
	"size":10,
	"query":{
		"match_all":{}
	}
}
```

##### 脚本字段

```
POST /index/_search
{
	"script_fields":{
		"new_field":{
			"script":{
				"lang":"painless",
				"source":"doc['order_date'].value+'_hello'"
			}
		}
	},
	"from":1,
	"size":10,
	"query":{
		"match_all":{}
	}
}

生成一个new_field:20200202_hello 字符拼接
```

##### 使用查询表达式 - Match

```
GET /index/_doc/_search
{
	"query":{
		"match":{
			"comment":"Last Date"
		}
	}
}

GET /index/_doc/_search
{
	"query":{
		"match":{
			"comment":{
				"query":"Last Date",
				"operator":"AND"
			}
		}
	}
}
```

##### 短语搜索 - Match Phrase

```
GET /index/_doc/_search
{
	"query":{
		"match_phrase":{
			"comment":{
				"query":"Last Date",
				"slop":1
			}
		}
	}
}
```

- Last Date 必须顺序出现 
- "slop":1 表示中间可以有一个字符