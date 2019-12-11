#数据库
##1、accounts表 消费明细表
_id:ID
_type:类别——食品饮料，交通工具
_money:用的钱
_time:消费的日期
_month:所在的账本月份
_week:所在的日期的星期几
_mark:备注
_other:数据更新时间
##2、groups表 账本表
_id:ID
_month:账本月份
_other:数据更新时间
##3、limits表 预算表
_id:ID
_type:类别
_used:已用的总额
_progress:占预算百分比
_limit:预算总额
_color:分配的颜色
##4、srzcs表 月账本统计表
_id:ID
_month:账本月份
_sr:收入
_zc:支出
_other:未知
##5、income_record
_id:ID
_time:时间
_income:收入多少
_detail：备注