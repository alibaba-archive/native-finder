# -*- coding:utf-8 -*-
#!/usr/bin/python

import sys
def parse_file(fileName):
    fp = open(fileName, mode="r")
    lineList = fp.readlines()
    sum = ""
    totalHash = {}
    sumArr = []
    sortedSumArr = []
    for line in lineList:
        line = line.strip()
        if line == "":
            continue
        tempArr = []
        tempArr = line.split("->")
        fileKey = tempArr[1].strip()
        if fileKey in totalHash:
            totalHash[fileKey] = totalHash[fileKey] + 1
        else:
            totalHash[fileKey] = 1
    fp.close(); 
    
    for item in totalHash:
        tempHash = {}
        tempHash['name'] = item
        tempHash['value'] = totalHash[item]
        sumArr.append(tempHash)
    sortedArr = sorted(sumArr,key=lambda x:x['value'], reverse=True)
    sortFinalArr = []
    for index in range(len(sortedArr)):
        tempLine = str(sortedArr[index]['value']) + '\t' + sortedArr[index]['name']
        sortFinalArr.append(tempLine)

    totalLines = '\n'.join(sortFinalArr)
    fp2 = open('./fdLeakInfoResult.txt', mode="w")
    fp2.write(totalLines)
    fp2.close()

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "please enter file path"
    parse_file(sys.argv[1]);
