package group.rxcloud.capa.component.pubsub;

public class PubSubConfig {
  Integer tryCount;
  Integer sendThreads;

  public PubSubConfig(){}

  public PubSubConfig(Integer tryCount, Integer sendThreads) {
    this.tryCount = tryCount;
    this.sendThreads = sendThreads;
  }

  public Integer getTryCount() {
    return tryCount;
  }

  public void setTryCount(Integer tryCount) {
    this.tryCount = tryCount;
  }

  public Integer getSendThreads() {
    return sendThreads;
  }

  public void setSendThreads(Integer sendThreads) {
    this.sendThreads = sendThreads;
  }
}
