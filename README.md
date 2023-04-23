# 装袋项目
这是我正在研究的课题————三维装袋项目的仓库，代码尚未完全整理，但整体框架基本确定  
整体算法框架为Combinatorial Benders Decomposition  
baseObject表示一些基本类  
bagPackingModel为最基本的MILP，并带有有效不等式（可通过传参调用）  
benders模块包括主问题和子问题，子问题可直接使用MILP或者基于Beam Search的启发式算法  
grasp为working中的纯启发式算法，算法思路参考Alvarez-Valdes R , Parreno F , Tamarit J M . A GRASP/Path Relinking algorithm for two- and three-dimensional multiple bin-size bin packing problems[J]. Computers & Operations Research, 2013, 40(12):3081-3090.
