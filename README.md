# 简介 #
一个手工高效处理验证码的策略

# 想达到什么程度 #
假设从 呈现一个验证码给用户, 到 用户完成输入一个验证码 的最少时间是2s.
那我们的目标就是 让用户能在1分钟之内处理30个验证码.

# 几个概念 #
1. Position / 位置 , 负责显示验证码, 一般会有多个位置.
2. Worker / 工作者 , 负责真正的业务逻辑, 一般数量比位置多, 否则用户需要等待.
3. PositionManager / 位置管理器 , 用于协调将哪个位置分配给哪个工作者.

通常来说,Position, PositionManager是可以复用的, 而对于不同的场景, Worker需要有不同的实现.


# 期待的结果 #
假设有n个位置, 用户面前会同时显示n张图片 和 一个命令行的输入窗口. 

那么用户只需要:
看着第1张图片, 输入对应的验证码, 回车.
...
看着第n张图片, 输入对应的验证码, 回车.
再次循环

**注意**, 必须按照 1, 2, ... n 的顺序输入. 没事为什么要跳着输入呢!? 而且顺着输入效率绝对是更高的, 如果你觉得某些验证码看不清楚, 那么也可以简单的提交一个无效的验证码(比如直接敲回车)来快速跳过这个验证码, 反正你的程序应该要有处理错误验证码的机制.

## 为什么要有多个位置 ##
1. 如果只有1个位置的话, 那么当第一张图片的验证码已经输入完毕之后, 需要显示下一张验证码图片, 而用户需要花额外的非常少的时间去重新适应.
2. 如果有多个位置的话, 那么当用户再输入第一张图片的验证码的时候, 他的眼睛就可能已经在看下一张验证码了! 大家可以自己想象一下. 这种的效率绝对比第一种高很多. 

# 基本工作方式 #
1. 初始化有多个空闲的位置有多个位置, 每个位置可以通过某种方式显示验证码信息(一般是显示验证码图片).
2. 有多个工作者(数量一般比位置多), 当该工作者的验证码图片已经下载下来之后, 它就会向PositionManager申请一个位置来显示它的验证码.
3. 如果此时没有空闲位置, 位置管理器就会将工作者记录下来, 等有空闲的位置再发给它.
4. 如果此时有空闲的位置, 那么:
	1. 将某位置标记为已使用
	2. 让该工作者在该位置上显示验证码图片
	3. 将 (工作者,位置) 这样一个组合加入到一个队列的尾巴
5. 有一个消费者, 不断从 4.3 的队列头获取 (工作者,位置)
6. 一旦获取到一个  (工作者,位置), 就询问用户该位置上对应的验证码
7. 用户该位置对应的验证码
8. 该验证码被提交给工作者, 工作者继续他的工作
9. 位置被标记为空闲, 还给位置管理器

因为位置的使用肯定是 1, 2, 3, 4, ... , n, 1, 2, ... , n, 1... 这样的循环方式.
因此用户只需要眼镜不断的看着图片1, ,2, ... n,  1 ... , 手也不断敲对应的验证码就行了.

# 一个例子 #
假设我们要批量注册某个网站的账号, 注册的时候需要输入, 账号, 密码 和验证码.

1. 创建位置管理器
	1. 它会有一个线程(一般直接在主线程上就行了)
	2. 从 基本工作方式-4.3 描述的队列头获取 (工作者,位置)对(pair), 如果没有元素的话,可以考虑阻塞或轮询.
	3. 询问用户该位置对应的验证码.
	4. 用户输入验证码.
	5. 提交该验证码给工作者, 让工作者继续执行, 这个步骤体现为条用工作者的某个方法, 而这个方法内部是会自己再开一个线程去处理的, 因此方法可以马上返回.
	6. 标记位置为 未使用.
	7. 跳到2  
2. 初始化4个位置, 并且依次绑定到位置管理器上
3. 启动8个工作者(一般是各自有一个线程)
	1. 初始化 [异步]
		1. 随机生成账号 和 密码, 下载验证码图片数据
		2. 请求 位置管理器 给自己分配一个位置, 位置管理器可能不会立即给你分配位置, 需要等一会儿.
		3. 本次初始化结束 并且 线程结束.
	2. 过了一会儿, 位置管理器分配一个合适的位置给该工作者, 工作者让位置显示验证码信息
	3. 过了一会儿, 工作者收到了验证码字符串, 继续他的处理流程 [异步]
		1. 往某个url post相应的参数
		2. 查看注册结果, 该回调的回调, 该怎样的怎样
		3. 调用 1.初始化
 
# 使用方法 #
TODO


# LICENSE #
```
Copyright [2015] [xzchaoo(70862045@qq.com)]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```