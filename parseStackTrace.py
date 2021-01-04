# -*- coding:utf-8 -*-
#!/usr/bin/python

import sys
def parse_file(fileName, soName):
    fp = open(fileName, mode="r")
    lineList = fp.readlines()
    totalHash = {}
    totalHash2 = {}
    sumArr = []
    sumArr2 = []
    for line in lineList:
        line = line.strip()
        if line == "":
            continue
        arr = line.split("|");
        isMalloc = True
        isFree = False
        if not (arr[2].find("malloc") > -1 or arr[2].find("calloc") > -1 or arr[2].find("realloc") > -1 or arr[2].find("mmap") > -1):
            isMalloc = False
        if arr[2].find("free") > -1 or arr[2].find("munmap") > -1:
            isFree = True
        stacktrace = ""
        mallocSize = 0
        ret = ""
        if arr and len(arr) > 0:
            stacktrace = arr[len(arr) - 1]
            if isMalloc:
                mallocStr = arr[5]
                mallocSize = int(mallocStr.split(":")[1])
                retStr = arr[6]
                ret = retStr.split(":")[1]
            elif isFree:
                retStr = arr[5]
                ret = retStr.split(":")[1]
            else:
                continue
        else:
            continue
        if (stacktrace in totalHash) and isMalloc:
            totalHash[stacktrace]['count'] = totalHash[stacktrace]['count'] + 1
            totalHash[stacktrace]['size'] = totalHash[stacktrace]['size'] + mallocSize
        elif isMalloc:
            totalTempHash = {}
            totalTempHash['count'] = 1
            totalTempHash['size'] = mallocSize
            totalTempHash['totalSize'] = mallocSize
            totalTempHash['ret'] = ret
            totalTempHash['ptrs'] = {}
            totalTempHash['ptrs'][ret] = mallocSize
            totalHash[stacktrace] = totalTempHash
        if isFree:
            for item in totalHash:
                if ret in totalHash[item]['ptrs']:
                    tempFreeSize = totalHash[item]['ptrs'][ret]
                    totalHash[item]['size'] = totalHash[item]['size'] - tempFreeSize
                    del totalHash[item]['ptrs'][ret]
                    break
            continue
        if not isMalloc:
            continue
        tempArr = stacktrace.split(';')
        for stackOne in tempArr:
            if stackOne.find(soName) > -1:
                tempArr2 = stackOne.split('+')
                if tempArr2 and len(tempArr2) > 1:
                    tempStrPc = tempArr2[len(tempArr2) - 1]
                    if tempStrPc in totalHash2:
                        totalHash2[tempStrPc] = totalHash2[tempStrPc] + 1
                    else:
                        totalHash2[tempStrPc] = 1
                    break
    fp.close(); 
    for item in totalHash:
        tempHash = {}
        tempHash['name'] = item
        tempHash['value'] = totalHash[item]['count']
        tempHash['size'] = totalHash[item]['size']
        tempHash['ret'] = totalHash[item]['ret']
        sumArr.append(tempHash)
    sortedArr = sorted(sumArr,key=lambda x:x['value'], reverse=True)
    sortFinalArr = []
    for index in range(len(sortedArr)):
        tempLine = str(sortedArr[index]['value']) + '\t' + str(sortedArr[index]['size']) + '\t' + sortedArr[index]['name']
        sortFinalArr.append(tempLine)
    totalLines = '\n'.join(sortFinalArr)
    fp2 = open('./resultStacktrace.txt', mode="w")
    fp2.write(totalLines)
    fp2.close()

    sortedArrSize = sorted(sumArr,key=lambda x:x['size'], reverse=True)
    sortFinalArrSize = []
    for index in range(len(sortedArrSize)):
        tempLine = str(sortedArrSize[index]['size']) + '\t' + str(sortedArrSize[index]['value']) + '\t' + str(sortedArrSize[index]['ret']) + '\t' + sortedArrSize[index]['name']
        sortFinalArrSize.append(tempLine)
    totalLines2 = '\n'.join(sortFinalArrSize)
    fp2 = open('./resultStacktrace2.txt', mode="w")
    fp2.write(totalLines2)
    fp2.close()

    for item in totalHash2:
        tempHash = {}
        tempHash['name'] = item
        tempHash['value'] = totalHash2[item]
        sumArr2.append(tempHash)
    sortedArr2 = sorted(sumArr2,key=lambda x:x['value'], reverse=True)
    sortFinalArr2 = []
    for index in range(len(sortedArr2)):
        tempLine = str(sortedArr2[index]['value']) + '\t' + sortedArr2[index]['name']
        sortFinalArr2.append(tempLine)
    totalLines3 = '\n'.join(sortFinalArr2)
    fp2 = open('./resultStacktrace3.txt', mode="w")
    fp2.write(totalLines3)
    fp2.close()

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print "please enter file path"
    parse_file(sys.argv[1], sys.argv[2]);
