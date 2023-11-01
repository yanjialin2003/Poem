# 获取诗词-唐宋元明清-TODO 待验证，但暂时不需要

import requests
from bs4 import BeautifulSoup
from lxml import etree
import time

headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36'}#创建头部信息
pom_list=[]
k=1
# 唐48330首 - (1,25) - (1,5)
# for j in range(5, 7):

# 宋200000首 - (1,101) - (1,4)
# for j in range(4, 6):

# 元39240首 - (1,25) - (1,4)
# for j in range(4, 6):

# 明100000首 - (1,51) - (1,4)
# for j in range(4, 6):

# 清91540首 - (1,47) - (1,5)
for j in range(5, 7):

    pages = j*200 - 199
    for i in range(pages,pages+199):
        # url='https://www.xungushici.com/shicis/cd-tang-p-'+str(i)
        # url='https://www.xungushici.com/shicis/cd-song-p-'+str(i)
        # url='https://www.xungushici.com/shicis/cd-yuan-p-'+str(i)
        # url='https://www.xungushici.com/shicis/cd-ming-p-'+str(i)
        url='https://www.xungushici.com/shicis/cd-qing-p-'+str(i)
        r=requests.get(url,headers=headers)
        content=r.content.decode('utf-8')
        soup = BeautifulSoup(content, 'html.parser')

        hed=soup.find('div',class_='col col-sm-12 col-lg-9')
        list=hed.find_all('div',class_="card mt-3")
        # print(len(list))

        for it in list:
            content = {}
            #1.1获取单页所有诗集
            href=it.find('p',class_='card-title').a['href']
            real_href='https://www.xungushici.com'+href
            title=it.find('p',class_='card-title').a.text

            #2.1爬取诗词
            get = requests.get(real_href).text
            selector = etree.HTML(get)
            #2.2获取标题
            xtitle=selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/h1/text()')[0]
            #2.3获取朝代
            desty=selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/p/a/text()')[0]
            #2.4获取作者
            if len(selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/p/span/text()'))==0:
                author=selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/p/a[2]/text()')[0]
            else:
                author =selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/p/span/text()')[0]
            #2.5获取文章
            ans=""
            if len(selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/div[1]/p/text()'))==0:
                artical=selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/div[1]/text()')
                for it in artical:
                    ans=ans+it.replace("\r","").replace("\t","").replace("\n","")
            else:
                artical = selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/div[1]/p/text()')
                for it in artical:
                    ans=ans+it.replace("\r","").replace("\t","").replace("\n","")
            #2.6获取译文
            trans=""
            flag=0
            for j in range(2,8):
                path='/html/body/div[1]/div/div[1]/div[2]/div[2]/p[%d]'%j
                if selector.xpath(path+'/text()')==[]:
                    break
                else:
                    translist=selector.xpath(path+'/text()')
                    for it in translist:
                        trans = trans + it + "\n"
            #2.7获取鉴赏
            appear=""
            for j in range(1,19):
                path='/html/body/div[1]/div/div[1]/div[3]/div[2]/p[%d]'%j
                if selector.xpath(path+'/text()')==[]:
                    break
                else:
                    apperlist=selector.xpath(path+'/text()')
                    for it in apperlist:
                        appear = appear + it + "\n"
            #2.8创作背景
            background=selector.xpath('/html/body/div[1]/div/div[1]/div[4]/div[2]/p/text()')
            text_back=""
            if background!=[]:
                for it in background:
                    text_back=text_back+it+"\n"
            #2.9标签
            tag = ""
            if len(selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/div[2]//a')) != 0:
                tag = selector.xpath('/html/body/div[1]/div/div[1]/div[1]/div/div[2]//a/text()')
                tag = ",".join(tag)
            content['title']=xtitle
            content['desty']=desty
            content['author']=author
            content['content']=ans
            content['trans_content']=trans
            content['appear']=appear
            content['background']=text_back
            pom_list.append(content)
            print("第"+str(k)+"个")
            k=k+1

    import xlwt

    xl = xlwt.Workbook()
    # 调用对象的add_sheet方法
    sheet1 = xl.add_sheet('sheet1', cell_overwrite_ok=True)

    sheet1.write(0,0,"title")
    sheet1.write(0,1,'desty')
    sheet1.write(0,2,'author')
    sheet1.write(0,3,'content')
    sheet1.write(0,4,'trans_content')
    sheet1.write(0,5,'appear')
    sheet1.write(0,6,'background')
    sheet1.write(0,7,'tag')

    for i in range(0,len(pom_list)):
        sheet1.write(i+1,0,pom_list[i]['title'])
        sheet1.write(i+1, 1, pom_list[i]['desty'])
        sheet1.write(i+1, 2, pom_list[i]['author'])
        sheet1.write(i+1, 3, pom_list[i]['content'])
        sheet1.write(i+1, 4, pom_list[i]['trans_content'])
        sheet1.write(i+1, 5, pom_list[i]['appear'])
        sheet1.write(i+1, 6, pom_list[i]['background'])
        sheet1.write(i+1, 7, pom_list[i]['tag'])
    # tang1(1990首) tang2(1990) tang3(3980) tang4()
    # xl.save("../data/tang4.xlsx")

    # song1(1990) song2(3980) song2()
    # xl.save("../data/song3.xlsx")

    # yuan1(1990) yuan2(3980) yuan3()
    # xl.save("../data/yuan3.xlsx")

    # ming1(3980) ming2(1990) ming3()
    # xl.save("../data/ming3.xlsx")

    # qing1(3980首) qing2(3980) qing3()
    xl.save("../data/qing3.xlsx")
    time.sleep(10)  # 休眠10秒
# print(pom_list)