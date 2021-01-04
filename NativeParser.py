# -*- coding:utf-8 -*-
#!/usr/bin/python

import sys
def parse_file(fileName):
    fp = open(fileName, mode="r")
    lineList = fp.readlines()
    sum = "";
    totalHash = {}
    for line in lineList:
        line = line.strip()
        if line == "":
            continue
        sum = sum + line;
    fp.close();
    totalArr = sum.split("|");
    for item in totalArr:
        itemOne = item.strip().split(":");
        if len(itemOne) < 9:
            continue
        key = itemOne[0].strip();
        if key in totalHash:
            tempHash = totalHash[key];
            tempHash['size'] = tempHash['size'] + int(itemOne[1])
            tempHash['totalMalloc'] = tempHash['totalMalloc'] + int(itemOne[2])
            tempHash['totalMmap'] = tempHash['totalMmap'] + int(itemOne[3])
            tempHash['totalFree'] = tempHash['totalFree'] + int(itemOne[4])
            tempHash['totalMunmap'] = tempHash['totalMunmap'] + int(itemOne[5])
            tempHash['totalMallocBytes'] = tempHash['totalMallocBytes'] + int(itemOne[6])
            tempHash['totalFreeBytes'] = tempHash['totalFreeBytes'] + int(itemOne[7])
            tempHash['totalMmapBytes'] = tempHash['totalMmapBytes'] + int(itemOne[8])
            tempHash['totalUnMmapBytes'] = tempHash['totalUnMmapBytes'] + int(itemOne[9])
            tempHash['totalBigMemMallocTimes'] = tempHash['totalBigMemMallocTimes'] + int(itemOne[10])
            totalHash[key] = tempHash
        else:
            tempHash = {}
            tempHash['size'] = int(itemOne[1])
            tempHash['totalMalloc'] = int(itemOne[2])
            tempHash['totalMmap'] = int(itemOne[3])
            tempHash['totalFree'] = int(itemOne[4])
            tempHash['totalMunmap'] = int(itemOne[5])
            tempHash['totalMallocBytes'] = int(itemOne[6])
            tempHash['totalFreeBytes'] = int(itemOne[7])
            tempHash['totalMmapBytes'] = int(itemOne[8])
            tempHash['totalUnMmapBytes'] = int(itemOne[9])
            tempHash['totalBigMemMallocTimes'] = int(itemOne[10])
            totalHash[key] = tempHash
    itemArr = []
    totalSize = 0
    for keyVal in totalHash:
        valueVal = totalHash[keyVal]
        valueVal['soName'] = keyVal
        totalSize = totalSize + valueVal['size']
        itemArr.append(valueVal)
    sortedArr = sorted(itemArr,key=lambda x:x['size'], reverse=True)
    sortedArr2 = sorted(itemArr,key=lambda x:x['totalMalloc'], reverse=True)
    sortedArr3 = sorted(itemArr,key=lambda x:x['totalMmap'], reverse=True)
    sortedArr4 = sorted(itemArr,key=lambda x:x['totalMallocBytes'], reverse=True)
    print "-*-字段说明：size表示：内存占用未释放的大小，单位时Byte；其他字段根据名称很容易分辨-*-"
    print "------------------------------------内存占用未释放的so倒排:-------------------------------------------"
    print sortedArr
    print "------------------------------------malloc次数按照so倒排:-------------------------------------------"
    print sortedArr2
    print "------------------------------------mmap次数按照so倒排:-------------------------------------------"
    print sortedArr3
    print "------------------------------------申请总内存按照so倒排:-------------------------------------------"
    print sortedArr4
    print "crash时总的内存占用且是未释放的大小(Byte)：" + str(totalSize)



if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "please enter file path"
    parse_file(sys.argv[1]);
