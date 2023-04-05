# Searcher
2023/3/24

# 2023/4/2 TKOTKCh
1、更新了数据库构建的相关函数，但目前存在问题，由于改动地方非常多，暂时请不要运行DBInitial里面的所有函数
2、增加了热门政策的获取和更新，具体数据表等上一条解决之后一起发

# 2023/4/4 DruncBread
1、实现部分后台管理接口
2、目前功能：搜索，联系搜索，热门政策
# 2023/4/4 TKOTKCh
1、解决了4月2日的问题
2、添加了最近搜索记录功能，详见usercontroller中的getLastQuery和addUserQuery，并附上user_query的sql文件
# 2023/4/5 TKOTKCh
1、添加了getDataByScore函数，在bm25匹配的基础上加了权重模块，此函数还有待优化
