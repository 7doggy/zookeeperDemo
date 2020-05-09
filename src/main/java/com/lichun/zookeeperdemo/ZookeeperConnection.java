package com.lichun.zookeeperdemo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperConnection {

  public static void main(String[] args) {
    try {
      //倒计时计数器对象
      final CountDownLatch countDownLatch = new CountDownLatch(1);
      //连接对象
      ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new Watcher() {
        public void process(WatchedEvent event) {
          if (event.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("连接创建成功！");
            countDownLatch.countDown();
          }
        }
      });
      //主线程阻塞等待连接对象创建成功
      countDownLatch.await();
      //会话编号
      System.out.println(zooKeeper.getSessionId());
      List<ACL> acls = new ArrayList<ACL>();
      Id id = new Id("world", "anyone");
      acls.add(new ACL(ZooDefs.Perms.READ, id));
      acls.add(new ACL(ZooDefs.Perms.WRITE, id));
      //添加节点
      zooKeeper.create("/node1", "hello".getBytes(), acls, CreateMode.PERSISTENT);
      //关闭连接
      zooKeeper.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
