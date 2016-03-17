# 简介 #
1. 一个手工处理验证码的模式

# 几个概念 #
1. Position / 位置 , 负责显示验证码
2. Worker / 工作者 , 负责真正的业务逻辑
3. PositionManager / 位置管理器 , 用于协调位置和工作者的组合

# 基本工作方式 #
1. 有多个位置, 每个位置可以通过某种方式显示验证码信息(目前主要是图片)
2. 有多个工作者(数量一般比位置多), 当它的验证码已经准备好的时候, 就回去请求一个位置来显示它的验证码
3. 位置管理器用于协调位置和工作者的组合.
4. 一般情况下, 一个需要输入验证码的操作, 除了验证码以外的数据都可以先让 工作者 准备好.
5. 同时工作者也要将图片的字节数据拿到, 然后交给 位置 去显示. 这个位置是由位置管理器决定的. 当然如果没有位置的话, 那么它就得等.

# 一个例子 #
假设某网站只需要输入账号,密码和验证码就可以进行注册, 现在要半自动注册该网站的账号.

先来描述一下用户需要做什么:
1. 在用户的面前会显示4张图片(p1,p2,p3,p4), 至于图片怎么显示的就看具体的 位置 如何实现了.
2. 程序启动后, 会提示用户输入p1图片对应的验证码
3. 用户看着p1图片, 输入其对应的验证码, 然后回车, 这样这个字符串就会作为用户给的答案返回给p1图片此时对应的工作者
4. 然后依次处理p2,p3,p4, 再循环 p1,p2,p3,p4 ...

再来描述一下程序如何配置:
1. 启动4个 位置.
2. 启动8个 工作者, 之所以工作者比位置多, 是因为工作者如果太少可能会赶不上用户的输入速度.
	1. 每个工作者自行产生账号和密码(比如通过随机的方式产生).
	2. 然后通过 HttpClient 或其他方式获得验证码的图片数据.
	3. 请求 位置管理器 为该工作者分配一个位置
		1. 位置的分配是按顺序的, p1, p2, p3, p4 然后再次循环
	4. 一旦分配成功, 就让该位置显示验证码图片
	5. 注意此时只是该位置显示了验证码的图片信息而已
	6. 可能这个图片还没有轮到被处理

这样的好处是, 一旦工作者的处理速度足够快 和 数量足够多, 位置的数量也足够多, 用户就可以发挥最高的打字速度. 比如两秒就可以搞定一个验证码.
TODO

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