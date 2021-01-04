# Native Finder开源说明

前言：本次开源工具，除了工具本身外，更多的是希望开源分享大型移动应用native稳定性治理的解决方案，欢迎大家使用native finder做移动应用的native深度治理，此次开源的是最基础版本，后续还会在此基础上做持续迭代优化更新，也希望广大开发者能参与共建。<br />下面先介绍native治理解决方案：
<a name="AIKkh"></a>
## 一、背景
![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523157367-ca82ad7e-4b55-4803-9fe2-de247449569b.png#align=left&display=inline&height=271&margin=%5Bobject%20Object%5D&name=image.png&originHeight=288&originWidth=792&size=39816&status=done&style=none&width=746)<br />
<br />无线客户端crash一般分为java crash和native crash，无线应用往往为了追求更好的性能，把一些复杂的计算放到native层去实现，java层通过jni调用native层实现，满足功能场景；在native层产生的崩溃就产生了native crash；无线客户端衡量稳定性的最重要的指标就是crash率，稳定性一直是各系统治理的重中之重，也是一直绕不开的话题，而native是目前业界认为治理最有难度，也是要重点突破的方向。接下来分析一下为什么native治理值得去做以及如何做好。<br />

<a name="fD9an"></a>
### 1.目前手淘Android的crash现状——找方向
从图中我们可以看出，java crash正常可以维持在较好的水平，手淘native crash一般比java crash要高，大促期间，由于手淘内存瓶颈， native crash率会涨到日常水准的2-3倍。从数据可见，native crash是java crash的6倍之多，如果要想进一步突破，native是有很大空间的，但native问题一般都很难定位， 堆栈不全，或者堆栈都集中在系统so上，无法直接定位问题，所以想要突破难度很大。手淘稳定性再上升一个台阶，native crash是瓶颈，需要突破。<br />

<a name="wfUFt"></a>
### 2.native crash治理挑战——难

<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523200785-54b618e8-8eeb-4c7b-ac4e-628b9205baaa.png#align=left&display=inline&height=308&margin=%5Bobject%20Object%5D&name=image.png&originHeight=304&originWidth=719&size=36400&status=done&style=none&width=728)<br />（1）难点一：crash堆栈目前绝大部分只有系统so，缺乏问题关联的业务模块so，定位问题难度大，可以看一下如下的一个native crash堆栈，堆栈中的so都是系统的，无法直接定位业务问题。<br />
<br />（2）难点二：线上so都是去符号化的，即使堆栈中有业务so，也需要记录该APP版本对应的符号化so做反解才能拿到能看得懂的堆栈。这点已经通过第一阶段的native工程标准化解决，打出来的SDK包里面必须要有对应的符号化so才可以集成。<br />
<br />（3）难点三：目前线下有效提前发现native问题的手段缺乏，想要提前发现，需要平台工具和有效手段。
<a name="QXzkO"></a>
### 3.native crash治理核心问题是什么？——找抓手
要治理、要解决问题，首先得理清目前导致native crash的问题，在做之前，做了一下crash数据分析，于是手动捞取了当时近5个版本的top native crash数据，占比最多的就是sig 6和sig 11，那能说明什么问题呢？signal 6这种崩溃信号要看具体场景，但根据具体数据分析，手淘里面一般都是堆栈溢出、OOM等导致的。signal 11这种崩溃基本就锁定为内存问题了。根据实际数据，大部分crash原因是因为内存，可以初步下的结论是，目前手淘native crash 治理的关键是内存，解决手淘native内存相关的问题即可解决掉不部分问题。

<a name="k9bpX"></a>
## 二、Native治理——Native Finder整体技术方案
Native Finder是利用elf hook的原理，对malloc、free、realloc、alloc、mmap、munmap等操作进行hook，在自己的hook函数中，做一些智能分析和统计，并最后会调用系统内存操作函数，不破坏so原本发起的malloc等操作。这个方案的缺点是会有一定的性能损耗，毕竟在malloc等操作中增加了一些分析和统计操作，但性能影响还好，在开了堆栈能力之后，APP性能会受影响，这个也是后面要优化的地方。整体技术方案如下：<br />
<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523259761-f5e41a9a-261c-4ff7-905a-646874c6e14a.png#align=left&display=inline&height=319&margin=%5Bobject%20Object%5D&name=image.png&originHeight=410&originWidth=960&size=105261&status=done&style=none&width=746)<br />

<a name="AywYL"></a>
## 三、native crash治理过程

<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523287126-5ddd6370-d40c-4135-9fda-2ebdff2fad10.png#align=left&display=inline&height=388&margin=%5Bobject%20Object%5D&name=image.png&originHeight=421&originWidth=810&size=42228&status=done&style=none&width=746)<br />首先在前期，花了比较多时间去研究历史数据及问题，认真分析crash的关键问题和痛点是什么，才找准了方向，分析出来内存问题是最痛的点。治理过程总结如上图，分为5个阶段。接一下讲详细介绍结合Native Finder工具平台的治理过程和心得。<br />

<a name="HHQSk"></a>
### 1. native工程标准化

<br />治理总结如下：<br />
<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523341173-57c3d8c9-6e6f-4346-9595-3805c32c6d02.png#align=left&display=inline&height=329&margin=%5Bobject%20Object%5D&name=image.png&originHeight=361&originWidth=790&size=20842&status=done&style=none&width=721)<br />
<br />总结起来这个阶段我们做了3件事情，第一是库迁移，为什么要做库迁移呢？ 我们把老的gnu C++基础库迁移成libc++_ shared库，所有so都做统一，归一化，方便归一native层问题；第二，我们还做了重复so治理，因为不同so可能依赖了相同的so基础库，举个例子，例如A so依赖了libopenSsl基础so，B so也依赖了libopenSsl库，但他们依赖的版本不同，这样带来的坏处是，首先会增加包大小，其次会有相同so的不同版本存在应用中，这给定位问题带来了麻烦，所以需要对基础so去重。第三，每个版本包包括灰度版本都需要存储下对应的符号化so，便于在crash发生后，我们对堆栈做符号化处理，这样堆栈我们就能看懂了，加快和精准定位问题。这个阶段是后面做的所有事情的基础，非常重要。接下来我们看看下一个阶段治理<br />

<a name="VTF96"></a>
### 2. Native Finder开发完成，线下monkey跑native问题

<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523385962-50d5e6b2-9705-4950-abf9-1750c94ca7b9.png#align=left&display=inline&height=386&margin=%5Bobject%20Object%5D&name=image.png&originHeight=461&originWidth=886&size=43277&status=done&style=none&width=742)<br />
<br />
<br />此阶段发现了几个堆破坏的问题，但是经过好几天反复线下执行monkey，并未有任何进展；后续调整思路，开始逐个分析线上存在的native crash，并根据这些crash特征和根因开始沉淀经典问题，并把这些问题做成检查项，跟同学交流和对焦后，通过进一步的数据，发现内存OOM是目前优先级较高，且比较严重的问题，所以开始做这方面的技术建设，跟crashSDK打通，Native Finder中统计和分析so维度占用内存未释放的数据，在crash的时候做内存信息dump，并输出辅助信息，例如malloc、mmap次数、大内存（大于2M，属于大内存申请，可配置，可动态调整）的申请等信息。 接下来看一下线下monkey驱动阶段<br />

<a name="O2DQ9"></a>
### 3. 线上灰度，结合用户真实操作场景crash

<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523423059-75f736ff-2b1d-46ac-9ec4-c96dd11fad2f.png#align=left&display=inline&height=383&margin=%5Bobject%20Object%5D&name=image.png&originHeight=461&originWidth=886&size=34233&status=done&style=none&width=737)<br />
<br />
<br />线下monkey并不能都能复现问题，借助工具平台拿到关键信息去做问题解决和定位，所以我们希望场景更加丰富和多样，所以我们把Native Finder放到线上，做了线上灰度。随即把Native Finder放到线上做外灰，在用户真实操作场景下的crash，拿到crash时的内存dump，但是经过一段时间的线上crash内存信息采集，然后分析之后，没发现明显问题，从现在回头看这个阶段，其实当时的数据是能够体现出问题的，只是当时的想法不对。<br />

<a name="K3Eue"></a>
### 4. 虚拟内存不足，是目前OOM主要原因
我们对线上crash做了分析，同时也在线下做了可疑场景的尝试复现，复现过程中，我们也做了大量数据的对比分析； 分析发现，正常crash跟native oom crash，在内存详细数据上做对比，发现OOM crash在native vmsize上有较大差异，又看了很多手淘OOM crash，发现都是这个原因，vmsize暴涨，大家都知道，32位系统下，系统vmszie只有4G，还要抛去一些系统内核占用、以及共享内存占用，vmsize比较有限，手淘又是一个体量很大的航空母舰，各个业务都想有最佳的业务体验，都想用空间换时间，每个业务泄漏那么一点，那手淘就被撑爆了，是累加泄漏的结果， 所以手淘Android OOM要一个一个解决，逐个挖出来，才能根治。我们整个过程沉淀了如下检查项：一共沉淀了8项内存检查，内存检查项已经能覆盖80%以上的内存问题了； fd文件句柄检查项一共沉淀了6项，fd的检查项已经能覆盖几乎95%以上的fd问题了。我们同时也研发了本地调试模式，方便开发和测试同学，能快速在本地复现和定位问题，具体的技术方案如下：<br />
<br />![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2020/png/27003/1582523481448-7c4f5a42-f2d2-4749-a23e-e3d09bce9ce1.png#align=left&display=inline&height=390&margin=%5Bobject%20Object%5D&name=image.png&originHeight=471&originWidth=886&size=37326&status=done&style=none&width=734)<br />
<br />

<a name="XbDqr"></a>
### 5. 开始陆续发现各种native问题
朝着这个方向，对Native Finder做了逐步优化，开始陆续发现各种问题，治理初步阶段，我们通过Native Finder工具平台一共治理发现20+问题，其中包括了多种问题类型，例如内存堆破坏、内存泄漏、OOM、内存操作越界、多次free、内存操作错误等； 同时手淘日常的native crash率也有明显降低。到这里，我们native crash已经初见成效。虽然通过治理，陆续发现了不少问题，但是还远远不够，手淘内存问题依然严峻，特别是双十一场景下，互动、活动以及各种场景链路互拉的场景，内存问题还是很严峻，后续还需更加努力。<br />
<br />目前native finder已经做为手淘大促和常态化稳定性治理工具。

<a name="eBkg5"></a>
## 四、Native Finder工具使用
<a name="DPiNn"></a>
#### 1. 依赖sdk
移动APP依赖工程lib目录下的native_finder.aar
<a name="TBMOt"></a>
#### 2. sdk初始化
1）初始化是要让SDK生效，鉴于Native Finder的技术原理，为了能让工具平台能在更早阶段发现检测问题，请在APP启动初始化阶段，在更早期做初始化，注意：如果是release，避免代码对线上正式包的污染，最好在release包中加入Native Finder SDK初始化生效的前提条件，例如可以基于文件是否存在的方式来判断：<br />adb shell touch /data/local/tmp/.tao_sec_io_enable<br />即/data/local/tmp/.tao_sec_io_enable这个文件。<br />如果确定不污染，也可不做初始化限制，取决于自己的实际情况。<br />2）方式：TaobaoStatistics._init_(context, null);<br />context即APP应用的context，第二个参数可忽略，填null即可
<a name="wxpPx"></a>
#### 3. 工具使用
工程exec目录下有现成可直接使用的工具，可选择平台安装直接使用
