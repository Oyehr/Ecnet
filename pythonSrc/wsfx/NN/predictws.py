#coding:utf-8

import sys

from ss2ft.predict import CnnModel as ssCnn
from ft2jl.predict import CnnModel as jlCnn
from wsfun import getJLMatchObject,getSSMatchObject,getFTList
from contentop import cutcontent

#事实到法条
def predictSs2Ft(wspath):
    sf_model = ssCnn()
    ssStrls = cutcontent(getSSMatchObject(wspath))
    ftmcls, ftnrls = getFTList(wspath)
    ssCount = 1 #事实序号，从1开始编号
    for ss in ssStrls:
        for i in range(len(ftmcls)):
            print(ssCount) #事实序号
            print(ss) #事实内容
            print(ftmcls[i]) #法条名称
            print(ftnrls[i]) #法条内容
            print(sf_model.predict(ss,ftnrls[i])) #事实与法条相关性
        ssCount += 1

#法条到事实
def predictFt2Ss(wspath):
    sf_model = ssCnn()
    ssStrls = cutcontent(getSSMatchObject(wspath))
    ftmcls, ftnrls = getFTList(wspath)
    for i in range(len(ftmcls)):
        ssCount = 1 #事实序号，从1开始编号
        for ss in ssStrls:
            print(ssCount) #事实序号
            print(ss) #事实内容
            print(ftmcls[i]) #法条名称
            print(ftnrls[i]) #法条内容
            print(sf_model.predict(ftnrls[i],ss)) #法条与事实相关性
            ssCount += 1

#法条到结论
def predictFt2Jl(wspath):
    fj_model = jlCnn()
    jlStrls = cutcontent(getJLMatchObject(wspath))
    ftmcls, ftnrls = getFTList(wspath)
    for i in range(len(ftmcls)):
        jlCount = 1 #结论序号，从1开始编号
        for jl in jlStrls:
            print(jlCount) #结论序号
            print(jl) #结论内容
            print(ftmcls[i]) #法条名称
            print(ftnrls[i]) #法条内容
            print(fj_model.predict(ftnrls[i],jl)) #法条与结论相关性
            jlCount += 1

# 结论到法条
def predictJl2Ft(wspath):
    fj_model = jlCnn()
    jlStrls = cutcontent(getJLMatchObject(wspath))
    ftmcls, ftnrls = getFTList(wspath)
    jlCount = 1 #结论序号，从1开始编号
    for jl in jlStrls:
        for i in range(len(ftmcls)):
            print(jlCount) #结论序号
            print(jl) #结论内容
            print(ftmcls[i]) #法条名称
            print(ftnrls[i]) #法条内容
            print(fj_model.predict(jl, ftnrls[i])) #结论与法条相关性
        jlCount += 1


if __name__ == "__main__":
    # sys.argv[1]是要解析的文书XML路径，sys.argv[2]是评估类型：事实到法条；法条到事实；法条到结论；结论到法条；
    if sys.argv[2] == 'f2l':  # 事实到法条
        predictSs2Ft(sys.argv[1])
    elif sys.argv[2] == 'l2f':  # 法条到事实
        predictFt2Ss(sys.argv[1])
    elif sys.argv[2] == 'l2j':  # 法条到结论
        predictFt2Jl(sys.argv[1])
    elif sys.argv[2] == 'j2l':  # 结论到法条
        predictJl2Ft(sys.argv[1])


