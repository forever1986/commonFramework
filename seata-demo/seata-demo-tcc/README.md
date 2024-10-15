# seata-demo-tcc
是一个基于seata框架实现tcc事务的demo
> **注意**：TCC虽然解决XA依赖于数据库的XA事务，提供更广泛的分布式事务集成，但是TCC也存在自己的弱点  
> 1）try、confirm、cancel需要自己手动写，业务存在入侵  
> 2）状态是最终一致性，并非强一致性  
> 3）需要处理好：空回滚、幂等性、悬挂  
> &ensp;&ensp;①空回滚：就是可能执行没有执行try方法或者try方法报错，压根没有预留库存，这时候就需要在cancel方法中处理  
> &ensp;&ensp;②幂等性：网络延迟等会导致seata重试confirm/cancel方法，因此confirm/cancel方法需要有幂等性  
> &ensp;&ensp;③悬挂：网络延迟等也会导致confirm/cancel方法在try方法之前执行，因此需要再try方法中解决悬挂  
> 4）如果操作库存表的还存在其它本地事务方法，那么有可能出现脏数据