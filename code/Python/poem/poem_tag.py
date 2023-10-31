# 全部标签-OK

import requests
from bs4 import BeautifulSoup
from lxml import etree

headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36'}#创建头部信息
tag=[]


url='https://www.xungushici.com/tags'
r=requests.get(url,headers=headers)
content=r.content.decode('utf-8')
soup = BeautifulSoup(content, 'html.parser')

hed=soup.find('ul',class_='list-unstyled d-flex flex-row flex-wrap align-items-center w-100')
list=hed.find_all('li',class_="m-2 p-2 badge badge-light")

for it in list:
    if it.a!=None:
        tag.append(it.a.text)

import xlwt

xl = xlwt.Workbook()
# 调用对象的add_sheet方法
sheet1 = xl.add_sheet('sheet1', cell_overwrite_ok=True)

sheet1.write(0,0,"tag")
for i in range(0,len(tag)):
    sheet1.write(i+1,0,tag[i])

xl.save("../data5/tag_name.xlsx")