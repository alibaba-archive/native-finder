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
        tempArr = line.split(",")
        for itemOne in tempArr:
            tempHash = {}
            itemOne = itemOne.strip()
            itemArr = itemOne.split('=')
            tempHash['value'] = int(itemArr[1].strip())
            tempHash['name'] = itemArr[0].strip()
            sortedSumArr.append(tempHash)
        break
    fp.close()
    sortedArr = sorted(sortedSumArr,key=lambda x:x['value'], reverse=True)
    sortFinalArr = []
    for index in range(len(sortedArr)):
        tempLine = str(sortedArr[index]['value']) + '\t' + sortedArr[index]['name']
        sortFinalArr.append(tempLine)
    totalLines = '\n'.join(sortFinalArr)
    fp2 = open('./resultThread.txt', mode="w")
    fp2.write(totalLines)
    fp2.close()

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "please enter file path"
    parse_file(sys.argv[1]);
