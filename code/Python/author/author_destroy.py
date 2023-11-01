# 抽取时间对应年号、在位皇帝-OK-处理完成

import requests
from bs4 import BeautifulSoup
from lxml import etree
import re

headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36'}#创建头部信息
# 唐 有问题,手动添加
url='http://www.360doc.com/content/15/0610/22/8378385_477253986.shtml'
# 北宋
# url='http://www.360doc.com/content/15/0610/22/8378385_477254404.shtml'
# 南宋
# url='http://www.360doc.com/content/15/0610/22/8378385_477254808.shtml'
# 元
# url='http://www.360doc.com/content/15/0610/22/8378385_477255108.shtml'
# 明
# url='http://www.360doc.com/content/15/0610/22/8378385_477251261.shtml'
# 清
# url='http://www.360doc.com/content/15/0610/22/8378385_477251847.shtml'
r=requests.get(url,headers=headers)
content=r.content.decode('utf-8')
soup = BeautifulSoup(content, 'html.parser')
table=soup.find('table',class_='MsoNormalTable')
tr_list=table.tbody.find_all('tr')
desty_all=[]
for i in range(2,len(tr_list)):
    tr=tr_list[i]
    d_list=tr.find_all('p',class_='MsoNormal')
    list=[]
    for it in d_list:
        text=str(it.text)
        if bool(re.search(r'\d', str(it.text)))  == True:
            text=str(it.text).replace('\xa0','').replace('公元','').replace('年','')
        list.append(text)
    desty_all.append(list)
# print(desty_all)
#
import xlwt

xl = xlwt.Workbook()
# 调用对象的add_sheet方法
sheet1 = xl.add_sheet('sheet1', cell_overwrite_ok=True)

sheet1.write(0,0,"emperor")
sheet1.write(0,1,'year_hao')
sheet1.write(0,2,'time')


for i in range(0,len(desty_all)):
    sheet1.write(i+1,0,desty_all[i][0])
    sheet1.write(i+1, 1, desty_all[i][1])
    sheet1.write(i + 1, 2, desty_all[i][2])
xl.save("../data2/destroy/tang_destroy.xlsx")
# xl.save("../data2/destroy/song_destroy.xlsx")
# xl.save("../data2/destroy/yuan_destroy.xlsx")
# xl.save("../data2/destroy/ming_destroy.xlsx")
# xl.save("../data2/destroy/qing_destroy.xlsx")