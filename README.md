# 装袋项目
这是我正在研究的课题————三维装袋项目的仓库，代码尚未完全整理，但整体框架基本确定，参考Jean-Franois Cté, Haouari M , Iori M . Combinatorial Benders Decomposition for the Two-Dimensional Bin Packing Problem[J]. Informs Journal on Computing, 2021.    
整体算法框架为Combinatorial Benders Decomposition  
调用了cplex12.10.0，基于generic callback接口实现  
## 代码结构
1.baseObject表示一些基本类  
2.bagPackingModel为最基本的MILP，并带有有效不等式（可通过传参调用）  
3.benders模块包括主问题和子问题，子问题可直接使用MILP或者基于Beam Search的启发式算法  
4.grasp为working中的纯启发式算法，算法思路参考Alvarez-Valdes R , Parreno F , Tamarit J M . A GRASP/Path Relinking algorithm for two- and three-dimensional multiple bin-size bin packing problems[J]. Computers & Operations Research, 2013, 40(12):3081-3090.  
## 注意
1.真实案例数据未上传至本仓库，data文件夹中均为随机生成数据  
2.clone下来后需要根据文件路径传参，才可运行
