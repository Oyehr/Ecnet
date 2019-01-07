#coding:utf-8

import sys
import json
import lxml.etree

'''（以下代码是针对Python2的，Python3中由于byte和str编码方式更改，不需要再reload）
# Python2.5 初始化后会删除 sys.setdefaultencoding 这个方法，我们需要重新载入   
reload(sys)
sys.setdefaultencoding('utf-8')
'''

#全文
def getQW(path):
    tree = lxml.etree.parse(path)
    root = tree.getroot()
    for qw in root:
        return qw

#文书全文下面的节点内容获取,如文首、诉讼情况、案件基本情况、裁判分析过程、判决结果这几个的value
def getQWChildContent(path,childname):
    content = ''
    qw = getQW(path)
    for qwchild in qw:
        if qwchild.tag == childname:
            content += qwchild.attrib['value']

    return content

# 认定事实
def getRDSS(path):
    content = ''
    qw = getQW(path)
    for qwchild in qw:
        if qwchild.tag == 'AJJBQK':
            for ajjbqkchild in qwchild:
                if ajjbqkchild.tag == 'BSSLD':
                    for bssldchild in ajjbqkchild:
                        if bssldchild.tag == 'ZJXX':
                            for zjxxchild in bssldchild:
                                if zjxxchild.tag == 'ZJFZ':
                                    for zjfzchild in zjxxchild:
                                        if zjfzchild.tag == 'RDSS':
                                            content = zjfzchild.attrib['value']
    return content

# 指控段落
def getZKDL(path):
    content = ''
    qw = getQW(path)
    for qwchild in qw:
        if qwchild.tag == 'AJJBQK':
            for ajjbqkchild in qwchild:
                if ajjbqkchild.tag == 'ZKDL':
                    content = ajjbqkchild.attrib['value']
    return content

# 从新填充了法条内容的文书里提取法条列表
def getFTList(path):
    ftnamelist = []
    ftnrlist = []
    qw = getQW(path)
    for qwchild in qw:
        if qwchild.tag == 'YYFLNR':
            for yyflfzchild in qwchild:
                if yyflfzchild.tag == 'FLNRFZ':
                    for flnrfzchild in yyflfzchild:
                        flag = 0
                        if flnrfzchild.tag == 'FLMC':
                            flmc = flnrfzchild.attrib['value']
                            flag += 1
                        if flnrfzchild.tag == 'FLNR':
                            flnr = flnrfzchild.attrib['value']
                            flag += 2
                        if flag == 2 and flmc and flnr and flnr != 'NOT FOUND':
                            ftnamelist.append(flmc)
                            ftnrlist.append(flnr)
    ftnamelistJson = json.dumps(ftnamelist, ensure_ascii=False)
    ftnrlistJson = json.dumps(ftnrlist, ensure_ascii=False)
    print("引用法条："+ftnamelistJson+ftnrlistJson)
    return ftnamelist, ftnrlist

def getFTfromQW(path):
    ftls = []
    qw = getQW(path)
    for qwchild in qw:
        if qwchild.tag == 'CPFXGC':
            for cpfxgcchild in qwchild:
                if cpfxgcchild.tag == 'CUS_FLFT_FZ_RY':
                    for fz in cpfxgcchild:
                        if fz.tag == 'CUS_FLFT_RY':
                            ftls.append(fz.attrib['value'])
    return ftls



# 获取案件基本信息内容：案号、案件名称、案由、承办法官、审判日期
def getAJBasicInfoObject(wspath):
    ah = '' #案号
    ajlb = '' #案件类别
    xgr = '' #相关人
    zkzm = '' #指控罪名
    qszay = '' #起诉主案由
    cpsj = '' #裁判时间
    spz = '' #审判长
    spzxm = '' # 审判长姓名

    qw = getQW(wspath)
    for qwchild in qw:
        if qwchild.tag == 'WS': #文首
            for wschild in qwchild:
                if wschild.tag == 'AH': #案号
                    ah = wschild.attrib['value']
                elif wschild.tag == 'AJLB': #案件类别
                    ajlb = wschild.attrib['value']
        elif qwchild.tag == 'SSJL': #诉讼记录
            for ssjlchild in qwchild: 
                if ssjlchild.tag == 'ZKXX': #指控信息
                    for zkxxchild in ssjlchild:
                        if zkxxchild.tag == 'ZKJL': #指控记录
                            for zkjlchild in zkxxchild:
                                if zkjlchild.tag == 'ZKZM': #指控罪名
                                    zkzm = zkjlchild.attrib['value']
                                elif zkjlchild.tag == 'XGR': #相关人
                                    xgr = zkjlchild.attrib['value']
                elif ssjlchild.tag == 'QSZAY': #起诉主案由
                    qszay = ssjlchild.attrib['value']
        elif qwchild.tag == 'WW': #文尾
            for wwchild in qwchild: 
                if wwchild.tag == 'CPSJ': #裁判时间
                    cpsj = wwchild.attrib['value']
                elif wwchild.tag == 'SPZZCY': #审判组织成员
                    for spzzcychild in wwchild:
                        if spzzcychild.tag == 'SPRYJS' and spzzcychild.attrib['value'] == '审判长':
                            spz = wwchild
                    for spzchild in spz:
                        if spzchild.tag == 'SPRYXM': #审判人员姓名
                            spzxm = spzchild.attrib['value']

    ajmc = ajlb+": "+xgr+zkzm+"一案" #案件名称 = 案件类别+“：”+相关人+指控罪名+“一案”
    print("案号："+ah)
    print("案件名称："+ajmc)
    print("起诉主案由"+qszay)
    print("裁判时间："+cpsj)
    print("审判长姓名："+spzxm)

# 获取事实内容
def getSSMatchObject(wspath):
    rdss = getRDSS(wspath) # 认定事实
    zkdl = getZKDL(wspath) # 指控段落
    print("案件事实："+rdss+zkdl)
    return rdss+zkdl

# 获取结论内容
def getJLMatchObject(wspath):
    cpfxgc = getQWChildContent(wspath, 'CPFXGC') # 裁判分析过程
    pjjg = getQWChildContent(wspath, 'PJJG') # 判决结果
    print("判决结论："+cpfxgc+pjjg)
    return cpfxgc+pjjg

if __name__ == "__main__":
    getAJBasicInfoObject(sys.argv[1])
    print("")
    getSSMatchObject(sys.argv[1])
    print("")
    getJLMatchObject(sys.argv[1])
    print("")
    getFTList(sys.argv[1])
