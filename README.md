# spring-boot-homework

#### 项目介绍
spring-boot-homework课堂作业

#### tag1
完成springboot基本脚架，集成redis、mybatis-plus

#### tag2
完成首页内容显示，登录注册功能

#### tag3
完成首页侧边内容显示，文章分类，文章详情等功能

#### tag4
完成文章收藏，喜欢



### 数据库
> 通用字段

id、created、modified

> 用户基本表

username
password
email
mobile
point积分
gender性别
wechat微信
birthday生日
avatar头像
status状态
lasted最后登录时间

postCount内容数量
commentCount评论数量


> 文章详情表

title
content
editMode模式，html或md
voteUp支持人数
voteDown反对人数
viewCount访问量
level置顶等级（默认为0，等级越高越置顶）
recommend是否精华
status转态

userId
categoryId分类ID


> 文章分类表

name
content
summary
icon
postCount该分类的内容数量
orderNum排序编码
parentId上级分类ID
metaKeywrods关键字SEO
metaDescription关键描述内容SEO

> 文章评论表

content
parentId回复评论的ID
postId回复内容的ID
userId回复用户ID
voteUp“顶”的数量
voteDown“踩”的数量
level置顶等级
status评论状态